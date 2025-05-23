package com.lph.selfcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.api.services.calendar.CalendarScopes;
import com.lph.selfcareapp.Utils.RescheduleReminderWorker;
import com.lph.selfcareapp.model.Reschedule;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.RescheduleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RescheduleActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView navText;
    ImageButton back;
    int id;
    List<Reschedule> rescheduleList;
    RescheduleAdapter rescheduleAdapter;
    Button addBtn;
    String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reschedule);
        recyclerView = findViewById(R.id.recyclerView);
        navText = findViewById(R.id.navText);
        back = findViewById(R.id.back);
        addBtn = findViewById(R.id.addBtn);
        navText.setText("Lịch tái khám ");
        back.setOnClickListener(v -> finish());
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        id = sp.getInt("id", 0);
        jwt = sp.getString("jwt", "");
        getReschedule();
        addBtn.setOnClickListener(v-> {
            try {
                addToCalendar();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getReschedule() {
        RetrofitInstance.getService(jwt).showreschedule(id).enqueue(new Callback<List<Reschedule>>() {
            @Override
            public void onResponse(Call<List<Reschedule>> call, Response<List<Reschedule>> response) {
                rescheduleList = response.body();
                rescheduleAdapter = new RescheduleAdapter(RescheduleActivity.this,rescheduleList);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(rescheduleAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(RescheduleActivity.this));
                rescheduleAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Reschedule>> call, Throwable throwable) {
                Log.d("Retrofit ", throwable.getMessage());
            }
        });
    }

    private void addToCalendar() throws ParseException {
       for(Reschedule r: rescheduleList){
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM", Locale.getDefault());
           Date rescheduleDate = sdf.parse(r.getScheduuledate());
           Date currentTime = new Date();
           long delayInMillis = rescheduleDate.getTime() - currentTime.getTime();
           if (delayInMillis > 0) {
               Data data = new Data.Builder()
                       .putString("title", "Lịch tái khám")
                       .putString("content", "Tái khám với bác sĩ " + r.getDocname() + " vào lúc " + sdf.format(rescheduleDate) + " tại " + r.getAddress())
                       .build();

               OneTimeWorkRequest rescheduleWorkRequest = new OneTimeWorkRequest.Builder(RescheduleReminderWorker.class)
                       .setInitialDelay(1, TimeUnit.MINUTES)
                       .setInputData(data)
                       .build();

               WorkManager.getInstance(getApplicationContext()).enqueue(rescheduleWorkRequest);
               Log.i("WorkManagerScheduler", "Đã lên lịch worker cho ID: " + r.getId() + " vào lúc: " + r.getScheduuledate());
           } else {
               Log.w("WorkManagerScheduler", "Bỏ qua worker cho ID: " + r.getId() + " vì đã qua thời gian: " + r.getScheduuledate());
           }

    }
    }
}