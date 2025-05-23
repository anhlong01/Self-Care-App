package com.lph.selfcareapp.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lph.selfcareapp.model.Clinic;

import java.util.Objects;

public class ClinicComparator extends DiffUtil.ItemCallback<Clinic> {
    @Override
    public boolean areItemsTheSame(@NonNull Clinic oldItem, @NonNull Clinic newItem) {
        return Objects.equals(oldItem.getClinic_id(), newItem.getClinic_id());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Clinic oldItem, @NonNull Clinic newItem) {
        return Objects.equals(oldItem.getClinic_id(), newItem.getClinic_id());
    }
}
