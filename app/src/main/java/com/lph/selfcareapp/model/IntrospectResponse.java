package com.lph.selfcareapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IntrospectResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("result")
    @Expose
    private IntrospectResult result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public IntrospectResult getResult() {
        return result;
    }

    public void setResult(IntrospectResult result) {
        this.result = result;
    }
}
