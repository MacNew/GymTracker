package com.mac.gymtracker.ui.lastsummery

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentLastSummeryBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo

@Suppress("UNREACHABLE_CODE")
class LastSummeryFragment : Fragment(), Page {

    private var _binding: FragmentLastSummeryBinding? = null

    private lateinit var viewmodle: ExerciseRecordViewModle

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume called");
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodle.updateList(
            null,
            (activity as AppCompatActivity).supportActionBar!!,
            requireActivity().resources
        ) {
        }
        binding.rvLastSummeryRecyclerView.layoutManager = LinearLayoutManager(context)
        viewmodle.lastSummery.observe(viewLifecycleOwner, {
            binding.rvLastSummeryRecyclerView.adapter = LastSummeryRecyclerViewAdapter(it)
            binding.progressBar.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var mi = menu.findItem(R.id.id_log_out)
        mi.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewmodle.updateList(
            item,
            (activity as AppCompatActivity).supportActionBar!!,
            requireActivity().resources
        ) {}
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPageCnangeListner() {
        viewmodle.updateList(
            null,
            (activity as AppCompatActivity).supportActionBar!!,
            requireActivity().resources
        ) {
        }
        (activity as AppCompatActivity).supportActionBar?.title =
            requireActivity().resources.getString(R.string.last_seven_day)
        PieChartFragment.flage = true

    }
}

private const val TAG = "MyTag"