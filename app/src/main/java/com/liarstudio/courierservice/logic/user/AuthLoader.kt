package com.liarstudio.courierservice.logic.user

import javax.inject.Inject

class AuthLoader @Inject constructor(
        val api: AuthApi
) {
    /**
     * Авторизация пользователя
     *
     * @param email электронный адрес
     * @param encryptedPassword зашифрованный пароль
     *
     * @return Observable с пользователем
     */
    fun login(email: String, encryptedPassword: String) =
            api.login(email, encryptedPassword)

    /**
     * Регистрация пользователя
     * @param email электронный адрес
     * @param name имя пользователя
     * @param password зашифрованный пароль
     *
     * @return Observable с созданным пользователем
     */
    fun register(email: String, name: String, encryptedPassword: String) =
            api.register(email, name, encryptedPassword)
}