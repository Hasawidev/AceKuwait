package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.data.api.model.pojo.Store;
import com.alhasawi.acekuwait.data.api.model.pojo.StoreInventory;
import com.alhasawi.acekuwait.databinding.FragmentStorePickupBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.adapters.PickupStoresAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StorePickupFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener, PickupStoresAdapter.StoreSelectedListener {
    FragmentStorePickupBinding fragmentStorePickupBinding;
    DashboardActivity dashboardActivity;
    PickupStoresAdapter pickupStoresAdapter;
    List<StoreInventory> storeList = new ArrayList<>();
    Bundle bundle;
    Gson gson;
    private Store selectedStore;
    private String selectedPickupDate = "", timeSlot = "";
    private ShoppingCartItem currentSelectedProduct;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_store_pickup;
    }

    @Override
    protected void setup() {

        fragmentStorePickupBinding = (FragmentStorePickupBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        try {
            bundle = getArguments();
            gson = new Gson();
            String selectedItemString = bundle.getString("selected_cart_item");
            currentSelectedProduct = gson.fromJson(selectedItemString, ShoppingCartItem.class);
            storeList = currentSelectedProduct.getProductConfigurable().getStoreInventoryList();
            if (storeList.size() > 0) {
                selectedStore = storeList.get(0).getStore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        pickupStoresAdapter = new PickupStoresAdapter(dashboardActivity, storeList, this);
        pickupStoresAdapter.setOnItemClickListener(this);
        fragmentStorePickupBinding.recyclerviewStores.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentStorePickupBinding.recyclerviewStores.setAdapter(pickupStoresAdapter);

        fragmentStorePickupBinding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InAppEvents.logStoreNameEvent(selectedStore.getStoreName());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                currentSelectedProduct.setStore(selectedStore);
                currentSelectedProduct.setPickupTime(timeSlot);
                bundle.putString("pickup_date", selectedPickupDate);
                bundle.putString("selected_cart_item", gson.toJson(currentSelectedProduct));
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), bundle, false, false);
            }
        });
    }

    @Override
    public void onItemClickListener(int position, View view) {
        pickupStoresAdapter.selectedItem();
        selectedStore = storeList.get(position).getStore();
    }

    @Override
    public void onResume() {
        super.onResume();
//        PickupStoresAdapter.setsSelected(-1);
    }

    @Override
    public void onStoreSelected(StoreInventory storeInventory, String pickupdate, String slot) {
        selectedPickupDate = pickupdate;
        timeSlot = slot;
    }
}
