package com.mac.gymtracker.ui.exerciserecord.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import io.reactivex.Completable
import io.reactivex.Single

import kotlin.collections.ArrayList

@Dao
interface ExerciseRecordDao {
    @Query("select *from exercise_record where roomDate between :currentDate and :oneWeekAfterDate")
    fun getAll(currentDate: Long, oneWeekAfterDate: Long): Single<List<ExerciseRecordModel>>

    @Query("select *from exercise_record")
    fun getAllCheck(): Single<List<ExerciseRecordModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(exerciseList: List<ExerciseRecordModel>): Completable

    @Query("select *from exercise_record where string_format_date=:date")
    fun getListByDate(date: String) : Single<List<ExerciseRecordModel>>

    @Query("update exercise_record set exercise_name=:name, image=:imageString where exercise_name=:previousName")
    fun editQuery(previousName: String, name: String, imageString: String?) : Completable

    @Query("delete from exercise_record where exercise_name=:name")
    fun deleteQuery(name: String): Completable

}