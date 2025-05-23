package com.lph.selfcareapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IntrospectResult {
    @SerializedName("valid")
    @Expose
    private Boolean valid;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
