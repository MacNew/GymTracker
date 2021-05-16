package com.mac.gymtracker.ui.exerciserecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo

class ExerciseRecordViewModle(repository: ExerciseRecordRepo, exerciseName: String) : ViewModel() {
   // private val _exerciseRecord

}


class ExerciseRecord(private val repository: ExerciseRecordRepo,
                                   private val exerciseName:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExerciseRecordViewModle(repository, exerciseName) as T
    }
}
