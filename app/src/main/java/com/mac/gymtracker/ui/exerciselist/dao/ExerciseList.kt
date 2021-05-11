package com.mac.gymtracker.ui.exerciselist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle

@Dao
interface ExerciseList {
    @Query("select *from exerciselist where exercise_id in (:id)")
    fun getAll(id:Int): List<ExerciseListModle>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(exercise:ExerciseListModle)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list:List<ExerciseListModle>)
}