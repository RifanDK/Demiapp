package com.test123.testmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import android.app.AlertDialog
import android.graphics.Color
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

internal class MyLocationLayerActivity : AppCompatActivity(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private val locationPermissionRequestCode = 1
    private lateinit var mMap: GoogleMap
    private lateinit var autocompleteFragment: AutocompleteSupportFragment
    private val markers = mutableListOf<Marker>() // List to keep track of markers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_location)

        // Initialize Places SDK
        Places.initialize(applicationContext, "AIzaSyARQpXsLnAF2GgSxLhVfMEcQXztNRTJ_yo")

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionRequestCode
            )
        } else {
            initMap()
        }

        // Set up button to clear markers
        findViewById<Button>(R.id.btn_clear_markers).setOnClickListener {
            clearMarkers()
            clearPolyline()
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configure AutocompleteSupportFragment
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setCountries("ID") // Restrict results to Indonesia

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Place selected, add marker to the map
                val placeLocation = place.latLng
                val markerOptions = MarkerOptions().position(placeLocation).title(place.name)
                val marker = mMap.addMarker(markerOptions)
                if (marker != null) {
                    markers.add(marker) // Keep track of added marker
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 15f))
            }

            override fun onError(status: Status) {
                // Handle error
                Toast.makeText(this@MyLocationLayerActivity, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.setOnMyLocationClickListener(this)
            mMap.setOnMarkerClickListener(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initMap()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun clearMarkers() {
        for (marker in markers) {
            marker.remove()
        }
        markers.clear()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        AlertDialog.Builder(this)
            .setTitle("Navigate")
            .setMessage("Do you want to navigate to this location?")
            .setPositiveButton("Yes") { dialog, which ->
                navigateToMarker(marker)
            }
            .setNegativeButton("No", null)
            .show()
        return true
    }

    private fun navigateToMarker(marker: Marker) {
        val userLocation = mMap.myLocation ?: return
        val origin = "${userLocation.latitude},${userLocation.longitude}"
        val destination = "${marker.position.latitude},${marker.position.longitude}"
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&mode=walking&key=AIzaSyARQpXsLnAF2GgSxLhVfMEcQXztNRTJ_yo"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = fetchDirections(url)
                withContext(Dispatchers.Main) {
                    drawPolyline(data)
                    showDirections(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MyLocationLayerActivity, "Failed to fetch directions.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDirections(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val routes = jsonObject.getJSONArray("routes")
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")
            val directions = StringBuilder()

            for (i in 0 until steps.length()) {
                val step = steps.getJSONObject(i)
                val distance = step.getJSONObject("distance").getString("text")
                val htmlInstructions = step.getString("html_instructions").replace(Regex("<(.*?)*>"), "") // Remove HTML tags
                directions.append("In $distance, $htmlInstructions\n")
            }

            AlertDialog.Builder(this)
                .setTitle("Directions")
                .setMessage(directions.toString())
                .setPositiveButton("OK", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchDirections(url: String): String {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }

    private fun drawPolyline(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val routes = jsonObject.getJSONArray("routes")
            val points = routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")
            val decodedPath = PolyUtil.decode(points)
            mMap.addPolyline(PolylineOptions().addAll(decodedPath).width(10f).color(Color.BLUE))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(decodedPath[0], 15f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearPolyline() {
        mMap.clear()
    }
}
