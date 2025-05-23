package com.lph.selfcareapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.paging.DoctorPagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class PagingDoctorViewModel extends ViewModel {
    public Flowable<PagingData<Doctor>> doctorPagingDataFlowable;
    private Application application;
    private final String name;
    private final Integer clinicId;
    private final String spe;
    private final String sortBy = "docid";
    private final String direction = "ASC";
    public PagingDoctorViewModel(Application application,String name, Integer clinicId, String spe) {
        this.name = name;
        this.clinicId = clinicId;
        this.spe = spe;
        DoctorPagingSource moviePagingSource = new DoctorPagingSource(application, name, clinicId, spe, sortBy, direction);

        Pager<Integer, Doctor> pager = new Pager(
                new PagingConfig(
                        20,
                        20,
                        false,
                        20,
                        20 * 499
                ),
                () -> new DoctorPagingSource(application, name, clinicId, spe, sortBy, direction));

        // Flowable
        doctorPagingDataFlowable = PagingRx.getFlowable(pager).cache();
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(doctorPagingDataFlowable, coroutineScope);
    }

}
