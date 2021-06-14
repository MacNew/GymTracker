package com.mac.gymtracker.splashscreen

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mac.gymtracker.R
import com.mac.gymtracker.utils.IS_DATA_LOADED
import com.mac.gymtracker.utils.PrefUtils
import com.mac.gymtracker.utils.workOut
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class SplashScreenActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    @AfterPermissionGranted(123)
    override fun onStart() {
        super.onStart()
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            saveAllImageOnFolder()
        } else {
            Log.e(TAG, "requestPermission")
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e(TAG, "onRequest PermissionResult")
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun requestPermission() {
        val perms = arrayOfNulls<String>(2)
        EasyPermissions.requestPermissions(
            this, "This application required all permisson to enable to function properly",
            125, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    @SuppressLint("CheckResult")
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.e(TAG, "OnPermissionGranted")
        saveAllImageOnFolder()
    }

   @SuppressLint("CheckResult")
   fun  saveAllImageOnFolder() {
       if (!PrefUtils.INSTANCE(this).getBoolean(IS_DATA_LOADED, false)) {
           this.workOut()
           PrefUtils.INSTANCE(this).setBoolean(IS_DATA_LOADED, true)
       } else {
           Log.e(com.mac.gymtracker.TAG, "Not called")
       }
    }



    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.e(TAG, "Sorry permission not granted")
        requestPermission()
    }
}

const val TAG = "splash"