package com.alhasawi.acekuwait.ui.main.viewmodel;

import androidx.lifecycle.ViewModel;

import com.alhasawi.acekuwait.data.api.model.pojo.PaymentMode;
import com.alhasawi.acekuwait.data.repository.OrderRepository;

public class PaymentViewModel extends ViewModel {
    PaymentMode selectedPaymentMode;
    OrderRepository orderRepository;

    public PaymentViewModel() {
        orderRepository = new OrderRepository();
    }

    public void setSelectedPaymentMode(PaymentMode selectedPaymentMode) {
        this.selectedPaymentMode = selectedPaymentMode;
    }
}