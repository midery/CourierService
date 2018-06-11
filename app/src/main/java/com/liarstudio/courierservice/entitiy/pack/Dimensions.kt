package com.liarstudio.courierservice.entitiy.pack

class Dimensions(val id: Long = 0L,
        var x: Double,
        var y: Double,
        var z: Double,
        var weight: Double
) {
    val isEmpty: Boolean = x == 0.0 || y == 0.0 || z == 0.0 || weight == 0.0
}