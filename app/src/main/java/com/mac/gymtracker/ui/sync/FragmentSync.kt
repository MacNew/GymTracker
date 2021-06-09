package com.mac.gymtracker.ui.sync
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentSyncBinding
import com.mac.gymtracker.ui.exercise.TrackExerciseViewModel
import com.mac.gymtracker.ui.exercise.TrackingExerciseViewModelFactory
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*

class FragmentSync : Fragment() {
    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding
    private lateinit var trackExerciseViewModel: TrackExerciseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding?.syncBtn?.setOnClickListener {
            binding?.syncProgressbar?.visibility = View.VISIBLE
            binding?.syncBtn?.visibility = View.GONE
            syncExerciseList()
        }
    }

    @SuppressLint("CheckResult")
    private fun syncExerciseList() {
        val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
        LocalExerciselistRepo(requireContext()).getAllList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TA, "Error ${it.message}")
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }.subscribe({ list ->
                Log.e(TA, "user name $userName")
                var exerciseListModle = ExerciseList(list)
                db.collection(userName!!).document(EXERCISE_LIST).set(exerciseListModle)
                    .addOnSuccessListener {
                        syncExerciseRecord()
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${it.message}")
                    }
            }) {
                Log.e(TA, "Error ${it.message}")
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
                binding?.syncBtn?.visibility = View.VISIBLE
                Log.e(TA, "Error ${it.message}")
                view?.showSnack("Error ${it.message}")
            }.subscribe({
                val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
                var exerciseListModle = ExerciseRecord(it)
                db.collection(userName!!).document(EXERCISE_RECORD).set(exerciseListModle)
                    .addOnSuccessListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Data Sync on Server Successfully")
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                       view?.showSnack("Error ${it.message}")
                        Log.e(TA, "Error ${it.message}")

                    }
            }){
                Log.e(TA, "Error ${it.message}")
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var mi = menu.findItem(R.id.id_log_out)
        mi.isVisible = false
    }
}

data class ExerciseRecord(
    var exerciseRecord: List<ExerciseRecordModel>? = null
)

data class ExerciseList(
    var exerciseList: List<ExerciseListModle>? = null
)

const val TA = "Sync"
