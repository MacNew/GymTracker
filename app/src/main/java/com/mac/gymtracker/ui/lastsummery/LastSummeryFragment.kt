package com.mac.gymtracker.ui.lastsummery

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentLastSummeryBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import com.mac.gymtracker.utils.showSnack

@Suppress("UNREACHABLE_CODE")
class LastSummeryFragment : Fragment() {

    private var _binding: FragmentLastSummeryBinding? = null

    private lateinit var viewmodle: ExerciseRecordViewModle

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewmodle = ViewModelProvider(
            this,
            ExerciseRecordFactory(
                ExerciseRecordRepo(requireActivity().applicationContext),
                "", TrackExerciseLocalDataSource(requireActivity().applicationContext)
            )
        ).get(
            ExerciseRecordViewModle::class.java
        )
        _binding = FragmentLastSummeryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDataOnLastSummery()
        binding.rvLastSummeryRecyclerView.layoutManager = LinearLayoutManager(context)
        viewmodle.lastSummery.observe(viewLifecycleOwner, {
            binding.rvLastSummeryRecyclerView.adapter = LastSummeryRecyclerViewAdapter(it)
            binding.progressBar.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.id_seven_day) {
            view?.showSnack("Seven day")
        } else if (itemId == R.id.id_one_month) {
            view?.showSnack("one month")
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateDataOnLastSummery() {
        viewmodle.exerciseRecord.observe(viewLifecycleOwner, { list ->
            val hsMap: HashMap<String, ArrayList<ExerciseRecordModel>> = HashMap()
            val key: HashSet<String> = HashSet()
            list.forEach { exerciseRecord ->
                key.add(exerciseRecord.saveTime)
                if (!hsMap.containsKey(exerciseRecord.saveTime)) {
                    val exerciseRecordList: ArrayList<ExerciseRecordModel> = ArrayList()
                    exerciseRecordList.add(exerciseRecord)
                    hsMap[exerciseRecord.saveTime] = exerciseRecordList
                } else {
                    hsMap[exerciseRecord.saveTime]!!.add(exerciseRecord)
                }
            }
            val lastSummeryModel: ArrayList<LastSummeryModel> = ArrayList()
            key.forEach {
                lastSummeryModel.add(
                    LastSummeryModel(
                        false, it,
                        hsMap[it]?.get(0)!!.mainExercise, hsMap[it]?.get(0)?.exerciseName!!,
                        hsMap[it]?.get(0)!!.image, hsMap[it]
                    )
                )
            }
            viewmodle.updateList(lastSummeryModel)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}