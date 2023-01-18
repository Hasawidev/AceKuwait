package com.alhasawi.acekuwait.data.api.model.pojo;

public class DeliveryMode {
    private String title;
    private String content;

    public DeliveryMode(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
