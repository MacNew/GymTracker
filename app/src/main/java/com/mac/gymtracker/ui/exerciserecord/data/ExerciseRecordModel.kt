package com.mac.gymtracker.ui.exerciserecord.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercise_record")
class ExerciseRecordModel(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date:String,
    @ColumnInfo(name = "exercise_name")
    val exerciseName: String?,
    @ColumnInfo(name = "weight")
    val weight: String?,
    @ColumnInfo(name = "reps")
    val reps: String?,
    @ColumnInfo(name = "Set")
    val set: String?,
    @ColumnInfo(name = "saveTime")
    var saveTime: String
) : Serializable