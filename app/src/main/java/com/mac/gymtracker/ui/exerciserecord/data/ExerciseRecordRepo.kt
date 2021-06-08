package com.mac.gymtracker.ui.exerciserecord.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciserecord.dao.ExerciseRecordDao
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExerciseRecordRepo(context: Context) {
    private var repo: ExerciseRecordDao =
        GymTrackerDatabase.getDatabase(context).exerciseRecordDao()

    fun insertRecord(record: List<ExerciseRecordModel>, message: (errorMsg: Boolean) -> Unit) {
        var date = Date().time.toString()
        var roomDate = Date()
        var sdf = SimpleDateFormat("d MMM yyyy")
        var stringformatedDate  = sdf.format(roomDate)
        record.map {
            it.saveTime = date
            it.roomDate = roomDate
            it.stringFormatDate = stringformatedDate
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
        repo.getAll(currentDate, oneweekAfterDate).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e("exerciseRec", "Message doOnError" + it.message!!)
            }.subscribe({
                Log.e("tage", "Size of list" + it.size.toString())
                function(it)
            }) {
                Log.e("exerciseRec", "Message it Error" + it.message!!)
            }
    }

    fun getAll() = repo.getAllCheck();

    @SuppressLint("CheckResult")
    fun getListByDate(value: String?, function: (record: ArrayList<ExerciseRecordModel>) -> Unit) {
        repo.getListByDate(value!!).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e("exerciseRec", "Message do" + it.message!!)

            }.subscribe({
                var arrayList:ArrayList<ExerciseRecordModel> = ArrayList()
                it.forEach { listmac ->
                    arrayList.add(listmac)
                }
                function(arrayList)
            }){
                Log.e("exerciseRec", "Message it" + it.message!!)
            }
    }

    fun editExerciseRecordContent(previousName: String, exerciseListModle: ExerciseListModle) =
        repo.editQuery(previousName, exerciseListModle.name, exerciseListModle.imageString)

    fun deleteContent(name: String) = repo.deleteQuery(name)
}