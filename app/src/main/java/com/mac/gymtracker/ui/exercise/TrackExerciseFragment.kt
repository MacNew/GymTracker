package com.mac.gymtracker.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentTrackExerciseBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciselist.FragmentExerciseList
import com.mac.gymtracker.utils.*

class TrackExerciseFragment<T> : Fragment() {

    private lateinit var trackExerciseViewModel: TrackExerciseViewModel
    private var _binding: FragmentTrackExerciseBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackExerciseViewModel = ViewModelProvider(
            this,
            TrackingExerciseViewModelFactory(TrackExerciseLocalDataSource(activity!!.applicationContext))
        ).get(TrackExerciseViewModel::class.java);
        _binding = FragmentTrackExerciseBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTrackExercise.layoutManager = GridLayoutManager(
            activity?.applicationContext,
            2
        ) //LinearLayoutManager(activity!!.applicationContext)
        trackExerciseViewModel.exerciseList.observe(this, Observer { it ->
            it.observe(this, {
                binding.rvTrackExercise.adapter = ExerciseListAdapter(it) {
                    addCardViewListner(it)
                }
            })
        })
    }

    private fun addCardViewListner(it: String) {
        val navController = activity!!.findNavController(R.id.nav_host_fragment_content_main)
        when (it) {
            CHEST -> {
                context!!.showToast("Chest clicked")
                navController.navigate(TrackExerciseFragmentDirections.
                actionNavTrackExerciseToFragmentExerciseList(
                    CHEST_ID))
            }
            SHOULDER -> {
                context!!.showToast("Shoulder")

            }
            BACK -> {

            }
            LEG -> {

            }

            BICEPS -> {

            }
            TRICEPS -> {

            }
        }
    }
}