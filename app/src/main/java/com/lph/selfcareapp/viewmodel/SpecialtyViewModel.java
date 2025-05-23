package com.lph.selfcareapp.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lph.selfcareapp.model.SpecialtyList;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialtyViewModel extends AndroidViewModel {
    LiveData<SpecialtyList> specialtyListLiveData;

    public SpecialtyViewModel(Application application ){
        super(application);
        MutableLiveData<SpecialtyList> mutableLiveData = new MutableLiveData<>();
        SharedPreferences sp = application.getSharedPreferences("UserData", MODE_PRIVATE);
        String jwt = sp.getString("jwt","");
        Call<SpecialtyList> call = new RetrofitInstance().getService(jwt).getAllSpecialties();
        call.enqueue(new Callback<SpecialtyList>() {
            @Override
            public void onResponse(Call<SpecialtyList> call, Response<SpecialtyList> response) {
                SpecialtyList list = response.body();
                mutableLiveData.setValue(list);
            }

            @Override
            public void onFailure(Call<SpecialtyList> call, Throwable throwable) {
                Log.e("SpecialtyViewModel", "Error fetching specialties: " + throwable.getMessage());
            }
        });
        specialtyListLiveData = mutableLiveData;
    }

    public LiveData<SpecialtyList> getSpecialtyListLiveData(){
        return specialtyListLiveData;
    }
}
