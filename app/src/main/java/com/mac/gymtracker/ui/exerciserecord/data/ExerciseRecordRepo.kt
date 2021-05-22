package com.mac.gymtracker.ui.exerciserecord.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciserecord.dao.ExerciseRecordDao
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ExerciseRecordRepo(context: Context) {
    private var repo: ExerciseRecordDao =
        GymTrackerDatabase.getDatabase(context).exerciseRecordDao()

    fun insertRecord(record: List<ExerciseRecordModel>, message: (errorMsg: Boolean) -> Unit) {
        var date = Date().time.toString()
        var roomDate = Date()
        record.map {
            it.saveTime = date
            it.roomDate = roomDate
        }
        repo.insert(record).subscribeONNewThread { error, isError ->
            if (isError) {
                message(isError)
                Log.e("msg", error?.message!!)
            } else {
                Log.e("msg", "Success on Inserting value")
                message(isError)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getAll(
        currentDate: Long,
        oneweekAfterDate: Long,
        function: (record: List<ExerciseRecordModel>) -> Unit
    ) {

      //  repo.getAllCheck().
             repo.getAll(currentDate, oneweekAfterDate).


        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e("exerciseRec", "Message doOnError" + it.message!!)
            }.subscribe({
                     Log.e("tage", "Size of list"+it.size.toString())
                function(it)
            }) {
                Log.e("exerciseRec", "Message it Error" + it.message!!)
            }
    }

}