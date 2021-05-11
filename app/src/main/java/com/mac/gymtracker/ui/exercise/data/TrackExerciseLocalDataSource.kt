package com.mac.gymtracker.ui.exercise.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao

class TrackExerciseLocalDataSource(context: Context) {

    private var exerciseDao: ExerciseDao = GymTrackerDatabase.getDatabase(context).exerciseDao()

    fun insertExercise(list:List<TrackExerciseModel>) {
        exerciseDao.insertAll(list)
    }

    fun getExerciseList() : List<TrackExerciseModel> {
        return exerciseDao.getAll()
    }
}