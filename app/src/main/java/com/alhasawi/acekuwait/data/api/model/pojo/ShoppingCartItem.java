
package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingCartItem {

    @SerializedName("shoppingCartItemId")
    @Expose
    private String shoppingCartItemId;
    //    @SerializedName("dateCreated")
//    @Expose
//    private Object dateCreated;
//    @SerializedName("dateModified")
//    @Expose
//    private Object dateModified;
    @SerializedName("updtId")
    @Expose
    private Object updtId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("refSku")
    @Expose
    private String refSku;
    @SerializedName("oneTimePrice")
    @Expose
    private Double oneTimePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("shippings")
    @Expose
    private List<ShippingMode> shippingModeList;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("pickupDate")
    @Expose
    private String pickupDate;
    @SerializedName("time")
    @Expose
    private String pickupTime;
    @SerializedName("shipping")
    @Expose
    private ShippingMode shippingMode;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("productConfigurable")
    @Expose
    private ProductConfigurable productConfigurable;

    private boolean isOutOfStock;
    @SerializedName("store")
    @Expose
    private Store store;
    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("address")
    @Expose
    private Address address;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getShoppingCartItemId() {
        return shoppingCartItemId;
    }

    public void setShoppingCartItemId(String shoppingCartItemId) {
        this.shoppingCartItemId = shoppingCartItemId;
    }

//    public Object getDateCreated() {
//        return dateCreated;
//    }
//
//    public void setDateCreated(Object dateCreated) {
//        this.dateCreated = dateCreated;
//    }
//
//    public Object getDateModified() {
//        return dateModified;
//    }
//
//    public void setDateModified(Integer dateModified) {
//        this.dateModified = dateModified;
//    }

    public Object getUpdtId() {
        return updtId;
    }

    public void setUpdtId(Object updtId) {
        this.updtId = updtId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRefSku() {
        return refSku;
    }

    public void setRefSku(String refSku) {
        this.refSku = refSku;
    }

    public Double getOneTimePrice() {
        return oneTimePrice;
    }

    public void setOneTimePrice(Double oneTimePrice) {
        this.oneTimePrice = oneTimePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        isOutOfStock = outOfStock;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ProductConfigurable getProductConfigurable() {
        return productConfigurable;
    }

    public void setProductConfigurable(ProductConfigurable productConfigurable) {
        this.productConfigurable = productConfigurable;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<ShippingMode> getShippingModeList() {
        return shippingModeList;
    }

    public void setShippingModeList(List<ShippingMode> shippingModeList) {
        this.shippingModeList = shippingModeList;
    }

    public ShippingMode getShippingMode() {
        return shippingMode;
    }

    public void setShippingMode(ShippingMode shippingMode) {
        this.shippingMode = shippingMode;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
