package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingMode {
    @SerializedName("shippingId")
    @Expose
    private String shippingId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("charge")
    @Expose
    private Double charge;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("sortOrder")
    @Expose
    private String sortOrder;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("targetAmount")
    @Expose
    private Double targetAmount;
    @SerializedName("defaultShipping")
    @Expose
    private boolean defaultShipping;
    @SerializedName("marketPlace")
    @Expose
    private boolean marketPlace;
    @SerializedName("pickup")
    private boolean isPickup;

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public boolean isDefaultShipping() {
        return defaultShipping;
    }

    public void setDefaultShipping(boolean defaultShipping) {
        this.defaultShipping = defaultShipping;
    }

    public boolean isMarketPlace() {
        return marketPlace;
    }

    public void setMarketPlace(boolean marketPlace) {
        this.marketPlace = marketPlace;
    }

    public boolean isPickup() {
        return isPickup;
    }

    public void setPickup(boolean pickup) {
        isPickup = pickup;
    }


}
