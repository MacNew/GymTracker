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
        viewmodle.updateList(null)
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
        viewmodle.updateList(item)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}