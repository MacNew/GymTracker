package com.mac.gymtracker.ui.exercise.dao

import androidx.room.*
import com.mac.gymtracker.ui.exercise.data.ExerciseModel

@Dao
interface ExerciseDao {
    @Query("select *from exercise")
    fun getAll(): List<ExerciseModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(exercise: List<ExerciseModel>)
}