package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.OrderReturnType;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.OrderProduct;
import com.alhasawi.acekuwait.databinding.FragmentProductReturnBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.AceReturnProductsAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ReturnViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductReturnFragment extends BaseFragment {
    FragmentProductReturnBinding fragmentProductReturnBinding;
    DashboardActivity dashboardActivity;
    AceReturnProductsAdapter returnProductsAdapter;
    Order selectedOrder;
    ReturnViewModel returnViewModel;
    List<String> reasonsList = new ArrayList<>();
    private int currentAdapterPosition = 0;
    private String languageId = "";
    private List<OrderProduct> selectedReturnList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_return;
    }

    @Override
    protected void setup() {

        fragmentProductReturnBinding = (FragmentProductReturnBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        returnViewModel = new ViewModelProvider(this).get(ReturnViewModel.class);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String objString = bundle.getString("selected_order");
                Gson gson = new Gson();
                selectedOrder = gson.fromJson(objString, Order.class);
                getReturnReasons();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentProductReturnBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedReturnList.size() == 0) {
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), getContext().getResources().getString(R.string.select_any_product));
                    generalDialog.show(getParentFragmentManager(), "GENERAL DIALOG");
                } else {
                    Gson gson = new Gson();
                    String objectString = gson.toJson(selectedOrder);
                    Bundle bundle = new Bundle();
                    bundle.putString("selected_order", objectString);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new SelectRefundModeFragment(), bundle, true, false);
                }
            }
        });

    }

    private void getReturnReasons() {
        fragmentProductReturnBinding.progressBar.setVisibility(View.VISIBLE);
        returnViewModel.getReturnReasons(languageId).observe(this, returnReasonResponseResource -> {
            switch (returnReasonResponseResource.status) {
                case SUCCESS:
                    if (returnReasonResponseResource.data.getStatusCode() == 200) {
                        reasonsList = returnReasonResponseResource.data.getReturnReasons();
                        setUiValues();
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", returnReasonResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", returnReasonResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    break;
            }
            fragmentProductReturnBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void setUiValues() {
        returnProductsAdapter = new AceReturnProductsAdapter(getContext(), selectedOrder.getOrderProducts(), reasonsList) {
            @Override
            public void onReturnClicked(List<OrderProduct> selectedProductList) {
                selectedReturnList = selectedProductList;
                for (int i = 0; i < selectedProductList.size(); i++) {
                    for (int j = 0; j < selectedOrder.getOrderProducts().size(); j++) {
                        if (selectedProductList.get(i).getOrderProductId().equalsIgnoreCase(selectedOrder.getOrderProducts().get(j).getOrderProductId())) {
                            selectedOrder.getOrderProducts().set(j, selectedProductList.get(i));
                            break;
                        }
                    }
                }
                if (selectedProductList.size() == 0)
                    selectedOrder.setOrderReturnType("");
                else {
                    if (selectedOrder.getOrderProducts().size() == selectedProductList.size())
                        selectedOrder.setOrderReturnType(OrderReturnType.FULL.name());
                    else
                        selectedOrder.setOrderReturnType(OrderReturnType.PARTIAL.name());
                }

            }

            @Override
            public void onEditAddressClicked(Address address, OrderProduct product, int adapterPosition) {
                currentAdapterPosition = adapterPosition;
                launchAddressFragment(address, true);


            }

            @Override
            public void onAddNewAddressClicked(OrderProduct product, int adapterPosition) {
                currentAdapterPosition = adapterPosition;
                launchAddressFragment(null, false);

            }
        };
        fragmentProductReturnBinding.recyclerviewProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentProductReturnBinding.recyclerviewProducts.setAdapter(returnProductsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.setTitle("My Returns");
    }

    public void setSelectedAddress(Address currentSelectedAddress) {
        selectedOrder.getOrderProducts().get(currentAdapterPosition).setAddress(currentSelectedAddress);
        returnProductsAdapter.notifyItemChanged(currentAdapterPosition);
    }

    public void launchAddressFragment(Address address, boolean isEdit) {
        ReturnAddressFragment returnAddressFragment = new ReturnAddressFragment();
        Bundle bundle = new Bundle();
        if (isEdit) {
            if (address != null) {
                bundle.putString("address", new Gson().toJson(address));
                returnAddressFragment.setArguments(bundle);
            }
        } else
            bundle = null;

        returnAddressFragment.setTargetFragment(this, 122);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, returnAddressFragment, bundle, true, false);
    }
}
