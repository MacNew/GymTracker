package com.mac.gymtracker.ui.exerciserecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import java.util.ArrayList

class ExerciseRecordViewModle(var repository: ExerciseRecordRepo, exerciseName: String) : ViewModel() {

    private val _exerciseRecord = MutableLiveData<LiveData<List<ExerciseRecordModel>>>().apply {
        value = repository.getAll()
    }


    fun addToLocalDatabase(recordList: ArrayList<ExerciseRecordModel>, function:(result:Boolean)-> Unit) {
         repository.insertRecord(recordList!!) {
             if (!it) {
                 Log.e("msg", "clear done")
                 recordList.clear()
                 function(true)
             } else {
                 function(false)
                 Log.e("msg", "Not clear")
             }
         }
    }

}


class ExerciseRecordFactory(private val repository: ExerciseRecordRepo,
                                   private val exerciseName:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExerciseRecordViewModle(repository, exerciseName) as T
    }
}
