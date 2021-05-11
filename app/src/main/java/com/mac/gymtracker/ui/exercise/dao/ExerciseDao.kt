package com.mac.gymtracker.ui.exercise.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel

@Dao
interface ExerciseDao {
    @Query("select *from exercise")
    fun getAll(): List<TrackExerciseModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(exercise: List<TrackExerciseModel>)
}