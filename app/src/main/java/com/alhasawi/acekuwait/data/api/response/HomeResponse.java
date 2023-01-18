package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.Language;
import com.alhasawi.acekuwait.ui.main.view.home.HomeSection;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class HomeResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private HashMap<String, Data> homeUiData;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public HashMap<String, Data> getHomeUiData() {
        return homeUiData;
    }

    public void setHomeUiData(HashMap<String, Data> homeUiData) {
        this.homeUiData = homeUiData;
    }

    public class Data {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("active")
        @Expose
        private boolean active;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("listView")
        @Expose
        private List<HomeSection> homeSectionList = null;
        @SerializedName("language")
        @Expose
        private Language language;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<HomeSection> getHomeSectionList() {
            return homeSectionList;
        }

        public void setHomeSectionList(List<HomeSection> homeSectionList) {
            this.homeSectionList = homeSectionList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }
    }


}