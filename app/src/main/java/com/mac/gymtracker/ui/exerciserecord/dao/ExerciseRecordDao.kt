package com.mac.gymtracker.ui.exerciserecord.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*

@Dao
interface ExerciseRecordDao {
    @Query("select *from exercise_record where roomDate between :currentDate and :oneWeekAfterDate")
    fun getAll(currentDate: Long, oneWeekAfterDate: Long): Single<List<ExerciseRecordModel>>

    @Query("select *from exercise_record")
    fun getAllCheck(): Single<List<ExerciseRecordModel>>


    @Insert()
    fun insert(exerciseList: List<ExerciseRecordModel>): Completable
}