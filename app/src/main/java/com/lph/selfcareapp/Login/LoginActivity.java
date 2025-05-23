package com.lph.selfcareapp.Login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputEditText;
import com.lph.selfcareapp.MainActivity;
import com.lph.selfcareapp.OtpInputActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.dto.LoginRequest;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.LoginResponse;
import com.lph.selfcareapp.model.LoginResult;
import com.lph.selfcareapp.model.Patient;
import com.lph.selfcareapp.model.Role;
import com.lph.selfcareapp.model.User;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView signUpRedirect, tv_error, forgotPassword;
    TextInputEditText usernameEditText, passwordEditText;
    Button loginBtn;
    Spinner roleSpiner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các view
        Anhxa();

        // click sign up
        signUpRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // click login
        loginBtn.setOnClickListener(view -> GoLogin());

        // click forgot pass
        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("WrongViewCast")
    private void Anhxa() {
        signUpRedirect = findViewById(R.id.signUpRedirect);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);
        tv_error = findViewById(R.id.tv_error);
        forgotPassword = findViewById(R.id.forgotPassword);
    }

    private void GoLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty()) {
            tv_error.setText("Please Enter Your Username");
        } else if (password.isEmpty()) {
            tv_error.setText("Please Enter Your Password");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            LoginRequest loginRequest = new LoginRequest(username, password);
//            if(selectedRole.equals("patient")){
                RetrofitInstance.getServiceWithoutAuth().login(loginRequest).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            LoginResponse loginResponse = response.body();
                            LoginResult authenResult = loginResponse.getResult();
                            if(authenResult.getAuthenticated()){
                                Intent intent = new Intent(LoginActivity.this, OtpInputActivity.class);
                                intent.putExtra("email", username);
                                startActivity(intent);
                            }
                        }
                        else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String message = jObjError.get("message").toString();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();}
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("LoginError", "Error: " + throwable.getMessage());
                    }
                });
        }
    }
    }

