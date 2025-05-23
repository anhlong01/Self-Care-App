package com.lph.selfcareapp.listener;

import com.lph.selfcareapp.model.Clinic;

public interface ChooseClinicListener {
    void onItemClicked(Clinic clinic);
    void openMap(Clinic clinic);
}
