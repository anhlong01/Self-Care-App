package com.lph.selfcareapp.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.CallDoctorListItemBinding;
import com.lph.selfcareapp.model.CallDoctor;
import com.lph.selfcareapp.listener.ChooseCallDoctorListener;

import java.util.List;

public class CallDoctorAdapter extends RecyclerView.Adapter<CallDoctorAdapter.CallDoctorHolder> {
    private Context context;
    private List<CallDoctor> doctorList;
    private ChooseCallDoctorListener listener;
    public CallDoctorAdapter(Context context, List<CallDoctor> doctorList, ChooseCallDoctorListener listener){
        this.context = context;
        this.doctorList = doctorList;
        this.listener = listener;
    }
    public class CallDoctorHolder extends RecyclerView.ViewHolder{
        private CallDoctorListItemBinding callDoctorListItemBinding;
        public CallDoctorHolder(CallDoctorListItemBinding callDoctorListItemBinding){
            super(callDoctorListItemBinding.getRoot());
            this.callDoctorListItemBinding = callDoctorListItemBinding;
        }
    }

    @NonNull
    @Override
    public CallDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CallDoctorListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.call_doctor_list_item,
                parent,
                false
        );
            return new CallDoctorHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CallDoctorHolder holder, int position) {
        CallDoctor doctor = doctorList.get(position);
        String price = String.format("%,d", doctor.getPrice());
        price = price +"đ";
        String docname = doctor.getAcacdemicrank() + " " + doctor.getDocname();
        if(doctor.getSex().equals("M"))
            holder.callDoctorListItemBinding.imageView.setImageResource(R.drawable.maledoctor);
        else
            holder.callDoctorListItemBinding.imageView.setImageResource(R.drawable.femaledoctor);
        holder.callDoctorListItemBinding.textView10.setText(price + "/15 phút");
        holder.callDoctorListItemBinding.textView6.setText(docname);
        holder.callDoctorListItemBinding.textView8.setText(doctor.getSname());
        holder.callDoctorListItemBinding.materialButton2.setOnClickListener(v -> listener.onItemCliked(doctor));
        holder.callDoctorListItemBinding.materialButton.setOnClickListener(v -> listener.call(doctor.getCallId()));
        if(doctor.getPaid().equals("1")){
            holder.callDoctorListItemBinding.materialButton.setClickable(true);
            holder.callDoctorListItemBinding.materialButton.setBackgroundColor(context.getColor(R.color.link_blue));
        }else{
            holder.callDoctorListItemBinding.materialButton.setClickable(false);
            holder.callDoctorListItemBinding.materialButton.setBackgroundColor(context.getColor(R.color.gray_btn_bg_color));
        }
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}