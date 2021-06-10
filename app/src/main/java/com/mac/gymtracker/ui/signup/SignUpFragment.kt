package com.mac.gymtracker.ui.signup

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentSignupBinding
import com.mac.gymtracker.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

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
        binding!!.signUp.setOnClickListener {
            if (binding!!.edFirstName.text.isNotEmpty()) {
                if (binding!!.edLastName.text.isNotEmpty()) {
                    if (binding!!.edEmail.text.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(binding!!.edEmail.text.toString())
                                .matches()
                        ) {
                            if (binding!!.edPassword.text.isNotEmpty()) {
                                if (binding!!.edPassword.text.toString().length>6) {
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
       /* val db = FirebaseFirestore.getInstance()
        db.collection(binding!!.edEmail.text.toString()).document("user")
            .set(User(firstName, lastName, email, lastPaymentDate, lastSyncDate, isPremium))
            .addOnSuccessListener {

            }.addOnFailureListener {

            }*/
        val actionCodeSettings = actionCodeSettings {
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
            }

    }
}
private const val TAG = "Signup"