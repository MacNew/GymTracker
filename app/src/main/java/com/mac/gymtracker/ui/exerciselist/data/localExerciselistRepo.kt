package com.mac.gymtracker.ui.exerciselist.data

import android.content.Context
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList

class localExerciselistRepo(context:Context) {

    private var repo: ExerciseList = GymTrackerDatabase.getDatabase(context).exerciseListDao()

    fun insertExercise(list:List<ExerciseListModle>) {
        repo.insertAll(list)
    }

    fun getExerciseListById(id:Int): List<ExerciseListModle> {
        return  repo.getAll(id)
    }

}