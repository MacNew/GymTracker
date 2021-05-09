package com.mac.gymtracker.ui.exercise.data

import android.content.Context
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao

class ExerciseLocalDataSource(context: Context) {

    private var exerciseDao: ExerciseDao = GymTrackerDatabase.getDatabase(context).exerciseDao()

    fun insertExercise(list:List<ExerciseModel>) {
        exerciseDao.insertAll(list)
    }
}