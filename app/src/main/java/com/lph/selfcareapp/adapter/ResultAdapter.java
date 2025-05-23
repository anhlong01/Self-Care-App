package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.DiagnoseListItemPatientBinding;
import com.lph.selfcareapp.listener.SeeResultListener;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Appointment2;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.TicketHolder>{
    private Context context;
    private List<Appointment> ticketList;
    private SeeResultListener seeResultListener;
    public ResultAdapter(Context context, List<Appointment> ticketList, SeeResultListener seeResultListener) {
        this.context = context;
        this.ticketList = ticketList;
        this.seeResultListener = seeResultListener;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiagnoseListItemPatientBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.diagnose_list_item_patient,
                parent,
                false
        );
        return new TicketHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        Appointment appointment = ticketList.get(position);
        holder.diagnoseListItemPatientBinding.ticketId.setText("Mã phiếu: " + appointment.getAppoid());
        holder.diagnoseListItemPatientBinding.ticketDoctor.setText("Bác sĩ: " + appointment.getDocname());
        holder.diagnoseListItemPatientBinding.ticketPatient.setText("Bệnh nhân: " + appointment.getPname());
        holder.diagnoseListItemPatientBinding.ticketTime.setText("GIờ khám: " + appointment.getScheduledate() + " " + appointment.getStarttime() +"-" + appointment.getEndtime());
        holder.diagnoseListItemPatientBinding.seeDiagnose.setOnClickListener(v->{
            seeResultListener.seeResult(appointment.getAppoid());
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class TicketHolder extends RecyclerView.ViewHolder{
        private DiagnoseListItemPatientBinding diagnoseListItemPatientBinding;
        public TicketHolder(DiagnoseListItemPatientBinding ticketListItemBinding){
            super(ticketListItemBinding.getRoot());
            this.diagnoseListItemPatientBinding = ticketListItemBinding;
        }
    }
}
