package com.lph.selfcareapp.model;

import java.time.LocalDate;

public class SlotDTO {
    private String date;
    private Integer weekday;
    private Integer slots;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = String.valueOf(date);
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }
}
