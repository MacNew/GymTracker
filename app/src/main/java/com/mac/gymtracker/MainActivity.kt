package com.mac.gymtracker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mac.gymtracker.databinding.ActivityMainBinding
import com.mac.gymtracker.utils.*
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONException
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_track_exercise, R.id.nav_last_summery, R.id.nav_report, R.id.nav_sync
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (!PrefUtils.INSTANCE(this).getBoolean(IS_DATA_LOADED, false)) {
          this.workOut()
        } else {
            Log.e(TAG, "Not called")
        }

        val intent = intent
        val emailLink = intent!!.data.toString()
        Log.e(TAG, emailLink)
        if (emailLink == "https://www.example.com/") {
            var email = PrefUtils.INSTANCE(this).getString(EMAIL, "")
            var password = PrefUtils.INSTANCE(this).getString(PASSWORD, "")
            Firebase.auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener() {
                    if (it.isSuccessful) {
                        drawerLayout.showSnack("User created Successfully")
                        PrefUtils.INSTANCE(this).setBoolean(IS_LOGIN, true)
                    } else {
                        drawerLayout.showSnack("Error on Creating User" + it.exception?.message!!)
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.setting_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.id_log_out -> {
                PrefUtils.INSTANCE(this).let { pref->
                    pref.setString(EMAIL, "")
                    pref.setString(PASSWORD, "")
                    pref.setBoolean(IS_LOGIN, false)
                }
                binding.drawerLayout.showSnack("Log out Successfully ")
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun requestPermission() {
        EasyPermissions.requestPermissions(
            this, "This application required all permisson to enable to function properly",
            125, Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}

const val TAG = "MainActivity"