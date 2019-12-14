package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummarySell {
    @Expose
    @SerializedName("tanggaldari") private String tanggaldari;
    @Expose
    @SerializedName("tanggalsampai") private String tanggalsampai;
    @Expose
    @SerializedName("tanggal") private String tanggal;
    @Expose
    @SerializedName("selling") private int selling;
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

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
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
