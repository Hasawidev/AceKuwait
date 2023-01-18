package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.PaymentMode;
import com.alhasawi.acekuwait.databinding.FragmentOrderPaymentBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.PaymentModeAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.PlaceOrderViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderPaymentFragment extends BaseFragment implements View.OnClickListener, RecyclerviewSingleChoiceClickListener {

    FragmentOrderPaymentBinding fragmentOrderPaymentBinding;
    DashboardActivity dashboardActivity;
    String userId, cartId, sessionToken;
    PaymentModeAdapter paymentModeAdapter;
    List<PaymentMode> paymentModeList = new ArrayList<>();
    String couponCode = "";
    double totalPrice = 0;
    private CheckoutFragment checkoutFragmentInstance;
    private LinearLayoutManager horizontalLayoutManager;
    private PaymentMode selectedPaymentMode;
    private boolean hasCouponApplied = false;
    private PlaceOrderViewModel placeOrderViewModel;
    private String languageId = "";

    public OrderPaymentFragment(CheckoutFragment checkoutFragment) {
        this.checkoutFragmentInstance = checkoutFragment;
    }

    public OrderPaymentFragment() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_payment;
    }

    @Override
    protected void setup() {

        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        fragmentOrderPaymentBinding = (FragmentOrderPaymentBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        placeOrderViewModel = new ViewModelProvider(this).get(PlaceOrderViewModel.class);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
            languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        checkoutFragmentInstance.handleTabView("payment");
        fragmentOrderPaymentBinding.btnApply.setOnClickListener(this);
        fragmentOrderPaymentBinding.btnDelete.setOnClickListener(this);
        fragmentOrderPaymentBinding.tvProceedToPay.setOnClickListener(this);
        callCheckoutApi(userId, cartId, "", sessionToken);
        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        fragmentOrderPaymentBinding.recyclerviewPaymentmode.setLayoutManager(horizontalLayoutManager);
    }

    public void callCheckoutApi(String userId, String cartId, String couponCode, String sessionToken) {
        fragmentOrderPaymentBinding.progressBar.setVisibility(View.VISIBLE);
        placeOrderViewModel.checkout(userId, cartId, couponCode, sessionToken, languageId).observe(this, checkoutResponseResource -> {
            switch (checkoutResponseResource.status) {
                case SUCCESS:
                    try {
                        if (checkoutResponseResource.data.getStatusCode() == 200) {
                            if (checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode() == null) {
                                hasCouponApplied = false;
                                fragmentOrderPaymentBinding.btnDelete.setVisibility(View.GONE);
                                fragmentOrderPaymentBinding.edtCouponCode.setText("");
                                fragmentOrderPaymentBinding.btnApply.setVisibility(View.VISIBLE);

                            } else {
                                hasCouponApplied = true;
                                fragmentOrderPaymentBinding.btnDelete.setVisibility(View.VISIBLE);
                                fragmentOrderPaymentBinding.btnApply.setVisibility(View.GONE);
                                dashboardActivity.showMessageToast("Coupon Applied", Toast.LENGTH_SHORT);
//                            logCouponAppliedEvent(checkoutResponseResource);
                                try {
                                    InAppEvents.logPromoCodeEvent(
                                            checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode(),
                                            checkoutResponseResource.data.getCheckoutData().getShoppingCart().getDiscountedAmount()
                                    );
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                try {
                                    InAppEvents.logDiscountedPurchase(checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal(),
                                            checkoutResponseResource.data.getCheckoutData().getShoppingCart().getDiscountedAmount(),
                                            checkoutResponseResource.data.getCheckoutData().getShoppingCart().getCouponCode()
                                    );
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }

                            }
                            fragmentOrderPaymentBinding.edtCouponCode.clearFocus();
                            paymentModeList = new ArrayList<>();
                            paymentModeList = checkoutResponseResource.data.getCheckoutData().getPaymentModes();
                            for (int i = 0; i < paymentModeList.size(); i++) {
                                if (selectedPaymentMode != null)
                                    if (paymentModeList.get(i).getName().equals(selectedPaymentMode.getName())) {
                                        selectedPaymentMode = paymentModeList.get(i);
                                    }

                            }
                            paymentModeAdapter = new PaymentModeAdapter(getContext(), (ArrayList<PaymentMode>) paymentModeList);
                            paymentModeAdapter.setOnItemClickListener(this);
                            fragmentOrderPaymentBinding.recyclerviewPaymentmode.setAdapter(paymentModeAdapter);

                            if (checkoutResponseResource.data.getCheckoutData().getShoppingCart().getDiscountedAmount() != null) {
                                String strDouble = String.format("%.3f", checkoutResponseResource.data.getCheckoutData().getShoppingCart().getDiscountedAmount());
                                fragmentOrderPaymentBinding.tvTotalDiscount.setText("KWD " + strDouble);

                            } else
                                fragmentOrderPaymentBinding.tvTotalDiscount.setText("KWD 0.00");
//                        fragmentOrderPaymentBinding.layoutCheckout.tvItemCount.setText("Bag " + checkoutResponseResource.data.getCheckoutData().getShoppingCart().getAvailable().size());

                            String strSubtotal = String.format("%.3f", checkoutResponseResource.data.getCheckoutData().getShoppingCart().getSubTotal());
                            fragmentOrderPaymentBinding.tvItemTotal.setText("KWD " + strSubtotal);
                            String strTotal = String.format("%.3f", checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal());
                            fragmentOrderPaymentBinding.tvAmountToPay.setText("KWD " + strTotal);
                            totalPrice = checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal();
                            fragmentOrderPaymentBinding.tvAmount.setText("KWD " + strTotal);
                            String strShippingCharge = String.format("%.3f", checkoutResponseResource.data.getCheckoutData().getShoppingCart().getShippingCharges());
                            fragmentOrderPaymentBinding.tvShippingCharge.setText("KWD " + strShippingCharge);
//
//                        logCheckoutEvent(checkoutResponseResource.data.getCheckoutData().getOrderId(), checkoutResponseResource.data.getCheckoutData().getShoppingCart().getTotal(),
//                                checkoutResponseResource.data.getCheckoutData().getShoppingCart().getAvailable().size());

                        } else if (checkoutResponseResource.data.getStatusCode() == 400) {
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), checkoutResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                            hasCouponApplied = false;
                            fragmentOrderPaymentBinding.btnDelete.setVisibility(View.GONE);
                            fragmentOrderPaymentBinding.edtCouponCode.setText("");
                            fragmentOrderPaymentBinding.btnApply.setVisibility(View.VISIBLE);

                        } else {
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), checkoutResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), checkoutResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
        });
        fragmentOrderPaymentBinding.progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onItemClickListener(int position, View view) {
        switch (view.getId()) {
            case R.id.lv_payment_mode_cod:
                selectedPaymentMode = paymentModeList.get(position);
                try {
                    InAppEvents.logPaymentMethodEvent(selectedPaymentMode);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                paymentModeAdapter.selectedItem();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply:
                dashboardActivity.hideSoftKeyboard(dashboardActivity);
                couponCode = fragmentOrderPaymentBinding.edtCouponCode.getText().toString();
                if (couponCode.equals(""))
                    Toast.makeText(dashboardActivity, "Invalid Coupon code", Toast.LENGTH_SHORT).show();
                else
                    callCheckoutApi(userId, cartId, couponCode, sessionToken);
                break;
            case R.id.btn_delete:
                dashboardActivity.hideSoftKeyboard(dashboardActivity);
                couponCode = "";
                dashboardActivity.showMessageToast("Coupon Removed", Toast.LENGTH_SHORT);
                callCheckoutApi(userId, cartId, couponCode, sessionToken);
                break;
            case R.id.tvProceedToPay:
                if (selectedPaymentMode == null) {
                    if (selectedPaymentMode == null)
                        Toast.makeText(dashboardActivity, "Please select any payment mode to proceed.", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    String paymentModeString = gson.toJson(selectedPaymentMode);
                    Bundle bundle = new Bundle();
                    bundle.putString("payment_mode", paymentModeString);
                    bundle.putDouble("total_price", totalPrice);
                    dashboardActivity.handleActionMenuBar(false, true);
                    checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.VISIBLE);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer_address_checkout, new PaymentFragment(checkoutFragmentInstance), bundle, false, false);

                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        PaymentModeAdapter.setsSelected(-1);
        dashboardActivity.setTitle("Checkout");
    }
}
