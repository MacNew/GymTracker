package com.mac.gymtracker.ui.lastsummery

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.PieChartFragmentBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.*

class PieChartFragment : Fragment(), Page {
    companion object Flage {
        var flage = true
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.lastSummery.observe(viewLifecycleOwner, { list ->
            var cchest = 0f
            var cshoulder = 0f
            var cback = 0f
            var cleg = 0f
            var cbiceps = 0f
            var cTricept = 0f
            list.forEach {
                when (it.exerciseName) {
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
            val pieDataSet = PieDataSet(pieEntries, "")
            binding?.piechart?.legend?.isWordWrapEnabled = true
            val colors = IntArray(10)
            var counter = 0

            for (color in ColorTemplate.JOYFUL_COLORS) {
                colors[counter] = color
                counter++
            }

            for (color in ColorTemplate.MATERIAL_COLORS) {
                colors[counter] = color
                counter++
            }
            pieDataSet.setColors(*colors)
            pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            pieDataSet.valueTextSize = 12f
            val pieData = PieData(pieDataSet)
            binding?.piechart?.data = pieData
            binding?.piechart?.description?.text = "Pi chart of current week exercise"
            binding?.piechart?.description?.textSize = 16f
            binding?.piechart?.legend?.textSize = 16f
            binding?.piechart?.notifyDataSetChanged()
            binding?.piechart?.invalidate()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateList(
            item,
            (activity as AppCompatActivity).supportActionBar!!,
            requireActivity().resources
        ) {
            pieEntries.clear()
            binding?.piechart?.invalidate()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageCnangeListner() {
        (activity as AppCompatActivity).supportActionBar?.title =
            requireActivity().resources.getString(R.string.last_seven_day)
        binding?.piechart?.notifyDataSetChanged()
        pieEntries.clear()
        binding?.piechart?.invalidate()
        viewModel.updateList(
            null,
            (activity as AppCompatActivity).supportActionBar!!,
            requireActivity().resources
        ) {}
        Log.e(TAG, "OnPieChangeFragment")
        flage = false
    }
}

private const val TAG = "PicChart"