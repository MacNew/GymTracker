package com.mac.gymtracker.ui.exerciselist.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseList {
    @Query("select *from exercise_list where exercise_id in (:id) order by mydate desc")
    fun getAll(id:Int): LiveData<List<ExerciseListModle>>

    @Query("select *from exercise_list where exercise_name=:name")
    fun getExercise(name:String): Single<ExerciseListModle>

    @Query("select exercise_name, exercise_id, stringImage, mydate, isSync from exercise_list")
    fun getAlExercise(): Single<List<ExerciseListModle>>

    @Query("select *from exercise_list where isSync=:isSync")
    fun getAllExercise(isSync:Boolean) : Single<List<ExerciseListModle>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(list:ExerciseListModle): Completable

    @Update
    fun updateAll(list:List<ExerciseListModle>) : Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun  insert(data:ExerciseListModle) : Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun  insert(data: List<ExerciseListModle>) : Completable

    @Query("Update exercise_list set exercise_name=:newName, stringImage=:newImage where exercise_name=:priviousName")
    fun editContent(newName:String, newImage:String , priviousName:String): Completable

    @Delete
    fun deleteExercise(objects: ExerciseListModle) : Completable

    @Query("delete from exercise_list where exercise_name=:exerciseName")
    fun delete(exerciseName: String?): Completable

    @Query("select *from exercise_list where exercise_name=:exerciseName")
    fun getImage(exerciseName: String?) : Single<ExerciseListModle>


}