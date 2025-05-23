package com.lph.selfcareapp.menu;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.lph.selfcareapp.DoctorSeeReportActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.UploadDiagnose;
import com.lph.selfcareapp.Utils.AppointmentComparator;
import com.lph.selfcareapp.adapter.AppointmentPagingAdapter;
import com.lph.selfcareapp.adapter.DoctorLoadStateAdapter;
import com.lph.selfcareapp.factory.PagingAppointmentViewModelFactory;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Result;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.DiagnoseAdapter;
import com.lph.selfcareapp.listener.DiagnoseListener;
import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiagnoseActivity extends AppCompatActivity implements DiagnoseListener {

    RecyclerView recyclerView;
    DiagnoseAdapter ticketAdapter;
    TextView navText;
    private int SELECT_FILE = 1;
    int id;
    ImageButton back;
    String jwt;
    AppointmentPagingAdapter appointmentPagingAdapter;
    PagingAppointmentsViewModel appointmentsViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnose_activity);
        recyclerView = findViewById(R.id.ticketRecyclerView);
        navText = findViewById(R.id.navText);
        back = findViewById(R.id.back);
        back.setOnClickListener(v->finish());
        navText.setText("Gửi chẩn đoán");
        SharedPreferences sp = getSharedPreferences("UserData",MODE_PRIVATE);
        id = sp.getInt("id",0);
        appointmentPagingAdapter = new AppointmentPagingAdapter(new AppointmentComparator(), this, this);
        appointmentsViewModel = new ViewModelProvider(this, new PagingAppointmentViewModelFactory(getApplication(), null, null, null, null, 2, id)).get(PagingAppointmentsViewModel.class);
        initRecyclerviewAndAdapter();

    }



    private void initRecyclerviewAndAdapter() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView.setAdapter(
                appointmentPagingAdapter.withLoadStateFooter(
                        new DoctorLoadStateAdapter(view -> {
                            appointmentPagingAdapter.retry();
                        })
                )
        );

        appointmentsViewModel.appointmentPagingDataLiveData.observe(this, appointmentPagingData -> {
            appointmentPagingAdapter.submitData(getLifecycle(), appointmentPagingData);
        });
    }


    @Override
    public void uploadDiagnose(Appointment appointment) {
        String jwt = getSharedPreferences("UserData", MODE_PRIVATE).getString("jwt", "");
        RetrofitInstance.getService(jwt).hasReport(appointment.getAppoid()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                Boolean res = response.body().getResult();
                if(res){
                    Intent intent = new Intent(DiagnoseActivity.this, DoctorSeeReportActivity.class);
                    intent.putExtra("appointment",appointment);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DiagnoseActivity.this, UploadDiagnose.class);
                    intent.putExtra("appointment",appointment);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable throwable) {

            }
        });

    }

    @Override
    public void onButtonClicked2(Appointment appointment) {

    }



}