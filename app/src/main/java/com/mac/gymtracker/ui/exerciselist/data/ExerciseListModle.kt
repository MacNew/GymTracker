package com.mac.gymtracker.ui.exerciselist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import kotlin.properties.Delegates

@Entity(tableName = "exercise_list")
data class ExerciseListModle(
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "exercise_name")
    val name: String,
    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int?= 0,
    @ColumnInfo(name = "image")
    val image: String? = "",
    @ColumnInfo(name= "stringImage")
    val imageString:String? = null,
    @ColumnInfo(name= "mydate")
    val date: Long
)