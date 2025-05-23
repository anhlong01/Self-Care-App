package com.lph.selfcareapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.paging.AppointmentPagingSource;
import com.lph.selfcareapp.paging.ClinicPagingSource;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class PagingClinicViewModel extends ViewModel {
    private Application application;
    private final Double latitude;
    private final Double longitude;
    private final MutableLiveData<Map<String, Object>> filterParams = new MutableLiveData<>();
    private final String direction = "ASC";
    private final CompositeDisposable disposables = new CompositeDisposable();

    public final LiveData<PagingData<Clinic>> clinicPagingData;
    public PagingClinicViewModel(Application application, String name, Double latitude, Double longitude, String sortBy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.application = application;
        Map<String, Object> initialFilters = new HashMap<>();
        initialFilters.put("name", name != null ? name : "");
        initialFilters.put("sortBy", sortBy != null ? sortBy : "clinicId");
        filterParams.setValue(initialFilters);
        MediatorLiveData<Void> triggerLiveData = new MediatorLiveData<>();
        triggerLiveData.addSource(filterParams, v -> triggerLiveData.setValue(null));
        clinicPagingData = Transformations.switchMap(triggerLiveData, trigger ->
                PagingLiveData.getLiveData(createPager(filterParams.getValue()))
        );

    }

    private Pager<Integer, Clinic> createPager(Map<String, Object> filters) {
        return new Pager(
                new PagingConfig(
                        20,
                        20,
                        false,
                        20,
                        20 * 499
                ),
                () -> new ClinicPagingSource(application, (String) filters.get("name"), latitude, longitude, (String) filters.get("sortBy"), direction));

    }

    public void updateFilter(String key, Object value){
        Map<String, Object> currentFilters = filterParams.getValue() != null ? filterParams.getValue() : new HashMap<>();
        currentFilters.put(key, value);
        filterParams.setValue(currentFilters);
    }

    public Object getFilterParams(String key) {

        return filterParams.getValue().get(key);
    }


    public void setName(String name) {
        updateFilter("name", name);
    }

    public void setSortBy(String sortBy) {
        updateFilter("sortBy", sortBy);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
