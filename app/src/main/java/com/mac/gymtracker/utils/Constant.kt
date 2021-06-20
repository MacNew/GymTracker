package com.mac.gymtracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.material.snackbar.Snackbar
import com.mac.gymtracker.R
import com.mac.gymtracker.splashscreen.TAG
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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


const val IS_LOGIN = "IS_LOGIN"
const val IS_FROM_SYNC = "IS_FROM_SYNC"
const val EMAIL = "EMAIL"
const val IS_PRIMIUM = "IS_PRIMIUM"
const val LAST_PAYMENT_DATE = "LAST_PAYMENT_DATE"
const val DUE_DATE = "DUE_DATE"
const val PASSWORD = "PASSWORD"
const val EXERCISE_RECORD = "exerciseRecord"
const val EXERCISE_LIST = "exerciseList"
const val FIRST_NAME = "FIRST_NAME"
const val LAST_NAME = "LAST_NAME"
const val IS_DATA_LOADED = "IS_DATA_LOADED"
const val USER_DETAILS = "UserDetails"

const val PAY_PAL_CLIENT_ID =
    "AessWbzwfvxe3kw3L9XDSHIrT-ag657j2b7ARQbdfrkwipksfflEV__UsyKNMvlipKZC8C28o55vuICu"

fun String?.toLocalBitMap(): Bitmap? {
    val imageAsBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    var bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    return bitmap
}


fun Context.workOut(){
    var ob1 = this.saveMainExercise(R.drawable.ic_chest, CHEST_ID, CHEST, "ic_chest") {
        var ob2 = this.saveMainExercise(R.drawable.ic_shoulder, SHOULDER_ID, SHOULDER, "ic_shoulder") {
            var ob3 = this.saveMainExercise(R.drawable.ic_back, BACK_ID, BACK, "ic_back") {
                var ob4 = this.saveMainExercise(R.drawable.ic_biceps, BICEPS_ID, BICEPS, "ic_biceps") {
                    var ob5 = this.saveMainExercise(R.drawable.ic_triceps, TRICEPS_ID, TRICEPS, "ic_triceps") {
                        var ob6 = this.saveMainExercise(R.drawable.ic_leg, LEG_ID, LEG, "ic_leg") {
                            loadExerciseList(this)
                        }
                    }
                }
            }
        }
    }
}

fun Context.saveMainExercise(
    drawableValue: Int,
    exerciseId: Int,
    exerciseName: String,
    fileName: String,
    function: (values: Boolean) -> Unit
){
    drawableValue.convertBase64(this).saveImage(fileName) {
        if (it != null) {
            TrackExerciseLocalDataSource(this).insertExercise(
                TrackExerciseModel(exerciseId, exerciseName, it)
            ) {
                if (it) {
                    Log.e(TAG, "Data Inserted Successfully")
                    function(true)
                } else {
                    Log.e(TAG, "Error on Saving data")
                    function(false)
                }
            }
        } else {
            function(false)
        }
    }
}

@SuppressLint("CheckResult")
private fun String.saveImage(fileName: String, function: (value: String?) -> Unit) {
    saveImageAndReturnUri(this, fileName)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe({
            if (it != null) {
                function(it)
                Log.e(TAG, "File Save")
            } else {
                function(null)
                Log.e(TAG, "File Not Save")
            }
        }) {
            function(null)
            Log.e(TAG, "Error ${it.message}")
        }
}

private fun saveImageAndReturnUri(image: String, filename: String): Observable<String?> {
    val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
    val finalBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    var bytes = ByteArrayOutputStream()
    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
    var dir: File = File(
        Environment.getExternalStorageDirectory().absolutePath + "//gymtracker"
    )
    try {
        if (!dir.exists()) {
            if (dir.mkdir()) {
                Log.e(TAG, "New folder made")
            } else {
                Log.e(TAG, "Error on Making new folder")
            }
        } else {
            Log.e(TAG, "Dir alreay exist")
        }

    } catch (exception: Exception) {
        Log.e(TAG, exception.message!!)

    }
    val destination = File(
        dir,
        "$filename.jpg"
    )
    return try {
        destination.createNewFile()
        var fo = FileOutputStream(destination)
        fo.write(bytes.toByteArray())
        fo.close()
        Log.e(TAG, "cool")
        var uri: String? =
            Uri.fromFile(File(Environment.getExternalStorageDirectory().absolutePath + "//gymtracker/" + filename))
                .toString()
        Observable.just(uri)
    } catch (e: Exception) {
        Log.e(TAG, "Error message " + e.message!!)
        Observable.just(null)
    }
}

private fun Context.loadExerciseListURI(drawableImage:Int,fileName: String, exerciseName:String, exerciseId:Int) {
    drawableImage.convertBase64(this).saveImage(fileName) {
        if (it!=null) {
            LocalExerciselistRepo(this).insertExercise(  ExerciseListModle(
                name = exerciseName,
                exercise_id = exerciseId,
                image = it,
                date = Date().time,
                isSync = false
            )) {

            }
        } else {
            Log.e(TAG, "error")
        }
    }

}
private fun loadExerciseList(context: Context) {
    context.loadExerciseListURI(R.drawable.ic_barbell_bench_press, "ic_barbell_bench_press", "Barbell Bench Press",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_incline_bench_press, "ic_incline_bench_press", "Incline Bench Press",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_dumbbell_bench_press, "ic_dumbbell_bench_press", "Dumbbell Bench Press",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_decline_press, "ic_decline_press", "Decline Bench Press",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_machine_chest_press, "ic_machine_chest_press", "Machine Chest Press",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_push_up, "ic_push_up", "Push-Up",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_dip, "ic_dip", "Dip",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_chest_fly, "ic_chest_fly", "Chest Fly",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_dumbbell_pull_over, "ic_dumbbell_pull_over", "Dumbbell Pull-Over",CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_machine_fly, "ic_machine_fly", "Machine Fly", CHEST_ID)
    context.loadExerciseListURI(R.drawable.ic_push_press, "ic_push_press", "Push-Press", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_military_press, "ic_military_press", "Military Press", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_rear_delt_row, "ic_rear_delt_row", "Rear Delt Row", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_seated_dumbbell_press, "ic_seated_dumbbell_press", "Seated Dumbbell Press", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_seated_dumbbell_press, "ic_seated_dumbbell_press", "Seated Dumbbell Press", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_upright_row, "ic_upright_row", "Upright Row", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_arnold_press, "ic_arnold_press", "Arnold Press", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_rear_delt_fly, "ic_rear_delt_fly", "Rear Delt Fly", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_lateral_raise, "ic_lateral_raise", "Lateral Raise", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_front_raise, "ic_front_raise", "Front Raise", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_machine_pump_shoulder, "ic_machine_pump_shoulder", "Machine Pump Shoulder", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_heard_and_heavy_shoulder_workout, "ic_heard_and_heavy_shoulder_workout", "Heavy Shoulder workout", SHOULDER_ID)
    context.loadExerciseListURI(R.drawable.ic_dead_lift, "ic_dead_lift", "Dead lift", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_bent_over_row, "ic_bent_over_row", "Bent-over row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_pull_up, "ic_pull_up", "Pull-up", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_t_bar_row, "ic_t_bar_row", "T-Bar Row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_seated_row, "ic_seated_row", "Seated Row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_single_arm_smith_machine_row, "ic_single_arm_smith_machine_row", "Single-Arm Smith Machine Row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_lat_pull_down, "ic_lat_pull_down", "lat pull-Down", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_single_arm_dumbbell_row, "ic_single_arm_dumbbell_row", "Single-Arm Dumbbell Row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_chest_supported_row, "ic_chest_supported_row", "Chest-Supported Row", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_row_to_grow_back_workout, "ic_row_to_grow_back_workout", "Row-To-Grow Back Workout", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_machine_pump_back_workout, "ic_machine_pump_back_workout", "Machine pump Back Workout", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_barbell_back_squat, "ic_barbell_back_squat", "Barbell back Squat", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_leg_deadlift, "ic_leg_deadlift", "Barbell Stiff-Leg Dead-lift", BACK_ID)
    context.loadExerciseListURI(R.drawable.ic_split_squat, "ic_split_squat", "Split Squat", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_hack_squat, "ic_hack_squat", "Hack Squat", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_lunge, "ic_lunge", "Lunge", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_leg_press, "ic_leg_press", "Leg Press", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_romanian_deadlift, "ic_romanian_deadlift", "Romanian Dead-lift", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_leg_curl, "ic_leg_curl", "Leg Curl", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_machine_pump_leg_workout, "ic_machine_pump_leg_workout", "Machine Pump Leg Workout", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_beginner_leg_work_out, "ic_beginner_leg_work_out", "Beginner Leg Workout", LEG_ID)
    context.loadExerciseListURI(R.drawable.ic_incline_dumbbell_hammer_curl, "ic_incline_dumbbell_hammer_curl", "Incline Dumbbell Hammer Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_incline_inner_biceps_curl, "ic_incline_inner_biceps_curl", "Incline Inner-Biceps Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_ez_bar_curl, "ic_ez_bar_curl", "EZ-Bar Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_wide_grip_standing_barbell_curl, "ic_wide_grip_standing_barbell_curl", "Wide-Grip Standing Barbell Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_zottman_curl, "ic_zottman_curl", "Zottman Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_barbell_curl, "ic_barbell_curl", "Barbell Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_dumbbell_biceps_curl, "ic_dumbbell_biceps_curl", "Dumbbell Biceps Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_hammer_curl, "ic_hammer_curl", "Hammer Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_overhead_cable_curl, "ic_overhead_cable_curl", "Overhead Cable Curl", BICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_skull_crusher, "ic_skull_crusher", "Skullcrusher", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_close_grip_bench_press, "Close-Grip-Bench-Press", "Close-Grip-Bench-Press", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_triceps_dip, "ic_triceps_dip", "Triceps Dip", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_bench_dip, "ic_bench_dip", "Bench Dip", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_triceps_machine_dip, "ic_triceps_machine_dip", "Triceps Machine Dip", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_dumbbell_overhead_triceps_extenstion, "ic_dumbbell_overhead_triceps_extenstion", "Dumbbell Overhead Triceps Extension", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_cable_overhead_extension_with_rope, "ic_cable_overhead_extension_with_rope", "Cable Overhead Extension with Rope", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_single_arm_cable_kick_back, "ic_single_arm_cable_kick_back", "Single-Arm Cable kick-back", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_cable_push_down, "ic_cable_push_down", "Cable Push-Down", TRICEPS_ID)
    context.loadExerciseListURI(R.drawable.ic_close_grip_push_up, "ic_close_grip_push_up", "Close-Grip push-up", TRICEPS_ID)
}

fun Int.convertBase64(context: Context): String {
    val bitmap = BitmapFactory.decodeResource(context.resources, this)
    val byteStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)
    val byteArray: ByteArray = byteStream.toByteArray()
    val baseString = Base64.encodeToString(byteArray, Base64.DEFAULT)

    return baseString
}

private fun <E> java.util.ArrayList<E>.addExercise(name: String, image: String, exerciseId: Int) {
    this as ArrayList<ExerciseListModle>
    this.add(
        ExerciseListModle(
            name = name,
            exercise_id = exerciseId,
            image = image,
            date = Date().time,
            isSync = false
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

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

@SuppressLint("CheckResult")
fun Completable.subscribeONNewThread(message: (error: Throwable?, isError: Boolean) -> Unit) {
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError {
            message(it, true)
            Log.e("Mac", "Yes it is error on Insert " + it.message)
        }.subscribe({
            Log.e("Mac", "Data Insert Successfully")
            message(Throwable("Nothing"), false)
        }) {
            Log.e("Mac", "Error on Insert" + it.message)
            message(it, true)
        }
}

fun Activity.getNavigationController(): NavController {
    return findNavController(R.id.nav_host_fragment_content_main)
}

fun Context.showAlertDialog(
    title: String, message: String, positiveString: String,
    negativeString: String, onPositiveBtnClicked: () -> Unit,
    onNegativeBtnClicked: () -> Unit
) {
    val alertDialogBuilder = AlertDialog.Builder(this)
    alertDialogBuilder.setTitle(title)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton(positiveString) { dialog, _ ->
        onPositiveBtnClicked()
        dialog.dismiss()
    }
    alertDialogBuilder.setNegativeButton(negativeString) { dialog, _ ->
        onNegativeBtnClicked()
        dialog.dismiss()
    }
    alertDialogBuilder.show()
}


fun Long.convertGymTrackerTime(): String {
    var formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(this)
}

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    return Bitmap.createBitmap(
        bm, 0, 0, width, height, matrix, false
    )
}
