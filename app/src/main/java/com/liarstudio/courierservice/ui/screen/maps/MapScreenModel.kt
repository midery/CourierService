package com.liarstudio.courierservice.ui.screen.maps

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.liarstudio.courierservice.ui.base.screen.BaseScreenModel

class MapScreenModel(
        var location: LatLng
) : BaseScreenModel() {
    lateinit var map: GoogleMap
    var hasParent = false
}