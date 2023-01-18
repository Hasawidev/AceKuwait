package com.alhasawi.acekuwait.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.CheckoutResponse;
import com.alhasawi.acekuwait.data.api.response.PaymentResponse;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutRepository {

    public MutableLiveData<Resource<CheckoutResponse>> checkout(String userID, String cartId, String couponCode, String sessionToken, String language) {
        MutableLiveData<Resource<CheckoutResponse>> cheResourceMutableLiveData = new MutableLiveData<>();
        Call<CheckoutResponse> addressResponseCall = RetrofitApiClient.getInstance().getApiInterface().checkout(userID, cartId, couponCode, "Bearer " + sessionToken, language);
        addressResponseCall.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                if (response.code() != 200) {
                    cheResourceMutableLiveData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    cheResourceMutableLiveData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                cheResourceMutableLiveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return cheResourceMutableLiveData;
    }

    public MutableLiveData<Resource<PaymentResponse>> getPaymentSuccess(String url) {
        MutableLiveData<Resource<PaymentResponse>> paymentSuccessMutableData = new MutableLiveData<>();
        Call<PaymentResponse> paymentSuccessResponseCall = RetrofitApiClient.getInstance().getApiInterface().getPaymentSuccessResponse(url);
        paymentSuccessResponseCall.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.code() != 200) {
                    paymentSuccessMutableData.setValue(Resource.error(response.message(), null));
                } else if (response.body() != null) {
                    paymentSuccessMutableData.setValue(Resource.success(response.body()));

                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                paymentSuccessMutableData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return paymentSuccessMutableData;
    }

}
