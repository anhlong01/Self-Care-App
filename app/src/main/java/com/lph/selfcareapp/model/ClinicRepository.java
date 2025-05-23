package com.lph.selfcareapp.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.lph.selfcareapp.serviceAPI.ApiService;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ClinicRepository {
    private ApiService clinicApiService;
    public ClinicRepository(Application application){
        SharedPreferences sp = application.getSharedPreferences("UserData", Application.MODE_PRIVATE);
        String jwt = sp.getString("jwt", "");
        this.clinicApiService = new RetrofitInstance().getService(jwt);
    }
    public MutableLiveData<ClinicList> getMutableLivedData(){
        MutableLiveData<ClinicList> mutableLiveData =new MutableLiveData<>();
        Call<ClinicList> call = clinicApiService.getAllClinics();
        call.enqueue(new Callback<ClinicList>() {
            @Override
            public void onResponse(Call<ClinicList> call, Response<ClinicList> response) {
                ClinicList list = response.body();
                mutableLiveData.setValue(list);
            }

            @Override
            public void onFailure(Call<ClinicList> call, Throwable throwable) {
                Log.d("TAG", throwable.toString());
            }
        });
        return mutableLiveData;
    }
}

