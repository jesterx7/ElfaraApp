package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummarySample {
    @Expose
    @SerializedName("tanggaldari") private String tanggaldari;
    @Expose
    @SerializedName("tanggalsampai") private String tanggalsampai;
    @Expose
    @SerializedName("tanggal") private String tanggal;
    @Expose
    @SerializedName("sampling") private int sampling;
    @Expose
    @SerializedName("success") private Boolean success;
    @Expose
    @SerializedName("message") private String message;

    public String getTanggaldari() {
        return tanggaldari;
    }

    public void setTanggaldari(String tanggaldari) {
        this.tanggaldari = tanggaldari;
    }

    public String getTanggalsampai() {
        return tanggalsampai;
    }

    public void setTanggalsampai(String tanggalsampai) {
        this.tanggalsampai = tanggalsampai;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getSampling() {
        return sampling;
    }

    public void setSampling(int sampling) {
        this.sampling = sampling;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
