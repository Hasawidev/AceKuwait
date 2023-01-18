package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.GetNotifyListResponse;
import com.alhasawi.acekuwait.data.api.response.WishlistResponse;
import com.alhasawi.acekuwait.data.repository.ProductRepository;

public class NotifyViewModel extends ViewModel {
    private ProductRepository productRepository;

    public NotifyViewModel() {
        productRepository = new ProductRepository();
    }

    public MutableLiveData<Resource<GetNotifyListResponse>> getNotifyList(String customerId, String page, String size, String sessionToken, String language) {
        return productRepository.getNotifyList(customerId, page, size, sessionToken, language);
    }

    public MutableLiveData<Resource<WishlistResponse>> addToWishlist(String productID, String userID, String sessionToken, String language) {
        return productRepository.addToWishlist(productID, userID, sessionToken, language);
    }


}
