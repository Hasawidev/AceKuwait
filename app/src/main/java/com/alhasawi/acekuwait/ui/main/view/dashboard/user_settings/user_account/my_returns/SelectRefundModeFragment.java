package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.OrderReturnType;
import com.alhasawi.acekuwait.data.api.model.RefundPaymentMode;
import com.alhasawi.acekuwait.data.api.model.ReturnPaymentType;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.databinding.FragmentSelectRefundModeBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.RefundModeAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ReturnViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SelectRefundModeFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentSelectRefundModeBinding fragmentSelectRefundModeBinding;
    List<RefundPaymentMode> paymentModeList = new ArrayList<>();
    RefundModeAdapter refundModeAdapter;
    DashboardActivity dashboardActivity;
    String selectedRefundMode;
    Order selectedOrder;
    ReturnViewModel returnViewModel;
    Bundle returnbundle;
    double totalRefundAmount = 0;
    private String objString;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_refund_mode;
    }

    @Override
    protected void setup() {

        fragmentSelectRefundModeBinding = (FragmentSelectRefundModeBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);
        try {
            returnbundle = getArguments();
            if (returnbundle != null) {
                Gson gson = new Gson();
                objString = returnbundle.getString("selected_order");
                selectedOrder = gson.fromJson(objString, Order.class);
                if (selectedOrder != null)
                    if (selectedOrder.getOrderReturnType().equals(OrderReturnType.FULL))
                        totalRefundAmount = selectedOrder.getTotal();
                    else if (selectedOrder.getOrderReturnType().equals(OrderReturnType.PARTIAL)) {
                        for (int i = 0; i < selectedOrder.getOrderProducts().size(); i++) {
                            if (selectedOrder.getOrderProducts().get(i).isProductReturn())
                                totalRefundAmount += selectedOrder.getOrderProducts().get(i).getAmount();
                        }
                    }
                String strTotal = String.format("%.3f", totalRefundAmount);
                fragmentSelectRefundModeBinding.tvRefundAmount.setText("KWD " + strTotal);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        paymentModeList.add(new RefundPaymentMode("Original mode of payment", "The refund will be creadited with respect to the mode of payment used while placing the order"));
        paymentModeList.add(new RefundPaymentMode("KNET", "The refund will be credited to your bank account"));
        paymentModeList.add(new RefundPaymentMode("Store credit", "The refund will be credited to your store credit wallet"));

        refundModeAdapter = new RefundModeAdapter(getContext(), paymentModeList);
        refundModeAdapter.setOnItemClickListener(this);
        fragmentSelectRefundModeBinding.recyclerviewPaymentmode.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentSelectRefundModeBinding.recyclerviewPaymentmode.setAdapter(refundModeAdapter);


        fragmentSelectRefundModeBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (selectedRefundMode) {
                    case "ORIGINAL_MODE_OF_PAYMENT":
                        if (selectedOrder.getPaymentType().equalsIgnoreCase("cod"))
                            callOrderReturnApi();
                        else if (selectedOrder.getPaymentType().equalsIgnoreCase("knet"))
                            dashboardActivity.replaceFragment(R.id.fragment_replacer, new RefundPaymentDetailsFragment(), returnbundle, true, false);
                        break;
                    case "STORE_CREDIT":
                        callOrderReturnApi();
                        break;
                    case "KNET":
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new RefundPaymentDetailsFragment(), returnbundle, true, false);
                        break;
                    default:
                        break;
                }

            }
        });

        fragmentSelectRefundModeBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    @Override
    public void onItemClickListener(int position, View view) {
        refundModeAdapter.selectedItem();
        switch (position) {
            case 0:
                selectedRefundMode = ReturnPaymentType.ORIGINAL_MODE_OF_PAYMENT.name();
                selectedOrder.setReturnPayment(ReturnPaymentType.ORIGINAL_MODE_OF_PAYMENT);
                break;
            case 1:
                selectedRefundMode = ReturnPaymentType.STORE_CREDIT.name();
                selectedOrder.setReturnPayment(ReturnPaymentType.STORE_CREDIT);
                break;
            case 2:
                selectedRefundMode = ReturnPaymentType.KNET.name();
                selectedOrder.setReturnPayment(ReturnPaymentType.KNET);
                break;
            default:
                break;
        }
    }

    private void callOrderReturnApi() {
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
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new ConfirmReturnFragment(), bundle, true, false);
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
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", orderReturnResponseResource.data.getMessage());
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
//            fragmentSelectRefundModeBinding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        RefundModeAdapter.setsSelected(-1);
        dashboardActivity.setTitle("My Returns");
    }
}
