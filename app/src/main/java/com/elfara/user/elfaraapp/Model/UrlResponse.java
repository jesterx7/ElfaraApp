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
    @SerializedName("upload_at_list") private String uploadAtList;
    @Expose
    @SerializedName("user_list") private String userList;
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

    public String getUploadAtList() {
        return uploadAtList;
    }

    public void setUploadAtList(String uploadAtList) {
        this.uploadAtList = uploadAtList;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
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
