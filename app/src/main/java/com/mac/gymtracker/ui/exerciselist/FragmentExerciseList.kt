package com.mac.gymtracker.ui.exerciselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.databinding.FregmentExerciseListBinding
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.utils.getNavigationController

class FragmentExerciseList : Fragment() {
    private lateinit var exerciseListViewModel: ExerciseListViewModel
    private var _binding: FregmentExerciseListBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exerciseListViewModel = ViewModelProvider(
            this,
            ExerciseListViewModelFactory(
                LocalExerciselistRepo(requireActivity().applicationContext),
                FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseid
            )
        ).get(ExerciseListViewModel::class.java)
        _binding = FregmentExerciseListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding!!.toolbarExerciseList.title =
            FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseName
        binding!!.toolbarExerciseList.setNavigationOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })
        binding!!.rvTrackExerciseList.layoutManager = GridLayoutManager(
            activity?.applicationContext, 2
        )
        exerciseListViewModel.exerciseList.observe(viewLifecycleOwner, {
            it.observe(viewLifecycleOwner, { list ->
                binding!!.rvTrackExerciseList.adapter =
                    ExerciseListAdapter(
                        list,
                        FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseid,
                        { list ->
                            requireActivity().getNavigationController().navigate(
                                FragmentExerciseListDirections.actionFragmentExerciseListToFragmentAddNew(
                                    FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseid,
                                    FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseName!!,
                                    list
                                )
                            )
                        }) { name, image ->
                        requireActivity().getNavigationController().navigate(
                            FragmentExerciseListDirections.actionFragmentExerciseListToFragmentExerciseRecord(
                                name,
                                image,
                                FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseName
                            )
                        )
                    }
            })
        })
        binding!!.addNewBtn.setOnClickListener {
            requireActivity().getNavigationController().navigate(
                FragmentExerciseListDirections.actionFragmentExerciseListToFragmentAddNew(
                    FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseid,
                    FragmentExerciseListArgs.fromBundle(requireArguments()).exerciseName!!
                )
            )
        }
    }
}


