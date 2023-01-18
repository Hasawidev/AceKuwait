package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.pojo.Category;
import com.alhasawi.acekuwait.data.api.model.pojo.Description;
import com.alhasawi.acekuwait.data.api.model.pojo.Manufacture;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OriginCategoryResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data = null;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("categoryCode")
        @Expose
        private String categoryCode;
        @SerializedName("depth")
        @Expose
        private Integer depth;
        @SerializedName("active")
        @Expose
        private boolean active;
        @SerializedName("parentId")
        @Expose
        private String parentId;
        @SerializedName("lineAge")
        @Expose
        private String lineAge;
        @SerializedName("categoryGroup")
        @Expose
        private Object categoryGroup;
        @SerializedName("sortOrder")
        @Expose
        private String sortOrder;
        @SerializedName("categories")
        @Expose
        private List<Category> categoryList = null;
        @SerializedName("descriptions")
        @Expose
        private List<Description> categoryDescriptionList = null;
        @SerializedName("manufactures")
        @Expose
        private List<Manufacture> manufactureListl = null;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public Integer getDepth() {
            return depth;
        }

        public void setDepth(Integer depth) {
            this.depth = depth;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getLineAge() {
            return lineAge;
        }

        public void setLineAge(String lineAge) {
            this.lineAge = lineAge;
        }

        public Object getCategoryGroup() {
            return categoryGroup;
        }

        public void setCategoryGroup(Object categoryGroup) {
            this.categoryGroup = categoryGroup;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public List<Category> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<Category> categoryList) {
            this.categoryList = categoryList;
        }

        public List<Description> getCategoryDescriptionList() {
            return categoryDescriptionList;
        }

        public void setCategoryDescriptionList(List<Description> categoryDescriptionList) {
            this.categoryDescriptionList = categoryDescriptionList;
        }

        public List<Manufacture> getManufactureListl() {
            return manufactureListl;
        }

        public void setManufactureListl(List<Manufacture> manufactureListl) {
            this.manufactureListl = manufactureListl;
        }
    }

}
