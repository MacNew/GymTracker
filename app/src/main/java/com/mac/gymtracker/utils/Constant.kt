package com.mac.gymtracker.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.localExerciselistRepo

const val CHEST: String = "Chest"
const val SHOULDER: String = "Shoulder"
const val BACK: String = "Back"
const val BICEPS: String = "Biceps"
const val TRICEPS: String = "Triceps"
const val LEG: String = "Leg"

const val CHEST_ID: Int = 1
const val SHOULDER_ID: Int = 2
const val BACK_ID: Int = 3
const val BICEPS_ID: Int = 4
const val TRICEPS_ID: Int = 5
const val LEG_ID: Int = 6

fun Context.workOut() {
        var list: ArrayList<TrackExerciseModel> = ArrayList()
        var exercieOne =  TrackExerciseModel(CHEST_ID, CHEST, R.drawable.ic_chest.toString())
        var exerciseTwo = TrackExerciseModel(SHOULDER_ID , SHOULDER, R.drawable.ic_shoulder.toString())
        var exerciseThreee = TrackExerciseModel(BACK_ID , BACK, R.drawable.ic_back.toString())
        var exercieFoure = TrackExerciseModel(BICEPS_ID , LEG, R.drawable.ic_leg.toString())
        var exerciseFive = TrackExerciseModel(TRICEPS_ID , BICEPS, R.drawable.ic_biceps.toString())
        var exerciseSix  = TrackExerciseModel(LEG_ID, TRICEPS, R.drawable.ic_triceps.toString())
        list.add(exercieOne)
        list.add(exerciseTwo)
        list.add(exerciseThreee)
        list.add(exercieFoure)
        list.add(exerciseFive)
        list.add(exerciseSix)
        TrackExerciseLocalDataSource(this).insertExercise(list!!)
        Log.e("TAG", "Hello mac")
}

fun Context.loadExercisList() {
        var list: ArrayList<ExerciseListModle> = ArrayList()
        list.add(ExerciseListModle(name = "Flat Bench Press", exercise_id = CHEST_ID, image = R.drawable.ic_chest.toString()))
        list.add(ExerciseListModle(name = " Incline Bench Press", exercise_id = CHEST_ID, image = R.drawable.ic_chest.toString()))
        localExerciselistRepo(this).insertExercise(list!!)
}

fun Context.showToast(message:String) {
        Toast.makeText(this, "Data $message", Toast.LENGTH_LONG).show()
}
