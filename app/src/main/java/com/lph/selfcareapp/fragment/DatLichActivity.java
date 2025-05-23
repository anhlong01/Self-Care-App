package com.lph.selfcareapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lph.selfcareapp.ConfirmActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.adapter.DateAdapter;
import com.lph.selfcareapp.adapter.TimeAdapter;
import com.lph.selfcareapp.listener.ChooseDateListener;
import com.lph.selfcareapp.listener.ChooseTImeListener;
import com.lph.selfcareapp.model.CallDoctor;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.ScheduleTime;
import com.lph.selfcareapp.model.SlotDTO;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.viewmodel.DatLichViewModel;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatLichActivity extends AppCompatActivity implements ChooseDateListener, ChooseTImeListener {
    ImageButton back;
    TextView navText;
    DatLichViewModel datLichViewModel;
    Button bookBtn;
    RecyclerView recyclerView;
    RecyclerView timeRecyclerView;
    List<SlotDTO> slotDTOList;
    TextView dateText;
    String jwt;
    Doctor selectedDoctor;
    TimeAdapter timeAdapter;
    List<ScheduleTime> timeList;
    ScheduleTime selectedScheduleTime;
    Clinic selectedClinic;
    String chosenTime;
    String chosenDate;
    CallDoctor callDoctor;
    CardView doctorInfo;
    ImageView doctorImage;
    TextView docname;
    TextView docspe;
    EditText symptom;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich);
        back = findViewById(R.id.back);
        back.setOnClickListener(v->finish());
        navText = findViewById(R.id.navText);
        bookBtn = findViewById(R.id.bookBtn);
        recyclerView = findViewById(R.id.slotsView);
        timeRecyclerView = findViewById(R.id.timeView);
        doctorInfo = findViewById(R.id.doctorInfo);
        doctorImage = findViewById(R.id.imageView);
        docname = findViewById(R.id.textView6);
        docspe = findViewById(R.id.textView8);
        symptom = findViewById(R.id.symptom);
        dateText = findViewById(R.id.dateText);
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = sp.getString("jwt", "");
        navText.setText("Chọn lịch khám");
        selectedClinic = (Clinic) getIntent().getSerializableExtra("clinic");
        callDoctor = (CallDoctor) getIntent().getSerializableExtra("callDoctor");
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        datLichViewModel = new ViewModelProvider(this).get(DatLichViewModel.class);
        datLichViewModel.getSelectedDoctor().observe(this, doctor -> {
            if(doctor!=null){
                doctorInfo.setVisibility(View.VISIBLE);
                docname.setText(doctor.getDocName());
                docspe.setText(doctor.getSpecialtiesName());
                if(timeAdapter!=null){
                    timeList.clear();
                    timeAdapter.notifyDataSetChanged();
                }
                selectedDoctor = doctor;
                displayDateView(doctor.getDocId());
//                bookBtn.setBackgroundColor(getColor(R.color.link_blue));
//                bookBtn.setClickable(true);
            }else{
//                bookBtn.setBackgroundColor(getColor(R.color.grey));
//                bookBtn.setClickable(false);
            }
        } );
        if(callDoctor!=null){
            tabLayout.setVisibility(View.GONE);
            viewPager2.setVisibility(View.GONE);
            Doctor doctor = new Doctor();
            doctor.setDocId(callDoctor.getDocid());
            doctor.setDocName(callDoctor.getDocname());
            doctor.setSpecialtiesName(callDoctor.getSname());
            doctor.setPrice(callDoctor.getPrice());
            doctor.setSex(callDoctor.getSex());
            selectedClinic = new Clinic();
            selectedClinic.setAddress(callDoctor.getAddress());
            selectedClinic.setClinic_name(callDoctor.getClinicName());
            datLichViewModel.setSelectedDoctor(doctor);
        }
        DatLichPageAdapter adapter = new DatLichPageAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(adapter.getTabTitle(position));
        }).attach();
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptom.getText() == null || symptom.getText().toString().equals("")){
                    symptom.setError("Vui lòng nhập triệu chứng");
                    return;
                }
                Intent intent = new Intent(DatLichActivity.this, ConfirmActivity.class);
                intent.putExtra("doctor", selectedDoctor);
                intent.putExtra("schedule", selectedScheduleTime);
                intent.putExtra("clinic",selectedClinic);
                intent.putExtra("time", chosenTime);
                intent.putExtra("date", chosenDate);
                intent.putExtra("symptom", symptom.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void displayDateView(int docId){

        RetrofitInstance.getService(jwt).getSlots(docId).enqueue(new Callback<List<SlotDTO>>() {
            @Override
            public void onResponse(Call<List<SlotDTO>> call, Response<List<SlotDTO>> response) {
                if(response.isSuccessful()) {
                    slotDTOList = response.body();
                    DateAdapter dateAdapter = new DateAdapter(getApplicationContext(), slotDTOList, DatLichActivity.this,-1);
                    recyclerView.setAdapter(dateAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false));
                    dateAdapter.notifyDataSetChanged();
                }else{
                    Log.d("Retrofit", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SlotDTO>> call, Throwable throwable) {
                Log.d("Retrofit", throwable.toString());
            }
        });
    }

    @Override
    public void onItemClicked(SlotDTO slotDTO) {
        chosenDate = slotDTO.getDate();
        bookBtn.setBackgroundColor(getColor(R.color.grey));
        bookBtn.setClickable(false);
        dateText.setText("Lịch ngày " + slotDTO.getDate());
        RetrofitInstance.getService(jwt).getScheduleTime(selectedDoctor.getDocId(), slotDTO.getDate()).enqueue(new Callback<List<ScheduleTime>>() {
            @Override
            public void onResponse(Call<List<ScheduleTime>> call, Response<List<ScheduleTime>> response) {
                timeList = response.body();
                timeAdapter = new TimeAdapter(response.body(), getApplicationContext(), DatLichActivity.this,-1);
                timeRecyclerView.setAdapter(timeAdapter);
                timeRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                timeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ScheduleTime>> call, Throwable throwable) {
                Log.d("Retrofit", throwable.toString());
            }
        });
    }

    @Override
    public void onButtonClicked(ScheduleTime scheduleTime, String time) {
        chosenTime = time;
        selectedScheduleTime = scheduleTime;
        bookBtn.setBackgroundColor(getColor(R.color.link_blue));
        bookBtn.setClickable(true);
    }
}