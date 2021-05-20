package com.mac.gymtracker.ui.lastsummery.dao

import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import java.util.ArrayList

class LastSummeryModel(
    var date: String,
    var exerciseName: String,
    var exerciseListName: String,
    var image: String,
    var list: ArrayList<ExerciseRecordModel>?
) {
    override fun toString(): String {
        return "LastSummeryModel(date='$date', exerciseName='$exerciseName', exerciseListName='$exerciseListName', list=$list)"
    }
}