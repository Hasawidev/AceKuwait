package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductConfigurable {

    @SerializedName("productConfigurableId")
    @Expose
    private String productConfigurableId;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("refSku")
    @Expose
    private String refSku;
    @SerializedName("variablePrice")
    @Expose
    private Object variablePrice;
    @SerializedName("variablePriceType")
    @Expose
    private Object variablePriceType;
    @SerializedName("colorCode")
    @Expose
    private String colorCode;
    @SerializedName("enable")
    @Expose
    private Boolean enable;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("storeInventorys")
    @Expose
    private List<StoreInventory> storeInventoryList;
    @SerializedName("productImages")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("genericSize")
    @Expose
    private String genericSize;

    public String getProductConfigurableId() {
        return productConfigurableId;
    }

    public void setProductConfigurableId(String productConfigurableId) {
        this.productConfigurableId = productConfigurableId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRefSku() {
        return refSku;
    }

    public void setRefSku(String refSku) {
        this.refSku = refSku;
    }

    public Object getVariablePrice() {
        return variablePrice;
    }

    public void setVariablePrice(Object variablePrice) {
        this.variablePrice = variablePrice;
    }

    public Object getVariablePriceType() {
        return variablePriceType;
    }

    public void setVariablePriceType(Object variablePriceType) {
        this.variablePriceType = variablePriceType;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getGenericSize() {
        return genericSize;
    }

    public void setGenericSize(String genericSize) {
        this.genericSize = genericSize;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<StoreInventory> getStoreInventoryList() {
        return storeInventoryList;
    }

    public void setStoreInventoryList(List<StoreInventory> storeInventoryList) {
        this.storeInventoryList = storeInventoryList;
    }
}
