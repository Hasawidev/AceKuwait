package com.alhasawi.acekuwait.ui.main.view.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timer {
    @SerializedName("start")
    @Expose
    private String starts_at;
    @SerializedName("end")
    @Expose
    private String ends_at;


    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getEnds_at() {
        return ends_at;
    }

    public void setEnds_at(String ends_at) {
        this.ends_at = ends_at;
    }
}
