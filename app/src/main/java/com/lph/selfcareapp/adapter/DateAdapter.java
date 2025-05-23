package com.lph.selfcareapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.databinding.DoctorListItemBinding;
import com.lph.selfcareapp.databinding.ScheduleItemBinding;
import com.lph.selfcareapp.listener.ChooseClinicListener;
import com.lph.selfcareapp.listener.ChooseDateListener;
import com.lph.selfcareapp.model.SlotDTO;
import com.lph.selfcareapp.listener.ChooseDoctorListener;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateHolder> {
    private Context context;
    private List<SlotDTO> slotDTOList;
    private ChooseDateListener listener;
    private int selectedPostion;
    public DateAdapter(Context context, List<SlotDTO> slotDTOList, ChooseDateListener listener, int selectedPostion){
        this.context = context;
        this.slotDTOList = slotDTOList;
        this.listener = listener;
        this.selectedPostion = selectedPostion;
    }

    public class DateHolder extends RecyclerView.ViewHolder{
        private ScheduleItemBinding scheduleItemBinding;
        public DateHolder(ScheduleItemBinding scheduleItemBinding){
            super(scheduleItemBinding.getRoot());
            this.scheduleItemBinding = scheduleItemBinding;
        }
    }

    @NonNull
    @Override
    public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ScheduleItemBinding scheduleItemBinding = ScheduleItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new DateHolder(scheduleItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.DateHolder holder, int position) {
        SlotDTO slotDTO = slotDTOList.get(position);
        String weekday = "T" + slotDTO.getWeekday().toString();
        String temp[] = slotDTO.getDate().split("-");
        holder.scheduleItemBinding.dateBtn.setText(temp[2]);
        holder.scheduleItemBinding.textView13.setText(weekday);
        holder.scheduleItemBinding.slots.setText(slotDTO.getSlots() + " slot");
        if(selectedPostion == position){
            holder.scheduleItemBinding.dateBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.link_blue));
            holder.scheduleItemBinding.dateBtn.setTextColor(context.getColor(R.color.white));
        }else{
            holder.scheduleItemBinding.dateBtn.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray_btn_bg_color));
            holder.scheduleItemBinding.dateBtn.setTextColor(context.getColor(R.color.black));

        }

        holder.scheduleItemBinding.dateBtn.setOnClickListener(v->{
            selectedPostion = holder.getAbsoluteAdapterPosition();
            listener.onItemClicked(slotDTO);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return slotDTOList.size();
    }
}
