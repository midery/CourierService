package com.liarstudio.courierservice.ui.screen.auth

import android.content.Intent
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entities.user.User
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.logic.ServerUtils.BASE_SERVER_URL
import com.liarstudio.courierservice.logic.user.UserAPI
import com.liarstudio.courierservice.ui.base.BasePresenter
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class AuthPresenter constructor(view: AuthActivity) : BasePresenter<AuthActivity>(view) {

    val model = AuthScreenModel()

    fun authenticate() {
        if (isInputValid()) {
            sendAuthRequest()
        }
    }

    fun isInputValid() : Boolean{
        var valid = true
        if (!model.isEmailValid()) {
            valid = false
            view.setEmailError("Введен неверный e-mail.")
        }
        if (model.isRegister) {
            if (!model.isNameValid()) {
                valid = false
                view.setNameError("Имя не может быть пустым.")
            }
        }
        if (!model.isPasswordValid()) {
            valid = false
           view.setPasswordError("Пароль слишком короткий.")
        }
        return valid
    }

    fun sendAuthRequest() {

    }

    private fun sendRequest() {
        model.loadState = LoadState.LOADING
        model.errorMessageRes = 0
        view.render(model)


        val retrofit = Retrofit.Builder()
                .baseUrl(model.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(UserAPI::class.java)


        val userCall = if (model.isRegister)
            api.register(model.email.toLowerCase(),
                    model.name,
                    ServerUtils.encryptPassword(model.password))
        else
            api.login(model.email.toLowerCase(),
                    ServerUtils.encryptPassword(model.password))

        userCall.enqueue(
                object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        model.errorMessageRes = when (response.code()) {
                        //Если удалось войти и тело ответа не пустое, устанавливаем текущего пользователя
                        //и запускаем MainActivity
                            HttpURLConnection.HTTP_OK -> {
                                if (response.body() != null) {
                                    ServerUtils.CURRENT_USER = response.body()
                                    ServerUtils.IS_ADMIN = ServerUtils.CURRENT_USER.role == 1
                                    view.startActivity(Intent(view, MainActivity::class.java))
                                    view.finish()
                                }
                                0
                            }
                            HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_http_forbidden
                            HttpURLConnection.HTTP_CONFLICT -> R.string.error_http_conflict
                            else -> R.string.error_auth
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        model.errorMessageRes = R.string.error_could_not_connect_to_server
                    }
                }
        )
    }



    fun registerChanged(isRegister: Boolean) {
        model.isRegister = isRegister
        view.render(model)
    }

    fun urlChanged(url: String) { model.url = url }
}