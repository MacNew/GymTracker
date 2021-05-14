package com.mac.gymtracker.ui.exerciserecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mac.gymtracker.databinding.FragmentExerciseRecordBinding
import java.lang.Exception

class FragmentExerciseRecord : Fragment() {
    private lateinit var viewmodle: ExerciseRecordViewModle
    private var _binding: FragmentExerciseRecordBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseRecordBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id: String? = FragmentExerciseRecordArgs.fromBundle(arguments!!).exerciseId
        setWeightAndRep()
    }

    private fun setWeightAndRep() {
        val weightData= arrayOfNulls<String>(100)
        for(i in 1..weightData.size) {
            weightData[i-1] = i.toString()
        }
        val repData = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        binding!!.numberPickerWeight.minValue = 1
        binding!!.numberPickerWeight.maxValue = weightData.size
        binding!!.numberPickerWeight.displayedValues= weightData
        binding!!.numberPickerWeight.value = 1

        binding!!.numberPickerRep.minValue = 1
        binding!!.numberPickerRep.maxValue = repData.size
        binding!!.numberPickerRep.displayedValues= repData
        binding!!.numberPickerRep.value = 1
    }

}