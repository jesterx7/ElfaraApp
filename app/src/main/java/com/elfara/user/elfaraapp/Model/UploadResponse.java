package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @Expose
    @SerializedName("sumSuccess") private int sumSuccess;
    @Expose
    @SerializedName("sumFailed") private int sumFailed;
    @Expose
    @SerializedName("message") private String message;
    @Expose
    @SerializedName("success") private Boolean success;

    public int getSumSuccess() {
        return sumSuccess;
    }

    public void setSumSuccess(int sumSuccess) {
        this.sumSuccess = sumSuccess;
    }

    public int getSumFailed() {
        return sumFailed;
    }

    public void setSumFailed(int sumFailed) {
        this.sumFailed = sumFailed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
