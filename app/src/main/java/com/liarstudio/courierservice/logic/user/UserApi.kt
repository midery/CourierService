package com.liarstudio.courierservice.logic.user

import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.logic.UrlUser.GET_USER
import com.liarstudio.courierservice.logic.UrlUser.GET_USERS_ROLE
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    /**
     * Получение списка пользователей
     *
     * @return  Call со списком пользователей
     */
    @GET(GET_USERS_ROLE)
    fun loadUsers(): Observable<List<User>>


    /**
     * Получение пользователя по id
     *
     * @param id идентификатор пользователя
     *
     * @return Call с пользователем
     */
    @GET(GET_USER)
    fun loadUser(@Path("user_id") id: Long): Observable<User>
}