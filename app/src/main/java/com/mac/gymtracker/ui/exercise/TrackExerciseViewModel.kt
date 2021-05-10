package com.mac.gymtracker.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel

class TrackExerciseViewModel(repository: TrackExerciseLocalDataSource) : ViewModel() {

    private val _exerciseList = MutableLiveData<List<TrackExerciseModel>>().apply {
        value = repository.getExerciseList()
    }
    val exerciseList: LiveData<List<TrackExerciseModel>> = _exerciseList
}

class SharedVMFactory(private val repository: TrackExerciseLocalDataSource) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackExerciseViewModel(repository) as T
    }
}