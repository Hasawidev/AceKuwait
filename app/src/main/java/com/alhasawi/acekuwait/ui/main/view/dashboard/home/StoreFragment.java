package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.AceStore;
import com.alhasawi.acekuwait.databinding.FragmentSelectStoreBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.StoreAdapter;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.StoreViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;

import java.util.ArrayList;

public class StoreFragment extends BaseFragment {
    FragmentSelectStoreBinding fragmentSelectStoreBinding;
    ArrayList<AceStore> storeArrayList = new ArrayList<>();
    StoreAdapter storeAdapter;
    StoreViewModel storeViewModel;
    DashboardActivity dashboardActivity;
    private String sessionToken = "", langaugeId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_select_store;
    }

    @Override
    protected void setup() {
        fragmentSelectStoreBinding = (FragmentSelectStoreBinding) viewDataBinding;
        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        langaugeId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        dashboardActivity = (DashboardActivity) getActivity();
        fragmentSelectStoreBinding.recyclerviewStores.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        callStoreApi();


    }

    private void callStoreApi() {
        fragmentSelectStoreBinding.progressBar.setVisibility(View.VISIBLE);
        storeViewModel.getStoreList(langaugeId, sessionToken).observe(getActivity(), storeResponseResource -> {
            switch (storeResponseResource.status) {
                case SUCCESS:
                    try {
                        if (storeResponseResource.data.getStatusCode() == 200) {
                            storeArrayList.clear();
                            storeArrayList = (ArrayList<AceStore>) storeResponseResource.data.getStoreList();
                            storeAdapter = new StoreAdapter(getContext(), storeArrayList) {
                                @Override
                                public void onStoreClicked(AceStore aceStore) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("store_name", aceStore.getStoreName());
                                    bundle.putString("store_url", aceStore.getLocationUrl());
                                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new StoreWebviewFragment(), bundle, true, false);

                                }
                            };
                            fragmentSelectStoreBinding.recyclerviewStores.setAdapter(storeAdapter);
                        } else {
                            try {
                                GeneralDialog generalDialog = new GeneralDialog("Error", storeResponseResource.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", storeResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentSelectStoreBinding.progressBar.setVisibility(View.GONE);
        });
    }
}
