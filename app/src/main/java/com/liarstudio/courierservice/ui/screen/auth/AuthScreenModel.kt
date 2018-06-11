package com.liarstudio.courierservice.ui.screen.auth

import android.util.Patterns
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * Модель экрана авторизации [AuthActivity]
 *
 * Содержит все поля необходимые для авторизации, а так же логику валидации
 */
class AuthScreenModel : BaseScreenModel() {
    var email: String = ""
    var name: String = ""
    var password: String = ""
    var isRegister = false

    val isEmailValid: Boolean get() = email.isNotBlank() && email.matches(Patterns.EMAIL_ADDRESS.toRegex())
    val isNameValid : Boolean get() = !isRegister || name.isNotBlank()
    val isPasswordValid : Boolean get() = password.length >= 4
    val isValid: Boolean get()  = isEmailValid  && isPasswordValid && isNameValid

    /**
     * Шифрование пароля для безопасной передачи на сервер
     */
    val encryptedPassword: String get() {
        val md5 = MessageDigest.getInstance("MD5")
        md5.reset()
        md5.update(StandardCharsets.UTF_8.encode(password))
        return String.format("%032x", BigInteger(1, md5.digest()))
    }
}