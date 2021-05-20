package com.mac.gymtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mac.gymtracker.ui.exercise.dao.ExerciseDao
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciserecord.dao.ExerciseRecordDao
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel


@Database(entities = [TrackExerciseModel::class, ExerciseListModle::class
                     , ExerciseRecordModel::class
                     ] , version = 2, exportSchema = false)
abstract class GymTrackerDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseListDao(): ExerciseList
    abstract fun exerciseRecordDao(): ExerciseRecordDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE exercise_record "
                            + " ADD COLUMN mainExercise TEXT"
                )
            }
        }

        @Volatile
        private var INSTANCE: GymTrackerDatabase? = null
        fun getDatabase(context: Context): GymTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymTrackerDatabase::class.java,
                    "gymtracker").
                    fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                instance
            }
        }
    }
}