package com.mac.gymtracker.ui.exerciselist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseList {
    @Query("select *from exercise_list where exercise_id in (:id)")
    fun getAll(id:Int): LiveData<List<ExerciseListModle>>

    @Query("select *from exercise_list where exercise_name=:name")
    fun getExercise(name:String): Single<ExerciseListModle>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list:List<ExerciseListModle>): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun  insert(data:ExerciseListModle) : Completable
}