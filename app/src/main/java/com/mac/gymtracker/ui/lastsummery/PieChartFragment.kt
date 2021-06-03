package com.mac.gymtracker.ui.lastsummery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.mac.gymtracker.databinding.PieChartFragmentBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.*


class PieChartFragment : Fragment() {
    private var _binding: PieChartFragmentBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ExerciseRecordViewModle
    var pieEntries: ArrayList<PieEntry> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,
            ExerciseRecordFactory(
                ExerciseRecordRepo(requireActivity().applicationContext),
                "", TrackExerciseLocalDataSource(requireActivity().applicationContext)
            )
        ).get(
            ExerciseRecordViewModle::class.java
        )

        _binding = PieChartFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.exerciseRecord.observe(viewLifecycleOwner, { list ->
            var cchest = 0f
            var cshoulder = 0f
            var cback = 0f
            var cleg = 0f
            var cbiceps = 0f
            var cTricept = 0f
            list.forEach {
                when (it.mainExercise) {
                    CHEST -> {
                        cchest += 1
                    }
                    SHOULDER -> {
                        cshoulder += 1
                    }
                    BACK -> {
                        cback += 1
                    }
                    LEG -> {
                        cleg += 1
                    }
                    BICEPS -> {
                        cbiceps += 1
                    }
                    TRICEPS -> {
                        cTricept += 1
                    }
                }
            }
            pieEntries.add(PieEntry(cchest, "Chest"))
            pieEntries.add(PieEntry(cshoulder, "Shoulder"))
            pieEntries.add(PieEntry(cback, "Back"))
            pieEntries.add(PieEntry(cleg, "Leg"))
            pieEntries.add(PieEntry(cbiceps, "Biceps"))
            pieEntries.add(PieEntry(cTricept, "Triceps"))

            val pieDataSet = PieDataSet(pieEntries, "Exercise list")
            pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
            pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.valueTextSize = 12f
            val pieData = PieData(pieDataSet)
            binding?.piechart?.data = pieData
            binding?.piechart?.description?.text = "Pi chart of current week exercise"
            binding?.piechart?.description?.textSize = 16f
            binding?.piechart?.legend?.textSize = 16f
            binding?.piechart?.invalidate()
            binding?.piechart?.notifyDataSetChanged()
        })
    }
}