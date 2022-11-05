package com.rs.storyapp.ui.addstory


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.rs.storyapp.R
import com.rs.storyapp.common.util.createCustomTempFile
import com.rs.storyapp.common.util.reduceFileImage
import com.rs.storyapp.common.util.showToastShort
import com.rs.storyapp.common.util.uriToFile
import com.rs.storyapp.data.Result
import com.rs.storyapp.databinding.ActivityAddStoryBinding
import com.rs.storyapp.viewmodels.AddStoryViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*

class AddStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddStoryBinding.inflate(layoutInflater) }
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: Location? = null
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        lifecycleScope.launchWhenCreated {
            launch {
                addStoryViewModel.getToken().collect { authToken ->
                    if (!authToken.isNullOrEmpty()) token = authToken
                }
            }
        }

        binding.apply {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { startGallery() }
            buttonAdd.setOnClickListener { addStory() }
        }


        binding.swLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLocation()
                binding.tvLocation.visibility = View.VISIBLE
            } else {
                this.location = null
                binding.tvLocation.visibility = View.GONE
            }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.rs.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun addStory() {
        val description = binding.edAddDescription.text.toString()

        when {
            getFile == null -> {
                Toast.makeText(
                    this@AddStoryActivity,
                    getString(R.string.validation_add_image),
                    Toast.LENGTH_SHORT
                ).show()
            }

            description.trim().isEmpty() -> {
                Toast.makeText(
                    this@AddStoryActivity,
                    getString(R.string.validation_add_caption),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                val file = reduceFileImage(getFile as File)
                val desc = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                var latitude: RequestBody? = null
                var longitude: RequestBody? = null

                if (location != null) {
                    latitude = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                    longitude = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                }
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                addStoryViewModel.addStory(imageMultipart, desc, token, latitude, longitude)
                    .observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressCircular.visibility = View.VISIBLE
                                    binding.buttonAdd.isEnabled = false
                                }
                                is Result.Success -> {
                                    binding.progressCircular.visibility = View.GONE
                                    Toast.makeText(this, result.data.message, Toast.LENGTH_LONG)
                                        .show()
                                    finish()
                                }
                                is Result.Error -> {
                                    binding.progressCircular.visibility = View.GONE
                                    binding.buttonAdd.isEnabled = true
                                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    this.location = currentLocation
                    binding.apply {
                        tvLocation.text =
                            getAddressName(currentLocation.latitude, currentLocation.longitude)
                    }

                } else {
                    showToastShort(getString(R.string.location_enable))
                    binding.swLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLocation()
                }
                else -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.location_denied),
                        Snackbar.LENGTH_SHORT
                    )
                        .setAction(getString(R.string.setting_location)) {
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { intent ->
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        .show()
                    binding.swLocation.isChecked = false
                }
            }
        }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@AddStoryActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = "${list[0].adminArea}, ${list[0].countryName}"
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

}
