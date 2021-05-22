package com.mac.gymtracker.ui.exerciserecord

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import java.util.*
import kotlin.Comparator

class ExerciseRecordViewModle(
    var repository: ExerciseRecordRepo,
    var exerciseId: String,
    var trackExericsRepo: TrackExerciseLocalDataSource?
) : ViewModel() {


    private val _exerciseRecord = MutableLiveData<List<ExerciseRecordModel>>().apply {
        var currentDate = Date().time - (86400*1000)
        var oneweekAfterDate = currentDate + (604800*1000)

        repository.getAll(currentDate, oneweekAfterDate) {
            value = it
        }

    }
    val exerciseRecord: LiveData<List<ExerciseRecordModel>> = _exerciseRecord

    private val _lastSummery = MutableLiveData<List<LastSummeryModel>>();

    val lastSummery: LiveData<List<LastSummeryModel>> = _lastSummery

    val _actualExerciseName = MutableLiveData<String>();

    val actualExerciseName: LiveData<String> = _actualExerciseName

    fun updateMainExerciseName(exerciseId: String) {
        trackExericsRepo?.getMainExerciseName(exerciseId) { error, exerciseName, isError ->
            if (!isError) {
                _actualExerciseName.postValue(exerciseName)
            } else {
                Log.e("msg", "Error " + error?.message)
            }
        }
    }

    fun updateList(lastSummery: ArrayList<LastSummeryModel>) {
        Collections.sort(lastSummery,
            Comparator<LastSummeryModel?> { o1, o2 ->
                o2!!.date.toLong().compareTo(o1!!.date.toLong())
            })
        _lastSummery.postValue(lastSummery)
    }


    fun addToLocalDatabase(
        recordList: ArrayList<ExerciseRecordModel>,
        function: (result: Boolean) -> Unit
    ) {
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


class ExerciseRecordFactory(
    private val repository: ExerciseRecordRepo,
    private val exerciseName: String, var trackExericsRepo: TrackExerciseLocalDataSource?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExerciseRecordViewModle(repository, exerciseName, trackExericsRepo) as T
    }
}
