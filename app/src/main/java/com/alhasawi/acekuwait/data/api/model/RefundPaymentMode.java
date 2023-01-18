package com.alhasawi.acekuwait.data.api.model;

public class RefundPaymentMode {

    String title;
    String description;

    public RefundPaymentMode(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
