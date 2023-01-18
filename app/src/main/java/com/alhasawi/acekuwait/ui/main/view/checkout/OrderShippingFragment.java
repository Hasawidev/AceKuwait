package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.databinding.FragmentOrderShippingBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.adapters.ShippingAddressAdapter;
import com.alhasawi.acekuwait.ui.main.adapters.ShippingModeAdapter;
import com.alhasawi.acekuwait.ui.main.listeners.RecyclerviewSingleChoiceClickListener;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book.AddressBookFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.PlaceOrderViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrderShippingFragment extends BaseFragment implements View.OnClickListener, RecyclerviewSingleChoiceClickListener {
    private static final int ADDRESS_BOOK_REQUEST_CODE = 100;
    private static final int ADD_ADDRESS_REQUEST_CODE = 101;
    FragmentOrderShippingBinding fragmentOrderShippingBinding;
    DashboardActivity dashboardActivity;
    String userId, cartId, sessionToken;
    ShippingAddressAdapter shippingAddressAdapter;
    ShippingModeAdapter shippingModeAdapter;
    List<Address> addressList = new ArrayList<>();
    List<ShippingMode> shippingModeList = new ArrayList<>();
    ShippingMode selectedShippingMode;
    String couponCode = "", shippingId = "";
    Bundle analyticsBundle = new Bundle();
    CheckoutFragment checkoutFragmentInstance;
    private LinearLayoutManager horizontalLayoutManagerAddress;
    private Address selectedAddress;
    private boolean hasCouponApplied = false, isEdit = false;
    private PlaceOrderViewModel placeOrderViewModel;
    private String languageId = "";
    private Gson gson = new Gson();

    public OrderShippingFragment(CheckoutFragment checkoutFragment) {
        this.checkoutFragmentInstance = checkoutFragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order_shipping;
    }

    @Override
    protected void setup() {

        fragmentOrderShippingBinding = (FragmentOrderShippingBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        placeOrderViewModel = new ViewModelProvider(this).get(PlaceOrderViewModel.class);
        try {
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            cartId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_CART_ID, "");
            languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkoutFragmentInstance.handleTabView("shipping");
        fragmentOrderShippingBinding.tvAddNewAddress.setOnClickListener(this);
        fragmentOrderShippingBinding.tvProceed.setOnClickListener(this);
        fragmentOrderShippingBinding.cvSelectAddress.setOnClickListener(this);
        callCheckoutApi(userId, cartId, "", sessionToken);
        horizontalLayoutManagerAddress = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        fragmentOrderShippingBinding.recyclerViewAddress.setLayoutManager(horizontalLayoutManagerAddress);
//        callGetAddressApi(userId, sessionToken);
    }

    public void callCheckoutApi(String userId, String cartId, String couponCode, String sessionToken) {
        fragmentOrderShippingBinding.progressBar.setVisibility(View.VISIBLE);
        placeOrderViewModel.checkout(userId, cartId, couponCode, sessionToken, languageId).observe(this, checkoutResponseResource -> {
            switch (checkoutResponseResource.status) {
                case SUCCESS:
                    if (checkoutResponseResource.data.getStatusCode() == 200) {
                        try {
                            fragmentOrderShippingBinding.tvCustomerName.setText(checkoutResponseResource.data.getCheckoutData().getUser().getCustomerFirstName() + " " + checkoutResponseResource.data.getCheckoutData().getUser().getCustomerLastName());
                            shippingModeList.clear();
                            shippingModeList = checkoutResponseResource.data.getCheckoutData().getShippingModes();
                            setShippingModeAdapter(shippingModeList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", checkoutResponseResource.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", checkoutResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
        fragmentOrderShippingBinding.progressBar.setVisibility(View.GONE);
    }

    private void setShippingModeAdapter(List<ShippingMode> shippingModeList) {
        LinearLayoutManager horizontalLayoutManagerShippingMode = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        fragmentOrderShippingBinding.recyclerViewShippingMode.setLayoutManager(horizontalLayoutManagerShippingMode);
        shippingModeAdapter = new ShippingModeAdapter(getContext(), (ArrayList<ShippingMode>) shippingModeList);
        shippingModeAdapter.setOnItemClickListener(this);
        fragmentOrderShippingBinding.recyclerViewShippingMode.setAdapter(shippingModeAdapter);

    }

    @Override
    public void onItemClickListener(int position, View view) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        switch (view.getId()) {
            case R.id.cv_background_address:
                selectedAddress = addressList.get(position);
                shippingAddressAdapter.selectedItem();

                break;
            case R.id.cv_background_shipping_mode:
                shippingModeAdapter.selectedItem();
                selectedShippingMode = shippingModeList.get(position);
                shippingId = selectedShippingMode.getShippingId();
//                callCheckoutApi(userId, cartId, couponCode, sessionToken);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        callGetAddressApi(userId, sessionToken);
        ShippingModeAdapter.setsSelected(-1);
        ShippingAddressAdapter.setsSelected(-1);
        dashboardActivity.setTitle("Checkout");
        checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.GONE);
        if (placeOrderViewModel.isEdit()) {
            fragmentOrderShippingBinding.lvSelectedAddress.setVisibility(View.VISIBLE);
            fragmentOrderShippingBinding.cvSelectAddress.setVisibility(View.GONE);
        }
    }

//    private void callGetAddressApi(String userId, String sessionToken) {
//        fragmentOrderShippingBinding.progressBar.setVisibility(View.VISIBLE);
//        placeOrderViewModel.getAddresses(userId, sessionToken).observe(getActivity(), getAllAddressResponseResource -> {
//            switch (getAllAddressResponseResource.status) {
//                case SUCCESS:
//                    try {
//                        addressList.clear();
//                        addressList = getAllAddressResponseResource.data.getData().getAddresses();
//                        if (addressList == null || addressList.size() == 0) {
//                            fragmentOrderShippingBinding.layoutNoAddress.setVisibility(View.VISIBLE);
////                            fragmentOrderShippingBinding.recyclerViewAddress.setVisibility(View.GONE);
//                        } else {
//                            fragmentOrderShippingBinding.layoutNoAddress.setVisibility(View.GONE);
////                            fragmentOrderShippingBinding.recyclerViewAddress.setVisibility(View.VISIBLE);
//                            shippingAddressAdapter = new ShippingAddressAdapter(getContext(), (ArrayList<Address>) addressList, AppConstants.ADDRESS_VIEW_TYPE_CHECKOUT) {
//                                @Override
//                                public void onEditClicked(Address address) {
//                                    dashboardActivity.handleActionMenuBar(true, false, "");
//                                    Gson gson = new Gson();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("address", gson.toJson(address));
//                                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddShippingAddressFragment(), bundle, true, false);
//                                }
//
//                                @Override
//                                public void onDeleteClicked(Address address) {
//                                    callDeleteAddressApi(address);
//                                }
//                            };
//                            shippingAddressAdapter.setOnItemClickListener(this);
////                            fragmentOrderShippingBinding.recyclerViewAddress.setAdapter(shippingAddressAdapter);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case LOADING:
//                    break;
//                case ERROR:
//                    Toast.makeText(getActivity(), getAllAddressResponseResource.message, Toast.LENGTH_SHORT).show();
//                    break;
//            }
//            fragmentOrderShippingBinding.progressBar.setVisibility(View.GONE);
//        });
//    }

    private void callDeleteAddressApi(Address address) {
        fragmentOrderShippingBinding.progressBar.setVisibility(View.VISIBLE);
        placeOrderViewModel.deleteAddress(address.getAddressId(), sessionToken, languageId).observe(this, deleteAddressResponseResource -> {
            switch (deleteAddressResponseResource.status) {
                case SUCCESS:
                    if (deleteAddressResponseResource.data.getStatusCode() == 200) {
                        Toast.makeText(dashboardActivity, "Deleted Address Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", deleteAddressResponseResource.data.getMessage());
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
                        GeneralDialog generalDialog = new GeneralDialog("Error", deleteAddressResponseResource.message);
                        generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            fragmentOrderShippingBinding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddNewAddress:
                launchAddAddressFragment();
                break;
            case R.id.tvProceed:
                if (selectedAddress == null || selectedShippingMode == null) {
                    if (selectedAddress == null) {
                        Toast.makeText(dashboardActivity, "Please select any address or add new address to proceed.", Toast.LENGTH_SHORT).show();
                    }
                    if (selectedShippingMode == null) {
                        Toast.makeText(dashboardActivity, "Please select any shipping mode to proceed", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String tagData = "Building Type. : " + selectedAddress.getBuildingType() + ", Area : " + selectedAddress.getArea() + ", Street : " + selectedAddress.getStreet() + ", Building No : " + selectedAddress.getBuildingNo() + ", Flat : " + selectedAddress.getFlat();
                        //InAppEvents.logTagAddress(tagData);
                        dashboardActivity.replaceFragment(R.id.fragment_replacer_checkout, new OrderPaymentFragment(checkoutFragmentInstance), null, false, false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.cvSelectAddress:
                launchAddressBookFragment();
                break;
            default:
                break;
        }

    }

    private void launchAddressBookFragment() {
        AddressBookFragment addressBookFragment = new AddressBookFragment();
        String TAG = "ADDRESS_BOOK";
        addressBookFragment.setTargetFragment(this, ADDRESS_BOOK_REQUEST_CODE);
        changeFragment(addressBookFragment, TAG, "My Addresses");

    }

    private void launchAddAddressFragment() {
        AddShippingAddressFragment addShippingAddressFragment = new AddShippingAddressFragment();
        String TAG = "ADD_ADDRESS";
        addShippingAddressFragment.setTargetFragment(this, ADD_ADDRESS_REQUEST_CODE);
        changeFragment(addShippingAddressFragment, TAG, "Add Address");

    }

    public void setSelectedAddress(Address selectedAddress) {
        hideSoftKeyboard(dashboardActivity);
        this.selectedAddress = selectedAddress;
        fragmentOrderShippingBinding.cvSelectAddress.setVisibility(View.GONE);
        fragmentOrderShippingBinding.viewSelectedAddress.tvName.setText(selectedAddress.getFirstName() + " " + selectedAddress.getLastName());
        fragmentOrderShippingBinding.viewSelectedAddress.tvAddressType.setText(selectedAddress.getBuildingType());
        fragmentOrderShippingBinding.viewSelectedAddress.tvAddress.setText(selectedAddress.getFlat() + " " + selectedAddress.getBuildingNo() + " " + selectedAddress.getBlock() + " " + selectedAddress.getArea()
                + "\n" + selectedAddress.getStreet());
        fragmentOrderShippingBinding.viewSelectedAddress.tvMobile.setText(selectedAddress.getMobile());
        fragmentOrderShippingBinding.viewSelectedAddress.tvCountry.setText(selectedAddress.getCountry());
        fragmentOrderShippingBinding.viewSelectedAddress.radioButtonSelectAddress.setChecked(true);
        fragmentOrderShippingBinding.tvSaveAsDefaultAddress.setVisibility(View.VISIBLE);
        fragmentOrderShippingBinding.lvSelectedAddress.setVisibility(View.VISIBLE);
        dashboardActivity.handleActionMenuBar(false, false);
        checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.GONE);
        getParentFragmentManager().popBackStackImmediate();

        fragmentOrderShippingBinding.viewSelectedAddress.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.handleActionMenuBar(false, false);
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("address", gson.toJson(selectedAddress));
                AddShippingAddressFragment addShippingAddressFragment = new AddShippingAddressFragment();
                String TAG = "ADD_ADDRESS";
                checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.VISIBLE);
                addShippingAddressFragment.setTargetFragment(OrderShippingFragment.this, ADD_ADDRESS_REQUEST_CODE);
                addShippingAddressFragment.setEditAddress(selectedAddress, true);
                changeFragment(addShippingAddressFragment, TAG, "Add Address");
//                dashboardActivity.replaceFragment(R.id.fragment_replacer_address_checkout, addShippingAddressFragment, bundle, false, false);
            }
        });
    }

    public void changeFragment(BaseFragment baseFragment, String TAG, String screenName) {
        checkoutFragmentInstance.fragmentPlaceOrderBinding.fragmentReplacerAddressCheckout.setVisibility(View.VISIBLE);
        dashboardActivity.handleActionMenuBar(true, false);

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_replacer_address_checkout, baseFragment, TAG)
                .addToBackStack(TAG)
                .commit();

//        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_replacer_address_checkout,).commit();
    }
}
