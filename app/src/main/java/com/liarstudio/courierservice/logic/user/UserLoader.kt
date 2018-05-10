package com.liarstudio.courierservice.logic.user

import com.liarstudio.courierservice.entitiy.user.User
import retrofit2.Call
import javax.inject.Inject

class UserLoader @Inject constructor(
        private val userApi: UserApi
){

    /**
     * Получение списка пользователей
     *
     * @param role роль пользователя
     *
     * @return  Observable со списком пользователей
     */
    fun loadUsers(role: Int): Call<List<User>>
        = userApi.loadUsersWithRole(role)


    /**
     * Получение пользователя по id
     *
     * @param id идентификатор пользователя
     *
     * @return Observable с пользователем
     */
    fun loadUser(id: Int): Call<User> =
            userApi.loadUser(id)
}