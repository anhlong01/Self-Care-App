package com.lph.selfcareapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.model.PrescriptionItem;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {

    private List<PrescriptionItem> prescriptionList;

    public PrescriptionAdapter(List<PrescriptionItem> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prescription, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        PrescriptionItem item = prescriptionList.get(position);
        holder.etDrugName.setText(item.getDrugName());
        holder.etUsage.setText(item.getUsage());
        holder.etQuantity.setText(String.valueOf(item.getQuantity()));

        holder.etDrugName.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                item.setDrugName(s.toString());
            }
        });

        holder.etUsage.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                item.setUsage(s.toString());
            }
        });

        holder.etQuantity.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                try {
                    item.setQuantity(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    item.setQuantity(0);
                }
            }
        });

        holder.ebtnDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAbsoluteAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && prescriptionList.size() > 1) {
                prescriptionList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public void addItem(PrescriptionItem item) {
        prescriptionList.add(item);
        notifyItemInserted(prescriptionList.size() - 1);
    }

    public List<PrescriptionItem> getPrescriptionList() {
        return prescriptionList;
    }

    public static class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        EditText etDrugName, etUsage, etQuantity;
        ImageButton ebtnDelete;
        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            etDrugName = itemView.findViewById(R.id.etDrugName);
            etUsage = itemView.findViewById(R.id.etUsage);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            ebtnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}