package com.mac.gymtracker.ui.exerciserecord.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciserecord.dao.ExerciseRecordDao
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.Single

class ExerciseRecordRepo(context: Context) {
    private var repo: ExerciseRecordDao = GymTrackerDatabase.getDatabase(context).exerciseRecordDao()
    fun insertRecord(record:ExerciseRecordModel, message:(errorMsg:Boolean)-> Unit) {
        repo.insert(record).subscribeONNewThread {
          error, isError->
          if (isError) {
             message(isError)
          } else {
              Log.e("Error", isError.toString())
              message(isError)
          }
        }
    }

    fun getAll(): LiveData<List<ExerciseRecordModel>> {
       return  repo.getAll();
    }

}