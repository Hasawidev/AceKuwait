package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.HomeResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.repository.DynamicDataRepository;
import com.alhasawi.acekuwait.data.repository.ProductRepository;

public class HomeViewModel extends ViewModel {

    DynamicDataRepository dynamicDataRepository;
    private ProductRepository productRepository;
    ;

    public HomeViewModel() {
        dynamicDataRepository = new DynamicDataRepository();
        productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<HomeResponse>> getHomeData(String languageId) {
        return dynamicDataRepository.getHomeData(languageId);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken, String language) {
        return productRepository.addToWishlist(productID, userID, sessionToken, language);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }

}
