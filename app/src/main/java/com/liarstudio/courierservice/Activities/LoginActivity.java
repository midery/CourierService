package com.liarstudio.courierservice.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.liarstudio.courierservice.API.UserAPI;
import com.liarstudio.courierservice.API.UtilsURL;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.API.User;

import org.apache.commons.validator.routines.EmailValidator;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {


    EditText loginEmail;
    EditText loginPassword;

    Button buttonLogin;
    Button buttonRegister;

    ProgressBar progressBarLogin;
    TextView loginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        loginPassword = (EditText) findViewById(R.id.editTextRegisterPassword);


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegisterStart);

        loginError = (TextView) findViewById(R.id.textViewLoginError);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.GONE);


        initButtons();
    }

    void initButtons() {
        buttonLogin.setOnClickListener(l -> {
            if (validate())
                loginUser();
        });

        buttonRegister.setOnClickListener(l -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }


    private void loginUser() {
        progressBarLogin.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserAPI api = retrofit.create(UserAPI.class);
        api.login(loginEmail.getText().toString(),
                UtilsURL.encryptPassword(loginPassword.getText().toString())).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                loginError.setText("");
                                UtilsURL.CURRENT_USER = response.body();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                break;
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                loginError.setText("Неверные email или пароль. Повторите ввод.");
                                break;
                            default:
                                loginError.setText("Произошла ошибка при авторизации. Попробуйте позднее.");
                                break;
                        }


                        progressBarLogin.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        loginError.setText("Произошла ошибка при авторизации. Попробуйте позднее.");
                        progressBarLogin.setVisibility(View.GONE);
                    }
                }
        );
    }

    private boolean validate() {
        boolean valid = true;
        String checkString = loginEmail.getText().toString();
        if (!EmailValidator.getInstance().isValid(checkString))
        {
            valid = false;
            loginEmail.setError("Введен неверный e-mail.");
        }

        checkString = loginPassword.getText().toString();
        if (checkString.length() < 4)
        {
            valid = false;
            loginPassword.setError("Пароль слишком короткий.");
        }

        return valid;

    }

}
