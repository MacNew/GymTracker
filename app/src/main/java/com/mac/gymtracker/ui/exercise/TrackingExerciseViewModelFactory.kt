package com.mac.gymtracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource

class  TrackingExerciseViewModelFactory(private val repository: TrackExerciseLocalDataSource) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackExerciseViewModel(repository) as T
    }
}