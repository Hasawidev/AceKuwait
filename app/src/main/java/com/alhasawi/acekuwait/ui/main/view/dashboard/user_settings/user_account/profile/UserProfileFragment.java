package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.profile;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.User;
import com.alhasawi.acekuwait.databinding.FragmentUserProfileBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.UserProfileViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

import java.util.Calendar;

public class UserProfileFragment extends BaseFragment {

    UserProfileViewModel userProfileViewModel;
    FragmentUserProfileBinding fragmentUserProfileBinding;
    DatePickerDialog datePickerDialog;
    DashboardActivity dashboardActivity;
    PreferenceHandler preferenceHandler;
    private String sessionToken, customerId;
    private String selectedCountryCode;
    private String selectedGender, selectedNationality, selectedDob = "";
    private User customerObj;
    private String languageId = "";
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void setup() {
        fragmentUserProfileBinding = (FragmentUserProfileBinding) viewDataBinding;
        userProfileViewModel = new ViewModelProvider(getActivity()).get(UserProfileViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionBarIcons(false);
        dashboardActivity.handleActionMenuBar(true, false);
        try {
            preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
            String name = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
            String email = preferenceHandler.getData(PreferenceHandler.LOGIN_EMAIL, "");
            String phone = preferenceHandler.getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
            String password = preferenceHandler.getData(PreferenceHandler.LOGIN_PASSWORD, "");
            sessionToken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
            customerId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
            languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");

            fragmentUserProfileBinding.layoutProfile.edtFirstname.setText(name);
            fragmentUserProfileBinding.layoutProfile.edtEmail.setText(email);
            fragmentUserProfileBinding.layoutProfile.edtMobile.setText(phone);
            fragmentUserProfileBinding.tvUsernameTop.setText(name);

            callUserProfileApi(email, sessionToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        fragmentUserProfileBinding.layoutProfile.pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
//                selectedCountryCode = selectedCountry.getPhoneCode();
//            }
//        });
//        fragmentUserProfileBinding.layoutProfile.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
//                selectedNationality = selectedCountry.getName();
//            }
//        });
//
//        fragmentUserProfileBinding.layoutProfile.edtBirthday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//
//            }
//        });

        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFemale();
            }
        });
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMale();
            }
        });

        fragmentUserProfileBinding.layoutProfile.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ChangePasswordFragment(), null, true, false);
                dashboardActivity.handleActionMenuBar(false, false);
            }
        });

        fragmentUserProfileBinding.tvSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String firstName = fragmentUserProfileBinding.layoutProfile.edtFirstname.getText().toString();
                    String lastName = fragmentUserProfileBinding.layoutProfile.edtLastname.getText().toString();
                    String mobileNo = fragmentUserProfileBinding.layoutProfile.edtMobile.getText().toString();
                    String emailId = fragmentUserProfileBinding.layoutProfile.edtEmail.getText().toString();

                    if (firstName.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvFirstNameError.setVisibility(View.VISIBLE);
                    if (lastName.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvLastNameError.setVisibility(View.VISIBLE);
                    if (emailId.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvEmailError.setVisibility(View.VISIBLE);
                    if (mobileNo.equals(""))
                        fragmentUserProfileBinding.layoutProfile.tvMobileError.setVisibility(View.VISIBLE);
                    else {
//                        Map<String, Object> inputParams = new ArrayMap<>();
                        User editUser = customerObj;
                        editUser.setCustomerFirstName(firstName);
                        editUser.setCustomerLastName(lastName);
                        editUser.setEmailId(emailId);
                        editUser.setGender(selectedGender);
                        editUser.setNationality("");
                        editUser.setDob("");
                        editUser.setMobileNo(mobileNo);
                        editUser.setCustomerId(customerId);
//                        inputParams.put("customerFirstName", firstName);
//                        inputParams.put("customerLastName", lastName);
//                        inputParams.put("emailId", emailId);
//                        inputParams.put("gender", selectedGender);
//                        inputParams.put("nationality", "");
//                        inputParams.put("dob", "");
//                        inputParams.put("mobileNo", mobileNo);
//                        inputParams.put("customerId", customerId);
                        callEditUserProfileApi(customerId, editUser);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fragmentUserProfileBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });

    }

    private void callUserProfileApi(String emailId, String sessionToken) {
        userProfileViewModel.userProfile(emailId, sessionToken, languageId).observe(getActivity(), userProfileResponseResource -> {
            switch (userProfileResponseResource.status) {
                case SUCCESS:
                    if (userProfileResponseResource.data.getStatusCode() == 200) {
                        if (userProfileResponseResource != null) {
                            fragmentUserProfileBinding.layoutProfile.edtFirstname.setText(userProfileResponseResource.data.getData().getUser().getCustomerFirstName());
                            fragmentUserProfileBinding.layoutProfile.edtLastname.setText(userProfileResponseResource.data.getData().getUser().getCustomerLastName());
                            fragmentUserProfileBinding.layoutProfile.edtEmail.setText(userProfileResponseResource.data.getData().getUser().getEmailId());
//                        if (userProfileResponseResource.data.getData().getUser().getDob() != null)
//                            fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(userProfileResponseResource.data.getData().getUser().getDob());
                            customerObj = userProfileResponseResource.data.getData().getUser();
                            if (userProfileResponseResource.data.getData().getUser().getGender() != null) {
                                if (userProfileResponseResource.data.getData().getUser().getGender().equalsIgnoreCase("f")) {
                                    fragmentUserProfileBinding.imageViewUser.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                    selectFemale();
                                } else if (userProfileResponseResource.data.getData().getUser().getGender().equalsIgnoreCase("m")) {
                                    fragmentUserProfileBinding.imageViewUser.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                    selectMale();
                                }

                            }
                            fragmentUserProfileBinding.tvUsernameTop.setText(userProfileResponseResource.data.getData().getUser().getCustomerFirstName() + " " + userProfileResponseResource.data.getData().getUser().getCustomerLastName());
//                        if (userProfileResponseResource.data.getData().getUser().getNationality() != null) {
//                            fragmentUserProfileBinding.layoutProfile.pickerNationality.setCountryForNameCode(userProfileResponseResource.data.getData().getUser().getNationality());
//                        }

                            InAppEvents.logUserEmailProperty(userProfileResponseResource.data.getData().getUser().getEmailId());
                            InAppEvents.logUserPhoneProperty(userProfileResponseResource.data.getData().getUser().getMobileNo());
                            InAppEvents.logUserGenderProperty(userProfileResponseResource.data.getData().getUser().getGender());
                            InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                            String loyalityJson = gson.toJson(userProfileResponseResource.data.getData().getUser().getLoyalty());
                            InAppEvents.logUserLoyalityProperty(loyalityJson);
                            try {
                                InAppEvents.logUserLoyalityTag(userProfileResponseResource.data.getData().getUser().getLoyalty().getPoints());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", userProfileResponseResource.data.getMessage());
                            generalDialog.show(dashboardActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", userProfileResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    break;
            }
            fragmentUserProfileBinding.progressBar.setVisibility(View.GONE);
        });
    }


    private void callEditUserProfileApi(String customerId, User editUser) {
        hideSoftKeyboard(dashboardActivity);
        fragmentUserProfileBinding.progressBar.setVisibility(View.VISIBLE);
        userProfileViewModel.editUserProfile(customerId, sessionToken, editUser, languageId).observe(getActivity(), userProfileResponseResource -> {
            switch (userProfileResponseResource.status) {
                case SUCCESS:
                    if (userProfileResponseResource.data.getStatusCode() == 200) {
                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        if (userProfileResponseResource.data.getData() != null) {
                            PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, userProfileResponseResource.data.getData().getUser().getCustomerFirstName());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, userProfileResponseResource.data.getData().getUser().getEmailId());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, userProfileResponseResource.data.getData().getUser().getPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, userProfileResponseResource.data.getData().getUser().getConfirmPassword());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, userProfileResponseResource.data.getData().getUser().getMobileNo());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, userProfileResponseResource.data.getData().getUser().getGender());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_NATIONALITY, userProfileResponseResource.data.getData().getUser().getNationality());

                            customerObj = userProfileResponseResource.data.getData().getUser();
                            sessionToken = userProfileResponseResource.data.getData().getToken();
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, userProfileResponseResource.data.getData().getToken());
                            preferenceHandler.saveData(PreferenceHandler.LOGIN_CUSTOMER, new Gson().toJson(customerObj));

                            fragmentUserProfileBinding.tvUsernameTop.setText(customerObj.getCustomerFirstName() + " " + customerObj.getCustomerLastName());
                            String updateJson = gson.toJson(userProfileResponseResource.data.getData().getUser());
                            InAppEvents.logUserUpdateProfileProperty(updateJson);

                            InAppEvents.logUserEmailProperty(userProfileResponseResource.data.getData().getUser().getEmailId());
                            InAppEvents.logUserPhoneProperty(userProfileResponseResource.data.getData().getUser().getMobileNo());
                            InAppEvents.logUserGenderProperty(userProfileResponseResource.data.getData().getUser().getGender());
                            InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                            String loyalityJson = gson.toJson(userProfileResponseResource.data.getData().getUser().getLoyalty());
                            InAppEvents.logUserLoyalityProperty(loyalityJson);
                            try {
                                InAppEvents.logUserLoyalityTag(userProfileResponseResource.data.getData().getUser().getLoyalty().getPoints());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", userProfileResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", userProfileResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    break;
            }
            fragmentUserProfileBinding.progressBar.setVisibility(View.GONE);
        });
    }

    private void showDatePickerDialog() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        selectedDob = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 16)));
        datePickerDialog.show();
    }

    private void selectMale() {
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.ace_red_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.tvMale.setTextColor(getResources().getColor(R.color.ace_theme_color));
        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.txtFemale.setTextColor(getResources().getColor(R.color.black));
        selectedGender = "M";
    }

    private void selectFemale() {
        fragmentUserProfileBinding.layoutProfile.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.ace_red_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.txtFemale.setTextColor(getResources().getColor(R.color.ace_theme_color));
        fragmentUserProfileBinding.layoutProfile.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_outlined_rounded_rectangle_12dp));
        fragmentUserProfileBinding.layoutProfile.tvMale.setTextColor(getResources().getColor(R.color.black));
        selectedGender = "F";
    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        // Create a Calender instance
//
//        Calendar mCalender = Calendar.getInstance();
//
//        // Set static variables of Calender instance
//
//        mCalender.set(Calendar.YEAR, year);
//
//        mCalender.set(Calendar.MONTH, month);
//
//        mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        // Get the date in form of string
//
//        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalender.getTime());
//
//        // Set the textview to the selectedDate String
//
//        fragmentUserProfileBinding.layoutProfile.edtBirthday.setText(selectedDate);
//    }
}
