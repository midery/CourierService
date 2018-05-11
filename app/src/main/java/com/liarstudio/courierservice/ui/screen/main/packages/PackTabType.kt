package com.liarstudio.courierservice.ui.screen.main.packages

enum class PackTabType(val pos: Int) {
    ACTIVE(0),
    DELIVERED(1),
    ALL(2);

    companion object {
        fun getByPos(pos: Int): PackTabType =
                values().firstOrNull { it.pos == pos } ?: ACTIVE
    }
}