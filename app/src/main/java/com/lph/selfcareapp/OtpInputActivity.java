package com.lph.selfcareapp;

import static com.lph.selfcareapp.Utils.SecureStorageHelper.getSessionKey;
import static com.lph.selfcareapp.Utils.SecureStorageHelper.saveSessionKey;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.LoginResponse;
import com.lph.selfcareapp.model.LoginResult;
import com.lph.selfcareapp.model.Role;
import com.lph.selfcareapp.model.User;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import org.json.JSONObject;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpInputActivity extends AppCompatActivity {
    PinView pinView;
    Button button;
    TextView resendOtp;
    int secondsLeft = 60;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_input);

        pinView = findViewById(R.id.pinview);
        button=findViewById(R.id.show_otp);
        resendOtp=findViewById(R.id.textView12);
        email = getIntent().getStringExtra("email");
        resendOtp();
        startResendTimer();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(OtpInputActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                // getting the PinView data
                String otp=pinView.getText().toString();

                // Toast to show the OTP Data
                RetrofitInstance.getServiceWithoutAuth().verifyOtp(email,otp.toString()).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            LoginResult authenResult = loginResponse.getResult();
                            User user = authenResult.getUser();
                            Log.d("LoginSuccess", "User: " + user);
                            editor.putString("jwt", authenResult.getToken());
                            editor.putString("email", user.getEmail());
                            editor.putString("fullname", user.getFullname());
                            editor.putInt("id", authenResult.getId());
                            editor.putString("token", authenResult.getCallToken());
                            try {
                                if(getSessionKey(OtpInputActivity.this) == null){
                                    String key = UUID.randomUUID().toString();
                                    saveSessionKey(OtpInputActivity.this, key);
                                    RetrofitInstance.getService(authenResult.getToken()).updateSessionKey(user.getEmail(), key).enqueue(new Callback<ApiResponse<User>>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                                            if(response.isSuccessful()){
                                                Log.d("SessionKey", "save session key successfully");
                                            }else{
                                                Log.d("SessionKey", "save session key failed");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {
                                            Log.d("SessionKey", "call api failed");
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            Set<Role> roles = user.getRole();
                            if(roles.contains(new Role("ROLE_PATIENT"))){
                                editor.putString("utype", "patient");
                                editor.apply(); // or editor.commit();
                                startActivity(new Intent(OtpInputActivity.this, MainActivity.class));
                            }else{
                                editor.putString("utype", "doctor");
                                editor.apply(); // or editor.commit();
                                startActivity(new Intent(OtpInputActivity.this, MainDoctorActivity.class));
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String message = jObjError.get("message").toString();
                                Toast.makeText(OtpInputActivity.this, message, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {

                    }
                });

            }
        });

    }

    public void startResendTimer(){
        resendOtp.setEnabled(false);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resendOtp.post(() -> {
                    if (secondsLeft > 0) {
                        resendOtp.setText("Resend OTP in " + secondsLeft + " seconds");
                        secondsLeft--;
                    } else {
                        resendOtp.setText("Resend OTP");
                        resendOtp.setEnabled(true);
                        resendOtp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendOtp();
                            }
                        });
                        timer.cancel();
                    }
                });
            }
        },0,1000);
    }

    public void resendOtp(){
        Toast.makeText(OtpInputActivity.this, "otp sent", Toast.LENGTH_SHORT).show();
        RetrofitInstance.getServiceWithoutAuth().sendOtp(email).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                Toast.makeText(OtpInputActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable throwable) {

            }
        });
    }
}