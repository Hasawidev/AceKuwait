package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.NotifyResponse;
import com.alhasawi.acekuwait.data.api.response.ProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.SearchedProductDetailsResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;

public class ProductDetailViewModel extends ViewModel {

    private ProductRepository productRepository;

    public ProductDetailViewModel() {
        productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<ProductDetailsResponse>> getProductDetails(String productName, String language) {
        return productRepository.getProductDetails(productName, language);
    }


    public MutableLiveData<Resource<SearchedProductDetailsResponse>> getSearchedProductDetails(String sku, String language, String customerId) {
        return productRepository.getSearchedProductDetails(sku, language, customerId);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken, String language) {
        return productRepository.addToWishlist(productID, userID, sessionToken, language);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }

    public MutableLiveData<Resource<NotifyResponse>> addToNotify(String jsonParams, String sessionToken, String language) {
        return productRepository.addToNotifyList(jsonParams, sessionToken, language);
    }
}

