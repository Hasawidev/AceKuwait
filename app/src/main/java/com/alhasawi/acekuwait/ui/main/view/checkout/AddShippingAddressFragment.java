package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Address;
import com.alhasawi.acekuwait.databinding.FragmentAddNewAddressBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book.AddressBookFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.ShippingAddressViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;
import java.util.Map;

public class
AddShippingAddressFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    FragmentAddNewAddressBinding fragmentAddAddressBinding;
    ShippingAddressViewModel shippingAddressViewModel;
    boolean isEdit = false, isFromCheckout = false;
    Address addressObj;
    DashboardActivity dashboardActivity;
    ArrayList<String> addressType = new ArrayList<>();
    ArrayList<String> areaList = new ArrayList<>();
    String selectedCountryName = "KUWAIT";
    Address currentAddress;
    private String languageId = "";
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_new_address;
    }

    @Override
    protected void setup() {
        fragmentAddAddressBinding = (FragmentAddNewAddressBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        shippingAddressViewModel = new ViewModelProvider(getActivity()).get(ShippingAddressViewModel.class);
        dashboardActivity.handleActionBarIcons(false);

        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentAddAddressBinding.spinnerAddressType.setOnItemSelectedListener(this);
        fragmentAddAddressBinding.spinnerArea.setOnItemSelectedListener(this);
        addressType.add("Building");
        addressType.add("House");
        addressType.add("Office");


        ArrayAdapter<String> addressTypeAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, addressType);
        addressTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAddAddressBinding.spinnerAddressType.setAdapter(addressTypeAdapter);
        areaList = shippingAddressViewModel.getAreaList();
        ArrayAdapter<String> areaAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, areaList);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAddAddressBinding.spinnerArea.setAdapter(areaAdapter);
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                String address = bundle.getString("address");
                Gson gson = new Gson();
                addressObj = gson.fromJson(address, Address.class);
                currentAddress = addressObj;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (addressObj != null) {
            setAddressUiValues();
        }
        fragmentAddAddressBinding.btnContinueShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(dashboardActivity);
                if (isEdit)
                    callEditAddressApi(addressObj);
                else
                    callAddNewAddressApi();
            }
        });

        fragmentAddAddressBinding.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                selectedCountryName = selectedCountry.getName();
            }
        });

        fragmentAddAddressBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    private void setAddressUiValues() {
        if (addressObj != null) {
            isEdit = true;
            fragmentAddAddressBinding.edtFirstName.setText(addressObj.getFirstName());
            fragmentAddAddressBinding.edtLastName.setText(addressObj.getLastName());
            fragmentAddAddressBinding.edtStreet.setText(addressObj.getStreet());
            fragmentAddAddressBinding.edtBuildingNumber.setText(addressObj.getBuildingNo());
            for (int i = 0; i < addressType.size(); i++) {
                if (addressType.get(i).equals(addressObj.getBuildingType())) {
                    fragmentAddAddressBinding.spinnerAddressType.setSelection(i);
                    break;
                }

            }
            for (int i = 0; i < areaList.size(); i++) {
                if (areaList.get(i).equals(addressObj.getArea())) {
                    fragmentAddAddressBinding.spinnerArea.setSelection(i);
                    break;
                }

            }
            fragmentAddAddressBinding.spinnerArea.setPrompt(addressObj.getArea());
            fragmentAddAddressBinding.edtFlatNumber.setText(addressObj.getFlat());
            fragmentAddAddressBinding.edtBlock.setText(addressObj.getBlock());
            fragmentAddAddressBinding.edtMobileNumber.setText(addressObj.getMobile());
            fragmentAddAddressBinding.pickerNationality.setCountryForNameCode(addressObj.getCountry());
        }

        fragmentAddAddressBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }


    private void callAddNewAddressApi() {

        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
        String firstName = fragmentAddAddressBinding.edtFirstName.getText().toString();
        String lastName = fragmentAddAddressBinding.edtLastName.getText().toString();
        String mobile = fragmentAddAddressBinding.edtMobileNumber.getText().toString();
        String area = fragmentAddAddressBinding.spinnerArea.getSelectedItem().toString();
        String flat = fragmentAddAddressBinding.edtFlatNumber.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
//        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        String buidingType = fragmentAddAddressBinding.spinnerAddressType.getSelectedItem().toString();
        String buildingNumber = fragmentAddAddressBinding.edtBuildingNumber.getText().toString();
        if (mobile.length() != 8 || (!mobile.startsWith("9") && !mobile.startsWith("6") && !mobile.startsWith("5"))) {
            try {
                GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), getContext().getResources().getString(R.string.enter_valid_mobile));
                generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!firstName.equalsIgnoreCase("") && !lastName.equalsIgnoreCase("") && !mobile.equalsIgnoreCase("") && mobile.length() == 8
                && !street.equalsIgnoreCase("") && !buidingType.equalsIgnoreCase("") && !buildingNumber.equalsIgnoreCase("") && !block.equalsIgnoreCase("") && !selectedCountryName.equalsIgnoreCase("")) {
            fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
            PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
            String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            Map<String, Object> jsonParams = new ArrayMap<>();
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
//                Log.e("callAddNewAddress", gson.toJson(addressResponseResource));
                switch (addressResponseResource.status) {
                    case SUCCESS:
                        if (addressResponseResource.data.getStatusCode() == 200) {
                            Toast.makeText(getContext(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                            try {
                                String tagData = "Building Type. : " + buidingType + ", Area : " + area + ", Street : " + street + ", Building No : " + buildingNumber + ", Flat : " + flat;
                                //InAppEvents.logTagAddress(tagData);
                                InAppEvents.logShippingAreaEvent(area);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            currentAddress = addressResponseResource.data.getData();
                            returnToAddressbook(addressObj);
                            getParentFragmentManager().popBackStackImmediate();
                        } else {
                            try {
                                GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), addressResponseResource.data.getMessage());
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
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), addressResponseResource.message);
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                fragmentAddAddressBinding.progressBar.setVisibility(View.GONE);
            });
        } else {
            try {
                GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), getContext().getResources().getString(R.string.enter_fields_to_proceed));
                generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private void callEditAddressApi(Address address) {
        fragmentAddAddressBinding.progressBar.setVisibility(View.VISIBLE);
        String firstName = fragmentAddAddressBinding.edtFirstName.getText().toString();
        String lastName = fragmentAddAddressBinding.edtLastName.getText().toString();
        String mobile = fragmentAddAddressBinding.edtMobileNumber.getText().toString();
        String street = fragmentAddAddressBinding.edtStreet.getText().toString();
        String area = fragmentAddAddressBinding.spinnerArea.getSelectedItem().toString();
        String flat = fragmentAddAddressBinding.edtFlatNumber.getText().toString();
        String block = fragmentAddAddressBinding.edtBlock.getText().toString();
        String buidingType = fragmentAddAddressBinding.spinnerAddressType.getSelectedItem().toString();
        String buildingNumber = fragmentAddAddressBinding.edtBuildingNumber.getText().toString();
//        String country = fragmentAddAddressBinding.edtCountry.getText().toString();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userID = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("addressId", address.getAddressId());
        jsonParams.put("firstName", firstName);
        jsonParams.put("lastName", lastName);
        jsonParams.put("mobile", mobile);
        jsonParams.put("street", street);
        jsonParams.put("area", area);
        jsonParams.put("flat", flat);
        jsonParams.put("block", block);
        jsonParams.put("country", selectedCountryName);
        jsonParams.put("customerId", userID);
        jsonParams.put("buildingType", buidingType);
        jsonParams.put("buildingNo", buildingNumber);


        shippingAddressViewModel.editAddress(userID, addressObj.getAddressId(), sessionToken, jsonParams, languageId).observe(getActivity(), addressResponseResource -> {
            //Log.e("callEditAddress", gson.toJson(addressResponseResource));
            switch (addressResponseResource.status) {
                case SUCCESS:
                    if (addressResponseResource.data.getStatusCode() == 200) {
                        currentAddress = addressResponseResource.data.getData();
                        Toast.makeText(getContext(), "Address Added Successfully", Toast.LENGTH_SHORT).show();
                        try {
                            String tagData = "Building Type. : " + buidingType + ", Area : " + area + ", Street : " + street + ", Building No : " + buildingNumber + ", Flat : " + flat;
                            //InAppEvents.logTagAddress(tagData);
                            InAppEvents.logShippingAreaEvent(area);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
//                        returnToAddressbook(currentAddress);
                        getParentFragmentManager().popBackStackImmediate();
                        returnToAddressbook(currentAddress);
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), addressResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), addressResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }

            fragmentAddAddressBinding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(view.getId()==R.id.spinnerAddressType)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void returnToAddressbook(Address selectedAddress) {
        try {
            AddressBookFragment addressBookFragment = (AddressBookFragment) getTargetFragment();
            addressBookFragment.setAddress(selectedAddress);
        } catch (Exception e) {
            Log.e("returnToAddressbook", "Address List Refresh Method not accessible", e);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setEditAddress(Address addressObj, boolean isFromCheckout) {
        this.currentAddress = addressObj;
        this.addressObj = addressObj;
        this.isFromCheckout = isFromCheckout;
    }
}
