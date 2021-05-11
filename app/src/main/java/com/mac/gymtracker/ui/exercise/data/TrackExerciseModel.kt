package com.mac.gymtracker.ui.exercise.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
class TrackExerciseModel(
    @PrimaryKey @ColumnInfo(name = "e_id") val eId:Int,
    @ColumnInfo(name = "exercise_name") val name:String?,
    @ColumnInfo(name = "image") val image:String?
)