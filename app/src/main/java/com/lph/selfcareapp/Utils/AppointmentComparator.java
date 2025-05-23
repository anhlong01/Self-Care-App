package com.lph.selfcareapp.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lph.selfcareapp.model.Appointment;

import java.util.Objects;

public class AppointmentComparator extends DiffUtil.ItemCallback<Appointment> {
    @Override
    public boolean areItemsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
        return Objects.equals(oldItem.getAppoid(), newItem.getAppoid());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
        return oldItem.getAppoid().equals(newItem.getAppoid()) ;
    }
}
