package com.elfara.user.elfaraapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventResponse {
    @Expose
    @SerializedName("list_idevent") private String jsonListIdEvent;
    @Expose
    @SerializedName("list_event_name") private String jsonListEventName;
    @Expose
    @SerializedName("message") private String message;
    @Expose
    @SerializedName("success") private Boolean success;

    public String getJsonListIdEvent() {
        return jsonListIdEvent;
    }

    public void setJsonListIdEvent(String jsonListIdEvent) {
        this.jsonListIdEvent = jsonListIdEvent;
    }

    public String getJsonListEventName() {
        return jsonListEventName;
    }

    public void setJsonListEventName(String jsonListEventName) {
        this.jsonListEventName = jsonListEventName;
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
