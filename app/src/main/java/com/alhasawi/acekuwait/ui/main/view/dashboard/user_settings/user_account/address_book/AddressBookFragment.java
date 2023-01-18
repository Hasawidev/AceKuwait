package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.databinding.FragmentAddressbookBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.ShippingAddressAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.AddShippingAddressFragment;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns.ReturnAddressFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.ShippingAddressViewModel;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddressBookFragment extends BaseFragment implements RecyclerviewSingleChoiceClickListener {
    FragmentAddressbookBinding fragmentAddressbookBinding;
    ShippingAddressAdapter shippingAddressAdapter;
    List<Address> addressList = new ArrayList<>();
    DashboardActivity dashboardActivity;
    Address selectedAddress;
    ShippingAddressViewModel shippingAddressViewModel;
    String userId = "", token = "";
    private boolean isFromDashboard = false;
    private boolean isFromCart = false;
    private boolean isFromReturns = false;
    private String languageId = "";
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_addressbook;
    }

    @Override
    protected void setup() {
        fragmentAddressbookBinding = (FragmentAddressbookBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        shippingAddressViewModel = new ViewModelProvider(this).get(ShippingAddressViewModel.class);
        fragmentAddressbookBinding.recyclerviewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        token = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("is_from_dashboard"))
            isFromDashboard = bundle.getBoolean("is_from_dashboard");
        if (bundle != null && bundle.containsKey("redirecting_from_cart"))
            isFromCart = bundle.getBoolean("redirecting_from_cart");
        if (bundle != null && bundle.containsKey("is_from_return"))
            isFromReturns = bundle.getBoolean("is_from_return");

        fragmentAddressbookBinding.cvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddressFragment();
            }
        });

        fragmentAddressbookBinding.layoutNoAddress.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddressFragment();
            }
        });
        fragmentAddressbookBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
        fragmentAddressbookBinding.btnConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAddress != null) {
                    if (bundle != null) {
                        try {
                            String selectedCartItemString = bundle.getString("selected_cart_item");
                            ShoppingCartItem selectedCartItem = new Gson().fromJson(selectedCartItemString, ShoppingCartItem.class);
                            selectedCartItem.setAddress(selectedAddress);
                            bundle.putString("selected_cart_item", new Gson().toJson(selectedCartItem));
                            String tagData = "Building Type. : " + selectedAddress.getBuildingType() + ", Area : " + selectedAddress.getArea() + ", Street : " + selectedAddress.getStreet() + ", Building No : " + selectedAddress.getBuildingNo() + ", Flat : " + selectedAddress.getFlat();
                            //InAppEvents.logTagAddress(tagData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (isFromReturns)
                        returnSelectedAddress(selectedAddress);
                    else
                        dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), bundle, true, false);

                } else {
                    GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), getResources().getString(R.string.select_address_to_proceed));
                    generalDialog.show(getParentFragmentManager(), "GENERAL DIALOG");
                }
            }
        });
    }

    private void callGetAddressApi(String userId, String sessionToken) {
        fragmentAddressbookBinding.progressBar.setVisibility(View.VISIBLE);
        shippingAddressViewModel.getAddresses(userId, sessionToken, languageId).observe(getActivity(), getAllAddressResponseResource -> {
            switch (getAllAddressResponseResource.status) {
                case SUCCESS:
                    try {
                        if (getAllAddressResponseResource.data.getStatusCode() == 200) {
                            addressList = getAllAddressResponseResource.data.getData().getAddresses();
                            if (addressList == null || addressList.size() == 0) {
                                fragmentAddressbookBinding.constrainLayoutNoData.setVisibility(View.VISIBLE);
                            } else {
                                if (addressList.size() > 0) {
                                    fragmentAddressbookBinding.btnConfirmAddress.setVisibility(View.VISIBLE);
                                    selectedAddress = addressList.get(0);
                                } else
                                    fragmentAddressbookBinding.btnConfirmAddress.setVisibility(View.GONE);
                                fragmentAddressbookBinding.constrainLayoutNoData.setVisibility(View.GONE);
                                shippingAddressAdapter = new ShippingAddressAdapter(getContext(), (ArrayList<Address>) addressList, AppConstants.ADDRESS_VIEW_TYPE_ADDRESSBOOK) {
                                    @Override
                                    public void onEditClicked(Address address) {
                                        dashboardActivity.handleActionMenuBar(true, false);
                                        launchEditAddressFragment(address);
                                        /*if (isFromDashboard) {
                                            launchEditAddressFragment(address);
                                        } else {
                                            AddShippingAddressFragment addShippingAddressFragment = new AddShippingAddressFragment();
                                            String TAG = "ADD_ADDRESS";
                                            addShippingAddressFragment.setEditAddress(address, false);
                                            changeFragment(addShippingAddressFragment, TAG, "Add Address");
                                        }*/
                                    }

                                    @Override
                                    public void onDeleteClicked(Address address) {
                                        callDeleteAddressApi(address);
                                    }
                                };
                                shippingAddressAdapter.setOnItemClickListener(this);
                                fragmentAddressbookBinding.recyclerviewAddress.setAdapter(shippingAddressAdapter);
                            }
                        } else {
                            GeneralDialog generalDialog = new GeneralDialog("Error", getAllAddressResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", getAllAddressResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentAddressbookBinding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onItemClickListener(int position, View view) {
        shippingAddressAdapter.selectedItem();
        selectedAddress = addressList.get(position);
//        if (!isFromCart) {
//            try {
//                returnSelectedAddress(addressList.get(position));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        ShippingAddressAdapter.setsSelected(-1);
        callGetAddressApi(userId, token);
    }

    private void returnSelectedAddress(Address selectedAddress) {
        ReturnAddressFragment returnAddressFragment = (ReturnAddressFragment) getTargetFragment();
        returnAddressFragment.setSelectedAddress(selectedAddress);
    }

    private void callDeleteAddressApi(Address address) {
        fragmentAddressbookBinding.progressBar.setVisibility(View.VISIBLE);
        shippingAddressViewModel.deleteAddress(address.getAddressId(), token, languageId).observe(this, deleteAddressResponseResource -> {
            switch (deleteAddressResponseResource.status) {
                case SUCCESS:
                    if (deleteAddressResponseResource.data.getStatusCode() == 200) {
                        callGetAddressApi(userId, token);
                        Toast.makeText(dashboardActivity, "Deleted Address Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", deleteAddressResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }
                    Log.e("response", gson.toJson(deleteAddressResponseResource));

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", deleteAddressResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentAddressbookBinding.progressBar.setVisibility(View.GONE);
        });

    }

    public void changeFragment(BaseFragment baseFragment, String TAG, String screenName) {
        dashboardActivity.handleActionMenuBar(true, false);

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_replacer_address_checkout, baseFragment, TAG)
                .addToBackStack(TAG)
                .commit();
    }

    private void launchAddressFragment() {
        AddShippingAddressFragment addShippingAddressFragment = new AddShippingAddressFragment();
        addShippingAddressFragment.setTargetFragment(this, 201);
        fragmentAddressbookBinding.btnConfirmAddress.setVisibility(View.GONE);
        dashboardActivity.handleActionMenuBar(true, false);
        dashboardActivity.replaceFragment(R.id.fragment_replacer_address, addShippingAddressFragment, null, true, false);

    }

    private void launchEditAddressFragment(Address address) {
        Gson gson = new Gson();
        Bundle bundle = new Bundle();
        bundle.putString("address", gson.toJson(address));
        AddShippingAddressFragment addShippingAddressFragment = new AddShippingAddressFragment();
        addShippingAddressFragment.setTargetFragment(this, 201);
        dashboardActivity.replaceFragment(R.id.fragment_replacer, addShippingAddressFragment, bundle, true, false);

    }

    public void setAddress(Address address) {
        callGetAddressApi(userId, token);
    }
}
