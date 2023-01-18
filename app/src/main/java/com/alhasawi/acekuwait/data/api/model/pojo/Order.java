package com.alhasawi.acekuwait.data.api.model.pojo;

import com.alhasawi.acekuwait.data.api.model.ReturnPaymentType;
import com.alhasawi.acekuwait.data.api.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("awb")
    @Expose
    private String awb;
    @SerializedName("dateOfPurchase")
    @Expose
    private String dateOfPurchase;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("customer")
    @Expose
    private User customer;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("subTotal")
    @Expose
    private Double subTotal;
    @SerializedName("discounted")
    @Expose
    private Double discounted;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("orderProducts")
    @Expose
    private List<OrderProduct> orderProducts = null;
    @SerializedName("payment")
    @Expose
    private PaymentMode payment;
    @SerializedName("getPaymentStatusResponse")
    @Expose
    private Invoice getPaymentStatusResponse;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("orderTracks")
    @Expose
    private List<OrderTrack> orderTrackList;
    @SerializedName("shippingId")
    @Expose
    private String shippingId;
    @SerializedName("shippingCharge")
    @Expose
    private Double shippingCharge;
    @SerializedName("synStatus")
    @Expose
    private boolean synStatus;
    @SerializedName("discountType")
    @Expose
    private String discountType;
    @SerializedName("discountTypeValue")
    @Expose
    private Object discountTypeValue;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("orderReturn")
    @Expose
    private boolean orderReturn;
    @SerializedName("orderReturnType")
    @Expose
    private String orderReturnType;
    @SerializedName("returnReason")
    @Expose
    private String returnReason;
    @SerializedName("returnPayment")
    @Expose
    private ReturnPaymentType returnPayment;
    @SerializedName("iban")
    @Expose
    private String iban;
    @SerializedName("bankAccount")
    @Expose
    private String bankAccount;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Double discounted) {
        this.discounted = discounted;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public PaymentMode getPayment() {
        return payment;
    }

    public void setPayment(PaymentMode payment) {
        this.payment = payment;
    }

    public Invoice getGetPaymentStatusResponse() {
        return getPaymentStatusResponse;
    }

    public void setGetPaymentStatusResponse(Invoice getPaymentStatusResponse) {
        this.getPaymentStatusResponse = getPaymentStatusResponse;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderTrack> getOrderTrackList() {
        return orderTrackList;
    }

    public void setOrderTrackList(List<OrderTrack> orderTrackList) {
        this.orderTrackList = orderTrackList;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public Double getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(Double shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public boolean isSynStatus() {
        return synStatus;
    }

    public void setSynStatus(boolean synStatus) {
        this.synStatus = synStatus;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Object getDiscountTypeValue() {
        return discountTypeValue;
    }

    public void setDiscountTypeValue(Object discountTypeValue) {
        this.discountTypeValue = discountTypeValue;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isOrderReturn() {
        return orderReturn;
    }

    public void setOrderReturn(boolean orderReturn) {
        this.orderReturn = orderReturn;
    }

    public String getOrderReturnType() {
        return orderReturnType;
    }

    public void setOrderReturnType(String orderReturnType) {
        this.orderReturnType = orderReturnType;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public ReturnPaymentType getReturnPayment() {
        return returnPayment;
    }

    public void setReturnPayment(ReturnPaymentType returnPayment) {
        this.returnPayment = returnPayment;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }
}