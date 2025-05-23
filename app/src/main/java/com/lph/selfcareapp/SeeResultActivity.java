package com.lph.selfcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.listener.SeeResultListener;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Appointment2;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.ResultAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeResultActivity extends AppCompatActivity implements SeeResultListener {
    RecyclerView recyclerView;
    List<Appointment> appointment2List;
    ResultAdapter resultAdapter;
    TextView navText;
    ImageButton back;
    String jwt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_result);
        recyclerView = findViewById(R.id.seeResView);
        navText = findViewById(R.id.navText);
        navText.setText("Kết quả khám bệnh ");
        back = findViewById(R.id.back);
        back.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        int pid = sp.getInt("id",0);
        jwt = sp.getString("jwt","");
        RetrofitInstance.getService(jwt).getResult(pid).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                appointment2List = response.body();
                resultAdapter = new ResultAdapter(SeeResultActivity.this, appointment2List, SeeResultActivity.this);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(resultAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(SeeResultActivity.this));
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d("Retrofit", throwable.getMessage());
            }
        });
    }

    @Override
    public void seeResult(int appoid) {
        Intent intent = new Intent(SeeResultActivity.this, PdfDisplayActivity.class);
        intent.putExtra("appoid", appoid);
        startActivity(intent);
    }
}