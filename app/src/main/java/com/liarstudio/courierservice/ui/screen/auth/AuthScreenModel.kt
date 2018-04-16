package com.liarstudio.courierservice.ui.screen.auth

import android.util.Patterns
import com.liarstudio.courierservice.logic.ServerUtils.BASE_SERVER_URL
import com.liarstudio.courierservice.ui.base.BaseScreenModel
import com.liarstudio.courierservice.ui.base.LoadState

class AuthScreenModel : BaseScreenModel(){
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var errorMessageRes: Int = 0
    var isRegister = false
    var loadState = LoadState.NONE
    var url = BASE_SERVER_URL

    fun isEmailValid() = email.isNotBlank() && email.matches(Patterns.EMAIL_ADDRESS.toRegex())

    fun isNameValid() = name.isNotBlank()

    fun isPasswordValid() = password.length >= 4
}