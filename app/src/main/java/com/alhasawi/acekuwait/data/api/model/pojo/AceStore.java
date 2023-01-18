package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AceStore {

    @SerializedName("storeId")
    @Expose
    private String storeId;

    @SerializedName("storeName")
    @Expose
    private String storeName;

    @SerializedName("storeAdress")
    @Expose
    private String storeAdress;

    @SerializedName("displayable")
    @Expose
    private boolean displayable;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("locationUrl")
    @Expose
    private String locationUrl;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAdress() {
        return storeAdress;
    }

    public void setStoreAdress(String storeAdress) {
        this.storeAdress = storeAdress;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }
}
