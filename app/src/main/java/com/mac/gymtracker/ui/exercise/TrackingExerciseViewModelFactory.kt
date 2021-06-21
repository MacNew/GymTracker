package com.mac.gymtracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.utils.PrefUtils

class  TrackingExerciseViewModelFactory(
    private val repository: TrackExerciseLocalDataSource,
    private var db: FirebaseFirestore?,
    private var instance: PrefUtils?

) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackExerciseViewModel(repository,db, instance) as T
    }
}