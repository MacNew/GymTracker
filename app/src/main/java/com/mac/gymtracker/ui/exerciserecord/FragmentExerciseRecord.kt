package com.mac.gymtracker.ui.exerciserecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentExerciseRecordBinding
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_exercise_record.view.*


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
        var image: String? = FragmentExerciseRecordArgs.fromBundle(arguments!!).image
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding!!.ivExerciseRecord.setImageResource(image!!.toInt())
        binding!!.tbExerciseRecord.title = id

        view.fab_action.setOnClickListener {
            showButtonSheet()
        }
    }

    private fun showButtonSheet() {
        var bottomSheetDialog = BottomSheetDialog(context!!)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        var imageButton = bottomSheetDialog.findViewById<ImageButton>(R.id.ib_add_set)
        var numberPickerWeight = bottomSheetDialog.findViewById<NumberPicker>(R.id.number_picker_weight)
        var numberPickerReps = bottomSheetDialog.findViewById<NumberPicker>(R.id.number_picker_rep)
        var tvRecord = bottomSheetDialog.findViewById<TextView>(R.id.tv_label_bs)
        tvRecord!!.text = FragmentExerciseRecordArgs.fromBundle(bundle = arguments!!).exerciseId
        imageButton!!.setOnClickListener {
            bottomSheetDialog.hide()
        }
        setWeightAndRep(numberPickerWeight!!, numberPickerReps!!)

        bottomSheetDialog.show()
    }

    private fun setWeightAndRep(numberPickerWeight: NumberPicker, repsNumberPicker: NumberPicker) {
        val weightData= arrayOfNulls<String>(100)
        for(i in 1..weightData.size) {
            weightData[i-1] = i.toString()
        }
        val repData = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        numberPickerWeight.minValue = 1
        numberPickerWeight.maxValue = weightData.size
        numberPickerWeight.displayedValues= weightData
        numberPickerWeight.value = 1
        repsNumberPicker.minValue = 1
        repsNumberPicker.maxValue = repData.size
        repsNumberPicker.displayedValues= repData
        repsNumberPicker.value = 1
    }

}