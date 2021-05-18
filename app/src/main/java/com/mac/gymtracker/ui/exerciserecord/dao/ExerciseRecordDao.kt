package com.mac.gymtracker.ui.exerciserecord.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import io.reactivex.Completable

@Dao
interface ExerciseRecordDao {
    @Query("select *from exercise_record")
    fun getAll(): LiveData<List<ExerciseRecordModel>>

    @Insert()
    fun insert(exerciseList: List<ExerciseRecordModel>): Completable
}