package com.rs.storyapp.ui.addstory


import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.rs.storyapp.R
import com.rs.storyapp.common.util.reduceFileImage
import com.rs.storyapp.common.util.rotateBitmap
import com.rs.storyapp.common.util.uriToFile
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.databinding.ActivityAddStoryBinding
import com.rs.storyapp.ui.liststory.ListStoryActivity.Companion.CAMERA_X_RESULT
import com.rs.storyapp.ui.login.dataStore
import com.rs.storyapp.viewmodels.AddStoryViewModel
import com.rs.storyapp.viewmodels.AddStoryViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddStoryBinding.inflate(layoutInflater) }
    private var getFile: File? = null
    private lateinit var token: String
    private lateinit var addStoryViewModel: AddStoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = DataUserPreference.getInstance(dataStore)
        addStoryViewModel = ViewModelProvider(
            this, AddStoryViewModelFactory(pref)
        )[AddStoryViewModel::class.java]
        addStoryViewModel.getToken().observe(this) { token ->
            this.token = token
        }

        addStoryViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                finish()
            }
        }

        addStoryViewModel.isLoading.observe(this) { isLoading ->
            binding.progressCircular.isVisible = isLoading
        }

        addStoryViewModel.message.observe(this) { message ->
            Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
        }

        addStoryViewModel.messageWhenFailure.observe(this) { messageWhenFailure ->
            Toast.makeText(this, messageWhenFailure, Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            btnCamera.setOnClickListener { startCameraX() }
            btnGallery.setOnClickListener { startGallery() }
            buttonAdd.setOnClickListener { addStory() }
        }

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(CameraActivity.PICTURE_KEY) as File
            val isBackCamera =
                it.data?.getBooleanExtra(CameraActivity.ISBACKCAMERA_KEY, true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

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
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                addStoryViewModel.addStory(imageMultipart, desc, token)
            }
        }
    }

}
