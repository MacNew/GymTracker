package com.mac.gymtracker.ui.exercise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import com.mac.gymtracker.ui.signup.User
import com.mac.gymtracker.utils.*
import java.util.*

class TrackExerciseViewModel(
    repository: TrackExerciseLocalDataSource,
    var db: FirebaseFirestore?,
    var instance: PrefUtils?
) : ViewModel() {

    private val _exerciseList = MutableLiveData<LiveData<List<TrackExerciseModel>>>().apply {
        value = repository.getExerciseList()
    }

    private val _userDetails = MutableLiveData<User>().apply {
        if (instance!=null) {
            Log.e(TAG, instance?.getString(EMAIL, "").toString())
            db?.collection(instance?.getString(EMAIL, "").toString())
                ?.document(USER_DETAILS)?.addSnapshotListener {
                        documentSnapShot, e->
                        if (documentSnapShot != null) {
                            Log.e(TAG,"IT is not null")
                            if (documentSnapShot!!.exists() && documentSnapShot != null) {
                                var expiredDate: Long
                                var currentDate: Long
                                try {
                                    expiredDate = documentSnapShot.getString("dueDate").toString().toLong()
                                    currentDate =  Date().time
                                    if(currentDate > expiredDate ) {
                                        value =  User(
                                            name = documentSnapShot.get("name").toString(),
                                            lastName = documentSnapShot.get("lastName").toString(),
                                            email = documentSnapShot.get("email").toString(),
                                            lastSyncDate = null,
                                            lastPaymentDate = documentSnapShot.getString("lastPaymentDate").toString(),
                                            isPremium = false,
                                            dueDate = documentSnapShot.getString("dueDate").toString()
                                        )
                                    } else {
                                       Log.e(TAG, "Else part done ")
                                        value =  User(
                                            name = documentSnapShot.get("name").toString(),
                                            lastName = documentSnapShot.get("lastName").toString(),
                                            email = documentSnapShot.get("email").toString(),
                                            lastSyncDate = null,
                                            lastPaymentDate = documentSnapShot.getString("lastPaymentDate").toString(),
                                            isPremium = documentSnapShot.getBoolean("premium")!!,
                                            dueDate = documentSnapShot.getString("dueDate").toString()
                                        )
                                    }

                                } catch (exception: Exception) {
                                    value =  User(
                                        name = documentSnapShot.get("name").toString(),
                                        lastName = documentSnapShot.get("lastName").toString(),
                                        email = documentSnapShot.get("email").toString(),
                                        lastSyncDate = null,
                                        lastPaymentDate = documentSnapShot.getString("lastPaymentDate").toString(),
                                        isPremium = documentSnapShot.getBoolean("premium")!!,
                                        dueDate = documentSnapShot.getString("dueDate").toString()
                                    )
                                }
                            }
                        }
                        else {
                            Log.e(TAG, "It is null")

                        }
                }
        }
    }

    val userDetails: LiveData<User> = _userDetails

    val exerciseList: LiveData<LiveData<List<TrackExerciseModel>>> = _exerciseList



    fun doPayment(function:()->Unit) {
        if (instance!= null) {
            instance.let {
                var dueDate : Long = Date().time
                var valueAdded: Long = 2592000000
                var totalDueDate: Long = dueDate+valueAdded
                db?.collection(it?.getString(EMAIL, "").toString())?.document(USER_DETAILS)
                    ?.set(
                        User(
                            name = it?.getString(FIRST_NAME, "").toString(),
                            lastName = it?.getString(LAST_NAME, "").toString(),
                            email = it?.getString(EMAIL, "").toString(),
                            lastSyncDate = null,
                            lastPaymentDate = Date().time.toString(),
                            isPremium = true,
                            dueDate = totalDueDate.toString()
                        )
                    )?.addOnSuccessListener {
                        function()
                    }
            }
        }

    }

}
const val TAG = "TrackExercise"

