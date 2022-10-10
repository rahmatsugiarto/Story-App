package com.rs.storyapp.ui.liststory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.storyapp.R
import com.rs.storyapp.common.util.goto
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.databinding.ActivityListStoryBinding
import com.rs.storyapp.ui.addstory.AddStoryActivity
import com.rs.storyapp.ui.login.LoginActivity
import com.rs.storyapp.ui.login.dataStore
import com.rs.storyapp.viewmodels.ListStoryViewModel
import com.rs.storyapp.viewmodels.ListStoryViewModelFactory


class ListStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityListStoryBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { StoryAdapter() }
    private lateinit var listStoryViewModel: ListStoryViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        setupRecyclerView()

        val pref = DataUserPreference.getInstance(dataStore)
        listStoryViewModel = ViewModelProvider(
            this, ListStoryViewModelFactory(pref)
        )[ListStoryViewModel::class.java]
        listStoryViewModel.getToken().observe(this) { token ->
            listStoryViewModel.getStories(token)
        }

        listStoryViewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                if (listStoryViewModel.listStories.isNotEmpty()) {
                    mAdapter.setData(listStoryViewModel.listStories)
                } else {
                    binding.noData.visibility = View.VISIBLE
                }
            }
        }

        listStoryViewModel.isLoading.observe(this) { isLoading ->
            binding.progressCircular.isVisible = isLoading
        }

        listStoryViewModel.message.observe(this) { message ->
            Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
        }

        listStoryViewModel.messageWhenFailure.observe(this) { messageWhenFailure ->
            Toast.makeText(this, messageWhenFailure, Toast.LENGTH_SHORT).show()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addStory -> {
                    goto(AddStoryActivity::class.java)
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                R.id.setting -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvStory.adapter = mAdapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        val alert = builder.create()
        builder.setTitle(R.string.logout)
        builder.setMessage(R.string.are_you_sure_logout)

        builder.setPositiveButton(R.string.yes) { _, _ ->
            listStoryViewModel.resetDataUser()
            Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show()
            goto(LoginActivity::class.java)
            finish()
        }

        builder.setNegativeButton(R.string.no) { _, _ ->
            alert.cancel()
        }
        builder.show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}