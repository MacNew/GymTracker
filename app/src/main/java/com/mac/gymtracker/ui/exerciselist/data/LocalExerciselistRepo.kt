package com.mac.gymtracker.ui.exerciselist.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.LiveData
import com.mac.gymtracker.database.GymTrackerDatabase
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.getResizedBitmap
import com.mac.gymtracker.utils.subscribeONNewThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.util.*

class LocalExerciselistRepo(var context: Context) {

    private var repo: ExerciseList = GymTrackerDatabase.getDatabase(context).exerciseListDao()

    fun insertExercise(list: List<ExerciseListModle>, message: (errorMsg: Boolean) -> Unit) {
        repo.insertAll(list).subscribeONNewThread { _, isError ->
            if (isError)
                message(isError)
            else
                message(isError)
        }
    }

    @SuppressLint("CheckResult")
    fun insertExercise(
        contentResolver: ContentResolver?, uri: Uri, exerciseName: String,
        exerciseId: Int,
        message: (errorMsg: Boolean, error:Throwable) -> Unit
    ) {
        var bitmaps = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        var bitmap = getResizedBitmap(bitmaps, 500)
        var outputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        var byteArray = outputStream.toByteArray()
        var encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
        var exerciseNewModle = ExerciseListModle(
            name = exerciseName,
            exercise_id = exerciseId,
            image = "2131165303",
            imageString = encodedString,
            date = Date().time
        )


        repo.getExercise(exerciseName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message(true, Throwable("data already exist"))
            }) {
                repo.insert(exerciseNewModle).subscribeONNewThread { throwableError, isError ->
                    if (isError)
                        message(isError, throwableError!!)
                    else
                        message(isError, throwableError!!)
                }
            }
        }

    fun getExerciseListById(exerciseId: Int): LiveData<List<ExerciseListModle>>? {
        return repo.getAll(exerciseId)
    }

    @SuppressLint("CheckResult")
    fun editContent(exerciseList: ExerciseListModle, previousName: String, message: (errorMsg: Boolean, error:Throwable) -> Unit) {
        repo.getExercise(exerciseList.name).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.name == exerciseList.name && it.imageString!=exerciseList.imageString) {
                    exerciseList.editContent(previousName,repo, context) {
                        errorMsg, error ->
                        message(errorMsg, error)
                    }
                } else {
                    message(true, Throwable("data already exist"))
                }
            }) {
                exerciseList.editContent(previousName,repo, context) {
                    errorMsg, error ->
                message(errorMsg, error)
            }

            }
    }
}

private fun ExerciseListModle.editContent(previousName: String, repo: ExerciseList, context:Context, message: (errorMsg: Boolean, error:Throwable) -> Unit) {
     ExerciseRecordRepo(context =context).editExerciseRecordContent(previousName, this).subscribeONNewThread {
          error: Throwable?, isError: Boolean ->
         if (!isError) {
             repo.editContent(this).subscribeONNewThread { throwableError, isErrors->
                 if (isErrors)
                     message(isErrors, throwableError!!)
                 else
                     message(isErrors, throwableError!!)
             }
         } else {
             message(isError, error!!)
         }
     }
}
