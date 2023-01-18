package com.alhasawi.acekuwait.data.api.response;

import com.alhasawi.acekuwait.data.api.model.pojo.ProductListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNotifyListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data = null;
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
        @SerializedName("products")
        @Expose
        private ProductListData productList;

        public ProductListData getProductList() {
            return productList;
        }

        public void setProductList(ProductListData productList) {
            this.productList = productList;
        }
    }
}
