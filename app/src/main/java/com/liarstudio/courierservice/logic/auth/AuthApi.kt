package com.liarstudio.courierservice.logic.auth

import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.logic.UrlAuth

import io.reactivex.Observable

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthApi {

    /**
     * Авторизация пользователя
     *
     * @param email    электронный адрес
     * @param password зашифрованный пароль
     * @return Observable с пользователем
     */
    @GET(UrlAuth.BASE_AUTH_URL)
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<User>


    /**
     * Регистрация пользователя
     *
     * @param email    электронный адрес
     * @param name     имя пользователя
     * @param password зашифрованный пароль
     * @return Observable с созданным пользователем
     */
    @FormUrlEncoded
    @POST(UrlAuth.BASE_AUTH_URL)
    fun register(
            @Field("email") email: String,
            @Field("name") name: String,
            @Field("password") password: String): Observable<User>

}
