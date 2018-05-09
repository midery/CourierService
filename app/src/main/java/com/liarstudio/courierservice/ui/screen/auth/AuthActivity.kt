package com.liarstudio.courierservice.ui.screen.auth

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import com.jakewharton.rxbinding2.widget.textChanges
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.BaseActivity
import com.liarstudio.courierservice.ui.base.LoadState
import javax.inject.Inject


class AuthActivity : BaseActivity<AuthScreenModel>() {

    @Inject
    lateinit var presenter: AuthPresenter

    lateinit var emailEt: EditText
    lateinit var nameEt: EditText
    lateinit var passwordEt: EditText

    lateinit var emailTil: TextInputLayout
    lateinit var passwordTil: TextInputLayout
    lateinit var nameTil: TextInputLayout

    lateinit var confirmBtn: Button
    lateinit var typeCb: CheckBox
    lateinit var authPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_auth)

        findViews()
        initListeners()

    }

    private fun findViews() {
        emailEt = findViewById(R.id.email_et)
        nameEt = findViewById(R.id.name_et)
        passwordEt = findViewById(R.id.password_et)

        emailTil = findViewById(R.id.email_til)
        nameTil = findViewById(R.id.name_til)
        passwordTil = findViewById(R.id.password_til)

        confirmBtn = findViewById(R.id.confirm_btn)
        typeCb = findViewById(R.id.type_cb)
        authPb = findViewById(R.id.auth_pb)
    }

    private fun initListeners() {

        presenter.observePasswordChanges(passwordEt.textChanges())
        presenter.observeEmailChanges(emailEt.textChanges())
        presenter.observeNameChanges(nameEt.textChanges())

        typeCb.setOnCheckedChangeListener { _, isChecked ->
            presenter.registerChanged(isChecked)
        }

        confirmBtn.setOnClickListener { _ -> presenter.authenticate() }
    }

    override fun render(screenModel: AuthScreenModel) {

        if (screenModel.isRegister) {
            confirmBtn.text = "Зарегистрироваться"
            nameTil.visibility = View.VISIBLE
        } else {
            confirmBtn.text = "Войти"
            nameTil.visibility = View.GONE
        }

        when (screenModel.loadState) {
            LoadState.NONE, LoadState.ERROR -> authPb.visibility = View.GONE
            LoadState.LOADING -> authPb.visibility = View.VISIBLE
        }
    }


    fun showEmailError(isError: Boolean) {
        emailTil.error = if (isError)
            getString(R.string.auth_email_error)
        else
            ""
        emailTil.isErrorEnabled = isError
    }

    fun showNameError(isError: Boolean) {
        nameTil.error = if (isError)
            getString(R.string.auth_name_error)
        else
            ""
        nameTil.isErrorEnabled = isError
    }

    fun showPasswordError(isError: Boolean) {
        passwordTil.error = if (isError)
            getString(R.string.auth_password_error)
        else
            ""
        passwordTil.isErrorEnabled = isError
    }

    fun showInputError() {
        Snackbar.make(findViewById(android.R.id.content), "Ошибка при вводе данных. Пожалуйста, повторите ввод.", Snackbar.LENGTH_LONG)
                .show()
    }
}
