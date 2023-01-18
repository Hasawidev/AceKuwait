package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.Resource;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.data.api.response.CartResponse;
import com.alhasawi.acekuwait.databinding.FragmentOrderSummaryBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.ConfirmedCartAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.OrderSummaryDeliveryModeAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.CartViewModel;
import com.alhasawi.acekuwait.ui.main.viewmodel.CheckoutViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryFragment extends BaseFragment {

    FragmentOrderSummaryBinding fragmentOrderSummaryBinding;
    CheckoutViewModel checkoutViewModel;
    ConfirmedCartAdapter confirmedCartAdapter;
    DashboardActivity dashboardActivity;
    CartViewModel cartViewModel;
    CartResponse cartResponse;
    double CarttotalPrice = 0;
    List<ShoppingCartItem> cartItemsList = new ArrayList<>();
    List<ShoppingCartItem> availableItemList = new ArrayList<>();
    List<ShoppingCartItem> outOfStockItemList = new ArrayList<>();
    List<ShippingMode> deliverModeList = new ArrayList<>();
    String languageId = "";
    private String userID;
    private String sessionToken;
    private String username;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_summary;
    }

    @Override
    protected void setup() {
        fragmentOrderSummaryBinding = (FragmentOrderSummaryBinding) viewDataBinding;
        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bundle bundle = getArguments();
            Gson gson = new Gson();
            confirmedCartAdapter = new ConfirmedCartAdapter(dashboardActivity) {
                @Override
                public void onCartItemClicked(ShoppingCartItem cartItem) {

                }
            };

            setUiValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentOrderSummaryBinding.recyclerviewDeliverymodes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        fragmentOrderSummaryBinding.recyclerviewCartItems.setLayoutManager(mLayoutManager);
        fragmentOrderSummaryBinding.recyclerviewCartItems.setItemAnimator(new DefaultItemAnimator());
        fragmentOrderSummaryBinding.recyclerviewCartItems.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        fragmentOrderSummaryBinding.recyclerviewCartItems.setAdapter(confirmedCartAdapter);
        callMyCartApi();

        fragmentOrderSummaryBinding.cvProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InAppEvents.logProceedCheckOutEvent(cartItemsList, CarttotalPrice);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                dashboardActivity.handleActionMenuBar(false, true);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new CheckoutFragment(), null, true, false);
            }
        });
        fragmentOrderSummaryBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    private void setUiValues() {
        String strTotal = String.format("%.3f", cartResponse.getCartData().getTotal());
        fragmentOrderSummaryBinding.tvTotalAmount.setText("KWD " + strTotal);
        fragmentOrderSummaryBinding.tvTotal.setText("KWD " + strTotal);
        String strSubTotal = String.format("%.3f", cartResponse.getCartData().getSubTotal());
        fragmentOrderSummaryBinding.tvSubtotal.setText("KWD " + strSubTotal);
    }

    public void callMyCartApi() {
        fragmentOrderSummaryBinding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.getCartItems(userID, sessionToken, languageId, false).observe(this, cartResponse -> {
            switch (cartResponse.status) {
                case SUCCESS:
                    if (cartResponse.data.getStatusCode() == 200) {
                        updateCartValues(cartResponse);
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", cartResponse.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", cartResponse.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentOrderSummaryBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void updateCartValues(Resource<CartResponse> cartResponse) {
        try {
            cartItemsList = new ArrayList<>();
            deliverModeList = new ArrayList<>();
            availableItemList = cartResponse.data.getCartData().getAvailable();
            for (int i = 0; i < availableItemList.size(); i++) {
                availableItemList.get(i).setOutOfStock(false);
                deliverModeList.add(availableItemList.get(i).getShippingMode());
                cartItemsList.add(availableItemList.get(i));
            }
            outOfStockItemList = cartResponse.data.getCartData().getOutOfStock();
            for (int i = 0; i < outOfStockItemList.size(); i++) {
                outOfStockItemList.get(i).setOutOfStock(true);
                cartItemsList.add(outOfStockItemList.get(i));
                deliverModeList.add(outOfStockItemList.get(i).getShippingMode());
            }

            confirmedCartAdapter.addAll(cartItemsList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CarttotalPrice = cartResponse.data.getCartData().getTotal();
            String totalPrice = String.format("%.3f", cartResponse.data.getCartData().getTotal());
            confirmedCartAdapter.addAll(cartItemsList);
            fragmentOrderSummaryBinding.tvTotalAmount.setText("KWD " + totalPrice);
            fragmentOrderSummaryBinding.tvTotal.setText("KWD " + totalPrice);
            String strSubTotal = String.format("%.3f", cartResponse.data.getCartData().getSubTotal());
            fragmentOrderSummaryBinding.tvSubtotal.setText("KWD " + strSubTotal);

            String strShipping = String.format("%.3f", cartResponse.data.getCartData().getShippingCharges());
            fragmentOrderSummaryBinding.tvShippingCharge.setText("KWD " + strShipping);
            String strDiscounted = String.format("%.3f", cartResponse.data.getCartData().getDiscountedAmount());
            fragmentOrderSummaryBinding.tvSavings.setText("KWD " + strDiscounted);
            OrderSummaryDeliveryModeAdapter orderSummaryDeliveryModeAdapter = new OrderSummaryDeliveryModeAdapter(dashboardActivity, (ArrayList<ShippingMode>) deliverModeList);
            fragmentOrderSummaryBinding.recyclerviewDeliverymodes.setAdapter(orderSummaryDeliveryModeAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
