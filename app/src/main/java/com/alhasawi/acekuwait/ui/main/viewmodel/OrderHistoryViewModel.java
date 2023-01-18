package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.OrderHistoryResponse;
import com.alhasawi.acekuwait.data.repository.OrderRepository;

public class OrderHistoryViewModel extends ViewModel {

    OrderRepository orderRepository;

    public OrderHistoryViewModel() {
        orderRepository = new OrderRepository();
    }

    public MutableLiveData<Resource<OrderHistoryResponse>> getOrderHistory(String customerId, String sessionToken, String language) {
        return orderRepository.orderHistory(customerId, sessionToken, language);
    }
}
