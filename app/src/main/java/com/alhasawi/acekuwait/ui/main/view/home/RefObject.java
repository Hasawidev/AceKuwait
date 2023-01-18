package com.alhasawi.acekuwait.ui.main.view.home;

import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.data.api.model.pojo.Manufacture;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductAttribute;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductConfigurable;
import com.alhasawi.acekuwait.data.api.model.pojo.ProductDescription;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RefObject {
    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("productType")
    @Expose
    private String productType;

    @SerializedName("descriptions")
    @Expose
    private List<ProductDescription> descriptions;

    @SerializedName("productGroup")
    @Expose
    private Object productGroup;

    @SerializedName("productAttributes")
    @Expose
    private List<ProductAttribute> productAttributes;

    @SerializedName("productConfigurables")
    @Expose
    private List<ProductConfigurable> productConfigurables;

    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    @SerializedName("brandLogoUrl")
    @Expose
    private String brandLogoUrl;

    @SerializedName("originalPrice")
    @Expose
    private Double originalPrice;

    @SerializedName("discountPrice")
    @Expose
    private Double discountPrice;

    @SerializedName("eta")
    @Expose
    private String eta;

    @SerializedName("discountPercentage")
    @Expose
    private Integer discountPercentage;

    @SerializedName("available")
    @Expose
    private boolean available;

    @SerializedName("productOrigin")
    @Expose
    private String productOrigin;

    @SerializedName("manufacture")
    @Expose
    private Manufacture manufacture;

    @SerializedName("wishlist")
    @Expose
    private boolean wishlist;

    @SerializedName("discount")
    @Expose
    private boolean discount;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public List<ProductDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ProductDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public Object getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(Object productGroup) {
        this.productGroup = productGroup;
    }

    public List<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public List<ProductConfigurable> getProductConfigurables() {
        return productConfigurables;
    }

    public void setProductConfigurables(List<ProductConfigurable> productConfigurables) {
        this.productConfigurables = productConfigurables;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    public Manufacture getManufacture() {
        return manufacture;
    }

    public void setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }
}
