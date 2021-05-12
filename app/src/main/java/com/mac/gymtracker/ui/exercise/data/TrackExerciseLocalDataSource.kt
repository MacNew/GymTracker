package com.mac.gymtracker.ui.exercise.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao
import com.mac.gymtracker.utils.subscribeONNewThread

class TrackExerciseLocalDataSource(context: Context) {

    private var exerciseDao: ExerciseDao = GymTrackerDatabase.getDatabase(context).exerciseDao()

    fun insertExercise(list:List<TrackExerciseModel>, isErrorMsg:(isError:Boolean)->Unit) {
        exerciseDao.insertAll(list).subscribeONNewThread {
                _, isError ->
            if(isError) {
                isErrorMsg(true)
            } else {
                isErrorMsg(false)
            }

        }
    }

    fun getExerciseList() : LiveData<List<TrackExerciseModel>> {
        return exerciseDao.getAll()
    }
}