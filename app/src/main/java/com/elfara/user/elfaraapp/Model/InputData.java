package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputData {
    @Expose
    @SerializedName("namapelanggan") private String namapelanggan;
    @Expose
    @SerializedName("tanggal") private String tanggal;
    @Expose
    @SerializedName("alamat") private String alamat;
    @Expose
    @SerializedName("mediasosial") private String mediasosial;
    @Expose
    @SerializedName("nomortelepon") private String nomortelepon;
    @Expose
    @SerializedName("selling") private int selling;
    @Expose
    @SerializedName("sampling") private int sampling;
    @Expose
    @SerializedName("umur") private int umur;
    @Expose
    @SerializedName("iduser") private int iduser;
    @Expose
    @SerializedName("success") private Boolean success;
    @Expose
    @SerializedName("message") private String message;

    public String getNamapelanggan() {
        return namapelanggan;
    }

    public void setNamapelanggan(String namapelanggan) {
        this.namapelanggan = namapelanggan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getMediasosial() {
        return mediasosial;
    }

    public void setMediasosial(String mediasosial) {
        this.mediasosial = mediasosial;
    }

    public String getNomortelepon() {
        return nomortelepon;
    }

    public void setNomortelepon(String nomortelepon) {
        this.nomortelepon = nomortelepon;
    }

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    public int getSampling() {
        return sampling;
    }

    public void setSampling(int sampling) {
        this.sampling = sampling;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
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
