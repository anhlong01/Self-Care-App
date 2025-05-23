package com.lph.selfcareapp.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;

public class PagingAppointmentViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final Integer pid;
    private final Integer appoid;
    private final String startdate;
    private final String enddate;
    private final Integer hasDone;
    private final Integer docid;
    public PagingAppointmentViewModelFactory(Application application, Integer pid, Integer appoid, String startdate, String enddate, Integer hasDone, Integer docid) {
        this.application = application;
        this.pid = pid;
        this.appoid = appoid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.hasDone = hasDone;
        this.docid = docid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PagingAppointmentsViewModel.class)) {
            return (T) new PagingAppointmentsViewModel(application, pid, appoid, startdate, enddate, hasDone, docid);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
