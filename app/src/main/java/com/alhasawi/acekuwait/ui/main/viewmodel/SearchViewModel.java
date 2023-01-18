package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.SearchProductListResponse;
import com.alhasawi.acekuwait.data.api.response.SearchResponse;
import com.alhasawi.acekuwait.data.api.response.SearchedProductDetailsResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;

public class SearchViewModel extends ViewModel {

    ProductRepository productRepository;
    MutableLiveData<SearchProductListResponse> searchProductListResponseMutableLiveData = new MutableLiveData<>();

    public SearchViewModel() {
        this.productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<SearchProductListResponse>> searchProducts(String searchQuery, String language) {
        return productRepository.searchProducts(searchQuery, language);
    }

    public MutableLiveData<Resource<SearchResponse>> getSearchResults(String searchQuery, String category, String language) {
        return productRepository.getSearchResults(searchQuery, category, language);
    }

    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String sku, String language, String customerId) {
        return productRepository.getSearchedProductDetails(sku, language, customerId);
    }
}
