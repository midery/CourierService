package com.liarstudio.courierservice.ui.screen.auth

import android.util.Patterns
import com.liarstudio.courierservice.logic.ServerUtils.BASE_SERVER_URL
import com.liarstudio.courierservice.ui.base.BaseScreenModel
import com.liarstudio.courierservice.ui.base.LoadState
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class AuthScreenModel : BaseScreenModel() {
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var errorMessageRes: Int = 0
    var isRegister = false
    var loadState = LoadState.NONE

    val isEmailValid: Boolean get() = email.isNotBlank() && email.matches(Patterns.EMAIL_ADDRESS.toRegex())
    val isNameValid : Boolean get() = !isRegister || name.isNotBlank()
    val isPasswordValid : Boolean get() = password.length >= 4
    val isValid: Boolean get()  = isEmailValid  && isPasswordValid && isNameValid

    val encryptedPassword: String get() {
        val md5 = MessageDigest.getInstance("MD5")
        md5.reset()
        md5.update(StandardCharsets.UTF_8.encode(password))
        return String.format("%032x", BigInteger(1, md5.digest()))
    }
}