package com.lph.selfcareapp.factory;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lph.selfcareapp.viewmodel.PagingDoctorViewModel;

public class PagingDoctorViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final String name;
    private final Integer clinicId;
    private final String spe;

    public PagingDoctorViewModelFactory(Application application, String name, Integer clinicId, String spe) {
        this.application = application;
        this.name = name;
        this.clinicId = clinicId;
        this.spe = spe;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PagingDoctorViewModel.class)) {
            return (T) new PagingDoctorViewModel(application, name, clinicId, spe);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
