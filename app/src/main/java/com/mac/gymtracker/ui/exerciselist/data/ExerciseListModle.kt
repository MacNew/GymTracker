package com.mac.gymtracker.ui.exerciselist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciselist")
class ExerciseListModle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?=0,
    @ColumnInfo(name = "exercise_name")
    val name: String? = "",
    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int?= 0,
    @ColumnInfo(name = "image")
    val image: String? = ""
)