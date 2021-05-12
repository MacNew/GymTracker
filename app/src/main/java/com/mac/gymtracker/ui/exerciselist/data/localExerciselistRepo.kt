package com.mac.gymtracker.ui.exerciselist.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList
import com.mac.gymtracker.utils.subscribeONNewThread

class localExerciselistRepo(context:Context) {

    private var repo: ExerciseList = GymTrackerDatabase.getDatabase(context).exerciseListDao()

    fun insertExercise(list:List<ExerciseListModle>, message:(errorMsg:Boolean)->Unit) {
        repo.insertAll(list).subscribeONNewThread {
            _, isError->
            if (isError) {
                message(isError)
            } else {
                message(isError)
            }
        }
    }

    fun getExerciseListById(id:Int): LiveData<List<ExerciseListModle>> {
        return  repo.getAll(id)
    }

}