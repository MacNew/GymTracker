package com.mac.gymtracker.ui.lastsummery

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.databinding.FragmentLastSummeryBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel

class LastSummeryFragment : Fragment() {

    private var _binding: FragmentLastSummeryBinding? = null

    private lateinit var viewmodle: ExerciseRecordViewModle

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodle = ViewModelProvider(
            this,
            ExerciseRecordFactory(
                ExerciseRecordRepo(activity!!.applicationContext),
                "", TrackExerciseLocalDataSource(activity!!.applicationContext)
            )
        ).get(
            ExerciseRecordViewModle::class.java)
        _binding = FragmentLastSummeryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodle.exerciseRecord.observe(this, { liveData ->
            liveData.observe(this, { list ->
              var hsMap:HashMap<String,ArrayList<ExerciseRecordModel>> = HashMap()
              var key:HashSet<String> = HashSet<String>();
                list.forEach { exerciseRecord ->
                    key.add(exerciseRecord.saveTime)
                   if (!hsMap.containsKey(exerciseRecord.saveTime)) {
                       var exerciseRecordList: ArrayList<ExerciseRecordModel> = ArrayList();
                       exerciseRecordList.add(exerciseRecord)
                       hsMap.put(exerciseRecord.saveTime, exerciseRecordList);
                   } else {
                       hsMap.get(exerciseRecord.saveTime)!!.add(exerciseRecord)
                   }
               }
               var lastSummeryModel: ArrayList<LastSummeryModel> = ArrayList()
                key.forEach {
                    lastSummeryModel.add(LastSummeryModel(it, hsMap[it]?.get(0)!!.mainExercise, hsMap[it]?.get(0)?.exerciseName!!,
                        hsMap[it]
                    ))
               }
                viewmodle.updateList(lastSummeryModel)
            })
        })
        viewmodle.lastSummery.observe(this, {
           Log.e("tag", it.toString());
           Log.e("tag", it.size.toString())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}