package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.pojo.FilterAttributeValues;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class FilterResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("statusCode")
    @Expose
    private int statusCode;

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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {
        @SerializedName("attributes")
        @Expose
        private Map<String, List<FilterAttributeValues>> filterAttributes = null;

        public Map<String, List<FilterAttributeValues>> getFilterAttributes() {
            return filterAttributes;
        }

        public void setFilterAttributes(Map<String, List<FilterAttributeValues>> filterAttributes) {
            this.filterAttributes = filterAttributes;
        }
    }
}
