package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.AddressResponse;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.data.repository.CheckoutRepository;
import com.alhasawi.acekuwait.data.repository.ProductRepository;
import com.alhasawi.acekuwait.data.repository.UserAccountRepository;

import java.util.Map;

public class CartViewModel extends ViewModel {
    ProductRepository productRepository;
    UserAccountRepository userAccountRepository;
    CheckoutRepository checkoutRepository;

    public CartViewModel() {
        productRepository = new ProductRepository();
        userAccountRepository = new UserAccountRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<CartResponse>> getCartItems(String userID, String sessiontoken, String language, boolean isAlertClicked) {
        return productRepository.getCartItems(userID, sessiontoken, language, isAlertClicked);
    }

    public MutableLiveData<Resource<CartResponse>> removeItemFromCart(String userId, String cartItemId, String sessiontoken, String language) {
        return productRepository.removeFromCart(userId, cartItemId, sessiontoken, language);
    }


    public MutableLiveData<Resource<AddressResponse>> addNewAddress(String userId, Map<String, Object> jsonParams, String sessiontoken, String language) {
        return userAccountRepository.addNewAddress(userId, jsonParams, sessiontoken, language);
    }

    public MutableLiveData<Resource<CartResponse>> addToCart(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.addToCart(userID, jsonParams, sessionToken, language);
    }

    public MutableLiveData<Resource<CartResponse>> updateCartItems(String userID, String jsonParams, String sessionToken, String language) {
        return productRepository.updateCartItems(userID, jsonParams, sessionToken, language);
    }


//    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userId, String cartId, String sessiontoken) {
//        return checkoutRepository.checkout(userId, cartId, sessiontoken);
//    }
}
