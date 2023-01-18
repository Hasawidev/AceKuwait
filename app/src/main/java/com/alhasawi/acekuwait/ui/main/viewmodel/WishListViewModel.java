package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;

public class WishListViewModel extends ViewModel {

    ProductRepository productRepository;

    public WishListViewModel() {
        this.productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<WishlistResponse>> getWishListedProducts(String userID, String sessiontoken, String language) {
        return productRepository.getWishListedProducts(userID, sessiontoken, language);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessiontoken, String language) {
        return productRepository.addToWishlist(productID, userID, sessiontoken, language);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }
}
