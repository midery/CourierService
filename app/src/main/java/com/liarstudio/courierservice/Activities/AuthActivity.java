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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.liarstudio.courierservice.API.ApiUtils;
import com.liarstudio.courierservice.API.UserAPI;
import com.liarstudio.courierservice.R;
import com.liarstudio.courierservice.API.User;

import org.apache.commons.validator.routines.EmailValidator;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.liarstudio.courierservice.API.ApiUtils.CURRENT_USER;
import static com.liarstudio.courierservice.API.ApiUtils.IS_ADMIN;


public class AuthActivity extends AppCompatActivity {

    UserAPI api;


    EditText editTextEmail;
    EditText editTextName;
    EditText editTextPassword;

    Button buttonConfirm;
    Switch switchRegister;

    ProgressBar progressBar;
    TextView textViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_auth);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);

        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        switchRegister = (Switch) findViewById(R.id.switchRegister);
        switchRegister.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonConfirm.setText("Зарегистрироваться");
                editTextName.setVisibility(View.VISIBLE);
            } else {
                buttonConfirm.setText("Войти");
                editTextName.setVisibility(View.GONE);
            }
        });



        textViewError = (TextView) findViewById(R.id.textViewError);
        progressBar = (ProgressBar) findViewById(R.id.progressBarAuth);
        progressBar.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(UserAPI.class);

        buttonConfirm.setOnClickListener(l -> {
            if (validate()) {
                    proceedRequest();
            }
        });

    }

    private void proceedRequest() {
        progressBar.setVisibility(View.VISIBLE);

        Call<User> userCall = switchRegister.isChecked() ?

                api.register(editTextEmail.getText().toString().toLowerCase(),
                        editTextName.getText().toString(),
                        ApiUtils.encryptPassword(editTextPassword.getText().toString())) :

                api.login(editTextEmail.getText().toString().toLowerCase(),
                ApiUtils.encryptPassword(editTextPassword.getText().toString()));

        userCall.enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                textViewError.setText("");
                                CURRENT_USER = response.body();
                                IS_ADMIN = (CURRENT_USER.getRole() == 1);
                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                finish();
                                break;
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                textViewError.setText("Неверные email или пароль. Повторите ввод.");
                                break;
                            case HttpURLConnection.HTTP_CONFLICT:
                                textViewError.setText("Пользователь с данным Email уже зарегистрирован.");
                                break;

                            default:
                                textViewError.setText("Произошла ошибка при авторизации. Попробуйте позднее.");
                                break;
                        }


                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(AuthActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        textViewError.setText("Истекло время ожидания от сервера.");
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
    }

    private boolean validate() {
        boolean valid = true;
        String checkString = editTextEmail.getText().toString();
        if (!EmailValidator.getInstance().isValid(checkString))
        {
            valid = false;
            editTextEmail.setError("Введен неверный e-mail.");
        }
        if (switchRegister.isChecked()) {
            checkString = editTextName.getText().toString();
            if (checkString.isEmpty()) {
                valid = false;
                editTextName.setError("Имя не может быть пустым.");
            }
        }

        checkString = editTextPassword.getText().toString();
        if (checkString.length() < 4)
        {
            valid = false;
            editTextPassword.setError("Пароль слишком короткий.");
        }

        return valid;

    }

}
