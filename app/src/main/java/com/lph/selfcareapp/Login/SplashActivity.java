package com.lph.selfcareapp.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lph.selfcareapp.MainActivity;
import com.lph.selfcareapp.MainDoctorActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.dto.IntrospectRequest;
import com.lph.selfcareapp.model.IntrospectResponse;
import com.lph.selfcareapp.model.IntrospectResult;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sp1=this.getSharedPreferences("UserData", MODE_PRIVATE);

        String jwt = sp1.getString("jwt", "");
        Log.d("SplashActivity", "jwt: " + jwt);
        if(!jwt.equals("")){
            IntrospectRequest introspectRequest= new IntrospectRequest(jwt);
            RetrofitInstance.getServiceWithoutAuth().checkToken(introspectRequest).enqueue(new Callback<IntrospectResponse>() {
                @Override
                public void onResponse(Call<IntrospectResponse> call, Response<IntrospectResponse> response) {
                    IntrospectResponse introspectResponse = response.body();
                    IntrospectResult introspectResult = introspectResponse.getResult();
                    if(introspectResult.getValid()){
                        if(sp1.getString("utype","").equals("doctor"))
                            startActivity(new Intent(SplashActivity.this, MainDoctorActivity.class));
                        else
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else{
                        String username = sp1.getString("email", "");
                        if(!username.equals("")){
                            startActivity(new Intent(SplashActivity.this, FingerprintLoginActivity.class));
                        }
                    }
                }

                @Override
                public void onFailure(Call<IntrospectResponse> call, Throwable throwable) {
                    Log.d("LoginError", "Error: " + throwable.getMessage());
                }
            });
        }

        String fullname = sp1.getString("email", "");

        if(!fullname.equals("")){
            startActivity(new Intent(SplashActivity.this, FingerprintLoginActivity.class));
        }

        Button btnLogin = findViewById(R.id.loginBtn);
        Button btnSignUP = findViewById(R.id.signUpBtn);

        // click login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // click sign up
        btnSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
