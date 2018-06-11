package com.liarstudio.courierservice.ui.screen.auth

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.screen.view.BaseActivity
import com.liarstudio.courierservice.ui.base.screen.LoadState
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

/**
 * Экран авторизации
 */
class AuthActivity : BaseActivity<AuthScreenModel>() {

    @Inject
    lateinit var presenter: AuthPresenter

    override fun requestPresenter() = presenter

    override fun getLayout() = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListeners()
    }

    /**
     * Инициализация listeners
     */
    private fun initListeners() {

        presenter.observePasswordChanges(password_et.textChanges())
        presenter.observeEmailChanges(email_et.textChanges())
        presenter.observeNameChanges(name_et.textChanges())

        type_cb.setOnCheckedChangeListener { _, isChecked ->
            presenter.observeRegisterChanged(isChecked)
        }

        confirm_btn.setOnClickListener { _ -> presenter.validateAndAuthorize() }
    }

    override fun renderState(loadState: LoadState) {
        when (loadState) {
            LoadState.NONE, LoadState.ERROR -> auth_pb.visibility = View.GONE
            LoadState.LOADING -> auth_pb.visibility = View.VISIBLE
        }
    }
    override fun renderData(screenModel: AuthScreenModel) {

        if (screenModel.isRegister) {
            confirm_btn.text = "Зарегистрироваться"
            name_til.visibility = View.VISIBLE
        } else {
            confirm_btn.text = "Войти"
            name_til.visibility = View.GONE
        }
    }


    /**
     * Переключатель ошибки ввода email
     *
     * @param isError флаг, показывающий есть ли ошибка
     */
    fun toggleEmailError(isError: Boolean) {
        email_til.error = if (isError)
            getString(R.string.auth_email_error)
        else
            ""
        email_til.isErrorEnabled = isError
    }

    /**
     * Показ ошибки ввода имени
     *
     * @param isError флаг, показывающий есть ли ошибка
     */
    fun toggleNameError(isError: Boolean) {
        name_til.error = if (isError)
            getString(R.string.auth_name_error)
        else
            ""
        name_til.isErrorEnabled = isError
    }

    /**
     * Переключатель ошибки ввода пароля
     *
     * @param isError флаг, показывающий есть ли ошибка
     */
    fun togglePasswordError(isError: Boolean) {
        password_til.error = if (isError)
            getString(R.string.auth_password_error)
        else
            ""
        password_til.isErrorEnabled = isError
    }
}
