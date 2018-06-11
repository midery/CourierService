package com.liarstudio.courierservice.logic.user

import com.liarstudio.courierservice.entitiy.user.User
import io.reactivex.Observable
import javax.inject.Inject

class UserLoader @Inject constructor(
        private val userApi: UserApi,
        private val userStorage: UserStorage
){

    /**
     * Получение списка пользователей
     *
     * @return  Observable со списком пользователей
     */
    fun loadUsers(): Observable<List<User>>
        = userApi.loadUsers()

    /**
     * Получение пользователя по id
     *
     * @param id идентификатор пользователя
     *
     * @return Observable с пользователем
     */
    fun loadUser(id: Long): Observable<User> =
            userApi.loadUser(id)

    fun getCurrentUser(): User = userStorage.get()

    fun putCurrentUser(current: User) = userStorage.put(current)
}