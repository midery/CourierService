package com.liarstudio.courierservice.ui.screen.maps

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.screen.BaseActivity
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject


class MapActivity : BaseActivity<MapScreenModel>(), OnMapReadyCallback, LocationListener {
    @Inject
    lateinit var loadPresenter: MapPresenter

    override fun requestPresenter() = loadPresenter

    override fun getLayout() = R.layout.activity_maps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        loadPresenter.init(intent, googleMap)
        initListeners()
    }

    fun initListeners() {
        ready_btn.setOnClickListener { loadPresenter.finish() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        loadPresenter.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 1
    }

    override fun renderData(screenModel: MapScreenModel) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderState(loadState: LoadState) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
