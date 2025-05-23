package com.lph.selfcareapp.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.Utils.AppointmentComparator;
import com.lph.selfcareapp.adapter.AppointmentPagingAdapter;
import com.lph.selfcareapp.adapter.DoctorLoadStateAdapter;
import com.lph.selfcareapp.factory.PagingAppointmentViewModelFactory;
import com.lph.selfcareapp.listener.DiagnoseListener;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.TicketAdapter;
import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicalDoctorTicketActivity extends AppCompatActivity implements DiagnoseListener {
    RecyclerView recyclerView;
    List<Appointment> appointmentList;
    List<Appointment> temp;
    TicketAdapter ticketAdapter;
    TextView navText;
    int id;
    String jwt;
    AppointmentPagingAdapter appointmentPagingAdapter;
    PagingAppointmentsViewModel appointmentsViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_doctor_activity);
        recyclerView = findViewById(R.id.ticketRecyclerView);
        navText = findViewById(R.id.navText);
        navText.setText("Lịch khám");
        SharedPreferences sp = getSharedPreferences("UserData",MODE_PRIVATE);
        id = sp.getInt("id",0);
        appointmentPagingAdapter = new AppointmentPagingAdapter(new AppointmentComparator(), this, this);
        appointmentsViewModel = new ViewModelProvider(this, new PagingAppointmentViewModelFactory(getApplication(), null, null, null, null, 0, id)).get(PagingAppointmentsViewModel.class);
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

    }

    @Override
    public void onButtonClicked2(Appointment appointment) {

    }
}