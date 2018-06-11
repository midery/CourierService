package com.liarstudio.courierservice.ui.screen.auth

import android.content.Intent
import com.jakewharton.rxbinding2.InitialValueObservable
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.logic.user.UserLoader
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.MessageShower
import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import io.reactivex.disposables.Disposables
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * Презентер экрана авторизации [AuthActivity]
 */
@PerActivity
class AuthPresenter @Inject constructor(
        private val authLoader: AuthLoader,
        private val userLoader: UserLoader,
        private val messageShower: MessageShower,
        view: AuthActivity
) : BasePresenter<AuthActivity>(view) {

    val model = AuthScreenModel()
    var emailChangesDisposable = Disposables.disposed()
    var passwordChangesDisposable = Disposables.disposed()
    var nameChangesDisposable = Disposables.disposed()
    var authDisposable = Disposables.disposed()

    /**
     * Функция валидации полей ввода и авторизации в случае успеха
     */
    fun validateAndAuthorize() {
        if (model.isValid)
            sendAuthRequest()
        else
            messageShower.show(R.string.auth_input_error)
    }

    /**
     * Подписка на изменение статуса флага регистрации
     *
     * @param isRegister логическое состояние флага
     */
    fun observeRegisterChanged(isRegister: Boolean) {
        model.isRegister = isRegister
        view.renderData(model)
    }

    /**
     * Подписка на изменение email
     *
     * @param textObservable Observable с новым email
     */
    fun observeEmailChanges(textObservable: InitialValueObservable<CharSequence>) {
        emailChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.email = it
                    view.toggleEmailError(!model.isEmailValid)
                })
    }

    /**
     * Подписка на изменение пароля
     *
     * @param textObservable Observable с новым паролем
     */
    fun observePasswordChanges(textObservable: InitialValueObservable<CharSequence>) {
        passwordChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.password = it
                    view.togglePasswordError(!model.isPasswordValid)
                })
    }

    /**
     * Подписка на изменение имени
     *
     * @param textObservable Observable с новым именем
     */
    fun observeNameChanges(textObservable: InitialValueObservable<CharSequence>) {
        nameChangesDisposable = subscribe(textObservable.skipInitialValue()
                .map { it.toString() },
                {
                    model.name = it
                    view.toggleNameError(!model.isNameValid)
                })
    }

    /**
     * Отправка запроса на авторизацию
     * В зависимости от флага isRegister выполняется либо регистрация, либо вход в уже существующий аккаунт
     */
    private fun sendAuthRequest() {
        view.renderState(LoadState.LOADING)

        val request = if (model.isRegister)
            authLoader.register(model.email, model.name, model.encryptedPassword)
        else
            authLoader.login(model.email, model.encryptedPassword)

        authDisposable = subscribe(request,
                {
                    //В случае успеха текущий пользователь сохраняется в локальное хранилище, а экран закрывается
                    model.loadState = LoadState.NONE
                    userLoader.putCurrentUser(it)
                    openMainActivity()
                },
                {
                    model.loadState = LoadState.ERROR
                    view.render(model)
                    if (it is HttpException) {
                        messageShower.show(
                                when (it.code()) {
                                    HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_http_forbidden
                                    HttpURLConnection.HTTP_CONFLICT -> R.string.error_http_conflict
                                    else -> R.string.error_auth
                                })
                    }
                })
    }

    /**
     * Открытие главного экрана
     */
    private fun openMainActivity() {
        view.startActivity(Intent(view, MainActivity::class.java))
        view.finish()
    }

}