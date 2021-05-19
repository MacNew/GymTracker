package com.mac.gymtracker.ui.lastsummery.dao

import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel

class LastSummeryModel(
    var date: String,
    var exerciseName: String,
    var exerciseListName: String,
    var list: List<ExerciseRecordModel>
)