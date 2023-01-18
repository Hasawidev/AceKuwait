package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.data.api.model.pojo.PaymentMode;
import com.alhasawi.acekuwait.data.api.response.OrderResponse;
import com.alhasawi.acekuwait.databinding.FragmentOrderConfirmationBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.CheckoutProductListAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.OrderViewModel;
import com.alhasawi.acekuwait.utils.DateTimeUtils;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class OrderConfirmationFragment extends BaseFragment {
    FragmentOrderConfirmationBinding fragmentOrderConfirmationBinding;
    DashboardActivity dashboardActivity;
    OrderViewModel orderViewModel;
    OrderResponse orderConfirmedResponse;
    String orderedDate = "";
    String paymentId = null, paymentModeString = "";
    PaymentMode selectedPaymentMode;
    private CheckoutFragment checkoutFragmentInstance;
    private String languageId = "";

    public OrderConfirmationFragment(CheckoutFragment checkoutFragment) {
        this.checkoutFragmentInstance = checkoutFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_confirmation;
    }

    @Override
    protected void setup() {

        fragmentOrderConfirmationBinding = (FragmentOrderConfirmationBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        dashboardActivity.handleActionMenuBar(false, true);
        dashboardActivity.handleActionBarIcons(false);
        //dashboardActivity.clearTopFragments();
        checkoutFragmentInstance.handleTabView("review");
        try {
            Bundle bundle = getArguments();
            paymentModeString = bundle.getString("payment");
            paymentId = bundle.getString("payment_id");
            String paymentUrl = bundle.getString("payment_url");
            Gson gson = new Gson();
            selectedPaymentMode = gson.fromJson(paymentModeString, PaymentMode.class);
            if (selectedPaymentMode != null) {
                if (selectedPaymentMode.isPostPay()) {
                    callReviewOrderApi();
                } else {
                    callPaymentSuccessApi(paymentUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentOrderConfirmationBinding.layoutOrderConfirmation.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHome();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                redirectToHome();
            }
        });
    }

    public void redirectToHome() {
        dashboardActivity.clearTopFragments();
        dashboardActivity.onLocaleChanged();
    }

    private void setUiValues() {
        try {
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvOrderId.setText(orderConfirmedResponse.getOrderData().getOrderId());
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvPaymentMode.setText(orderConfirmedResponse.getOrderData().getPayment().getName());
            String strDouble = String.format("%.3f", orderConfirmedResponse.getOrderData().getTotal());
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvOrderAmount.setText("KWD " + strDouble);
            try {
                fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvModeOfDelivery.setText(orderConfirmedResponse.getOrderData().getOrderProducts().get(0).getShippingMode().getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.recyclerViewOrderedProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            CheckoutProductListAdapter checkoutProductListAdapter = new CheckoutProductListAdapter((ArrayList<OrderProduct>) orderConfirmedResponse.getOrderData().getOrderProducts(), getActivity());
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.recyclerViewOrderedProducts.setAdapter(checkoutProductListAdapter);
            try {
                orderedDate = DateTimeUtils.changeDateFormatFromAnother(orderConfirmedResponse.getOrderData().getDateOfPurchase());
//                String formattedString = changeDateFormatFromAnother(orderedDate);
                fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvOrderDate.setText(orderedDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvTransactionId.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getTransactionId());
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse() != null) {
//                fragmentOrderConfirmationBinding.layoutOrderReview.layoutOrderConfirmation.tvPaymentDate.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getTransactionDate());

                fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvOrderAmount.setText("KWD " + strDouble);
//                fragmentOrderConfirmationBinding.layoutOrderReview.layoutOrderConfirmation.tvReferenceNo.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getReferenceId());
//                fragmentOrderConfirmationBinding.layoutOrderReview.layoutOrderConfirmation.tvTransactionId.setText(orderConfirmedResponse.getOrderData().getGetPaymentStatusResponse().getInvoiceTransactions().get(0).getTransactionId());
            }
            try {
                InAppEvents.logOrderConfirmedEvent(
                        InAppEvents.cartItemsList,
                        orderConfirmedResponse.getOrderData().getTotal(),
                        orderConfirmedResponse.getOrderData().getOrderId()
                );
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                InAppEvents.logPurchaseEvent(InAppEvents.cartItemsList, orderConfirmedResponse.getOrderData().getTotal(), orderConfirmedResponse.getOrderData().getOrderId());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                InAppEvents.logRevenueEvent(orderConfirmedResponse.getOrderData().getTotal());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            dashboardActivity.setCartBadgeNumber(0);
            fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvPaymentMode.setText(orderConfirmedResponse.getOrderData().getPaymentType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callPaymentSuccessApi(String url) {
        fragmentOrderConfirmationBinding.cvProgressbar.setVisibility(View.VISIBLE);
        orderViewModel.getPaymentSuccess(url).observe(getActivity(), paymentResponseResource -> {
            fragmentOrderConfirmationBinding.cvProgressbar.setVisibility(View.GONE);
            switch (paymentResponseResource.status) {
                case SUCCESS:
                    if (paymentResponseResource.data.getStatusCode() == 200) {
                        paymentId = paymentResponseResource.data.getData().getInvoiceTransactions().get(0).getPaymentId();
                        try {
                            callReviewOrderApi();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), paymentResponseResource.data.getMessage());
                            generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", paymentResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
    }

    private void callReviewOrderApi() throws JSONException {
        JSONObject paymentJson = new JSONObject(paymentModeString);
        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("payment", paymentJson);
        jsonParams.put("paymentId", paymentId);
        fragmentOrderConfirmationBinding.cvProgressbar.setVisibility(View.VISIBLE);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");

        orderViewModel.orderConfirmation(customerId, cartId, sessionToken, jsonParams, languageId).observe(getActivity(), orderResponseResource -> {
            fragmentOrderConfirmationBinding.cvProgressbar.setVisibility(View.GONE);
            switch (orderResponseResource.status) {
                case SUCCESS:
                    if (orderResponseResource.data.getStatusCode() == 200) {
                        orderConfirmedResponse = orderResponseResource.data;
                        setUiValues();
                        if (orderConfirmedResponse.getOrderData().getPayment().getName().equalsIgnoreCase("cod") ||
                                orderConfirmedResponse.getOrderData().getPayment().getPaymentId().equalsIgnoreCase("1")) {
                            fragmentOrderConfirmationBinding.layoutOrderConfirmation.tvPaymentMode.setText("Cash on Delivery");
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", orderResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", orderResponseResource.message);
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
//                        Toast.makeText(dashboardActivity, orderResponseResource.data.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.GONE);
        dashboardActivity.handleActionMenuBar(false, true);
    }
}
