package com.mac.gymtracker.ui.fetch

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.utils.*
import java.util.*
import kotlin.collections.ArrayList

class FetchDataFromFireStoreService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        dataFetchExerciseList(intent?.getStringExtra("username"))
        dataFetchExerciseResult(intent?.getStringExtra("username"))
        userDetailsFetch(intent?.getStringExtra("username"))
        return Service.START_NOT_STICKY
    }

    private fun userDetailsFetch(userName: String?) {
        FirebaseFirestore.getInstance().collection(userName!!)
            .document(USER_DETAILS).get().addOnCompleteListener { task->
            if (task.isSuccessful) {
                var documentSnapShot = task.result
                if (documentSnapShot.exists()) {
                    PrefUtils.INSTANCE(this).let {
                        var firstName = documentSnapShot.get("name").toString()
                        var lastName = documentSnapShot.get("lastName").toString()
                        var isPrimum = documentSnapShot.getBoolean("premium")
                        var lastPaymentDate = documentSnapShot.getString("lastPaymentDate").toString()
                        var dueDate = documentSnapShot.getString("dueDate").toString()
                        var email = documentSnapShot.getString("email").toString()
                        it.setString(FIRST_NAME, firstName)
                        it.setString(LAST_NAME, lastName)
                        it.setString(EMAIL, email)
                        it.setBoolean(IS_PRIMIUM,isPrimum!!)
                        it.setString(LAST_PAYMENT_DATE, lastPaymentDate)
                        it.setString(DUE_DATE,dueDate)
                    }
                }
            }
            }.addOnFailureListener {
                Log.e(TAG, "User details Fetching Error")
            }

    }

    private fun dataFetchExerciseResult(userName: String?) {
        var drf = FirebaseFirestore.getInstance().collection(userName!!)
            .document(EXERCISE_RECORD)
        drf.get().
        addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var documentSnapshot = task.result
                if (documentSnapshot.exists()) {
                    var list: ArrayList<ExerciseRecordModel> = ArrayList()
                    list.clear()
                    var myData: List<Map<String, Objects>> =
                        documentSnapshot.get(EXERCISE_RECORD) as List<Map<String, Objects>>
                    myData.forEach {
                        list.add(
                            ExerciseRecordModel(
                                date = it["date"].toString(),
                                exerciseName = it["exerciseName"].toString(),
                                image = it["image"].toString(),
                                mainExercise = it["mainExercise"].toString(),
                                reps = it["reps"].toString(),
                                roomDate = (it["roomDate"] as Timestamp).toDate(),
                                saveTime = it["saveTime"].toString(),
                                set = it["set"].toString(),
                                stringFormatDate = it["stringFormatDate"].toString(),
                                weight = it["weight"].toString(),
                                timeInSecond = it["timeInSecond"].toString()
                            )
                        )
                    }
                    ExerciseRecordRepo(applicationContext).insertFromFireStore(
                        list,
                        applicationContext
                    )

                } else {
                    Log.e(TAG, "Data not exist")
                }

            } else {
                Log.e(TAG, "Task is Not successfull " + task?.exception?.message)
            }
        }


    }

    private fun getDate(timestamp: Timestamp): Date {
        var mac = "Machhindra"
        var sDate1 = timestamp.toDate()
        return sDate1
    }

    data class ExerciseListMac(var mac: List<ExerciseListModle>)

    private fun dataFetchExerciseList(userName: String?) {
        var drf = FirebaseFirestore.getInstance().collection(userName!!)
            .document(EXERCISE_LIST)

        drf.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var list: ArrayList<ExerciseListModle> = ArrayList()
                list.clear()
                var documentSnapshot = task.result
                if (documentSnapshot.exists()) {
                    var myData: List<Map<String, Objects>> =
                        documentSnapshot.get(EXERCISE_LIST) as List<Map<String, Objects>>
                    myData.forEach {
                        if (it["imageString"] != null) {
                            Log.e(TAG, it["imageString"].toString())
                        }
                        list.add(
                            ExerciseListModle(
                                name = it["name"].toString(),
                                exercise_id = it["exercise_id"].toString().toInt(),
                                image = it["image"].toString(),
                                date = it["date"].toString().toLong(),
                                imageString = it["imageString"] as String?,
                                isSync = true
                            )
                        )
                    }
                    Log.e(TAG, list.size.toString())

                    /*list.map { exerciseModel ->
                        LocalExerciselistRepo(applicationContext)
                            .getImage(exerciseModel.name).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                exerciseModel.image = it
                            }) {
                                Log.e(TAG, "Error on mac "+it.message)
                            }
                    }*/

                    LocalExerciselistRepo(applicationContext).updateExercise(list) {
                    }
                } else {
                    Log.e(TAG, "Data not exist")
                }
            } else {
                Log.e(TAG, "Else part " + task.exception!!.message!!)
            }
        }.addOnFailureListener { error ->
            Log.e(TAG, error.message!!)
        }
    }
}

private const val TAG = "Service"