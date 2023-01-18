package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.pojo.Preference;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreferenceResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Preference preferenceData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Preference getPreferenceData() {
        return preferenceData;
    }

    public void setPreferenceData(Preference preferenceData) {
        this.preferenceData = preferenceData;
    }


}
