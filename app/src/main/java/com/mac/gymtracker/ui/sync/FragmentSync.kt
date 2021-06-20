package com.mac.gymtracker.ui.sync

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.mac.gymtracker.R
import com.mac.gymtracker.TAG
import com.mac.gymtracker.databinding.FragmentSyncBinding
import com.mac.gymtracker.ui.exercise.TrackExerciseViewModel
import com.mac.gymtracker.ui.exercise.TrackingExerciseViewModelFactory
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.signup.User
import com.mac.gymtracker.utils.*
import com.paypal.android.sdk.payments.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sync.*
import org.json.JSONException
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList

class FragmentSync : Fragment() {
    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding
    private lateinit var trackExerciseViewModel: TrackExerciseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Sync data"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackExerciseViewModel = ViewModelProvider(
            this,
            TrackingExerciseViewModelFactory(TrackExerciseLocalDataSource(requireActivity().applicationContext))
        ).get(TrackExerciseViewModel::class.java);
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private val db = FirebaseFirestore.getInstance()
    val config = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
        .clientId(PAY_PAL_CLIENT_ID)

    override fun onDestroy() {
        requireContext().stopService(Intent(context, PayPalService::class.java))
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                val confirm: PaymentConfirmation? =
                    data?.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        Log.i("paymentExample", paymentDetails)
                        Log.e(TA, "Payment Successfully")

                    } catch (e: JSONException) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "paymentExample",
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        initView()
        binding?.syncBtn?.setOnClickListener {
            binding?.syncProgressbar?.visibility = View.VISIBLE
            binding?.syncBtn?.visibility = View.GONE
            syncExerciseList()
        }
        var intent = Intent(context, PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        context?.startService(intent)
        binding?.btnPaypal?.setOnClickListener {
            val payment = PayPalPayment(
                BigDecimal(java.lang.String.valueOf("10")), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE
            )
            var intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
            startActivityForResult(intent, 123)
        }
    }

    private fun initView() {
        PrefUtils.INSTANCE(requireContext()).let {
            binding?.firstName?.text = it.getString(FIRST_NAME, "null")
            binding?.lastName?.text = it.getString(LAST_NAME, "null")
            binding?.email?.text = it.getString(EMAIL, "null")
            binding?.accountType?.text = it.getBoolean(IS_PRIMIUM, false).isPrimium()
            binding?.lastPaymentDate?.text = it.getString(LAST_PAYMENT_DATE, "null")
            binding?.dueDate?.text = it.getString(DUE_DATE, "null")
        }

        var email = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "null")
        var drf = FirebaseFirestore.getInstance().collection(email!!)
            .document(USER_DETAILS)
        drf.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var documentSnapShot = task.result
                if (documentSnapShot.exists()) {
                    PrefUtils.INSTANCE(requireContext()).let {
                        var firstName = documentSnapShot.get("name").toString()
                        var lastName = documentSnapShot.get("lastName").toString()
                        var isPrimum = documentSnapShot.getBoolean("premium")
                        var lastPaymentDate = documentSnapShot.getString("lastPaymentDate").toString()
                        var dueDate = documentSnapShot.getString("dueDate").toString()
                        it.setString(FIRST_NAME, firstName)
                        it.setString(LAST_NAME, lastName)
                        it.setString(EMAIL, email)
                        it.setBoolean(IS_PRIMIUM,isPrimum!!)
                        it.setString(LAST_PAYMENT_DATE, lastPaymentDate)
                        it.setString(DUE_DATE,dueDate)
                    }
                    binding?.firstName?.text = documentSnapShot.get("name").toString()
                    binding?.lastName?.text = documentSnapShot.get("lastName").toString()
                    binding?.email?.text = documentSnapShot.get("email").toString()
                    binding?.accountType?.text =
                        documentSnapShot.get("premium").toString().toBoolean().isPrimium()
                    binding?.lastPaymentDate?.text =
                        documentSnapShot.get("lastPaymentDate").toString()
                    binding?.dueDate?.text = documentSnapShot.getString("dueDate").toString()
                }
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun syncExerciseList() {
        val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
        LocalExerciselistRepo(requireContext()).getAllList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TA, "Error ${it.message}")
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }.subscribe({ list ->
                var exerciseListModle = ExerciseList(list)
                /*var batch = db.batch()
                var mycRef = db.collection(userName!!).document("payment")


                batch.set(mycRef, User("Machhindra Neupane", false))

                var mysecondRef = db.collection(userName!!).document(EXERCISE_LIST)
                batch.set(mysecondRef, exerciseListModle).commit()
*/
                db.collection(userName!!)
                    .document(EXERCISE_LIST).set(exerciseListModle)
                    .addOnSuccessListener {
                        syncExerciseRecord()
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${it.message}")
                        Log.e(TA, "Error " + it.message)
                    }
            }) {
                Log.e(TA, "Error ${it.message}")
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }

    @SuppressLint("CheckResult")
    private fun syncExerciseRecord() {
        ExerciseRecordRepo(requireContext()).getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                Log.e(TA, "Error ${it.message}")
                view?.showSnack("Error ${it.message}")
            }.subscribe({
                val userName = PrefUtils.INSTANCE(requireContext()).getString(EMAIL, "")
                var exerciseListModle = ExerciseRecord(it)

                db.collection(userName!!).document(EXERCISE_RECORD).set(exerciseListModle)
                    .addOnSuccessListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Data Sync on Server Successfully")
                    }.addOnFailureListener {
                        binding?.syncProgressbar?.visibility = View.GONE
                        binding?.syncBtn?.visibility = View.VISIBLE
                        view?.showSnack("Error ${it.message}")
                        Log.e(TA, "Error ${it.message}")
                    }
            }) {
                Log.e(TA, "Error ${it.message}")
                binding?.syncProgressbar?.visibility = View.GONE
                binding?.syncBtn?.visibility = View.VISIBLE
                view?.showSnack("Error ${it.message}")
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var mi = menu.findItem(R.id.id_log_out)
        mi.isVisible = false
    }
}

private fun Boolean.isPrimium(): CharSequence? {
    if (this) {
        return "Primum"
    } else {
        return "Basic"
    }
}

data class ExerciseRecord(
    var exerciseRecord: List<ExerciseRecordModel>? = null
)

data class ExerciseList(
    var exerciseList: List<ExerciseListModle>? = null
)

const val TA = "Sync"
