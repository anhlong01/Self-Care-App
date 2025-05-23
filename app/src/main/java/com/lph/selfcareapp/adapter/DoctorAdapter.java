package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.DoctorListItemBinding;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.listener.ChooseDoctorListener;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorHolder> {
    private Context context;
    private List<Doctor> doctorList;
    private ChooseDoctorListener listener;

    public DoctorAdapter(Context context, List<Doctor> doctorList, ChooseDoctorListener listener){
        this.context = context;
        this.doctorList = doctorList;
        this.listener = listener;
    }
    public class DoctorHolder extends RecyclerView.ViewHolder{
        private DoctorListItemBinding doctorListItemBinding;
        public DoctorHolder(DoctorListItemBinding doctorListItemBinding){
            super(doctorListItemBinding.getRoot());
            this.doctorListItemBinding = doctorListItemBinding;
        }
    }

    @NonNull
    @Override
    public DoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DoctorListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.doctor_list_item,
                parent,
                false
        );
            return new DoctorHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        String price = String.format("%,d", doctor.getPrice());
        price = price +"đ";
        holder.doctorListItemBinding.setDoctor(doctor);
        holder.doctorListItemBinding.docname.setText(doctor.getAcademicRank() + " " + doctor.getDocName());
        holder.doctorListItemBinding.price.setText(price);
        holder.doctorListItemBinding.cvDoctor.setOnClickListener(v -> listener.onItemClicked(doctor));
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}
