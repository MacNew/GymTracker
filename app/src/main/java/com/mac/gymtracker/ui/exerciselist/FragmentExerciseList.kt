package com.mac.gymtracker.ui.exerciselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.databinding.FregmentExerciseListBinding
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.utils.CHEST_ID
import com.mac.gymtracker.utils.showToast

class FragmentExerciseList: Fragment() {
   private lateinit var exerciseListViewModel: ExerciseListViewModel
   private var _binding: FregmentExerciseListBinding?= null
   private val binding get() = _binding

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      exerciseListViewModel = ViewModelProvider(this,
         ExerciseListViewModelFactory(
            LocalExerciselistRepo(activity!!.applicationContext),
            FragmentExerciseListArgs.fromBundle(arguments!!).exerciseid
         )).get(ExerciseListViewModel::class.java)
      _binding = FregmentExerciseListBinding.inflate(inflater, container, false)
      return binding!!.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      var exerciseId = FragmentExerciseListArgs.fromBundle(arguments!!).exerciseid
      activity!!.applicationContext.showToast("Exercise id $exerciseId")
   }
}