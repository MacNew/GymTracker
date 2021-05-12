package com.mac.gymtracker.ui.exerciselist.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.properties.Delegates

@Entity(tableName = "exerciselist")
data class ExerciseListModle(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "exercise_name")
    val name: String,
    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int?= 0,
    @ColumnInfo(name = "image")
    val image: String? = ""
)