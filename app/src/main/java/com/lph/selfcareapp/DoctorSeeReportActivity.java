package com.lph.selfcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lph.selfcareapp.model.Appointment;

public class DoctorSeeReportActivity extends AppCompatActivity {
    Appointment appointment;
    TextView patient_name,patient_number,patient_birthday,patient_location,patient_symptom;
    Button seeReportBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_see_report);
        appointment = (Appointment) getIntent().getSerializableExtra("appointment");
        patient_name = findViewById(R.id.patient_name);
        patient_number = findViewById(R.id.patient_number);
        patient_birthday = findViewById(R.id.patient_birthday);
        patient_location = findViewById(R.id.patient_location);
        patient_symptom = findViewById(R.id.patient_symptom);
        patient_name.setText(appointment.getPname());
        patient_symptom.setText(appointment.getSymptom());
        seeReportBtn = findViewById(R.id.seeBtn);
        seeReportBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorSeeReportActivity.this, PdfDisplayActivity.class);
            intent.putExtra("appoid", appointment.getAppoid());
            startActivity(intent);
        });
    }


}