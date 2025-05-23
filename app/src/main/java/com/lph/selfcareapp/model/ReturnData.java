package com.lph.selfcareapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnData {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("result")
    @Expose
    private VnpayResult result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public VnpayResult getResult() {
        return result;
    }

    public void setResult(VnpayResult result) {
        this.result = result;
    }
}
