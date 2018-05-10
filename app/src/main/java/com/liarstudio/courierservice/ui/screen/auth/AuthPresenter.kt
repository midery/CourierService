package com.liarstudio.courierservice.ui.screen.auth

import android.content.Intent
import com.jakewharton.rxbinding2.InitialValueObservable
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.ui.base.BasePresenter
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.SnackController
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import io.reactivex.disposables.Disposables
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

@PerScreen
class AuthPresenter @Inject constructor(
        private val authLoader: AuthLoader,
        private val snackController: SnackController,
        view: AuthActivity
) : BasePresenter<AuthActivity>(view) {

    val model = AuthScreenModel()
    var emailChangesDisposable = Disposables.disposed()
    var passwordChangesDisposable = Disposables.disposed()
    var nameChangesDisposable = Disposables.disposed()
    var authDisposable = Disposables.disposed()

    fun authenticate() {
        if (model.isValid)
            sendAuthRequest()
        else
            snackController.show(R.string.auth_input_error)
    }

    private fun sendAuthRequest() {
        model.loadState = LoadState.LOADING
        model.errorMessageRes = 0
        view.render(model)

        val request = if (model.isRegister)
            authLoader.register(model.email, model.name, model.encryptedPassword)
        else
            authLoader.login(model.email, model.encryptedPassword)

        authDisposable = subscribe(request,
                {
                    model.loadState = LoadState.NONE
                    ServerUtils.CURRENT_USER = it
                    view.startActivity(Intent(view, MainActivity::class.java))
                    view.finish()
                },
                {
                    model.loadState = LoadState.ERROR
                    view.render(model)
                    if (it is HttpException) {
                        snackController.show(
                                when (it.code()) {
                                    HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_http_forbidden
                                    HttpURLConnection.HTTP_CONFLICT -> R.string.error_http_conflict
                                    else -> R.string.error_auth
                                })
                    }
                })
    }

    fun registerChanged(isRegister: Boolean) {
        model.isRegister = isRegister
        view.render(model)
    }

    fun observeEmailChanges(textObservable: InitialValueObservable<CharSequence>) {
        emailChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.email = it
                    view.showEmailError(!model.isEmailValid)
                })
    }

    fun observePasswordChanges(textObservable: InitialValueObservable<CharSequence>) {
        passwordChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.password = it
                    view.showPasswordError(!model.isPasswordValid)
                })
    }

    fun observeNameChanges(textObservable: InitialValueObservable<CharSequence>) {
        nameChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.name = it
                    view.showNameError(!model.isNameValid)
                })
    }
}