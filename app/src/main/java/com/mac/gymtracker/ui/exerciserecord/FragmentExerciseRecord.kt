package com.mac.gymtracker.ui.exerciserecord

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentExerciseRecordBinding
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.showSnack
import com.mac.gymtracker.utils.toLocalBitMap
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_exercise_record.view.*
import java.util.*
import kotlin.collections.ArrayList

class FragmentExerciseRecord : Fragment() {
    private lateinit var viewmodle: ExerciseRecordViewModle
    private var _binding: FragmentExerciseRecordBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewmodle = ViewModelProvider(
            this,
            ExerciseRecordFactory(
                ExerciseRecordRepo(requireActivity().applicationContext),
                FragmentExerciseRecordArgs.fromBundle(requireArguments()).exerciseId as String, null)
        ).get(
            ExerciseRecordViewModle::class.java)
        _binding = FragmentExerciseRecordBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("list", recordList)
        outState.putInt("count", setCount)
        super.onSaveInstanceState(outState)

    }

    var flage: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            recordList = savedInstanceState["list"] as ArrayList<ExerciseRecordModel>
            setCount = savedInstanceState["count"] as Int
            binding!!.rvRecordFragment.layoutManager = LinearLayoutManager(context)
            binding!!.rvRecordFragment.adapter = ExerciseRecordAdapter(recordList) {}
            flage = false
        }


        val id: String? = FragmentExerciseRecordArgs.fromBundle(requireArguments()).exerciseId
        val image: String? = FragmentExerciseRecordArgs.fromBundle(requireArguments()).image
        (activity as AppCompatActivity).supportActionBar!!.hide()
        try {
            binding!!.ivExerciseRecord.setImageResource(image!!.toInt())
        }catch (exception: Exception) {
            binding!!.ivExerciseRecord.setImageBitmap(image!!.toLocalBitMap())
        }
        binding!!.tbExerciseRecord.title = id
        if (flage) {
            binding!!.rvRecordFragment.layoutManager = LinearLayoutManager(context)
            binding!!.rvRecordFragment.adapter = ExerciseRecordAdapter(recordList) {
            }
        }

        view.fab_action.setOnClickListener {
            showButtonSheet()
        }

        binding!!.btnSave.setOnClickListener {
            if (recordList.size != 0) {
                viewmodle.addToLocalDatabase(recordList) {
                    if (it) {
                        Log.e("msg", recordList.size.toString())
                        setCount = 1
                        binding!!.rvRecordFragment.adapter!!.notifyDataSetChanged()
                        view.showSnack("Data Added Successfully ")
                        binding!!.cardViewMsg.visibility = View.VISIBLE
                        (activity as MainActivity).onBackPressed()
                        /*activity!!.getNavigationController().navigate(FragmentExerciseRecordDirections.
                        actionFragmentExerciseRecordToFragmentExerciseList(1, ""))*/

                    } else {
                        Log.e("msg", "cannot find record list")
                    }
                }

            } else {
                view.showSnack("Please Add set ")
            }
        }
    }


    var setCount: Int = 1
    private fun showButtonSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        val imageButton = bottomSheetDialog.findViewById<ImageButton>(R.id.ib_add_set)
        val weightEditText = bottomSheetDialog.findViewById<EditText>(R.id.ed_weight)
        val numberPickerReps = bottomSheetDialog.findViewById<NumberPicker>(R.id.number_picker_rep)
        val tvRecord = bottomSheetDialog.findViewById<TextView>(R.id.tv_label_bs)
        val tvDate = bottomSheetDialog.findViewById<TextView>(R.id.tv_label_date)
        tvRecord!!.text = FragmentExerciseRecordArgs.fromBundle(bundle = requireArguments()).exerciseId
        tvDate!!.text = Date().time.toString()
        var repData = "1"
        var weight: String = weightEditText?.text.toString()

        imageButton!!.setOnClickListener {
            bottomSheetDialog.hide()
            weight = weightEditText?.text.toString()
            if (weight.isNotBlank() || weight !="") {
                addDataOnRecyclerView(repData, weight, setCount)
                setCount += 1
            } else {
                requireView().showSnack("Add Weight")
            }
        }

        numberPickerReps!!.setOnScrollListener(NumberPicker.OnScrollListener { picker, scrollState ->
            if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                repData = picker.value.toString()
            }
        })
        numberPickerReps!!.setOnValueChangedListener { picker, oldVal, newVal ->
            repData = newVal.toString()
        }

        setWeightAndRep(numberPickerReps!!)
        bottomSheetDialog.show()
    }

    var recordList: ArrayList<ExerciseRecordModel> = ArrayList()
    private fun addDataOnRecyclerView(reps: String, weight: String, setCount: Int) {
        var modle = ExerciseRecordModel(
            date = Date().time.toString(),
            exerciseName = FragmentExerciseRecordArgs.fromBundle(requireArguments()).exerciseId,
            weight = weight,
            reps = reps,
            set = setCount.toString(),
            saveTime = "",
            mainExercise = FragmentExerciseRecordArgs.fromBundle(requireArguments()).mainExerciseName!!,
            image = FragmentExerciseRecordArgs.fromBundle(requireArguments()).image!!,
            roomDate = Date(),
            stringFormatDate = ""

        )
        recordList.add(modle)
        binding!!.rvRecordFragment.adapter!!.notifyItemInserted(setCount)
        if (recordList.size>0) {
            binding!!.cardViewMsg.visibility = View.GONE
        } else {
            binding!!.cardViewMsg.visibility = View.VISIBLE
        }
    }

    private fun setWeightAndRep(repsNumberPicker: NumberPicker) {
        val repData = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "13", "14", "15", "16", "17", "18", "19", "20","21", "22", "23", "24", "25")
        repsNumberPicker.minValue = 1
        repsNumberPicker.maxValue = repData.size
        repsNumberPicker.displayedValues = repData
        repsNumberPicker.value = 1
    }

}