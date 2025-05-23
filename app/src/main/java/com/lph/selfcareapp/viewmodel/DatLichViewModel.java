package com.lph.selfcareapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.ScheduleTime;

import java.util.List;

public class DatLichViewModel extends ViewModel {
    private MutableLiveData<Doctor> selectedDoctor = new MutableLiveData<>();
    private MutableLiveData<List<ScheduleTime>> schedules = new MutableLiveData<>();

    public void setSelectedDoctor(Doctor doctor) {
        selectedDoctor.setValue(doctor);
    }

    public LiveData<Doctor> getSelectedDoctor() {
        return selectedDoctor;
    }

    public void loadSchedules(String doctorId) {
        // Gọi API hoặc lấy dữ liệu từ database
        // Ví dụ:

//        List<ScheduleTime> fakeSchedules = getSchedulesFromAPI(doctorId);
//        schedules.postValue(fakeSchedules);
    }

    public LiveData<List<ScheduleTime>> getSchedules() {
        return schedules;
    }
}
