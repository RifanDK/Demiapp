package com.test123.testmap

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*

class MapAddressActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var currentLocationButton: Button
    private lateinit var saveLocationButton: Button
    private var selectedLatLng: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_address_layout)

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key), Locale.US)
        }

        // Initialize the AutocompleteSupportFragment
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    selectedLatLng = latLng
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    currentMarker?.remove()
                    currentMarker = map.addMarker(MarkerOptions().position(latLng).title(place.name))
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(this@MapAddressActivity, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })

        // Set up the MapView
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up buttons
        currentLocationButton = findViewById(R.id.current_location_button)
        saveLocationButton = findViewById(R.id.save_location_button)

        currentLocationButton.setOnClickListener {
            // Check location permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                // Permissions already granted, fetch current location
                fetchCurrentLocation()
            }
        }

        saveLocationButton.setOnClickListener {
            // Save the selected location and return it to AddressActivity
            selectedLatLng?.let {
                val resultIntent = Intent()
                resultIntent.putExtra("ADDRESS", "${it.latitude}, ${it.longitude}")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } ?: run {
                Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)
                        selectedLatLng = currentLatLng
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        currentMarker?.remove()
                        currentMarker = map.addMarker(MarkerOptions().position(currentLatLng).title("Current Location"))
                    } ?: run {
                        Toast.makeText(this, "Unable to fetch current location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Security Exception: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMapClickListener { latLng ->
            selectedLatLng = latLng
            currentMarker?.remove()
            currentMarker = map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        // Default location (for example, Jakarta)
        val defaultLocation = LatLng(-6.2088, 106.8456)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, fetch current location
                fetchCurrentLocation()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
