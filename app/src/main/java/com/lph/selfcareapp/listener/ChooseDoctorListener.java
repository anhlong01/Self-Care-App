package com.lph.selfcareapp.listener;

import com.lph.selfcareapp.model.Doctor;

public interface ChooseDoctorListener {
    void onItemClicked(Doctor doctor);
    void seeInfo(Doctor doctor);
}
