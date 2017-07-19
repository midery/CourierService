package com.liarstudio.courierservice.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liarstudio.courierservice.API.User;
import com.liarstudio.courierservice.API.UserAPI;
import com.liarstudio.courierservice.API.UtilsURL;
import com.liarstudio.courierservice.R;

import org.apache.commons.validator.routines.EmailValidator;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {


    EditText registerEmail;
    EditText registerPassword;
    EditText registerName;

    Button buttonRegister;

    TextView registerError;

    ProgressBar progressBarRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        registerPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        registerName = (EditText) findViewById(R.id.editTextRegisterName);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        registerError = (TextView) findViewById(R.id.textViewRegisterError);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);
        progressBarRegister.setVisibility(View.GONE);


        buttonRegister.setOnClickListener(l -> {
            if (validate())
                registerUser();
        });

    }


    private void registerUser() {
        progressBarRegister.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UtilsURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserAPI api = retrofit.create(UserAPI.class);

        api.register(registerEmail.getText().toString(),
                registerName.getText().toString(),
                UtilsURL.encryptPassword(registerPassword.getText().toString())).enqueue(

                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                registerError.setText("");

                                UtilsURL.CURRENT_USER = response.body();
                                Toast.makeText(RegisterActivity.this, "Регистрация успешно завершена.", Toast.LENGTH_LONG);
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                                break;
                            case HttpURLConnection.HTTP_FORBIDDEN:
                                registerError.setText("Неверные email или пароль. Повторите ввод.");
                                break;
                            default:
                                registerError.setText("Произошла ошибка при авторизации. Попробуйте позднее.");
                                break;
                        }


                        progressBarRegister.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        registerError.setText("Произошла ошибка при авторизации. Попробуйте позднее.");
                        progressBarRegister.setVisibility(View.GONE);
                    }
                }
        );
    }

    private boolean validate() {
        boolean valid = true;
        String checkString = registerEmail.getText().toString();
        if (!EmailValidator.getInstance().isValid(checkString)) {
            valid = false;
            registerEmail.setError("Введен неверный e-mail.");
        }

        checkString = registerName.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            registerName.setError("Имя не может быть пустым.");
        }

        checkString = registerPassword.getText().toString();
        if (checkString.length() < 4) {
            valid = false;
            registerPassword.setError("Пароль слишком короткий.");
        }

        return valid;
    }
}
