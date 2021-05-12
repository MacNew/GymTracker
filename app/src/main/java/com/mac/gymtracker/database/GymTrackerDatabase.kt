package com.mac.gymtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle

@Database(entities = [TrackExerciseModel::class, ExerciseListModle::class] , version = 2)
abstract class GymTrackerDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseListDao(): ExerciseList

    companion object {
        @Volatile
        private var INSTANCE: GymTrackerDatabase? = null
        fun getDatabase(context: Context): GymTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymTrackerDatabase::class.java,
                    "gymtracker").
                build()
                INSTANCE = instance
                instance
            }
        }
    }
}