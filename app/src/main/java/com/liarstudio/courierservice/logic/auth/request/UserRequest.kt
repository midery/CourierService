package com.liarstudio.courierservice.logic.auth.request

import com.google.gson.annotations.SerializedName

class UserRequest(
        @SerializedName("email") val email: String,
        @SerializedName("name") val name: String,
        @SerializedName("password") val password: String
) {
}