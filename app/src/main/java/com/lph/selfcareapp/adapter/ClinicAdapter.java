package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.ClinicListItemBinding;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.ClinicList;
import com.lph.selfcareapp.listener.ChooseClinicListener;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.CLinicHolder>{
    private Context context;
    private ClinicList clinicList;
    private ChooseClinicListener listener;
    public ClinicAdapter(Context context, ClinicList clinicList, ChooseClinicListener listener){
        this.context = context;
        this.clinicList = clinicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CLinicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClinicListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.clinic_list_item,
                parent,
                false
        );
                return new CLinicHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CLinicHolder holder, int position) {
        Clinic clinic = clinicList.get(position);

        holder.clinicListItemBinding.distance.setText("Cách bạn: "+clinic.getDistance()/1000 +" km ");
        holder.clinicListItemBinding.setClinic(clinic);
        holder.clinicListItemBinding.bookDoctorBtn.setOnClickListener(
                v -> listener.onItemClicked(clinic)
        );
        holder.clinicListItemBinding.detailBtn.setOnClickListener(v->listener.openMap(clinic));
    }

    @Override
    public int getItemCount() {
        return clinicList.size();
    }

    public void sortData(){
        clinicList.sort((o1, o2) -> (int)(o1.getDistance()-o2.getDistance()));
        notifyDataSetChanged();
    }
    public class CLinicHolder extends RecyclerView.ViewHolder{
        private ClinicListItemBinding clinicListItemBinding;
        public CLinicHolder(ClinicListItemBinding clinicListItemBinding){
            super(clinicListItemBinding.getRoot());
            this.clinicListItemBinding = clinicListItemBinding;
        }
    }
}
