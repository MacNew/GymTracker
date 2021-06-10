package com.mac.gymtracker.ui.exerciserecord

import android.content.res.Resources
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentReportBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class ExerciseRecordViewModle(
    var repository: ExerciseRecordRepo,
    var trackExericsRepo: TrackExerciseLocalDataSource?
) : ViewModel() {

    private val _lastSummery = MutableLiveData<List<LastSummeryModel>>();

    val lastSummery: LiveData<List<LastSummeryModel>> = _lastSummery

    private val _stringDate = MutableLiveData<String>()

    val stringDate: LiveData<String> = _stringDate


    fun updateList(lastSummery: ArrayList<LastSummeryModel>, binding: FragmentReportBinding, date:String) {
        Collections.sort(lastSummery,
            Comparator<LastSummeryModel?> { o1, o2 ->
                o2!!.date.toLong().compareTo(o1!!.date.toLong())
            })
        if (lastSummery.isEmpty()) {
            binding.cardViewMsg.visibility = View.VISIBLE
            binding.tvMessageError.text = "No data Found on $date"
        } else {
            binding.cardViewMsg.visibility = View.INVISIBLE
        }
        _lastSummery.postValue(lastSummery)
    }

    fun updateList(item:MenuItem?, supportActionBar: ActionBar, resources: Resources?,function:()->Unit ) {
        val currentDate:Long = Date().time
        var oneweekbefore: Long = (604800 * 1000)
        when (item?.itemId) {
            R.id.id_one_day -> {
                supportActionBar.title = resources?.getString(R.string.last_one_day)
                oneweekbefore =  currentDate - (86400 * 1000)
            }
            R.id.id_seven_day -> {
                supportActionBar.title = resources?.getString(R.string.last_seven_day)
                oneweekbefore = currentDate - (86400*7 * 1000)

            }
            R.id.id_one_month -> {
                supportActionBar.title = resources?.getString(R.string.last_one_month_data)
                var data:Long = 86400*30
                var newdata:Long = data * 1000
                oneweekbefore = currentDate - newdata
            }
            R.id.id_three_month -> {
                supportActionBar.title = resources?.getString(R.string.last_three_month_data)
                var data:Long = 86400*30*3
                var newdata:Long = data * 1000
                oneweekbefore = currentDate - newdata
            }
            R.id.id_six_month -> {
                supportActionBar.title = resources?.getString(R.string.last_six_month_data)
                var data:Long = 86400*30*6
                var newdata:Long = data * 1000
                oneweekbefore = currentDate - newdata
            }
            R.id.id_one_year -> {
                supportActionBar.title = resources?.getString(R.string.last_one_year_data)
                var data:Long = 86400*30*12
                var newdata:Long = data * 1000
                oneweekbefore = currentDate - newdata
            }
        }

        Log.e("TAG", (oneweekbefore).toString())
        Log.e("TAG", (currentDate).toString() )

        repository.getAll(oneweekbefore, Date().time) { list->
            val hsMap: HashMap<String, ArrayList<ExerciseRecordModel>> = HashMap()
            val key: HashSet<String> = HashSet()
            list.forEach { exerciseRecord ->
                key.add(exerciseRecord.saveTime)
                if (!hsMap.containsKey(exerciseRecord.saveTime)) {
                    val exerciseRecordList: ArrayList<ExerciseRecordModel> = ArrayList()
                    exerciseRecordList.add(exerciseRecord)
                    hsMap[exerciseRecord.saveTime] = exerciseRecordList
                } else {
                    hsMap[exerciseRecord.saveTime]!!.add(exerciseRecord)
                }
            }
            val lastSummeryModel: ArrayList<LastSummeryModel> = ArrayList()
            key.forEach {
                lastSummeryModel.add(
                    LastSummeryModel(
                        false, it,
                        hsMap[it]?.get(0)!!.mainExercise, hsMap[it]?.get(0)?.exerciseName!!,
                        hsMap[it]?.get(0)!!.image!!, hsMap[it]
                    )
                )
            }
            Collections.sort(lastSummeryModel,
                Comparator<LastSummeryModel?> { o1, o2 ->
                    o2!!.date.toLong().compareTo(o1!!.date.toLong())
                })
            function()
           _lastSummery.postValue(lastSummeryModel)
        }
    }


    fun addToLocalDatabase(
        recordList: ArrayList<ExerciseRecordModel>,
        function: (result: Boolean) -> Unit
    ) {
        repository.insertRecord(record = recordList) {
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

    fun sendDate(stringDate: String) {
        _stringDate.postValue(stringDate)
    }



}


@Suppress("UNCHECKED_CAST")
class ExerciseRecordFactory(
    private val repository: ExerciseRecordRepo,
    private val exerciseName: String, var trackExericsRepo: TrackExerciseLocalDataSource?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExerciseRecordViewModle(repository, trackExericsRepo) as T
    }
}
