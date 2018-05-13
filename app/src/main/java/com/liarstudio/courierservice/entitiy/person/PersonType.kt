package com.liarstudio.courierservice.entitiy.person

enum class PersonType(val pos: Int) {
    SENDER(0),
    RECEIVER(1);

    companion object {
        fun getByPos(pos: Int): PersonType =
                values().firstOrNull { it.pos == pos } ?: SENDER
    }
}