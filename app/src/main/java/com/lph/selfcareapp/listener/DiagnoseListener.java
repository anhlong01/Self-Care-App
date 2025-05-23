package com.lph.selfcareapp.listener;

import com.lph.selfcareapp.model.Appointment;

public interface DiagnoseListener {
    void uploadDiagnose(Appointment appointment);
    void onButtonClicked2(Appointment appointment);
}
