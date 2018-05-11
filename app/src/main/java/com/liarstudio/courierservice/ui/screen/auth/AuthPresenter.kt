package com.liarstudio.courierservice.ui.screen.auth

import android.content.Intent
import com.jakewharton.rxbinding2.InitialValueObservable
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.SnackController
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import io.reactivex.disposables.Disposables
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

@PerActivity
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

    fun registerChanged(isRegister: Boolean) {
        model.isRegister = isRegister
        view.renderData(model)
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

    private fun sendAuthRequest() {
        view.renderState(LoadState.LOADING)

        val request = if (model.isRegister)
            authLoader.register(model.email, model.name, model.encryptedPassword)
        else
            authLoader.login(model.email, model.encryptedPassword)

        authDisposable = subscribe(request,
                {
                    model.loadState = LoadState.NONE
                    User.CURRENT = it
                    openMainActivity()
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

    private fun openMainActivity() {
        view.startActivity(Intent(view, MainActivity::class.java))
        view.finish()
    }

}