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
import com.mac.gymtracker.utils.*
import com.paypal.android.sdk.payments.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

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
            TrackingExerciseViewModelFactory(
                TrackExerciseLocalDataSource(requireActivity().applicationContext),
                FirebaseFirestore.getInstance(),
                PrefUtils.INSTANCE(requireContext())
            )
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
                        trackExerciseViewModel.doPayment() {
                            binding!!.parentRecyclerView.showSnack("Payment Done Thank you ")
                        }
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
        try {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        } catch (exception: Exception) {
            Log.e(TAG, "Exception " + exception.message)
        }

        initView()
        binding?.syncBtn?.setOnClickListener {
            binding?.syncProgressbar?.visibility = View.VISIBLE
            binding?.syncBtn?.visibility = View.GONE
            binding?.parentRecyclerView?.showSnack("Click sync Exercise list")
            //syncExerciseList()
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
        trackExerciseViewModel.userDetails.observe(viewLifecycleOwner, {
            binding?.firstName?.text = it.name
            binding?.lastName?.text = it.lastName
            binding?.email?.text = it.email
            binding?.accountType?.text = it.isPremium.toString().toBoolean().isPrimium()
            binding?.lastPaymentDate?.text = it.lastPaymentDate.toString().appDateFormater()
            binding?.dueDate?.text = it.dueDate.toString().appDateFormater()
            PrefUtils.INSTANCE(requireContext()).let { data ->
                var firstName = it.name
                var lastName = it.lastName
                var isPrimum = it.isPremium
                var lastPaymentDate = it.lastPaymentDate.toString()
                var email = it.email.toString()
                var dueDate = it.dueDate.toString()
                data.setString(FIRST_NAME, firstName)
                data.setString(LAST_NAME, lastName)
                data.setString(EMAIL, email)
                data.setBoolean(IS_PRIMIUM, isPrimum!!)
                data.setString(LAST_PAYMENT_DATE, lastPaymentDate)
                data.setString(DUE_DATE, dueDate)
            }
        })
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

private fun String.appDateFormater(): CharSequence? {
    return try {
        var date = Date(this.toLong())
        var pattern = "EEE, d MMM yyyy";
        var simpleDateFormater = SimpleDateFormat(pattern)
        var stringDate = simpleDateFormater.format(date)
        stringDate
    }catch (exception:Exception) {
        "null"
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
