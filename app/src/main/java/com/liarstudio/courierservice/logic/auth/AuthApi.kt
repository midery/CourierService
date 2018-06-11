package com.liarstudio.courierservice.logic.auth

import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.logic.UrlAuth
import com.liarstudio.courierservice.logic.auth.request.UserRequest

import io.reactivex.Observable

import retrofit2.Call
import retrofit2.http.*


interface AuthApi {

    /**
     * Авторизация пользователя
     *
     * @param email    электронный адрес
     * @param password зашифрованный пароль
     * @return Observable с пользователем
     */
    @GET(UrlAuth.AUTH_LOGIN)
    fun login(
            @Query("email") email: String,
            @Query("password") password: String
    ): Observable<User>

    /**
     * Регистрация пользователя
     *
     * @param userRequest    маппинг-модель пользователя
     * @return Observable с созданным пользователем
     */
    //@FormUrlEncoded
    @POST(UrlAuth.AUTH_REGISTER)
    fun register(@Body userRequest: UserRequest) : Observable<User>
            //@Field("email") email: String,
            //@Field("name") name: String,
            //@Field("password") password: String): Observable<User>

}
