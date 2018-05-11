package com.liarstudio.courierservice.entitiy.user

import com.liarstudio.courierservice.ui.base.EMPTY_STRING
import com.orm.SugarRecord

class User(
        var id: Int = -1,
        var email: String = EMPTY_STRING,
        var password: String = EMPTY_STRING,
        var name: String = EMPTY_STRING,
        var role: Int = 0
) {
    val isAdmin: Boolean get() = role == 1

    companion object {
        var CURRENT = User()
    }
}
