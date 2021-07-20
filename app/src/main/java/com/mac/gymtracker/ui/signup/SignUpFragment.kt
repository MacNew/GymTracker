package com.mac.gymtracker.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentSignupBinding
import com.mac.gymtracker.ui.login.FragmentLoginDirections
import com.mac.gymtracker.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*
import kotlin.collections.ArrayList

class SignUpFragment : Fragment() {
    private var _bindin: FragmentSignupBinding? = null
    val binding get() = _bindin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var mi = menu.findItem(R.id.id_log_out)
        mi.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindin = FragmentSignupBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Sign up"

        binding!!.signUp.setOnClickListener {
            if (binding!!.edFirstName.text.isNotEmpty()) {
                if (binding!!.edLastName.text.isNotEmpty()) {
                    if (binding!!.edEmail.text.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(binding!!.edEmail.text.toString())
                                .matches()
                        ) {
                            if (binding!!.edPassword.text.isNotEmpty()) {
                                if (binding!!.edPassword.text.toString().length > 6) {
                                    if (binding!!.edPassword.text.toString() == binding!!.edConfPassword.text.toString()) {
                                        generateSignupLink()
                                    } else {
                                        binding!!.edConfPassword.error = "Password does not match "
                                    }
                                } else {
                                    binding!!.edPassword.error = "Password must be six character"
                                }
                            } else {
                                binding!!.edPassword.error = "Enter Password"
                            }
                        } else {
                            binding!!.edEmail.error = "Enter valid Email Address"
                        }
                    } else {
                        binding!!.edEmail.error = "Enter Email Address"
                    }
                } else {
                    binding!!.edLastName.error = "Enter Last Name"
                }
            } else {
                binding!!.edFirstName.error = "Enter FirstName"
            }
        }
    }

    private fun generateSignupLink() {
        loading.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()
        Firebase.auth.createUserWithEmailAndPassword(
            binding!!.edEmail.text.toString(),
            binding!!.edPassword.text.toString()
        ).addOnCompleteListener() {
                if (it.isSuccessful) {
                    var dates:ArrayList<PaymentDates> = ArrayList()
                    dates.add(PaymentDates(null))

                    db.collection(binding!!.edEmail.text.toString())
                        .document(USER_DETAILS)
                        .set(
                            User(
                                binding!!.edFirstName.text.toString(),
                                binding!!.edLastName.text.toString(),
                                binding!!.edEmail.text.toString(),
                                null,
                                null,
                                false,
                                null,
                                dates
                            ), SetOptions.merge()
                        )
                        .addOnSuccessListener {
                            PrefUtils.INSTANCE(requireActivity()).setBoolean(IS_LOGIN, true)
                            PrefUtils.INSTANCE(requireContext()).let { pref ->
                                pref.setString(EMAIL, binding!!.edEmail.text.toString())
                                pref.setString(PASSWORD, binding!!.edPassword.text.toString())
                                pref.setString(FIRST_NAME, binding!!.edFirstName.text.toString())
                                pref.setString(LAST_NAME, binding!!.edLastName.text.toString())
                                pref.setBoolean(IS_LOGIN, true)
                            }
                            loading.visibility = View.GONE
                            binding!!.parentSignup.showSnack("Sign up Successfully")
                            redirectSync()
                        }.addOnFailureListener {
                            loading.visibility = View.GONE
                            binding!!.parentSignup.showSnack(it.message!!)
                        }
                } else {
                    loading.visibility = View.GONE
                    binding!!.parentSignup.showSnack(it.exception!!.message!!)
                }
            }
        /*val actionCodeSettings = actionCodeSettings {
            url = "https://www.example.com"
            handleCodeInApp = true
            setAndroidPackageName("com.mac.gymtracker.ui", true, "12")
        }
        Firebase.auth.sendSignInLinkToEmail(
            binding!!.edEmail.text.toString(),
            actionCodeSettings
        )
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    loading.visibility = View.GONE
                    view?.showSnack("Sign link send in your Email")
                    PrefUtils.INSTANCE(requireContext()).let { pref ->
                        pref.setString(EMAIL, binding!!.edEmail.text.toString())
                        pref.setString(PASSWORD, binding!!.edPassword.text.toString())
                        pref.setString(FIRST_NAME, binding!!.edFirstName.text.toString())
                        pref.setString(LAST_NAME, binding!!.edLastName.text.toString())
                        pref.setBoolean(IS_LOGIN, false)
                    }
                } else {
                    Log.e(TAG, task.exception!!.message!!)
                    loading.visibility = View.GONE
                    view?.showSnack("Error on Sending Email Link")
                }
            }*/
    }

     fun redirectSync() {
        var navController: NavController = requireActivity().getNavigationController()
        if (requireActivity().getNavigationController().currentDestination!!.id == R.id.signUpFragment) {
            var action = SignUpFragmentDirections.actionSignUpFragmentToFragmentSync()
            var option = NavOptions.Builder().setPopUpTo(R.id.nav_sync, true).build()
            navController.navigate(action, option)
        }
    }
}

class User(
    var name: String,
    var lastName: String,
    var email: String,
    var lastSyncDate: String?,
    var lastPaymentDate: String?,
    var isPremium: Boolean,
    var dueDate: String?,
    var dates:ArrayList<PaymentDates>
)
class PaymentDates(var dates: String?)

private const val TAG = "Signup"