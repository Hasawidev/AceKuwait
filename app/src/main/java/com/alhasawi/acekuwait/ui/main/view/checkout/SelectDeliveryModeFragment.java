package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.databinding.FragmentSelectDeliveryModeBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.DeliveryModeAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book.AddressBookFragment;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.DefaultDeliveryModeDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SelectDeliveryModeFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentSelectDeliveryModeBinding fragmentSelectDeliveryModeBinding;
    ArrayList<ShippingMode> deliveryModeArrayList = new ArrayList<>();
    DeliveryModeAdapter deliveryModeAdapter;
    DashboardActivity dashboardActivity;
    ShippingMode selectedDeliveryMode;
    ShoppingCartItem currentSelectedProduct;
    DefaultDeliveryModeDialog defaultDeliveryModeDialog;
    Bundle bundle;
    Gson gson;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_delivery_mode;
    }

    @Override
    protected void setup() {

        fragmentSelectDeliveryModeBinding = (FragmentSelectDeliveryModeBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        try {
            bundle = getArguments();
            gson = new Gson();
            String selectedItemString = bundle.getString("selected_cart_item");
            currentSelectedProduct = gson.fromJson(selectedItemString, ShoppingCartItem.class);
            deliveryModeArrayList = (ArrayList<ShippingMode>) currentSelectedProduct.getShippingModeList();
            if (deliveryModeArrayList.size() > 0)
                selectedDeliveryMode = deliveryModeArrayList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryModeAdapter = new DeliveryModeAdapter(getContext(), deliveryModeArrayList);
        fragmentSelectDeliveryModeBinding.recyclerviewDeliveryMode.setLayoutManager(new LinearLayoutManager(getContext()));
        deliveryModeAdapter.setOnItemClickListener(this);
        fragmentSelectDeliveryModeBinding.recyclerviewDeliveryMode.setAdapter(deliveryModeAdapter);
        fragmentSelectDeliveryModeBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedProduct.setShippingMode(selectedDeliveryMode);
                if (selectedDeliveryMode != null)
                    if (selectedDeliveryMode.isPickup()) {
                        bundle.putString("selected_cart_item", gson.toJson(currentSelectedProduct));
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new StorePickupFragment(), bundle, true, false);

                    } else {
                        bundle.putString("selected_cart_item", gson.toJson(currentSelectedProduct));
                        bundle.putBoolean("redirecting_from_cart", true);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddressBookFragment(), bundle, true, false);
                    }

            }
        });

        fragmentSelectDeliveryModeBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    public void showDefaultDeliveryModeDialog() {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        Gson gson = new Gson();
        defaultDeliveryModeDialog = new DefaultDeliveryModeDialog();
        defaultDeliveryModeDialog.setClickListener(new DefaultDeliveryModeDialog.DefaultDeliveryModeInterface() {
            @Override
            public void onNo() {
//                preferenceHandler.saveData(PreferenceHandler.DEFAULT_SHIPPING_MODE, "");
            }

            @Override
            public void onYes() {
                try {
//                    preferenceHandler.saveData(PreferenceHandler.DEFAULT_SHIPPING_MODE, gson.toJson(selectedDeliveryMode));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        defaultDeliveryModeDialog.showDialog(dashboardActivity);
    }

    @Override
    public void onItemClickListener(int position, View view) {
        deliveryModeAdapter.selectedItem();
        selectedDeliveryMode = deliveryModeArrayList.get(position);
//        showDefaultDeliveryModeDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        DeliveryModeAdapter.setsSelected(-1);
    }
}
