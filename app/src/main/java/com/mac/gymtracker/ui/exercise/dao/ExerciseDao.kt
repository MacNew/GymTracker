package com.mac.gymtracker.ui.exercise.dao

import androidx.room.*
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel

@Dao
interface ExerciseDao {
    @Query("select *from exercise")
    fun getAll(): List<TrackExerciseModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(exercise: List<TrackExerciseModel>)
}