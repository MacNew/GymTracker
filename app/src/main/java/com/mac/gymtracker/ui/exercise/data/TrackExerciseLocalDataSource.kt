package com.mac.gymtracker.ui.exercise.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrackExerciseLocalDataSource(context: Context) {

    private var exerciseDao: ExerciseDao = GymTrackerDatabase.getDatabase(context).exerciseDao()

    fun insertExercise(objects:TrackExerciseModel, isErrorMsg:(isError:Boolean)->Unit) {
        exerciseDao.insert(objects).subscribeONNewThread {
                _, isError ->
            if(isError) {
                isErrorMsg(true)
            } else {
                isErrorMsg(false)
            }
        }
    }
    @SuppressLint("CheckResult")
    fun getMainExerciseName(primaryKey: String, message:(error:Throwable?,exerciseName:String?,isError:Boolean)->Unit) {
         exerciseDao.getMainExerciseName(primaryKey).subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .doOnError {
                 message(it, null, true)
             }.subscribe({
                 message(null, it, false)
             }){
                message(it,null, true)
             }
    }

    fun getExerciseRxJava() = exerciseDao.getAllRxJava()

    fun getExerciseList() : LiveData<List<TrackExerciseModel>> {
        return exerciseDao.getAll()
    }
}