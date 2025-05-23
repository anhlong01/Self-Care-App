package com.lph.selfcareapp.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;
import com.lph.selfcareapp.viewmodel.PagingClinicViewModel;

public class PagingClinicViewModelFactory implements ViewModelProvider.Factory{
    private final Application application;
    private final String name;
    private final Double longitude;
    private final Double latitude;
    private final String sortBy;

    public PagingClinicViewModelFactory(Application application, String name, Double longitude, Double latitude, String sortBy) {
        this.application = application;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sortBy = sortBy;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PagingClinicViewModel.class)) {
            return (T) new PagingClinicViewModel(application, name, latitude, longitude, sortBy);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
