package com.alhasawi.acekuwait.ui.main.view.dashboard.product.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ProductDataFactory extends DataSource.Factory {

    private MutableLiveData<ProductDataSource> mutableLiveData;
    private ProductDataSource productDataSource;
    private String inputPayload = "";

    public ProductDataFactory(String inputPayload) {
        this.inputPayload = inputPayload;
        this.mutableLiveData = new MutableLiveData<ProductDataSource>();
    }

    @Override
    public DataSource create() {
        productDataSource = new ProductDataSource(inputPayload);
        mutableLiveData.postValue(productDataSource);
        return productDataSource;
    }

    public MutableLiveData<ProductDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}