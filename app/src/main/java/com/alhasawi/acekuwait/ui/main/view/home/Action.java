package com.alhasawi.acekuwait.ui.main.view.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action {

    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("json")
    @Expose
    private String json;
    @SerializedName("method")
    @Expose
    private String method;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}