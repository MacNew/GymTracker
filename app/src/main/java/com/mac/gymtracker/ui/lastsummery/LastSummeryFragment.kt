package com.mac.gymtracker.ui.lastsummery

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.databinding.FragmentLastSummeryBinding
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import java.util.stream.Collectors

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
                "")
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
               list.forEach { exerciseRecord ->
                   if (!hsMap.containsKey(exerciseRecord.saveTime)) {
                       var exerciseRecordList: ArrayList<ExerciseRecordModel> = ArrayList();
                       exerciseRecordList.add(exerciseRecord)
                       hsMap.put(exerciseRecord.saveTime, exerciseRecordList);
                   } else {
                       hsMap.get(exerciseRecord.saveTime)!!.add(exerciseRecord)
                   }
               }
               var listss =  hsMap.get("1621438804413")

               var byGroup: Map<String, ArrayList<ExerciseRecordModel>>
                 = HashMap()
                byGroup = list.stream().collect(Collectors.groupingBy {
                    it.saveTime
                }) as Map<String, ArrayList<ExerciseRecordModel>>

                var checklist = byGroup.get("1621438804413")






            })
        })
    }

    fun getList(): List<ExerciseRecordModel> {
        return ArrayList<ExerciseRecordModel>()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}