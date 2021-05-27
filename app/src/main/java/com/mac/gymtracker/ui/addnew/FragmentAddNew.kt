package com.mac.gymtracker.ui.addnew

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.databinding.FragmentAddNewBinding
import com.mac.gymtracker.ui.exerciselist.dao.ExerciseList
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.ui.exerciselist.data.LocalExerciselistRepo
import com.mac.gymtracker.utils.getResizedBitmap
import com.mac.gymtracker.utils.showSnack
import com.mac.gymtracker.utils.toLocalBitMap
import com.theartofdev.edmodo.cropper.CropImage
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.RationaleCallbacks
import java.io.ByteArrayOutputStream
import java.util.*

class FragmentAddNew : Fragment(), EasyPermissions.PermissionCallbacks, RationaleCallbacks {
    private var _binding: FragmentAddNewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.toolbar.title = FragmentAddNewArgs.fromBundle(requireArguments()).exerciseName
        if (FragmentAddNewArgs.fromBundle(requireArguments()).exerciselist != null) {
            var exerciseList: ExerciseListModle =
                FragmentAddNewArgs.fromBundle(requireArguments()).exerciselist as ExerciseListModle
            binding.ivExercise.setImageBitmap(exerciseList.imageString.toLocalBitMap())
            binding.edNewExerciseName.setText(exerciseList.name)
            binding.addNewBtn.text = "Edit"
        } else {
            binding.addNewBtn.text = "Add"
        }

        binding.ivExercise.setOnClickListener {
            openCropImageIntent()
        }

        binding.addNewBtn.setOnClickListener {
            addDataOnDatabase()
        }

        binding.toolbar.setNavigationOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private fun addDataOnDatabase() {
        if (binding.addNewBtn.text.equals("Add")) {
            val exerciseName = binding.edNewExerciseName.text.toString()
            if (exerciseName != "" && resultUri != null) {
                LocalExerciselistRepo(requireContext()).insertExercise(
                    activity?.contentResolver,
                    resultUri!!,
                    exerciseName,
                    FragmentAddNewArgs.fromBundle(requireArguments()).exerciseId
                ) { isError, error ->
                    if (!isError) {
                        view?.showSnack("Data Inserted Successfully")
                        (activity as MainActivity).onBackPressed()
                    } else {
                        view?.showSnack(error.message!!)
                    }
                }
            } else {
                view?.showSnack("Please select both image adn Exercise Name")
            }
        } else {
            var exerciseList: ExerciseListModle =
                FragmentAddNewArgs.fromBundle(requireArguments()).exerciselist as ExerciseListModle
            exerciseList.name = binding.edNewExerciseName.text.toString()
            if (resultUri!=null) {
                var bitmaps = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, resultUri)
                var bitmap = getResizedBitmap(bitmaps, 500)
                var outputStream = ByteArrayOutputStream()
                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                var byteArray = outputStream.toByteArray()
                var encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
                exerciseList.imageString = encodedString
            }
            LocalExerciselistRepo(requireContext()).editContent(exerciseList) {
                isError,throwable->
                if (!isError) {
                    view?.showSnack("Data Edited Successfully")
                    (activity as MainActivity).onBackPressed()
                } else {
                    view?.showSnack("Error ${throwable.message}")
                }

            }

        }

    }

    private var resultUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                resultUri = result.uri
                Glide.with(requireContext()).load(resultUri).into(binding.ivExercise)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
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
        Log.e("TAG", "Sorry permission not granted")
        (activity as MainActivity).requestPermission()

    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.e("TAG", "onRationaleAccepted")
    }

    override fun onRationaleDenied(requestCode: Int) {
        (activity as MainActivity).requestPermission()
    }
}