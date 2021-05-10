package com.mac.gymtracker.utils

import android.content.Context
import android.widget.Toast
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel

fun Context.loadValue() {
        var list: ArrayList<TrackExerciseModel> = ArrayList()
        var exercieOne =  TrackExerciseModel(1, "Chest", R.drawable.ic_chest)
        var exerciseTwo = TrackExerciseModel(2 , "Shoulder", R.drawable.ic_shoulder)
        var exerciseThreee = TrackExerciseModel(3 , "Back", R.drawable.ic_back)
        var exercieFoure = TrackExerciseModel(4 , "Leg", R.drawable.ic_leg)
        var exerciseFive = TrackExerciseModel(5 , "Biceps", R.drawable.ic_biceps)
        var exerciseSix  = TrackExerciseModel(6, "Triceps", R.drawable.ic_triceps)
        list.add(exercieOne)
        list.add(exerciseTwo)
        list.add(exerciseThreee)
        list.add(exercieFoure)
        list.add(exerciseFive)
        list.add(exerciseSix)
        TrackExerciseLocalDataSource(this).insertExercise(list!!)
}

fun Context.showToast(message:String) {
        Toast.makeText(this, "Data $message", Toast.LENGTH_LONG).show()
}
