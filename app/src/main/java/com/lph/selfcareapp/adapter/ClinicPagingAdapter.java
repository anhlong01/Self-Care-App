package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.databinding.ClinicListItemBinding;
import com.lph.selfcareapp.listener.ChooseClinicListener;
import com.lph.selfcareapp.model.Clinic;

public class ClinicPagingAdapter extends PagingDataAdapter<Clinic, ClinicPagingAdapter.ClinicViewHolder> {
    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 1;
    private Context context;
    private ChooseClinicListener listener;
    public ClinicPagingAdapter(@NonNull DiffUtil.ItemCallback<Clinic> diffCallback,Context context, ChooseClinicListener listener) {
        super(diffCallback);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClinicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClinicPagingAdapter.ClinicViewHolder(ClinicListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ClinicViewHolder holder, int position) {
        Clinic clinic = getItem(position);
        String distance = String.format("%.2f", clinic.getDistance());
        holder.clinicListItemBinding.distance.setText("Cách bạn: "+distance +" km ");
        holder.clinicListItemBinding.setClinic(clinic);
        holder.clinicListItemBinding.bookDoctorBtn.setOnClickListener(
                v -> listener.onItemClicked(clinic)
        );
        holder.clinicListItemBinding.detailBtn.setOnClickListener(v->listener.openMap(clinic));
    }

    public class ClinicViewHolder extends RecyclerView.ViewHolder{
        private ClinicListItemBinding clinicListItemBinding;
        public ClinicViewHolder(ClinicListItemBinding clinicListItemBinding){
            super(clinicListItemBinding.getRoot());
            this.clinicListItemBinding = clinicListItemBinding;
        }
    }
}
