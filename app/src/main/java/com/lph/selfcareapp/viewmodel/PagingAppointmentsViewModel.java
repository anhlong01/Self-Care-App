package com.lph.selfcareapp.viewmodel;

import static androidx.paging.PagingLiveData.getLiveData;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.paging.AppointmentPagingSource;
import com.lph.selfcareapp.paging.DoctorPagingSource;
import androidx.paging.PagingLiveData;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import kotlinx.coroutines.CoroutineScope;

public class PagingAppointmentsViewModel extends ViewModel {
    public LiveData<PagingData<Appointment>> appointmentPagingDataLiveData;;
    private Application application;
    private final Integer pid;
    private final MutableLiveData<Integer> searchQuery = new MutableLiveData<>();
    private final String startDate;
    private final String endDate;
    private final Integer hasDone;
    private final Integer docid;
    private final String sortBy = "appoid";
    private final String direction = "DESC";
    private final CompositeDisposable disposables = new CompositeDisposable();
    public PagingAppointmentsViewModel(Application application, Integer pid, Integer appoid, String startDate, String endDate, Integer hasDone, Integer docid) {
        this.pid = pid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasDone = hasDone;
        this.docid = docid;
        this.application = application;
        searchQuery.setValue(appoid);
        appointmentPagingDataLiveData = Transformations.switchMap(searchQuery, filter -> PagingLiveData.getLiveData(createPager(filter)));

        // Flowable

    }

    private Pager<Integer, Appointment> createPager(Integer filter) {
        return new Pager(
                new PagingConfig(
                        20,
                        20,
                        false,
                        20,
                        20 * 499
                ),
                () -> new AppointmentPagingSource( pid, filter, startDate, endDate, hasDone, docid, sortBy, direction, application));

    }

    public void setSearchQuery(Integer query) {
        searchQuery.setValue(query);
    }



}
