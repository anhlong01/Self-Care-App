package com.lph.selfcareapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.databinding.DoctorListItemBinding;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.listener.ChooseDoctorListener;

public class DoctorPagingAdapter extends PagingDataAdapter<Doctor, DoctorPagingAdapter.DoctorViewHolder> {
    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 1;
    private ChooseDoctorListener listener;
    public DoctorPagingAdapter(@NonNull DiffUtil.ItemCallback<Doctor> diffCallback, ChooseDoctorListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorPagingAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorViewHolder(DoctorListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorPagingAdapter.DoctorViewHolder holder, int position) {
        Doctor doctor = getItem(position);
        if (doctor!=null){
            String price = String.format("%,d", doctor.getPrice());
            price = price +"đ";
            holder.doctorListItemBinding.setDoctor(doctor);
            holder.doctorListItemBinding.docname.setText(doctor.getAcademicRank() + " " + doctor.getDocName());
            holder.doctorListItemBinding.price.setText(price);
            holder.doctorListItemBinding.spe.setText(doctor.getSpecialtiesName());
            holder.doctorListItemBinding.cvDoctor.setOnClickListener(v -> listener.onItemClicked(doctor));
            holder.doctorListItemBinding.infoButton.setOnClickListener(v->listener.seeInfo(doctor));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() ? MOVIE_ITEM : LOADING_ITEM;
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{
        private DoctorListItemBinding doctorListItemBinding;
        public DoctorViewHolder(DoctorListItemBinding doctorListItemBinding){
            super(doctorListItemBinding.getRoot());
            this.doctorListItemBinding = doctorListItemBinding;
        }
    }
}
