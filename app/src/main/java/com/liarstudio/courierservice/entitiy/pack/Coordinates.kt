package com.liarstudio.courierservice.entitiy.pack

import java.io.Serializable

data class Coordinates(val id: Long = 0L,
                       var latitude: Double = 0.0,
                       var longitude: Double = 0.0
) : Serializable {

    val isEmpty: Boolean = longitude == 0.0 || latitude == 0.0
}