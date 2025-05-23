package com.lph.selfcareapp;

import static com.stringee.call.f.t;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.lph.selfcareapp.Login.LoginActivity;
import com.lph.selfcareapp.adapter.PrescriptionAdapter;
import com.lph.selfcareapp.dto.MedicalReport;
import com.lph.selfcareapp.menu.DiagnoseActivity;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.PrescriptionItem;
import com.lph.selfcareapp.model.Reschedule;
import com.lph.selfcareapp.model.Result;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDiagnose extends AppCompatActivity {
    Button sendBtn;
    Button pickImage;
    ImageView imageView;
    private int SELECT_FILE =1;
    String imageUrl;
    Appointment appointment;
    String jwt;
    ProgressBar progressBar;
    private PrescriptionAdapter adapter;
    private List<PrescriptionItem> prescriptionList;
    TextView patient_name,patient_number,patient_birthday,patient_location,patient_symptom, rescheduleTV;
    EditText etDiagnose, etAdvice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.upload_diagnose);
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = sp.getString("jwt", "");
        Intent intent = getIntent();
        appointment = (Appointment) intent.getSerializableExtra("appointment");
        progressBar = findViewById(R.id.progressBar);
        sendBtn = findViewById(R.id.sendBtn);
        RecyclerView rvPrescription = findViewById(R.id.rvPrescription);
        rvPrescription.setLayoutManager(new LinearLayoutManager(this));
        prescriptionList = new ArrayList<>();
        adapter = new PrescriptionAdapter(prescriptionList);
        rvPrescription.setAdapter(adapter);
        patient_name = findViewById(R.id.patient_name);
        patient_number = findViewById(R.id.patient_number);
        patient_birthday = findViewById(R.id.patient_birthday);
        patient_location = findViewById(R.id.patient_location);
        patient_symptom = findViewById(R.id.patient_symptom);
        patient_name.setText(appointment.getPname());
        patient_symptom.setText(appointment.getSymptom());
        etDiagnose = findViewById(R.id.diagnoseEt);
        etAdvice = findViewById(R.id.adviceET);
        rescheduleTV = findViewById(R.id.rescheduleTV);
        // Add initial item (optional)
        prescriptionList.add(new PrescriptionItem("", "", 0));
        adapter.notifyDataSetChanged();

        // Add button click listener
        findViewById(R.id.btnAddPrescription).setOnClickListener(v -> {
            prescriptionList.add(new PrescriptionItem("", "", 0));
            adapter.notifyItemInserted(prescriptionList.size() - 1);
        });
        rescheduleTV.setOnClickListener(v->reschedule());
        sendBtn.setOnClickListener(v->uploadDiagnose());
    }

    private void uploadDiagnose(){
        if(etDiagnose.getText().toString().isEmpty()){
            Toast.makeText(this, "Chẩn đoán không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etAdvice.getText().toString().isEmpty()){
            Toast.makeText(this, "Hướng dẫn không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!hasValidPrescriptionData()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin cho ít nhất một loại thuốc", Toast.LENGTH_SHORT).show();
            return;
        }
        MedicalReport medicalReport = new MedicalReport();
        medicalReport.setDoctorDiagnosis(etDiagnose.getText().toString());
        medicalReport.setAddress(appointment.getPaddress());
        medicalReport.setClinicAddress(appointment.getAddress());
        medicalReport.setDoctorGuide(etAdvice.getText().toString());
        medicalReport.setClinicImage(appointment.getClinicImage());
        medicalReport.setClinicName(appointment.getClinic_name());
        medicalReport.setDateOfBirth(appointment.getDob());
        medicalReport.setGender(appointment.getSex());
        medicalReport.setPatientEmail(appointment.getPname());
        medicalReport.setDoctorName(appointment.getDocname());
        medicalReport.setPrescriptionList(prescriptionList);
        medicalReport.setSymptom(appointment.getSymptom());
        String jwt = getSharedPreferences("UserData", MODE_PRIVATE).getString("jwt", "");
        if(rescheduleTV.getText()!=null){
            medicalReport.setRescheduleDate(rescheduleTV.getText().toString());
            RetrofitInstance.getService(jwt).insertDate(appointment.getPid(), appointment.getDocid(), rescheduleTV.getText().toString()).enqueue(new Callback<ApiResponse<Reschedule>>() {
                @Override
                public void onResponse(Call<ApiResponse<Reschedule>> call, Response<ApiResponse<Reschedule>> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(UploadDiagnose.this, "Tái khám thành công", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String message = jObjError.get("message").toString();
                            Toast.makeText(UploadDiagnose.this, message, Toast.LENGTH_LONG).show();}
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                }

                @Override
                public void onFailure(Call<ApiResponse<Reschedule>> call, Throwable throwable) {
                    Log.d("Retrofit", throwable.getMessage().toString());
                }

        });
        };
        progressBar.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(UploadDiagnose.this, PdfDisplayActivity.class);
//        intent.putExtra("report", medicalReport);
//        startActivity(intent);
        RetrofitInstance.getService(jwt).uploadDiag(appointment.getAppoid(), medicalReport).enqueue(new Callback<ApiResponse<Appointment>>() {
            @Override
            public void onResponse(Call<ApiResponse<Appointment>> call, Response<ApiResponse<Appointment>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    Toast.makeText(UploadDiagnose.this, "Upload thành công", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.get("message").toString();
                        Toast.makeText(UploadDiagnose.this, message, Toast.LENGTH_LONG).show();}
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Appointment>> call, Throwable throwable) {
                Log.d("Retrofit", throwable.getMessage().toString());
            }
        });
    }

    private void reschedule(){
        Dialog dateDialog = new Dialog(UploadDiagnose.this);
        dateDialog.setContentView(R.layout.dialog_calendar_view);

        //set custom width and height
        dateDialog.getWindow().setLayout(1000,1500);
        // set transparent background
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
        CalendarView calendarView = dateDialog.findViewById(R.id.calendarView);

        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar clickedDayCalendar = calendarDay.getCalendar();
                String day = String.valueOf(clickedDayCalendar.get(Calendar.DAY_OF_MONTH));
                String month = String.valueOf(clickedDayCalendar.get(Calendar.MONTH)+1);
                String year = String.valueOf(clickedDayCalendar.get(Calendar.YEAR));
                dateDialog.dismiss();

                String date = year + "-"+month+"-"+day;
                rescheduleTV.setText(date);

            }
        });
    }

    public boolean hasValidPrescriptionData() {
        for (PrescriptionItem item : prescriptionList) {
            if (item.getDrugName() != null && !item.getDrugName().trim().isEmpty() &&
                    item.getUsage() != null && !item.getUsage().trim().isEmpty() &&
                    item.getQuantity() > 0) {
                return true; // At least one item has valid data
            }
        }
        return false; // No valid data found
    }


    public List<PrescriptionItem> getPrescriptionData() {
        return adapter.getPrescriptionList();
    }

}