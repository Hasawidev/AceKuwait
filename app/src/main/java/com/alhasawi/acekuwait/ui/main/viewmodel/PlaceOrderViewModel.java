package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.AddressResponse;
import com.alhasawi.acekuwait.data.api.response.CheckoutResponse;
import com.alhasawi.acekuwait.data.api.response.DeleteAddressResponse;
import com.alhasawi.acekuwait.data.api.response.GetAllAddressResponse;
import com.alhasawi.acekuwait.data.repository.CheckoutRepository;
import com.alhasawi.acekuwait.data.repository.UserAccountRepository;

import java.util.Map;

public class PlaceOrderViewModel extends ViewModel {

    CheckoutRepository checkoutRepository;
    UserAccountRepository userAccountRepository;
    boolean isEdit = false;

    public PlaceOrderViewModel() {
        userAccountRepository = new UserAccountRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userId, String cartId, String couponCode, String sessiontoken, String language) {
        return checkoutRepository.checkout(userId, cartId, couponCode, sessiontoken, language);
    }

    public MutableLiveData<Resource<GetAllAddressResponse>> getAddresses(String userId, String sessiontoken, String language) {
        return userAccountRepository.getAddresses(userId, sessiontoken, language);
    }

    public MutableLiveData<Resource<AddressResponse>> editAddress(String userId, String addressId, String sessionToken, Map<String, Object> jsonParams, String language) {
        return userAccountRepository.editAddress(userId, addressId, sessionToken, jsonParams, language);
    }

    public MutableLiveData<Resource<DeleteAddressResponse>> deleteAddress(String addressId, String sessiontoken, String language) {
        return userAccountRepository.deleteAddress(addressId, sessiontoken, language);
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }
}
