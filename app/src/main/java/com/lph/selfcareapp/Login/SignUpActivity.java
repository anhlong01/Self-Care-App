package com.lph.selfcareapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.lph.selfcareapp.MainActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.LoginResponse;
import com.lph.selfcareapp.model.LoginResult;
import com.lph.selfcareapp.model.Patient;
import com.lph.selfcareapp.model.PatientBody;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText fullnameEditText, emailEditText, phoneEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    TextView loginRedirect;
    Button signUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Ánh xạ các view
        Anhxa();

        // Xử lý sự kiện khi nhấn vào nút login redirect
        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn vào nút sign up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoRegister();
            }
        });
    }

    private void Anhxa() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        loginRedirect = findViewById(R.id.loginRedirect);
        signUpButton = findViewById(R.id.signUpButton);
    }

    private void GoRegister() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (email.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
            return;
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.show();
        PatientBody patientBody = new PatientBody(email, password);
        RetrofitInstance.getServiceWithoutAuth().signUp(patientBody).enqueue(new Callback<ApiResponse<Patient>>() {
            @Override
            public void onResponse(Call<ApiResponse<Patient>> call, Response<ApiResponse<Patient>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, SplashActivity.class));
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.get("message").toString();
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Patient>> call, Throwable throwable) {
                Log.d("LoginError", "Error: " + throwable.getMessage());
            }
        });


    }
}

