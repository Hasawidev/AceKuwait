package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.databinding.FragmentReturnAddressBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book.AddressBookFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.ShippingAddressViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReturnAddressFragment extends BaseFragment {
    FragmentReturnAddressBinding fragmentReturnAddressBinding;
    DashboardActivity dashboardActivity;
    ShippingAddressViewModel shippingAddressViewModel;
    Address currentSelectedAddress;
    String selectedCountryName = "KUWAIT";
    private boolean isEdit = false;
    private List<String> addressType = new ArrayList<>();
    private List<String> areaList = new ArrayList<>();
    private String languageId = "";
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_return_address;
    }

    @Override
    protected void setup() {
        fragmentReturnAddressBinding = (FragmentReturnAddressBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        shippingAddressViewModel = new ViewModelProvider(getActivity()).get(ShippingAddressViewModel.class);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        addressType.add("Building");
        addressType.add("House");
        addressType.add("Office");
        ArrayAdapter<String> addressTypeAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, addressType);
        addressTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentReturnAddressBinding.spinnerAddressType.setAdapter(addressTypeAdapter);
        areaList = shippingAddressViewModel.getAreaList();
        ArrayAdapter<String> areaAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentReturnAddressBinding.spinnerArea.setAdapter(areaAdapter);

        try {
            Bundle bundle = getArguments();
            String addressString = bundle.getString("address");
            Gson gson = new Gson();
            currentSelectedAddress = gson.fromJson(addressString, Address.class);
            if (currentSelectedAddress != null) {
                setAddressUiValues(currentSelectedAddress);
                isEdit = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentReturnAddressBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddressUiValues(currentSelectedAddress);
                isEdit = true;

            }
        });
        fragmentReturnAddressBinding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isEdit)
                        callAddressApi(currentSelectedAddress, true);
                    else
                        callAddressApi(null, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        fragmentReturnAddressBinding.tvSavedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressBookFragment addressBookFragment = new AddressBookFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_from_return", true);
                addressBookFragment.setArguments(bundle);
                addressBookFragment.setTargetFragment(ReturnAddressFragment.this, 121);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, addressBookFragment, bundle, true, false);
            }
        });

        fragmentReturnAddressBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnAddedAddress(currentSelectedAddress);
            }
        });
    }

    private void returnAddedAddress(Address currentSelectedAddress) {
        ProductReturnFragment productReturnFragment = (ProductReturnFragment) getTargetFragment();
        productReturnFragment.setSelectedAddress(currentSelectedAddress);
    }

    private void setAddressUiValues(Address addressObj) {
        if (addressObj != null) {
            isEdit = true;
            fragmentReturnAddressBinding.edtFirstName.setText(addressObj.getFirstName());
            fragmentReturnAddressBinding.edtSecondName.setText(addressObj.getLastName());
            fragmentReturnAddressBinding.edtStreet.setText(addressObj.getStreet());
            fragmentReturnAddressBinding.edtBuildingNo.setText(addressObj.getBuildingNo());
            for (int i = 0; i < addressType.size(); i++) {
                if (addressType.get(i).equals(addressObj.getBuildingType())) {
                    fragmentReturnAddressBinding.spinnerAddressType.setSelection(i);
                    break;
                }

            }
            for (int i = 0; i < areaList.size(); i++) {
                if (areaList.get(i).equals(addressObj.getArea())) {
                    fragmentReturnAddressBinding.spinnerArea.setSelection(i);
                    break;
                }

            }
            fragmentReturnAddressBinding.spinnerArea.setPrompt(addressObj.getArea());
            fragmentReturnAddressBinding.edtFlatNumber.setText(addressObj.getFlat());
            fragmentReturnAddressBinding.edtBlock.setText(addressObj.getBlock());
            fragmentReturnAddressBinding.edtMobile.setText(addressObj.getMobile());
            setCurrentAddressCard(addressObj);

        }


    }

    public void setCurrentAddressCard(Address addressObj) {
        fragmentReturnAddressBinding.tvUserName.setText(addressObj.getFirstName() + " " + addressObj.getLastName());
        String address = addressObj.getStreet() + ", " +
                addressObj.getFlat() + " " + addressObj.getBlock() + ", "
                + addressObj.getArea() + "\n" + addressObj.getMobile();
        fragmentReturnAddressBinding.tvLocationAddress.setText(address);
    }

    private void callAddressApi(Address address, boolean isEdit) {

        String street = fragmentReturnAddressBinding.edtStreet.getText().toString();
        String firstName = fragmentReturnAddressBinding.edtFirstName.getText().toString();
        String lastName = fragmentReturnAddressBinding.edtSecondName.getText().toString();
        String mobile = fragmentReturnAddressBinding.edtMobile.getText().toString();
        String area = fragmentReturnAddressBinding.spinnerArea.getSelectedItem().toString();
        String flat = fragmentReturnAddressBinding.edtFlatNumber.getText().toString();
        String block = fragmentReturnAddressBinding.edtBlock.getText().toString();
//        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        String buidingType = fragmentReturnAddressBinding.spinnerAddressType.getSelectedItem().toString();
        String buildingNumber = fragmentReturnAddressBinding.edtBuildingNo.getText().toString();
        if (!firstName.equalsIgnoreCase("") && !lastName.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("")
                && !street.equalsIgnoreCase("") && !buidingType.equalsIgnoreCase("") && !buildingNumber.equalsIgnoreCase("") && !block.equalsIgnoreCase("") && !selectedCountryName.equalsIgnoreCase("")) {
            fragmentReturnAddressBinding.progressBar.setVisibility(View.VISIBLE);
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            Map<String, Object> jsonParams = new ArrayMap<>();
            if (isEdit && address != null)
                jsonParams.put("addressId", address.getAddressId());
            jsonParams.put("firstName", firstName);
            jsonParams.put("lastName", lastName);
            jsonParams.put("mobile", mobile);
            jsonParams.put("street", street);
            jsonParams.put("area", area);
            jsonParams.put("flat", flat);
            jsonParams.put("block", block);
            jsonParams.put("buildingType", buidingType);
            jsonParams.put("buildingNo", buildingNumber);
            jsonParams.put("country", selectedCountryName);
            jsonParams.put("customerId", userID);

            shippingAddressViewModel.addNewAddress(userID, jsonParams, sessionToken, languageId).observe(getActivity(), addressResponseResource -> {
                switch (addressResponseResource.status) {
                    case SUCCESS:
                        if (addressResponseResource.data.getStatusCode() == 200) {
                            Toast.makeText(getActivity(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                            currentSelectedAddress = addressResponseResource.data.getData();
                            setCurrentAddressCard(currentSelectedAddress);
//                        if (isFromCheckout)
//                            returnSelectedAddress(currentAddress);
//                        else
//                        getParentFragmentManager().popBackStackImmediate();
                            try {
                                InAppEvents.logShippingAreaEvent(currentSelectedAddress.getArea());
                                String tagData = "Building Type. : " + buidingType + ", Area : " + area + ", Street : " + street + ", Building No : " + buildingNumber + ", Flat : " + flat;
                                //InAppEvents.logTagAddress(tagData);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }

                        } else {
                            GeneralDialog generalDialog = new GeneralDialog("Error", addressResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        }

                        break;
                    case LOADING:
                        break;
                    case ERROR:
                        GeneralDialog generalDialog = new GeneralDialog("Error", addressResponseResource.message);
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        break;
                }

                fragmentReturnAddressBinding.progressBar.setVisibility(View.GONE);
            });
        } else {
            GeneralDialog generalDialog = new GeneralDialog("Error", "Please enter required fields to proceed?");
            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
        }


    }

    public void setSelectedAddress(Address selectedAddress) {
        getParentFragmentManager().popBackStackImmediate();
        if (selectedAddress != null) {
            this.currentSelectedAddress = selectedAddress;
            setCurrentAddressCard(currentSelectedAddress);
        }

    }
}
