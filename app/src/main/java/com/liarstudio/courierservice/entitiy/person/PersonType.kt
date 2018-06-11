package com.liarstudio.courierservice.entitiy.person

enum class PersonType(val pos: Int) {
    PRIVATE(0),
    COMPANY(1);

    companion object {
        fun getByPos(pos: Int): PersonType =
                values().firstOrNull { it.pos == pos } ?: PRIVATE
    }
}