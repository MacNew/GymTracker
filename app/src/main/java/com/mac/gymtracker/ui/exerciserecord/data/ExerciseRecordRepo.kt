package com.mac.gymtracker.ui.exerciserecord.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciserecord.dao.ExerciseRecordDao
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.Single
import java.util.*

class ExerciseRecordRepo(context: Context) {
    private var repo: ExerciseRecordDao = GymTrackerDatabase.getDatabase(context).exerciseRecordDao()
    fun insertRecord(record:List<ExerciseRecordModel>, message:(errorMsg:Boolean)-> Unit) {
        var date = Date().time.toString()
        record.map {
            it.saveTime = date
        }
        repo.insert(record).subscribeONNewThread {
          error, isError->
          if (isError) {
             message(isError)
             Log.e("msg" , error?.message!!)
          } else {
              Log.e("msg", "Success on Inserting value")
              message(isError)
          }
        }
    }

    fun getAll(): LiveData<List<ExerciseRecordModel>> {
       return  repo.getAll();
    }

}