package com.mac.gymtracker.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentLoginBinding
import com.mac.gymtracker.utils.*
import kotlinx.android.synthetic.main.fragment_login.*

class FragmentLogin : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isLogin = PrefUtils.INSTANCE(requireContext()).getBoolean(IS_LOGIN, false)
        if (isLogin) {
            redirectSync()
        }
    }

    fun redirectSync() {
        var navController: NavController = requireActivity().getNavigationController()
        if (requireActivity().getNavigationController().currentDestination!!.id == R.id.nav_sync) {
            var action = FragmentLoginDirections.actionNavSyncToFragmentSync()
            var option = NavOptions.Builder().setPopUpTo(R.id.nav_sync, true).build()
            navController.navigate(action, option)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Login/SignUP"
        binding?.signUp?.setOnClickListener {
            if (username.text.isEmpty()) {
                username.error = "Enter UserName"
                username.isFocusable = true
            } else {
                if (password.text.isEmpty()) {
                    password.error = "Enter Password"
                    password.isFocusable = true
                } else {
                    loading.visibility = View.VISIBLE
                    val actionCodeSettings = actionCodeSettings {
                        url = "https://www.example.com"
                        handleCodeInApp = true
                        setAndroidPackageName("com.mac.gymtracker.ui", true, "12")
                    }
                    Firebase.auth.sendSignInLinkToEmail(
                        binding!!.username.text.toString(),
                        actionCodeSettings
                    )
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                loading.visibility = View.GONE
                                view.showSnack("Sign link send in your Email")
                                PrefUtils.INSTANCE(requireContext()).let {
                                    it.setString(EMAIL, binding!!.username.text.toString())
                                    it.setString(PASSWORD, binding!!.password.text.toString())
                                }
                            } else {
                                Log.e(TAG, task.exception!!.message!!)
                                loading.visibility = View.GONE
                                view.showSnack("Error on Sending Email Link")
                            }
                        }
                }
            }
        }
        binding?.login?.setOnClickListener {
            if (username.text.isEmpty()) {
                username.error = "Enter UserName"
                username.isFocusable = true
            } else {
                if (password.text.isEmpty()) {
                    password.error = "Enter Password"
                    password.isFocusable = true
                } else {
                    loading.visibility = View.VISIBLE
                    Firebase.auth.signInWithEmailAndPassword(
                        binding!!.username.text.toString(),
                        binding!!.password.text.toString()
                    )
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                PrefUtils.INSTANCE(requireContext()).setBoolean(IS_LOGIN, true)
                                view.showSnack("Login Successfully")
                                loading.visibility = View.GONE
                                redirectSync()
                            } else {
                                view.showSnack(it.exception?.message!!)
                                loading.visibility = View.GONE
                            }
                        }
                }
            }
        }
    }
}

const val TAG = "Signup"

class User(var name: String, var email: String)