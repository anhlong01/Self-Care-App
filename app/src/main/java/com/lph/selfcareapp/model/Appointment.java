package com.lph.selfcareapp.model;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Appointment extends BaseObservable implements Serializable {
    @SerializedName("appoid")
    @Expose
    private Integer appoid;
    @SerializedName("pid")
    @Expose
    private Integer pid;
    @SerializedName("docid")
    @Expose
    private Integer docid;
    @SerializedName("hasDone")
    @Expose
    private Integer hasDone;
    @SerializedName("docname")
    @Expose
    private String docname;
    @SerializedName("pname")
    @Expose
    private String pname;
    @SerializedName("scheduledate")
    @Expose
    private String scheduledate;
    @SerializedName("starttime")
    @Expose
    private String starttime;
    @SerializedName("endtime")
    @Expose
    private String endtime;
    @SerializedName("clinicName")
    @Expose
    private String clinic_name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("symptom")
    @Expose
    private String symptom;

    private String clinicImage;
    private String paddress;
    private String dob;
    private String sex;

    public String getClinicImage() {
        return clinicImage;
    }

    public void setClinicImage(String clinicImage) {
        this.clinicImage = clinicImage;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public Integer getAppoid() {
        return appoid;
    }

    public void setAppoid(Integer appoid) {
        this.appoid = appoid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getHasDone() {
        return hasDone;
    }

    public void setHasDone(Integer hasDone) {
        this.hasDone = hasDone;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocid(String docname) {
        this.docname = docname;
    }

    public String getScheduledate() {
        return scheduledate;
    }

    public void setScheduledate(String scheduledate) {
        this.scheduledate = scheduledate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
    }
}
