package com.liarstudio.courierservice.logic.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.EMPTY_STRING
import javax.inject.Inject

val USER_KEY = "current_user"

class UserStorage @Inject constructor(
        private val preferences: SharedPreferences,
        private val gson: Gson
) {

    fun get(): User {
        val serializedUser = preferences.getString(USER_KEY, EMPTY_STRING)
        return if (serializedUser.isNotEmpty())
            gson.fromJson<User>(serializedUser, User::class.java)
        else
            User()
    }

    fun put(user: User) {
        val serializedUser = gson.toJson(user, User::class.java)
        preferences.edit().putString(USER_KEY, serializedUser).apply()
    }
}