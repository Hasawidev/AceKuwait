package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.databinding.FragmentRefundPaymentDetailsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ReturnViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

public class RefundPaymentDetailsFragment extends BaseFragment {
    FragmentRefundPaymentDetailsBinding fragmentRefundPaymentDetailsBinding;
    DashboardActivity dashboardActivity;
    Bundle returnBundle;
    ReturnViewModel returnViewModel;
    private Order selectedOrder;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_refund_payment_details;
    }

    @Override
    protected void setup() {

        fragmentRefundPaymentDetailsBinding = (FragmentRefundPaymentDetailsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);
        returnBundle = getArguments();
        try {
            returnBundle = getArguments();
            if (returnBundle != null) {
                Gson gson = new Gson();
                String objString = returnBundle.getString("selected_order");
                selectedOrder = gson.fromJson(objString, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentRefundPaymentDetailsBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankAccountNumber = fragmentRefundPaymentDetailsBinding.edtBankNumber.getText().toString();
                String IBAN = fragmentRefundPaymentDetailsBinding.edtIban.getText().toString();
                if (selectedOrder != null) {
                    selectedOrder.setBankAccount(bankAccountNumber);
                    selectedOrder.setIban(IBAN);
                }
                callOrderReturnApi();
            }
        });
    }

    private void callOrderReturnApi() {
        fragmentRefundPaymentDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        String orderObject = gson.toJson(selectedOrder);
        PreferenceHandler preferenceHandler = new PreferenceHandler(dashboardActivity, PreferenceHandler.TOKEN_LOGIN);
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        returnViewModel.returnOrder(orderObject, sessionToken, languageId).observe(this, orderReturnResponseResource -> {
            switch (orderReturnResponseResource.status) {
                case SUCCESS:
                    if (orderReturnResponseResource.data.getStatusCode() == 200) {
                        Order order = orderReturnResponseResource.data.getReturnData();
                        String orderString = gson.toJson(order);
                        Bundle bundle = new Bundle();
                        bundle.putString("returned_order", orderString);
                        try {
                            InAppEvents.logRefundEvent();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        try {
                            InAppEvents.logRefundedValueEvent(order.getTotal());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        try {
                            InAppEvents.logRefundedProductsEvent(order);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ConfirmReturnFragment(), bundle, true, false);
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), orderReturnResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", orderReturnResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    break;
            }
            fragmentRefundPaymentDetailsBinding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.setTitle("My Returns");
    }
}
