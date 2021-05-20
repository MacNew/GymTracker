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
    var saveTime: String,
    @ColumnInfo(name = "mainExercise")
    var mainExercise: String,
    @ColumnInfo(name = "image")
    var image: String
) : Serializable {
    override fun toString(): String {
        return "ExerciseRecordModel(date='$date', exerciseName=$exerciseName, weight=$weight, reps=$reps, set=$set, saveTime='$saveTime', mainExercise='$mainExercise')"
    }
}