package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.TicketListItemBinding;
import com.lph.selfcareapp.model.Appointment;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder>{
    private Context context;
    private List<Appointment> ticketList;

    public TicketAdapter(Context context, List<Appointment> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TicketListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.ticket_list_item,
                parent,
                false
        );
        return new TicketHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        Appointment appointment = ticketList.get(position);
        if (appointment.getHasDone() == 1) {
            holder.ticketListItemBinding.statusText.setText("Đã hủy");
            holder.ticketListItemBinding.statusText.setTextColor(context.getColor(R.color.red));
        } else {
            holder.ticketListItemBinding.statusText.setText("Đã đặt");
            holder.ticketListItemBinding.statusText.setTextColor(context.getColor(R.color.green));
        }
        holder.ticketListItemBinding.ticketId.setText("Mã phiếu: " + appointment.getAppoid());
        holder.ticketListItemBinding.doctor.setText(appointment.getDocname());
        holder.ticketListItemBinding.ticketPatient.setText( appointment.getPname());
        holder.ticketListItemBinding.ticketTime.setText(appointment.getScheduledate() + " " + appointment.getStarttime() +"-" + appointment.getEndtime());
        holder.ticketListItemBinding.ticketClinic.setText(appointment.getClinic_name());
        holder.ticketListItemBinding.ticketaddress.setText(appointment.getAddress());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder{
        private TicketListItemBinding ticketListItemBinding;
        public TicketHolder(TicketListItemBinding ticketListItemBinding){
            super(ticketListItemBinding.getRoot());
            this.ticketListItemBinding = ticketListItemBinding;
        }
    }
}
