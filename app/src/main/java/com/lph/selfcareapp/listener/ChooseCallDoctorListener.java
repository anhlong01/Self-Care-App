package com.lph.selfcareapp.listener;

import com.lph.selfcareapp.model.CallDoctor;

public interface ChooseCallDoctorListener {
    void onItemCliked(CallDoctor callDoctor);
    void call(String callId);
}
