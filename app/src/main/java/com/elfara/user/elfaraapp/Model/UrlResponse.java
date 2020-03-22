package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UrlResponse {
    @Expose
    @SerializedName("url_list") private String urlList;
    @Expose
    @SerializedName("filename_list") private String filenameList;
    @Expose
    @SerializedName("message") private String message;
    @Expose
    @SerializedName("success") private Boolean success;

    public String getUrlList() {
        return urlList;
    }

    public void setUrlList(String urlList) {
        this.urlList = urlList;
    }

    public String getFilenameList() {
        return filenameList;
    }

    public void setFilenameList(String filenameList) {
        this.filenameList = filenameList;
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
