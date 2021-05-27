package com.mac.gymtracker.ui.addnew

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.databinding.FragmentAddNewBinding
import com.theartofdev.edmodo.cropper.CropImage
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.RationaleCallbacks

class FragmentAddNew : Fragment(), EasyPermissions.PermissionCallbacks, RationaleCallbacks {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setTitle(FragmentAddNewArgs.fromBundle(arguments!!).exerciseName)
        binding.ivExercise.setOnClickListener {
            openCropImageIntent()
        }

        binding!!.toolbar.setNavigationOnClickListener(View.OnClickListener {
            (activity as MainActivity).onBackPressed()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                var resultUri = result.uri
                Glide.with(requireContext()).load(resultUri).into(binding.ivExercise)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error
                Log.e("TAG", error.message!!)
            }
        }
    }

    private fun openCropImageIntent() {
        CropImage.activity().start(requireContext(), this)
    }


    @AfterPermissionGranted(123)
    override fun onStart() {
        super.onStart()
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Log.e("TAG", "ok Permission granted")

        } else {
            (activity as MainActivity).requestPermission()
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.e("TAG", "Permission code$requestCode")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.e("TAG", "Sorry permission not granted");
        (activity as MainActivity).requestPermission()

    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.e("TAG", "onRationaleAccepted")
    }

    override fun onRationaleDenied(requestCode: Int) {
        (activity as MainActivity).requestPermission()
    }

}