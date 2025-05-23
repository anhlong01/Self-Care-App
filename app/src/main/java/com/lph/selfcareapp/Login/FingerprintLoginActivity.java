package com.lph.selfcareapp.Login;

import static com.lph.selfcareapp.Utils.SecureStorageHelper.getSessionKey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.lph.selfcareapp.MainActivity;
import com.lph.selfcareapp.OtpInputActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.dto.LoginRequest;
import com.lph.selfcareapp.menu.FingerprintHelper;
import com.lph.selfcareapp.model.LoginResponse;
import com.lph.selfcareapp.model.LoginResult;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FingerprintLoginActivity extends AppCompatActivity implements FingerprintHelper.FingerprintAuthListener {
    TextView username;
    TextView signUpRedirect, tv_error, forgotPassword;
    TextInputEditText passwordEditText;
    FingerprintHelper fingerprintHelper;
    Button loginBtn;
    String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fingerprint_login);
        username = findViewById(R.id.selfCareText);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        fullname = sharedPreferences.getString("email", "");
        username.setText(fullname);
        fingerprintHelper = new FingerprintHelper(this);
        // Ánh xạ các view
        Anhxa();

        // click sign up
        signUpRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(FingerprintLoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // click login
        loginBtn.setOnClickListener(view -> GoLogin());

        // click forgot pass
        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(FingerprintLoginActivity.this, ForgotPassActivity.class);
            startActivity(intent);
        });
        Button btnAuth = findViewById(R.id.fingerprint);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                boolean isfpOn = sp.getBoolean("fingerprint", false);
                if (isfpOn) {
                    authenticateWithFingerprint();
                } else {
                    Toast.makeText(FingerprintLoginActivity.this, "Vui lòng bật xác thực vân tay", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void authenticateWithFingerprint() {
        if (fingerprintHelper.isFingerprintAvailable()) {
            fingerprintHelper.showFingerprintDialog(this, this);
        } else {
            Toast.makeText(this,
                    "Thiết bị không hỗ trợ hoặc chưa đăng ký vân tay",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthenticationSuccessful() {
        runOnUiThread(() -> {
            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            String email = sp.getString("email", "");
            try {
                RetrofitInstance.getServiceWithoutAuth().loginFingerprint(email, getSessionKey(FingerprintLoginActivity.this)).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            LoginResult authenResult = loginResponse.getResult();
                            editor.putString("jwt", authenResult.getToken());
                            editor.apply();
                            Toast.makeText(FingerprintLoginActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FingerprintLoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String message = jObjError.get("message").toString();
                                Toast.makeText(FingerprintLoginActivity.this, message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                        Toast.makeText(FingerprintLoginActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Thực hiện hành động sau khi xác thực thành công
        });
    }

    @Override
    public void onAuthenticationError(int errorCode, String errorMessage) {
        runOnUiThread(() ->
                Toast.makeText(this, "Lỗi xác thực: " + errorMessage, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAuthenticationFailed() {
        runOnUiThread(() ->
                Toast.makeText(this, "Xác thực thất bại", Toast.LENGTH_SHORT).show());
    }

    private void Anhxa() {
        signUpRedirect = findViewById(R.id.signUpRedirect);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginBtn = findViewById(R.id.loginBtn);
        tv_error = findViewById(R.id.tv_error);
        forgotPassword = findViewById(R.id.forgotPassword);
    }

    private void GoLogin() {
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = sp.getString("email", "");
        String username = email;
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
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        LoginResult authenResult = loginResponse.getResult();
                        if (authenResult.getAuthenticated()) {
                            Intent intent = new Intent(FingerprintLoginActivity.this, OtpInputActivity.class);
                            intent.putExtra("email", username);
                            startActivity(intent);
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String message = jObjError.get("message").toString();
                            Toast.makeText(FingerprintLoginActivity.this, message, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    progressDialog.dismiss();
                    Toast.makeText(FingerprintLoginActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("LoginError", "Error: " + throwable.getMessage());
                }
            });
        }
    }
}