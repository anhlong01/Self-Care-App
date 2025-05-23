package com.lph.selfcareapp.model;

import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class PrescriptionItem extends BaseObservable implements Serializable {
    private String drugName;
    private String usage;
    private int quantity;

    public PrescriptionItem(String drugName, String usage, int quantity) {
        this.drugName = drugName;
        this.usage = usage;
        this.quantity = quantity;
    }

    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }

    public String getUsage() { return usage; }
    public void setUsage(String usage) { this.usage = usage; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}