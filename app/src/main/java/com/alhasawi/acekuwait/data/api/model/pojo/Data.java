
package com.alhasawi.acekuwait.data.api.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Data {

    @SerializedName("attributes")
    @Expose
    private Map<String, List<FilterAttributeValues>> filterAttributes = null;
    @SerializedName("preferenceProduct")
    @Expose
    private List<Product> preferenceProductList = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName(("sort"))
    @Expose
    private List<String> sortStrings = null;
    @SerializedName("products")
    @Expose
    private ProductListData productList;

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    /**
     * @param attributes
     * @param categories
     * @param products
     */
    public Data(Map<String, List<FilterAttributeValues>> attributes, List<Category> categories, List<String> sortStrings, ProductListData products) {
        super();
        this.filterAttributes = attributes;
        this.categories = categories;
        this.sortStrings = sortStrings;
        this.productList = products;
    }

    public Map<String, List<FilterAttributeValues>> getFilterAttributes() {
        return filterAttributes;
    }

    public void setFilterAttributes(Map<String, List<FilterAttributeValues>> filterAttributes) {
        this.filterAttributes = filterAttributes;
    }

    public Data withAttributes(Map<String, List<FilterAttributeValues>> attributes) {
        this.filterAttributes = attributes;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Data withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public ProductListData getProductList() {
        return productList;
    }

    public void setProductList(ProductListData productList) {
        this.productList = productList;
    }

    public Data withProducts(ProductListData products) {
        this.productList = products;
        return this;
    }

    public List<String> getSortStrings() {
        return sortStrings;
    }

    public void setSortStrings(List<String> sortStrings) {
        this.sortStrings = sortStrings;
    }

    public List<Product> getPreferenceProductList() {
        return preferenceProductList;
    }

    public void setPreferenceProductList(List<Product> preferenceProductList) {
        this.preferenceProductList = preferenceProductList;
    }
}