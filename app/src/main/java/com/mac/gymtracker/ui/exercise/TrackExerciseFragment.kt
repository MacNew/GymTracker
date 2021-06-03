package com.mac.gymtracker.ui.exercise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentTrackExerciseBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.utils.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TrackExerciseFragment : Fragment() {

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
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.rvTrackExercise.layoutManager = GridLayoutManager(
            activity?.applicationContext,
            2
        )
        trackExerciseViewModel.exerciseList.observe(viewLifecycleOwner, Observer { it ->
            it.observe(viewLifecycleOwner, {
                binding.rvTrackExercise.adapter = ExerciseAdapter(it) { string->
                    addCardViewListner(string)
                }
            })
        })
        // threadCheck()
    }


    private fun addCardViewListner(it: String) {
        val navController = requireActivity().findNavController(R.id.nav_host_fragment_content_main)
        when (it) {
            CHEST -> {
                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        CHEST_ID, CHEST
                    )
                )
            }
            SHOULDER -> {

                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        SHOULDER_ID, SHOULDER
                    )
                )
            }
            BACK -> {
                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        BACK_ID, BACK
                    )
                )
            }
            LEG -> {
                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        LEG_ID, LEG
                    )
                )
            }

            BICEPS -> {
                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        BICEPS_ID, BICEPS
                    )
                )
            }
            TRICEPS -> {
                requireActivity().getNavigationController().navigate(
                    TrackExerciseFragmentDirections.actionNavTrackExerciseToFragmentExerciseList(
                        TRICEPS_ID, TRICEPS
                    )
                )

            }
        }
    }
}