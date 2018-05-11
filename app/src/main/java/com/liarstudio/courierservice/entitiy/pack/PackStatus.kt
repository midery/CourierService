package com.liarstudio.courierservice.entitiy.pack

enum class PackStatus(val pos: Int) {
    NEW(0),
    SET(1),
    IN_PROCESS(2),
    REJECTED(3),
    DELIVERED(4)
}