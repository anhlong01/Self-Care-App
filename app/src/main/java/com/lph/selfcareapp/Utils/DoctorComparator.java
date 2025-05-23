package com.lph.selfcareapp.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.lph.selfcareapp.model.Doctor;

import java.util.Objects;

public class DoctorComparator extends DiffUtil.ItemCallback<Doctor> {
    @Override
    public boolean areItemsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
        return Objects.equals(oldItem.getDocId(), newItem.getDocId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
        return oldItem.getDocId().equals(newItem.getDocId()) ;
    }
}
