package com.mac.gymtracker.ui.exercise.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseDao {
    @Query("select *from exercise")
    fun getAll(): LiveData<List<TrackExerciseModel>>

    @Query("select exercise_name from exercise where e_id=:primaryKey")
    fun getMainExerciseName(primaryKey: String): Single<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(exercise: List<TrackExerciseModel>) : Completable
}