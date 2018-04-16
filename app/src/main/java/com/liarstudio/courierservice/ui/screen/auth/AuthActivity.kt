package com.liarstudio.courierservice.ui.screen.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView

import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entities.user.User
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.logic.user.UserAPI
import com.liarstudio.courierservice.ui.base.BaseActivity
import com.liarstudio.courierservice.ui.screen.main.MainActivity

import org.apache.commons.validator.routines.EmailValidator

import java.net.HttpURLConnection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.liarstudio.courierservice.logic.ServerUtils.BASE_URL
import com.liarstudio.courierservice.logic.ServerUtils.CURRENT_USER
import com.liarstudio.courierservice.logic.ServerUtils.IS_ADMIN


class AuthActivity : BaseActivity<AuthScreenModel>() {
    /*
     ****** FIELDS AREA ******
     */

    internal var editTextEmail: EditText
    internal var editTextName: EditText
    internal var editTextPassword: EditText

    internal var buttonConfirm: Button
    internal var switchRegister: Switch
    internal var switchPreferredServer: Switch

    internal var progressBar: ProgressBar
    internal var textViewError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_auth)
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        editTextEmail = findViewById(R.id.editTextEmail) as EditText
        editTextPassword = findViewById(R.id.editTextPassword) as EditText
        editTextName = findViewById(R.id.editTextName) as EditText

        buttonConfirm = findViewById(R.id.buttonConfirm) as Button

        switchRegister = findViewById(R.id.switchRegister) as Switch
        switchRegister.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonConfirm.text = "Зарегистрироваться"
                editTextName.visibility = View.VISIBLE
            } else {
                buttonConfirm.text = "Войти"
                editTextName.visibility = View.GONE
            }
        }

        switchPreferredServer = findViewById(R.id.switchPreferredServer) as Switch
        switchPreferredServer.setOnCheckedChangeListener { bv, isChecked -> }


        textViewError = findViewById(R.id.textViewError) as TextView
        progressBar = findViewById(R.id.progressBarAuth) as ProgressBar
        progressBar.visibility = View.GONE


        buttonConfirm.setOnClickListener { l ->
            if (validate()) {
                sendRequest()
            }
        }

    }


    /**
     * Функция, посылающая запрос к серверу
     */
    private fun sendRequest() {

        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl(ServerUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(UserAPI::class.java)


        //Проверяем, в каком состоянии находится switchRegister
        val userCall = if (switchRegister.isChecked)
        //Если isChecked - вызываем api.register(...)
            api.register(editTextEmail.text.toString().toLowerCase(),
                    editTextName.text.toString(),
                    ServerUtils.encryptPassword(editTextPassword.text.toString()))
        else
        //если !isChecked - вызываем api.login(...)
            api.login(editTextEmail.text.toString().toLowerCase(),
                    ServerUtils.encryptPassword(editTextPassword.text.toString()))
        //Посылаем асинхронный запрос на сервер
        userCall.enqueue(
                object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        when (response.code()) {
                        //Если удалось войти и тело ответа не пустое, устанавливаем текущего пользователя
                        //и запускаем MainActivity
                            HttpURLConnection.HTTP_OK -> if (response.body() != null) {
                                textViewError.text = ""
                                CURRENT_USER = response.body()
                                IS_ADMIN = CURRENT_USER.role == 1
                                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                finish()
                            }
                            HttpURLConnection.HTTP_FORBIDDEN ->

                                textViewError.setText(R.string.error_http_forbidden)
                            HttpURLConnection.HTTP_CONFLICT -> textViewError.setText(R.string.error_http_conflict)

                            else -> textViewError.setText(R.string.error_auth)
                        }
                        progressBar.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        textViewError.setText(R.string.error_could_not_connect_to_server)
                        progressBar.visibility = View.GONE
                    }
                }
        )
    }

    /**
     * Функция валидации
     * Проверяет email, имя и пароль на корректность
     */
    private fun validate(): Boolean {
        var valid = true
        var checkString = editTextEmail.text.toString()
        if (!EmailValidator.getInstance().isValid(checkString)) {
            valid = false
            editTextEmail.error = "Введен неверный e-mail."
        }
        if (switchRegister.isChecked) {
            checkString = editTextName.text.toString()
            if (checkString.isEmpty()) {
                valid = false
                editTextName.error = "Имя не может быть пустым."
            }
        }

        checkString = editTextPassword.text.toString()
        if (checkString.length < 4) {
            valid = false
            editTextPassword.error = "Пароль слишком короткий."
        }

        return valid

    }

    override fun render(screenModel: AuthScreenModel) {

    }
}
