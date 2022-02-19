package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class SplashScreen : AppCompatActivity() {

    private lateinit var mFusedLocation: FusedLocationProviderClient
    private var myRequestCode = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (CheckPermission()) {
            if (LocationEnable()) {
                mFusedLocation.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        NewLocation()
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("lat", location.latitude.toString())
                            intent.putExtra("long", location.longitude.toString())
                            startActivity(intent)
                            finish()
                        }, 3000)
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn On Your GPS Location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestMyPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {
        val locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = LocationRequest.QUALITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.requestLocationUpdates(
            locationRequest, locationCallBack,
            Looper.myLooper()!!
        )
    }

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
        }
    }

    /**
     * This function is to check whether users GPS is ON or OFF
     */
    private fun LocationEnable(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * This function requests the permissions from user
     */
    private fun requestMyPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), myRequestCode
        )
    }

    /**
     * This function checks whether user has granted the permission or not
     */
    private fun CheckPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == myRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }
}