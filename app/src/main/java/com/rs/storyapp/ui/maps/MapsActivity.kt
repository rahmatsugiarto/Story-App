package com.rs.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.rs.storyapp.R
import com.rs.storyapp.common.util.showToastShort
import com.rs.storyapp.data.Result
import com.rs.storyapp.databinding.ActivityMapsBinding
import com.rs.storyapp.ui.liststory.ListStoryActivity
import com.rs.storyapp.viewmodels.MapsStoryViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsStoryViewModel: MapsStoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        token = intent.getStringExtra(ListStoryActivity.EXTRA_TOKEN).toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getMyLocation()
        setMapStyle()
        getStories(token)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", exception)
        }
    }


    private fun getStories(token: String) {
        mapsStoryViewModel.getStoriesWithLocation(token).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showToastShort(getString(R.string.loading))
                    }
                    is Result.Success -> {
                        if (result.data.listStory.isNotEmpty()) {
                            val boundsBuilder = LatLngBounds.Builder()
                            result.data.listStory.forEach { story ->
                                val latLng = LatLng(story.lat, story.lon)
                                mMap.addMarker(
                                    MarkerOptions().position(latLng).title(story.name)
                                        .snippet(story.description)
                                )
                                boundsBuilder.include(latLng)
                            }

                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    300
                                )
                            )
                        } else {
                            showToastShort(getString(R.string.no_data))
                        }
                    }
                    is Result.Error -> {
                        showToastShort(result.error)
                    }
                }
            }
        }
    }
}

