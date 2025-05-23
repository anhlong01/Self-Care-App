package com.lph.selfcareapp.listener;

import com.lph.selfcareapp.model.ScheduleTime;

public interface ChooseTImeListener {
    void onButtonClicked(ScheduleTime scheduleTime, String chosenTime);
}
