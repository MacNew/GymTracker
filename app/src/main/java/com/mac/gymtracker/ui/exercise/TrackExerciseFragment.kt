package com.mac.gymtracker.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mac.gymtracker.databinding.FragmentTrackExerciseBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource

class TrackExerciseFragment : Fragment() {

    private lateinit var  trackExerciseViewModel: TrackExerciseViewModel
    private var _binding: FragmentTrackExerciseBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackExerciseViewModel = ViewModelProvider(this,SharedVMFactory(TrackExerciseLocalDataSource(activity!!.applicationContext)))
              .get(TrackExerciseViewModel::class.java);
        _binding = FragmentTrackExerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTrackExercise.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        trackExerciseViewModel.exerciseList.observe(this, Observer {
            binding.rvTrackExercise.adapter = ExerciseListAdapter(it)
        })
    }

}