package com.lph.selfcareapp.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.Utils.AppointmentComparator;
import com.lph.selfcareapp.Utils.BottomNavigationViewHelper;
import com.lph.selfcareapp.adapter.AppointmentPagingAdapter;
import com.lph.selfcareapp.adapter.DoctorLoadStateAdapter;
import com.lph.selfcareapp.factory.PagingAppointmentViewModelFactory;
import com.lph.selfcareapp.listener.DiagnoseListener;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.paging.AppointmentPagingSource;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.TicketAdapter;
import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedicalTicketActivity extends AppCompatActivity implements DiagnoseListener {
    RecyclerView recyclerView;
    List<Appointment> appointmentList;
    List<Appointment> temp;
    TicketAdapter ticketAdapter;
    TextView navText;
    AppointmentPagingAdapter appointmentPagingAdapter;
    PagingAppointmentsViewModel appointmentsViewModel;
    ImageButton back;
    EditText search;
    int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_activity);
        recyclerView = findViewById(R.id.ticketRecyclerView);
        navText = findViewById(R.id.navText);
        navText.setText("Lịch khám");
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        search = findViewById(R.id.edit_text);
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        id = sp.getInt("id", 0);
        appointmentPagingAdapter = new AppointmentPagingAdapter(new AppointmentComparator(), this, this);
        appointmentsViewModel = new ViewModelProvider(this, new PagingAppointmentViewModelFactory(getApplication(), id, null, null, null, null,null)).get(PagingAppointmentsViewModel.class);
//        getTicket();
        initRecyclerviewAndAdapter();
        setupNavigationView();
        setUpSearchListener();
    }

    private void setupNavigationView() {
        Log.d("Main", "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.bottomNavBar);
        BottomNavigationViewHelper.setupTopNavigationView(tvEx);
        BottomNavigationViewHelper.enableNavigation(MedicalTicketActivity.this, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    private void displayAppointmentRecyclerview() {
        temp = appointmentList;
        ticketAdapter = new TicketAdapter(this, temp);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ticketAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter.notifyDataSetChanged();
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

    private void setUpSearchListener(){
        final Handler handler= new Handler();
        final Runnable[] runnable = {null};
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Xóa callback trước đó nếu có
                if (runnable[0] != null) {
                    handler.removeCallbacks(runnable[0]);
                }

                // Tạo callback mới
                runnable[0] = () -> {
                    String searchQuery = s.toString().trim();
                    if(searchQuery.matches("\\d+"))
                        appointmentsViewModel.setSearchQuery(Integer.parseInt(searchQuery));
                    else
                        appointmentsViewModel.setSearchQuery(null);
                };

                // Đợi 500ms sau khi ngừng gõ
                handler.postDelayed(runnable[0], 500);

            }
        });
    }


    @Override
    public void uploadDiagnose(Appointment appointment) {

    }

    @Override
    public void onButtonClicked2(Appointment appointment) {

    }
}