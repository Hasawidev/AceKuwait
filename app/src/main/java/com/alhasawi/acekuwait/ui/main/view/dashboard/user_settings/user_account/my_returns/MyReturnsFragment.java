package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.databinding.FragmentMyReturnsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.ReturnOrdersAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ReturnViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.List;

public class MyReturnsFragment extends BaseFragment {
    DashboardActivity dashboardActivity;
    FragmentMyReturnsBinding fragmentMyReturnsBinding;
    ReturnOrdersAdapter returnOrdersAdapter;
    ReturnViewModel returnViewModel;
    String languageId = "";
    //    List<Order> orderArrayList = new ArrayList<>();
    private String userId;
    private String sessionToken;
    private List<Order> orderArrayList;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_returns;
    }

    @Override
    protected void setup() {
        fragmentMyReturnsBinding = (FragmentMyReturnsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentMyReturnsBinding.recyclerviewReturns.setLayoutManager(new LinearLayoutManager(getActivity()));
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        returnOrdersAdapter = new ReturnOrdersAdapter(getContext()) {
            @Override
            public void onProductReturn(Order order) {
//                Order selectedOrder = order;
//                Gson gson = new Gson();
//                String objectString = gson.toJson(selectedOrder);
//                Bundle bundle = new Bundle();
//                bundle.putString("selected_order", objectString);
//                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductReturnFragment(), bundle, true, false);

            }

            @Override
            public void onFullOrderReturn(Order order) {
                Order selectedOrder = order;
                Gson gson = new Gson();
                String objectString = gson.toJson(selectedOrder);
                Bundle bundle = new Bundle();
                bundle.putString("selected_order", objectString);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ProductReturnFragment(), bundle, true, false);

            }
        };
        fragmentMyReturnsBinding.recyclerviewReturns.setAdapter(returnOrdersAdapter);
        fragmentMyReturnsBinding.layoutReturnPolicy.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMyReturnsBinding.framelayoutReturnPolicy.setVisibility(View.GONE);
            }
        });
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        getElibileOrders(userId, sessionToken);

    }

    private void getElibileOrders(String userId, String sessionToken) {
        fragmentMyReturnsBinding.progressBar.setVisibility(View.VISIBLE);
        returnViewModel.getEligibleReturnOrders(userId, sessionToken, languageId).observe(this, returnResponseResource -> {
            switch (returnResponseResource.status) {
                case SUCCESS:
                    try {
                        if (returnResponseResource.data.getStatusCode() == 200) {
                            orderArrayList = returnResponseResource.data.getEligibleOrders();
                            if (orderArrayList.size() > 0) {
                                returnOrdersAdapter.addAll(orderArrayList);
                                fragmentMyReturnsBinding.recyclerviewReturns.setAdapter(returnOrdersAdapter);
                                fragmentMyReturnsBinding.lvNoReturnOrders.setVisibility(View.GONE);
                            } else {
                                fragmentMyReturnsBinding.lvNoReturnOrders.setVisibility(View.VISIBLE);
                            }
                            getReturnPolicy();
                        } else {
                            try {
                                GeneralDialog generalDialog = new GeneralDialog("Error", returnResponseResource.data.getMessage());
                                generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        fragmentMyReturnsBinding.lvNoReturnOrders.setVisibility(View.VISIBLE);
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    fragmentMyReturnsBinding.lvNoReturnOrders.setVisibility(View.VISIBLE);
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", returnResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentMyReturnsBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void getReturnPolicy() {
        fragmentMyReturnsBinding.progressBar.setVisibility(View.VISIBLE);
        returnViewModel.getReturnPolicy(languageId).observe(this, returnPolicyResponseResource -> {
            switch (returnPolicyResponseResource.status) {
                case SUCCESS:
                    try {
                        if (returnPolicyResponseResource.data.getStatusCode() == 200) {
                            if (returnPolicyResponseResource.data != null) {
                                String htmlContent = returnPolicyResponseResource.data.getData().getFulfilment();
                                final String mimeType = "text/html";
                                final String encoding = "UTF-8";
                                fragmentMyReturnsBinding.layoutReturnPolicy.webview.loadData(htmlContent, mimeType, encoding);
                            }
                        } else {
                            try {
                                GeneralDialog generalDialog = new GeneralDialog("Error", returnPolicyResponseResource.data.getMessage());
                                generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", returnPolicyResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentMyReturnsBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.setTitle("My Returns");
    }
}
