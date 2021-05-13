package com.mac.gymtracker.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel

class TrackExerciseViewModel(repository: TrackExerciseLocalDataSource) : ViewModel() {

    private val _exerciseList = MutableLiveData<LiveData<List<TrackExerciseModel>>>().apply {
        value = repository.getExerciseList()
    }
    val exerciseList: LiveData<LiveData<List<TrackExerciseModel>>> = _exerciseList
}


