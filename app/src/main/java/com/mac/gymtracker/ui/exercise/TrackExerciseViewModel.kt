package com.mac.gymtracker.ui.exercise

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
            db?.collection(instance?.getString(EMAIL, "").toString())
                ?.document(USER_DETAILS)?.addSnapshotListener {
                        documentSnapShot, e->
                     if (documentSnapShot!!.exists() && documentSnapShot != null) {
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
    }

    val userDetails: LiveData<User> = _userDetails

    val exerciseList: LiveData<LiveData<List<TrackExerciseModel>>> = _exerciseList



    fun doPayment(function:()->Unit) {
        if (instance!= null) {
            instance.let {
                db?.collection(it?.getString(EMAIL, "").toString())?.document(USER_DETAILS)
                    ?.set(
                        User(
                            name = it?.getString(FIRST_NAME, "").toString(),
                            lastName = it?.getString(LAST_NAME, "").toString(),
                            email = it?.getString(EMAIL, "").toString(),
                            lastSyncDate = null,
                            lastPaymentDate = Date().time.toString(),
                            isPremium = true,
                            dueDate = (Date().time + (86400 * 1000 * 30)).toString()
                        )
                    )?.addOnSuccessListener {
                        function()
                    }
            }
        }

    }

}


