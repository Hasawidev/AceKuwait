package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.FragmentOrderHistoryBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.OrderHistoryAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.OrderHistoryViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderHistoryFragment extends BaseFragment {
    FragmentOrderHistoryBinding fragmentOrderHistoryBinding;
    OrderHistoryViewModel orderHistoryViewModel;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;
    DashboardActivity dashboardActivity;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_history;
    }

    @Override
    protected void setup() {
        fragmentOrderHistoryBinding = (FragmentOrderHistoryBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentOrderHistoryBinding.recyclerviewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        callOrderHistoryApi(userId, sessionToken);

        orderHistoryAdapter = new OrderHistoryAdapter(getActivity()) {
            @Override
            public void onCancelOrderClicked(Order order) {

            }

            @Override
            public void onProductClicked(Order order, OrderProduct product) {
                try {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String orderObject = gson.toJson(order);
                    bundle.putString("order_object", orderObject);
                    String orderProduct = gson.toJson(product);
                    bundle.putString("order_product", orderProduct);
                    dashboardActivity.handleActionMenuBar(false, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderHistoryDetailFragment(), bundle, true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        fragmentOrderHistoryBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
        fragmentOrderHistoryBinding.layoutNoOrders.btnShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.clearTopFragments();
            }
        });
    }

    private void callOrderHistoryApi(String userId, String sessionToken) {
        fragmentOrderHistoryBinding.progressBar.setVisibility(View.VISIBLE);
        orderHistoryViewModel.getOrderHistory(userId, sessionToken, languageId).observe(this, orderHistoryResponseResource -> {
            switch (orderHistoryResponseResource.status) {
                case SUCCESS:
                    if (orderHistoryResponseResource.data.getStatusCode() == 200) {
                        if (orderHistoryResponseResource.data != null) {
                            orderArrayList = (ArrayList<Order>) orderHistoryResponseResource.data.getOrders();
                            if (orderArrayList == null || orderArrayList.size() == 0) {
                                fragmentOrderHistoryBinding.lvNoOrders.setVisibility(View.VISIBLE);
                            } else {
                                fragmentOrderHistoryBinding.lvNoOrders.setVisibility(View.GONE);
                                orderHistoryAdapter.addAll(orderArrayList);
                                fragmentOrderHistoryBinding.recyclerviewOrders.setAdapter(orderHistoryAdapter);
                            }

                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", orderHistoryResponseResource.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", orderHistoryResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentOrderHistoryBinding.progressBar.setVisibility(View.GONE);
        });

    }
}
