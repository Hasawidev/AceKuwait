package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.OrderResponse;
import com.alhasawi.acekuwait.data.api.response.PaymentResponse;
import com.alhasawi.acekuwait.data.repository.CheckoutRepository;
import com.alhasawi.acekuwait.data.repository.OrderRepository;

import java.util.Map;

public class OrderViewModel extends ViewModel {

    private OrderRepository orderRepository;
    private CheckoutRepository checkoutRepository;


    public OrderViewModel() {
        orderRepository = new OrderRepository();
        checkoutRepository = new CheckoutRepository();
    }

    public MutableLiveData<Resource<OrderResponse>> orderConfirmation(String userId, String cartId, String sessiontoken, Map<String, Object> jsonParams, String language) {
        return orderRepository.orderConfirmation(userId, cartId, sessiontoken, jsonParams, language);
    }

    public MutableLiveData<Resource<PaymentResponse>> getPaymentSuccess(String url) {
        return checkoutRepository.getPaymentSuccess(url);
    }
}
