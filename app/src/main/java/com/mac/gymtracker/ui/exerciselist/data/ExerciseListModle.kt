package com.mac.gymtracker.ui.exerciselist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "exercise_list")
data class ExerciseListModle(
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "exercise_name")
    var name: String,
    @ColumnInfo(name = "exercise_id")
    val exercise_id: Int? = 0,
    @ColumnInfo(name = "image")
    var image: String? = "",
    @ColumnInfo(name = "stringImage")
    var imageString: String? = null,
    @ColumnInfo(name = "mydate")
    val date: Long,
    @ColumnInfo(name ="isSync")
    val isSync: Boolean
) : Serializable {
    override fun toString(): String {
        return "ExerciseListModle(name='$name', exercise_id=$exercise_id, image=$image, imageString=$imageString, date=$date)"
    }
}