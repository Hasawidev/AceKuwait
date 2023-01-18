package com.alhasawi.acekuwait.ui.main.view.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("titleName")
    @Expose
    private String titleName;
    @SerializedName("align")
    @Expose
    private String align;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("refKey")
    @Expose
    private String refKey;
    @SerializedName("refObject")
    @Expose
    private RefObject refObject;


    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Object getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public RefObject getRefObject() {
        return refObject;
    }

    public void setRefObject(RefObject refObject) {
        this.refObject = refObject;
    }
}