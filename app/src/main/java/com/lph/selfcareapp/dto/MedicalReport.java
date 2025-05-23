package com.lph.selfcareapp.dto;

import androidx.databinding.BaseObservable;

import com.lph.selfcareapp.model.PrescriptionItem;

import java.io.Serializable;
import java.util.List;

public class MedicalReport extends BaseObservable implements Serializable {
    private String patientName;
    private String gender;
    private String patientEmail;
    private String dateOfBirth;
    private String address;
    private String symptom;
    private String clinicName;
    private String clinicAddress;
    private String doctorDiagnosis;
    private String doctorGuide;
    private String doctorName;
    private String clinicImage;
    private String reschedule;
    private List<PrescriptionItem> prescriptionList;

    public MedicalReport() {
    }

    public MedicalReport(String patientName, String gender, String patientEmail, String dateOfBirth, String address, String symptom, String clinicName, String clinicAddress, String doctorDiagnosis, String doctorGuide, String doctorName, List<PrescriptionItem> prescriptionList, String clinicImage, String rescheduleDate) {
        this.patientName = patientName;
        this.gender = gender;
        this.patientEmail = patientEmail;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.symptom = symptom;
        this.clinicName = clinicName;
        this.clinicAddress = clinicAddress;
        this.doctorDiagnosis = doctorDiagnosis;
        this.doctorGuide = doctorGuide;
        this.doctorName = doctorName;
        this.prescriptionList = prescriptionList;
        this.clinicImage = clinicImage;
        this.reschedule = rescheduleDate;
    }

    public String getRescheduleDate() {
        return reschedule;
    }

    public void setRescheduleDate(String rescheduleDate) {
        this.reschedule = rescheduleDate;
    }

    public String getClinicImage() {
        return clinicImage;
    }

    public void setClinicImage(String clinicImage) {
        this.clinicImage = clinicImage;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getDoctorDiagnosis() {
        return doctorDiagnosis;
    }

    public void setDoctorDiagnosis(String doctorDiagnosis) {
        this.doctorDiagnosis = doctorDiagnosis;
    }

    public String getDoctorGuide() {
        return doctorGuide;
    }

    public void setDoctorGuide(String doctorGuide) {
        this.doctorGuide = doctorGuide;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public List<PrescriptionItem> getPrescriptionList() {
        return prescriptionList;
    }

    public void setPrescriptionList(List<PrescriptionItem> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }
}
