package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.DoctorListItemBinding;
import com.lph.selfcareapp.databinding.TicketListItemBinding;
import com.lph.selfcareapp.listener.ChooseDoctorListener;
import com.lph.selfcareapp.listener.DiagnoseListener;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Doctor;

public class AppointmentPagingAdapter extends PagingDataAdapter<Appointment, AppointmentPagingAdapter.AppointmentViewHolder> {
    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 1;
    private DiagnoseListener listener;
    private Context context;
    public AppointmentPagingAdapter(@NonNull DiffUtil.ItemCallback<Appointment> diffCallback, Context context, DiagnoseListener listener) {
        super(diffCallback);
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentPagingAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentPagingAdapter.AppointmentViewHolder(TicketListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentPagingAdapter.AppointmentViewHolder holder, int position) {
        Appointment appointment = getItem(position);
        if (appointment.getHasDone() == 1) {
            holder.ticketListItemBinding.statusText.setText("Đã hủy");
            holder.ticketListItemBinding.statusText.setTextColor(context.getColor(R.color.red));
        } else if (appointment.getHasDone() == 0) {
            holder.ticketListItemBinding.statusText.setText("Đã đặt");
            holder.ticketListItemBinding.statusText.setTextColor(context.getColor(R.color.link_blue));
        }else{
            holder.ticketListItemBinding.statusText.setText("Đã khám");
            holder.ticketListItemBinding.statusText.setTextColor(context.getColor(R.color.green));
            SharedPreferences sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            if(sp.getString("utype","").equals("doctor")){
                holder.ticketListItemBinding.uploadDiagnoseButton.setVisibility(View.VISIBLE);
                holder.ticketListItemBinding.uploadDiagnoseButton.setOnClickListener(v -> {
                    listener.uploadDiagnose(appointment);
                });
            }

        }
        holder.ticketListItemBinding.ticketId.setText("Mã phiếu: " + appointment.getAppoid());
        holder.ticketListItemBinding.doctor.setText(appointment.getDocname());
        holder.ticketListItemBinding.ticketPatient.setText( appointment.getPname());
        holder.ticketListItemBinding.ticketTime.setText(appointment.getScheduledate() + " " + appointment.getStarttime() +"-" + appointment.getEndtime());
        holder.ticketListItemBinding.ticketClinic.setText(appointment.getClinic_name());
        holder.ticketListItemBinding.ticketaddress.setText(appointment.getAddress());
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() ? MOVIE_ITEM : LOADING_ITEM;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        private TicketListItemBinding ticketListItemBinding;
        public AppointmentViewHolder(TicketListItemBinding ticketListItemBinding){
            super(ticketListItemBinding.getRoot());
            this.ticketListItemBinding = ticketListItemBinding;
        }
    }
}
