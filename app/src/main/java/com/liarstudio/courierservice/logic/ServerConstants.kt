package com.liarstudio.courierservice.logic

import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.logic.UrlUser.BASE_USER_URL

object UrlServer {

    //URL, по которому расположен локальный сервер
    const val BASE_LOCAL_URL = "http://192.168.0.102:8080/"

    //URL, по которому расположен сервер
    const val BASE_SERVER_URL = "http://207.154.210.82:8080/"

}

object UrlAuth {
    const val BASE_AUTH_URL = "auth"
    const val AUTH_LOGIN = "$BASE_AUTH_URL/login"
    const val AUTH_REGISTER = "$BASE_AUTH_URL/register"
    const val AUTH_LOGOUT = "$BASE_AUTH_URL/logout"
}

object UrlUser {
    const val BASE_USER_URL = "users"
    const val GET_USER = "$BASE_USER_URL/{user_id}"
    const val GET_USERS_ROLE = "$BASE_USER_URL/role/{role}"
}

object UrlPackage {
    const val BASE_PACKAGE_URL = "packages"
    const val GET_PACKAGES = BASE_PACKAGE_URL
    const val GET_PACKAGE = "$BASE_PACKAGE_URL/{package_id}"
    const val GET_USER_PACKAGES = "$BASE_USER_URL/{user_id}/$BASE_PACKAGE_URL"
    const val GET_COURIER_PACKAGE = "$BASE_USER_URL/{user_id}/$BASE_PACKAGE_URL/{package_id}"
}