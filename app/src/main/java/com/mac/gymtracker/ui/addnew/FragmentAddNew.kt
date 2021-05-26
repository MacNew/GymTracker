package com.mac.gymtracker.ui.addnew

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mac.gymtracker.databinding.FragmentAddNewBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.RationaleCallbacks

class FragmentAddNew : Fragment(), EasyPermissions.PermissionCallbacks
, RationaleCallbacks
{
    private var _binding: FragmentAddNewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewBinding.inflate(inflater, container, false)
        return binding.root
    }



    @AfterPermissionGranted(123)
    override fun onStart() {
        super.onStart()
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.e("TAG", "ok Permission granted")

        } else {
            EasyPermissions.
            requestPermissions(this, "This application required all permisson to enable to function properly",
             125, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.e("TAG", "Permission code$requestCode")

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.e("TAG", "Sorry permission not granted");

    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.e("TAG", "onRationaleAccepted")
    }

    override fun onRationaleDenied(requestCode: Int) {
         Log.e("TAG", "onRationaleDenied")
    }

}