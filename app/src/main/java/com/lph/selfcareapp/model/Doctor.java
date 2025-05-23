package com.lph.selfcareapp.model;

import android.content.res.AssetManager;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lph.selfcareapp.R;

import java.io.Serializable;

public class Doctor extends BaseObservable implements Serializable {
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("docEmail")
    @Expose
    private String docEmail;
    @SerializedName("docName")
    @Expose
    private String docName;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    @SerializedName("specialtiesName")
    @Expose
    private String specialtiesName;
    @SerializedName("academicRank")
    @Expose
    private String academicRank;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("intro")
    @Expose
    private String intro;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDocEmail() {
        return docEmail;
    }

    public void setDocEmail(String docEmail) {
        this.docEmail = docEmail;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getSpecialtiesName() {
        return specialtiesName;
    }

    public void setSpecialtiesName(String specialtiesName) {
        this.specialtiesName = specialtiesName;
    }

    public String getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(String academicRank) {
        this.academicRank = academicRank;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @BindingAdapter("docImagePath")
    public static void loadImage(ImageView imageView, String sex){

        if(sex.equals("Nam"))
            imageView.setImageResource(R.drawable.maledoctor);
        else
            imageView.setImageResource(R.drawable.femaledoctor);
    }
}


