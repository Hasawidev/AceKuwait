package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wishlist {
    @SerializedName("wishListId")
    @Expose
    private String wishListId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("customerId")
    @Expose
    private String customerId;

    public String getWishListId() {
        return wishListId;
    }

    public void setWishListId(String wishListId) {
        this.wishListId = wishListId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}