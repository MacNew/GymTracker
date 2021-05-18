package com.mac.gymtracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
    var exercieOne = TrackExerciseModel(CHEST_ID, CHEST, R.drawable.ic_chest.toString())
    var exerciseTwo = TrackExerciseModel(SHOULDER_ID, SHOULDER, R.drawable.ic_shoulder.toString())
    var exerciseThreee = TrackExerciseModel(BACK_ID, BACK, R.drawable.ic_back.toString())
    var exercieFoure = TrackExerciseModel(BICEPS_ID, LEG, R.drawable.ic_leg.toString())
    var exerciseFive = TrackExerciseModel(TRICEPS_ID, BICEPS, R.drawable.ic_biceps.toString())
    var exerciseSix = TrackExerciseModel(LEG_ID, TRICEPS, R.drawable.ic_triceps.toString())
    list.add(exercieOne)
    list.add(exerciseTwo)
    list.add(exerciseThreee)
    list.add(exercieFoure)
    list.add(exerciseFive)
    list.add(exerciseSix)
    TrackExerciseLocalDataSource(this).insertExercise(list!!) {
        if (!it)
            loadExercisList(this)
    }
    Log.e("TAG", "Hello mac")
}

private fun loadExercisList(context: Context) {
    var image: String = R.drawable.ic_chest.toString()
    var list: ArrayList<ExerciseListModle> = ArrayList()
    list.addExercise("Barbell Bench Press", R.drawable.ic_barbell_bench_press.toString())
    list.addExercise("Incline Bench Press", R.drawable.ic_incline_bench_press.toString())
    list.addExercise("Dumbbell Bench Press", R.drawable.ic_dumbbell_bench_press.toString())
    list.addExercise("Decline Bench Press", R.drawable.ic_decline_press.toString())
    list.addExercise("Machine Chest Press", R.drawable.ic_machine_chest_press.toString())
    list.addExercise("Push-Up", R.drawable.ic_push_up.toString())
    list.addExercise("Dip", R.drawable.ic_dip.toString())
    list.addExercise("Chest Fly", R.drawable.ic_chest_fly.toString())
    list.addExercise("Dumbbell Pull-Over", R.drawable.ic_dumbbell_pull_over.toString())
    list.addExercise("Machine Fly", R.drawable.ic_machine_fly.toString())
    LocalExerciselistRepo(context).insertExercise(list!!) {
    }
}

private fun <E> java.util.ArrayList<E>.addExercise(name: String, image: String) {
    this as ArrayList<ExerciseListModle>
    this.add(
        ExerciseListModle(
            name = name,
            exercise_id = CHEST_ID,
            image = image
        )
    )
}

fun View.showSnack(message: String) {
    var snackbar = Snackbar.make(
        this, message,
        Snackbar.LENGTH_SHORT
    )
    snackbar.show()
}

@SuppressLint("CheckResult")
fun Completable.subscribeONNewThread(message: (error: Throwable?, isError: Boolean) -> Unit) {
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {
            message(it, true)
        }.subscribe({
            message(Throwable("Nothing"), false)
        }) {
            message(it, true)
        }
}

fun Activity.getNavigationController(): NavController {
    return findNavController(R.id.nav_host_fragment_content_main)

}
