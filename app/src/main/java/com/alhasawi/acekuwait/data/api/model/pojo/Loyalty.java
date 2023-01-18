package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loyalty {
    @SerializedName("cardNumber")
    @Expose
    private String cardNumber;

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("remeeded")
    @Expose
    private int remeeded;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRemeeded() {
        return remeeded;
    }

    public void setRemeeded(int remeeded) {
        this.remeeded = remeeded;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
