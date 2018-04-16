package com.liarstudio.courierservice.ui.screen.auth

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView

import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.logic.ServerUtils.BASE_LOCAL_URL
import com.liarstudio.courierservice.logic.ServerUtils.BASE_SERVER_URL
import com.liarstudio.courierservice.ui.base.BaseActivity

import com.liarstudio.courierservice.ui.base.LoadState


class AuthActivity : BaseActivity<AuthScreenModel>() {
    /*
     ****** FIELDS AREA ******
     */

    lateinit var editTextEmail: EditText
    lateinit var editTextName: EditText
    lateinit var editTextPassword: EditText

    lateinit var buttonConfirm: Button
    lateinit var switchRegister: Switch
    lateinit var switchPreferredServer: Switch

    lateinit var progressBar: ProgressBar
    lateinit var textViewError: TextView

    lateinit var presenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_auth)
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


        findViews()
        presenter = AuthPresenter(this)
        initListeners()

    }

    private fun findViews() {
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextName = findViewById(R.id.editTextName)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonConfirm = findViewById(R.id.buttonConfirm)
        switchRegister = findViewById(R.id.switchRegister)
        switchPreferredServer = findViewById(R.id.switchPreferredServer)
        textViewError = findViewById(R.id.textViewError)
        progressBar = findViewById(R.id.progressBarAuth)

    }

    private fun initListeners() {
        switchRegister.setOnCheckedChangeListener { _, isChecked ->
            presenter.registerChanged(isChecked)
        }

        switchPreferredServer.setOnCheckedChangeListener { _, isChecked->
            presenter.urlChanged(if (isChecked) BASE_LOCAL_URL else BASE_SERVER_URL)
        }

        buttonConfirm.setOnClickListener { _ -> presenter.authenticate() }
    }

    override fun render(screenModel: AuthScreenModel) {

        if (screenModel.isRegister) {
            buttonConfirm.text = "Зарегистрироваться"
            editTextName.visibility = View.VISIBLE
        } else {
            buttonConfirm.text = "Войти"
            editTextName.visibility = View.GONE
        }

        when (screenModel.loadState) {
            LoadState.NONE, LoadState.ERROR -> progressBar.visibility = View.GONE
            LoadState.LOADING -> progressBar.visibility = View.VISIBLE
        }

        if (screenModel.errorMessageRes == 0)
            textViewError.text = ""
        else
            textViewError.setText(screenModel.errorMessageRes)
    }

    fun setEmailError(text: String) {
        editTextEmail.error = text
    }

    fun setNameError(text: String) {
        editTextName.error = text
    }

    fun setPasswordError(text: String) {
        editTextPassword.error = text
    }
}
