package com.lph.selfcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.ReturnData;
import com.lph.selfcareapp.model.ScheduleTime;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.vnpay.VnpayActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmActivity extends AppCompatActivity {
    TextView clinicname;
    TextView clinicAddress;
    TextView patientName;
    TextView patientNumber;
    TextView patientBirthday;
    TextView patientAddress;
    TextView doctorName;
    TextView doctorSpe;
    TextView scheduledate;
    TextView date;
    TextView price;
    MaterialButton payBtn;
    TextView textView;
    String jwt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm);
        Intent intent = getIntent();
        Clinic clinic = (Clinic) intent.getSerializableExtra("clinic");
        Doctor doctor = (Doctor) intent.getSerializableExtra("doctor");
        String specialty = doctor.getSpecialtiesName();
        String dateText = intent.getStringExtra("date");
        String timeText = intent.getStringExtra("time");
        String symptom = intent.getStringExtra("symptom");
        ScheduleTime scheduleTime = (ScheduleTime) intent.getSerializableExtra("schedule");
        clinicname = findViewById(R.id.clinic_name);
        clinicAddress = findViewById(R.id.clinic_address);
        patientName = findViewById(R.id.patient_name);
        patientNumber = findViewById(R.id.patient_number);
        patientBirthday = findViewById(R.id.patient_birthday);
        patientAddress = findViewById(R.id.patient_location);
        doctorName = findViewById(R.id.doctor_name);
        doctorSpe = findViewById(R.id.doctor_spe);
        scheduledate = findViewById(R.id.date);
        date = findViewById(R.id.date);
        price = findViewById(R.id.price);
        payBtn = findViewById(R.id.payButton);
        textView = findViewById(R.id.navText);
        SharedPreferences sp1 = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = sp1.getString("jwt", "");
        textView.setText("Xác nhận thanh toán");
        clinicname.setText(clinic.getClinic_name());
        clinicAddress.setText(clinic.getAddress());
        doctorName.setText(doctor.getDocName());
        doctorSpe.setText(specialty);
        date.setText(dateText + " "+timeText);
        String priceText = String.format("%,d", doctor.getPrice());
        priceText = priceText +"đ";
        price.setText(priceText);
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        patientName.setText(sp.getString("fullname",""));
        patientNumber.setText(sp.getString("phone",""));
        SharedPreferences sp3 = getSharedPreferences("UserData", MODE_PRIVATE);
        int id = sp3.getInt("id", 0);
        String orderInfo =  id +"#"+ scheduleTime.getScheduleId() + "#" + symptom;
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitInstance.getService(jwt).createPayment(doctor.getPrice(),orderInfo).enqueue(new Callback<ReturnData>() {
                    @Override
                    public void onResponse(Call<ReturnData> call, Response<ReturnData> response) {
                        ReturnData returnData = response.body();
                        Intent i = new Intent(getApplicationContext(), VnpayActivity.class);
                        i.putExtra("payment_url",returnData.getResult().getPaymentUrl());
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<ReturnData> call, Throwable throwable) {
                        Log.d("Retrofit",throwable.toString());
                    }
                });
            }
        });
    }


}