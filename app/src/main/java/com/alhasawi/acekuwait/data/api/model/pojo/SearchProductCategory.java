package com.alhasawi.acekuwait.data.api.model.pojo;

import java.util.ArrayList;
import java.util.List;

public class SearchProductCategory {
    private String categoryId;
    private String categoryName;
    private List<String> productList = new ArrayList<>();

    public SearchProductCategory(String categoryId, String categoryName, List<String> productList) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.productList = productList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }
}
