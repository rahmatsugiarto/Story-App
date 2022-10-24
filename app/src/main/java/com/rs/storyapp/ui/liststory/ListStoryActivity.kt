package com.rs.storyapp.ui.liststory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.rs.storyapp.R
import com.rs.storyapp.adapter.LoadingStateAdapter
import com.rs.storyapp.adapter.StoryAdapter
import com.rs.storyapp.common.util.goto
import com.rs.storyapp.common.util.gotoWithToken
import com.rs.storyapp.common.util.wrapEspressoIdlingResource
import com.rs.storyapp.databinding.ActivityListStoryBinding
import com.rs.storyapp.ui.addstory.AddStoryActivity
import com.rs.storyapp.ui.login.LoginActivity
import com.rs.storyapp.ui.maps.MapsActivity
import com.rs.storyapp.viewmodels.ListStoryViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ListStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityListStoryBinding.inflate(layoutInflater) }
    private lateinit var storyAdapter: StoryAdapter
    private val listStoryViewModel: ListStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var token = ""

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, getString(R.string.didnt_get_permission), Toast.LENGTH_SHORT
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
        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        getStories()

        binding.fab.setOnClickListener {
            gotoWithToken(MapsActivity::class.java, token)
        }

        binding.swiperefresh.setOnRefreshListener {
            getStories()
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

//    override fun onRestart() {
//        super.onRestart()
//        getStories()
//        storyAdapter.refresh()
//    }

    private fun getStories() {
        storyAdapter = StoryAdapter()
        listStoryViewModel.getStories(token).observe(this) { pagingData ->
            wrapEspressoIdlingResource {
                storyAdapter.submitData(lifecycle, pagingData)
            }

            lifecycleScope.launch {
                storyAdapter.loadStateFlow.collectLatest {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            binding.apply {
                                progressCircular.visibility = View.VISIBLE
                                rvStory.visibility = View.GONE
                                noData.visibility = View.GONE
                                tvError.visibility = View.GONE
                            }
                        }
                        is LoadState.NotLoading -> {
                            binding.progressCircular.visibility = View.GONE
                            if (storyAdapter.itemCount < 1) {
                                binding.apply {
                                    rvStory.visibility = View.GONE
                                    noData.visibility = View.VISIBLE
                                }
                            } else {
                                binding.apply {
                                    rvStory.visibility = View.VISIBLE
                                    noData.visibility = View.GONE
                                    tvError.visibility = View.GONE
                                }
                            }
                        }
                        is LoadState.Error -> {
                            binding.apply {
                                progressCircular.visibility = View.GONE
                                rvStory.visibility = View.GONE
                                tvError.visibility = View.VISIBLE
                            }

                        }
                    }

                }
            }
            binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
        binding.swiperefresh.isRefreshing = false
    }

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        val alert = builder.create()
        builder.setTitle(R.string.logout)
        builder.setMessage(R.string.are_you_sure_logout)

        builder.setPositiveButton(R.string.yes) { _, _ ->
            listStoryViewModel.resetUserData()
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
        const val EXTRA_TOKEN = "token"
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}