package com.alhasawi.acekuwait.ui.main.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.response.OrderReturnResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnPolicyResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnReasonResponse;
import com.alhasawi.acekuwait.data.api.response.ReturnResponse;
import com.alhasawi.acekuwait.data.repository.OrderRepository;

public class ReturnViewModel extends ViewModel {
    private OrderRepository orderRepository;


    public ReturnViewModel() {
        orderRepository = new OrderRepository();
    }

    public MutableLiveData<Resource<ReturnResponse>> getEligibleReturnOrders(String userId, String sessiontoken, String language) {
        return orderRepository.getEligibleReturnOrders(userId, sessiontoken, language);
    }

    public MutableLiveData<Resource<ReturnPolicyResponse>> getReturnPolicy(String language) {
        return orderRepository.getReturnPolicy(language);
    }

    public MutableLiveData<Resource<ReturnReasonResponse>> getReturnReasons(String language) {
        return orderRepository.getReturnReasons(language);
    }

    public MutableLiveData<Resource<OrderReturnResponse>> returnOrder(String orderObject, String sessionToken, String language) {
        return orderRepository.orderReturn(orderObject, sessionToken, language);
    }
}
