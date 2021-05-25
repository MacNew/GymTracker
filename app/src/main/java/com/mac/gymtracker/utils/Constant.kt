package com.mac.gymtracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
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
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList

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
    list.addExercise("Barbell Bench Press", R.drawable.ic_barbell_bench_press.toString(), CHEST_ID)
    list.addExercise("Incline Bench Press",
        R.drawable.ic_incline_bench_press.toString(), CHEST_ID)
    list.addExercise(
        "Dumbbell Bench Press",
        R.drawable.ic_dumbbell_bench_press.toString(),
        CHEST_ID
    )
    list.addExercise("Decline Bench Press", R.drawable.ic_decline_press.toString(), CHEST_ID)
    list.addExercise("Machine Chest Press", R.drawable.ic_machine_chest_press.toString(), CHEST_ID)
    list.addExercise("Push-Up", R.drawable.ic_push_up.toString(), CHEST_ID)
    list.addExercise("Dip", R.drawable.ic_dip.toString(), CHEST_ID)
    list.addExercise("Chest Fly", R.drawable.ic_chest_fly.toString(), CHEST_ID)
    list.addExercise("Dumbbell Pull-Over", R.drawable.ic_dumbbell_pull_over.toString(), CHEST_ID)
    list.addExercise("Machine Fly", R.drawable.ic_machine_fly.toString(), CHEST_ID)
    list.addExercise("Push-Press", R.drawable.ic_push_press.toString(), SHOULDER_ID)
    list.addExercise("Military Press", R.drawable.ic_military_press.toString(), SHOULDER_ID)
    list.addExercise("Rear Delt Row", R.drawable.ic_rear_delt_row.toString(), SHOULDER_ID)
    list.addExercise("Seated Dumbbell Press", R.drawable.ic_seated_dumbbell_press.toString(), SHOULDER_ID)
    list.addExercise("Seated Barbell Press", R.drawable.ic_seated_barbell_press.toString(), SHOULDER_ID)
    list.addExercise("Upright Row", R.drawable.ic_upright_row.toString(), SHOULDER_ID)
    list.addExercise("Arnold Press", R.drawable.ic_arnold_press.toString(), SHOULDER_ID)
    list.addExercise("Rear Delt Fly", R.drawable.ic_rear_delt_fly.toString(), SHOULDER_ID)
    list.addExercise("Lateral Raise", R.drawable.ic_lateral_raise.toString(), SHOULDER_ID)
    list.addExercise("Front Raise", R.drawable.ic_front_raise.toString(), SHOULDER_ID)
    list.addExercise("Machine Pump Shoulder ", R.drawable.ic_machine_pump_shoulder.toString(), SHOULDER_ID)
    list.addExercise("Heavy Shoulder workout", R.drawable.ic_heard_and_heavy_shoulder_workout.toString(), SHOULDER_ID)
    list.addExercise("Dead lift ", R.drawable.ic_dead_lift.toString(), BACK_ID)
    list.addExercise("Bent-over row", R.drawable.ic_bent_over_row.toString(), BACK_ID)
    list.addExercise("Pull-up", R.drawable.ic_pull_up.toString(), BACK_ID)
    list.addExercise("T-Bar Row", R.drawable.ic_t_bar_row.toString(), BACK_ID)
    list.addExercise("Seated Row", R.drawable.ic_seated_row.toString(), BACK_ID)
    list.addExercise("Single-Arm Smith Machine Row", R.drawable.ic_single_arm_smith_machine_row.toString(), BACK_ID)
    list.addExercise("lat pull-Down", R.drawable.ic_lat_pull_down.toString(), BACK_ID)
    list.addExercise("Single-Arm Dumbbell Row", R.drawable.ic_single_arm_dumbbell_row.toString(), BACK_ID)
    list.addExercise("Dumbbell pull-over", R.drawable.ic_dumbbell_pull_over.toString(), CHEST_ID)
    list.addExercise("Chest-Supported Row", R.drawable.ic_chest_supported_row.toString(), BACK_ID)
    list.addExercise("Row-To-Grow Back Workout", R.drawable.ic_row_to_grow_back_workout.toString(), BACK_ID)
    list.addExercise("Machine pump Back Workout", R.drawable.ic_machine_pump_back_workout.toString(), BACK_ID)
    list.addExercise("Barbell back Squat", R.drawable.ic_barbell_back_squat.toString(), LEG_ID)
    list.addExercise("Barbell Front Squat", R.drawable.ic_barbell_font_squat.toString(), LEG_ID)
    list.addExercise("Barbell Stiff-Leg Dead-lift", R.drawable.ic_leg_deadlift.toString(), LEG_ID)
    list.addExercise("Split Squat", R.drawable.ic_split_squat.toString(), LEG_ID)
    list.addExercise("Hack Squat", R.drawable.ic_hack_squat.toString(), LEG_ID)
    list.addExercise("Lunge", R.drawable.ic_lunge.toString(), LEG_ID)
    list.addExercise("Leg Press", R.drawable.ic_leg_press.toString(), LEG_ID)
    list.addExercise("Romanian Dead-lift", R.drawable.ic_romanian_deadlift.toString(), LEG_ID)
    list.addExercise("Leg Curl", R.drawable.ic_leg_curl.toString(), LEG_ID)
    list.addExercise("Machine Pump Leg Workout", R.drawable.ic_machine_pump_leg_workout.toString(), LEG_ID)
    list.addExercise("Beginner Leg Workout", R.drawable.ic_beginner_leg_work_out.toString(), LEG_ID)
    list.addExercise("Incline Dumbbell Hammer Curl", R.drawable.ic_incline_dumbbell_hammer_curl.toString(), BICEPS_ID)
    list.addExercise("Incline Inner-Biceps Curl", R.drawable.ic_incline_inner_biceps_curl.toString(), BICEPS_ID)
    list.addExercise("EZ-Bar Curl", R.drawable.ic_ez_bar_curl.toString(), BICEPS_ID)
    list.addExercise("Wide-Grip Standing Barbell Curl", R.drawable.ic_wide_grip_standing_barbell_curl.toString(), BICEPS_ID)
    list.addExercise("Zottman Curl", R.drawable.ic_zottman_curl.toString(), BICEPS_ID)
    list.addExercise("Barbell Curl", R.drawable.ic_barbell_curl.toString(), BICEPS_ID)
    list.addExercise("Dumbbell Biceps Curl", R.drawable.ic_dumbbell_biceps_curl.toString(), BICEPS_ID)
    list.addExercise("Hammer Curl", R.drawable.ic_hammer_curl.toString(), BICEPS_ID)
    list.addExercise("Overhead Cable Curl", R.drawable.ic_overhead_cable_curl.toString(), BICEPS_ID)
    list.addExercise("Skullcrusher", R.drawable.ic_skull_crusher.toString(), TRICEPS_ID)
    list.addExercise("Close-Grip-Bench-Press", R.drawable.ic_close_grip_bench_press.toString(), TRICEPS_ID)
    list.addExercise("Triceps Dip", R.drawable.ic_triceps_dip.toString(), TRICEPS_ID)
    list.addExercise("Bench Dip", R.drawable.ic_bench_dip.toString(), TRICEPS_ID)
    list.addExercise("Triceps Machine Dip", R.drawable.ic_triceps_machine_dip.toString(), TRICEPS_ID)
    list.addExercise("Dumbbell Overhead Triceps Extension", R.drawable.ic_dumbbell_overhead_triceps_extenstion.toString(), TRICEPS_ID)
    list.addExercise("Cable Overhead Extension with Rope", R.drawable.ic_cable_overhead_extension_with_rope.toString(), TRICEPS_ID)
    list.addExercise("Single-Arm Cable kick-back", R.drawable.ic_single_arm_cable_kick_back.toString(), TRICEPS_ID)
    list.addExercise("Cable Push-Down", R.drawable.ic_cable_push_down.toString(), TRICEPS_ID)
    list.addExercise("Close-Grip push-up", R.drawable.ic_close_grip_push_up.toString(), TRICEPS_ID)
    LocalExerciselistRepo(context).insertExercise(list!!) {
    }
}

private fun <E> java.util.ArrayList<E>.addExercise(name: String, image: String, exerciseId: Int) {
    this as ArrayList<ExerciseListModle>
    this.add(
        ExerciseListModle(
            name = name,
            exercise_id = exerciseId,
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

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}


fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

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

fun Long.convertGymTrackerTime(): String {
     val date =  Date()
     var formatter = SimpleDateFormat("yyyy-MM-dd")
     return formatter.format(this)
}
