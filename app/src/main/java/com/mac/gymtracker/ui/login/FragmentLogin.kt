package com.mac.gymtracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentLoginBinding
import com.mac.gymtracker.ui.fetch.FetchDataFromFireStoreService
import com.mac.gymtracker.utils.*
import kotlinx.android.synthetic.main.fragment_login.*

class FragmentLogin : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        var isLogin = PrefUtils.INSTANCE(requireContext()).getBoolean(IS_LOGIN, false)
        if (isLogin) {
            redirectSync()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var mi = menu.findItem(R.id.id_log_out)
        mi.isVisible = false
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
        (activity as MainActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Login"
        binding?.signUpLogin?.setOnClickListener {
            (activity as AppCompatActivity).getNavigationController()
                .navigate(FragmentLoginDirections.actionNavSyncToSignUpFragment())
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
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            PrefUtils.INSTANCE(requireContext()).let { pref ->
                                pref.setString(EMAIL, binding!!.username.text.toString())
                                pref.setString(PASSWORD, binding!!.password.text.toString())
                                pref.setBoolean(IS_LOGIN, true)
                            }
                            loading.visibility = View.GONE
                            var intent = Intent(activity, FetchDataFromFireStoreService::class.java)
                            intent.putExtra("username", binding!!.username.text.toString())
                            (activity as AppCompatActivity).startService(intent)
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

