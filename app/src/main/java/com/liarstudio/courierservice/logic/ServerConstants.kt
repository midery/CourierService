package com.liarstudio.courierservice.logic

import com.liarstudio.courierservice.entitiy.user.User

object UrlServer {

    //URL, по которому расположен локальный сервер
    const val BASE_LOCAL_URL = "http://207.154.210.82:8080/"

    //URL, по которому расположен сервер
    const val BASE_SERVER_URL = BASE_LOCAL_URL //"http://207.154.210.82:8080/"

}

object UrlAuth {
    const val BASE_AUTH_URL = "auth"
}

object UrlUser {
    const val BASE_USER_URL = "users"
    const val GET_USER = "$BASE_USER_URL/{user_id}"
    const val GET_USERS_ROLE = "$BASE_USER_URL/role/{role}"
}

object UrlPackage {
    const val BASE_PACKAGE_URL = "package"
    const val GET_PACKAGES = BASE_PACKAGE_URL
    const val GET_PACKAGE = "$BASE_PACKAGE_URL/{package_id}"
    const val GET_COURIER_PACKAGES = "$BASE_PACKAGE_URL/courier/{courier_id}"
    const val GET_NEW_COURIER_PACKAGES = "$GET_COURIER_PACKAGES/new"
    const val GET_PACKAGES_ADMIN = "$BASE_PACKAGE_URL/admin"
    const val DELETE_PACKAGE = "$BASE_PACKAGE_URL/delete/{package_id}"
}