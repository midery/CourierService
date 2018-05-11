package com.liarstudio.courierservice.ui.screen.auth

import android.os.Bundle
import android.view.View
import android.view.Window
import com.jakewharton.rxbinding2.widget.textChanges
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.screen.BaseActivity
import com.liarstudio.courierservice.ui.base.LoadState
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject


class AuthActivity : BaseActivity<AuthScreenModel>() {

    @Inject
    lateinit var presenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_auth)
        initListeners()
    }


    private fun initListeners() {

        presenter.observePasswordChanges(password_et.textChanges())
        presenter.observeEmailChanges(email_et.textChanges())
        presenter.observeNameChanges(name_et.textChanges())

        type_cb.setOnCheckedChangeListener { _, isChecked ->
            presenter.registerChanged(isChecked)
        }


        confirm_btn.setOnClickListener { _ -> presenter.authenticate() }
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


    fun showEmailError(isError: Boolean) {
        email_til.error = if (isError)
            getString(R.string.auth_email_error)
        else
            ""
        email_til.isErrorEnabled = isError
    }

    fun showNameError(isError: Boolean) {
        name_til.error = if (isError)
            getString(R.string.auth_name_error)
        else
            ""
        name_til.isErrorEnabled = isError
    }

    fun showPasswordError(isError: Boolean) {
        password_til.error = if (isError)
            getString(R.string.auth_password_error)
        else
            ""
        password_til.isErrorEnabled = isError
    }
}
