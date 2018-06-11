package com.liarstudio.courierservice.ui.screen.maps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.support.v13.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.liarstudio.courierservice.entitiy.pack.Coordinates
import com.liarstudio.courierservice.ui.base.COORDINATES
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter
import javax.inject.Inject


class MapPresenter @Inject constructor(
        view: MapActivity
) : BasePresenter<MapActivity>(view) {

    val REQUEST_USER_LOCATION = 31
    val EMPTY_LOCATION = LatLng(.0, .0)

    val model = MapScreenModel(EMPTY_LOCATION)

    fun init(extra: Intent, map: GoogleMap) {
        val coordinates = extra.getSerializableExtra(EXTRA_FIRST) as? Coordinates
                ?: Coordinates()
        model.location = LatLng(coordinates.latitude, coordinates.longitude)
        model.map = map
        model.hasParent = view.callingActivity != null

        if (model.hasParent)
            map.setOnMapClickListener { addMarker(it) }

        if (model.location != EMPTY_LOCATION) {
            moveCamera(model.location)
            addMarker(model.location)
        } else {
            requestUserLocation()
        }

    }

    fun finish() {
        if (model.hasParent) {
            if (model.location != EMPTY_LOCATION) {
                view.setResult(Activity.RESULT_OK,
                        Intent().putExtra(COORDINATES, Coordinates(0L,
                                model.location.latitude,
                                model.location.longitude
                        )))
                view.finish()
            }
        } else {
            view.finish()
        }
    }

    fun addMarker(location: LatLng) {
        model.map.clear()
        model.location = location
        model.map.addMarker(MarkerOptions().position(location).title("Адрес доставки"))
    }

    fun requestUserLocation() {
        ActivityCompat.requestPermissions(view, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_USER_LOCATION)
    }

    fun onRequestPermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val resultsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        val locationManager = view.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (requestCode == REQUEST_USER_LOCATION && resultsGranted && (isGPSEnabled || isNetworkEnabled)) {
            try {
                if (isNetworkEnabled)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (1000 * 60).toLong(), 10f, view)

                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (1000 * 60).toLong(), 10f, view)

                val location = locationManager.getLastKnownLocation(locationManager.getBestProvider(Criteria(), false))

                if (location == null)
                    moveToDefault()
                else {
                    val myLocation = LatLng(location.latitude, location.longitude)
                    moveCamera(myLocation)
                }
            } catch (se: SecurityException) {
                se.printStackTrace()
                moveToDefault()
            }
        } else {
            moveToDefault()
        }
    }

    private fun moveCamera(location: LatLng) {
        model.map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f))
    }

    private fun moveToDefault() {
        val defaultLoc = LatLng(51.662805, 39.184641)
        moveCamera(defaultLoc)
    }
}