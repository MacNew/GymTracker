package com.mac.gymtracker.ui.sync
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mac.gymtracker.databinding.FragmentSyncBinding
import com.mac.gymtracker.ui.exercise.TrackExerciseViewModel
import com.mac.gymtracker.ui.exercise.TrackingExerciseViewModelFactory
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.EMAIL
import com.mac.gymtracker.utils.PrefUtils
import com.mac.gymtracker.utils.showSnack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FragmentSync : Fragment() {
    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding
    private lateinit var trackExerciseViewModel: TrackExerciseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Sync data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackExerciseViewModel = ViewModelProvider(
            this,
            TrackingExerciseViewModelFactory(TrackExerciseLocalDataSource(requireActivity().applicationContext))
        ).get(TrackExerciseViewModel::class.java);
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    private val db = FirebaseFirestore.getInstance()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.syncBtn?.setOnClickListener {
            syncDataOnDataBase()
        }
    }

    @SuppressLint("CheckResult")
    private fun syncDataOnDataBase() {
        binding?.syncProgressbar?.visibility = View.VISIBLE
        binding?.syncBtn?.visibility = View.GONE
        val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
        TrackExerciseLocalDataSource(requireContext()).getExerciseRxJava()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")

            }.subscribe({
                var exerciseModel = Exercise(it)
                db.collection(userName!!).document("exercise").set(exerciseModel)
                    .addOnSuccessListener {
                        syncExerciseList()
                        //   view?.showSnack("Exercise Data Added on Server")
                    }.addOnFailureListener { e ->
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${e.message!!}")
                    }
            }) {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }

    @SuppressLint("CheckResult")
    private fun syncExerciseList() {
        val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
        LocalExerciselistRepo(requireContext()).getAllList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }.subscribe({ list ->
                var exerciseListModle = ExerciseList(list)
                db.collection(userName!!).document("exerciselist").set(exerciseListModle)
                    .addOnSuccessListener {
                        syncExerciseRecord()
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${it.message}")
                    }
            }) {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }

    @SuppressLint("CheckResult")
    private fun syncExerciseRecord() {
        ExerciseRecordRepo(requireContext()).getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncProgressbar?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }.subscribe({
                val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
                var exerciseListModle = ExerciseRecord(it)
                db.collection(userName!!).document("exerciseRecord").set(exerciseListModle)
                    .addOnSuccessListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncProgressbar?.visibility = View.VISIBLE
                        view?.showSnack("Data Sync on Server Successfully")
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${it.message}")
                    }
            }){
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncProgressbar?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }
}

data class ExerciseRecord(
    var exerciseRecord: List<ExerciseRecordModel>? = null
)

data class ExerciseList(
    var exerciseList: List<ExerciseListModle>? = null
)

data class Exercise(
    var exercise: List<TrackExerciseModel>? = null
)
