package com.alhasawi.acekuwait.ui.main.view.signin.signup;

import android.app.DatePickerDialog;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentSignupBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.SignupViewModel;
import com.alhasawi.acekuwait.utils.DateTimeUtils;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.alhasawi.acekuwait.utils.dialogs.RegistrationSuccessDialog;
import com.google.gson.Gson;

import java.util.Map;

public class SignupFragment extends BaseFragment implements View.OnClickListener {

    FragmentSignupBinding fragmentSignupBinding;
    SigninActivity signinActivity;
    String selectedCountryCode = "", selectedNationality = "";
    SignupViewModel signupViewModel;
    String selectedDate = "";
    //    AppEventsLogger logger;
    private String selectedGender = "M";
    private DatePickerDialog datePickerDialog;
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    protected void setup() {

        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        }

        fragmentSignupBinding = (FragmentSignupBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
//        logger = AppEventsLogger.newLogger(signinActivity);
        signupViewModel = new ViewModelProvider(getActivity()).get(SignupViewModel.class);
        fragmentSignupBinding.btnSignup.setOnClickListener(this);
//        fragmentSignupBinding.layoutSignup.edtBirthday.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setOnClickListener(this);
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setOnClickListener(this);
        fragmentSignupBinding.cvBackground.setOnClickListener(this);

//        fragmentSignupBinding.layoutSignup.pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
//                selectedCountryCode = selectedCountry.getPhoneCode();
//            }
//        });
//        fragmentSignupBinding.layoutSignup.pickerCountryCode.setCountryForPhoneCode(965);
//        fragmentSignupBinding.layoutSignup.pickerNationality.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected(Country selectedCountry) {
////                selectedNationality = selectedCountry.getName();
//            }
//        });

    }

    private void userRegistration() {
        fragmentSignupBinding.layoutSignup.tvFirstNameError.setVisibility(View.GONE);
        fragmentSignupBinding.layoutSignup.tvLastNameError.setVisibility(View.GONE);
        fragmentSignupBinding.layoutSignup.tvPasswordError.setVisibility(View.GONE);
        fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.GONE);
        String firstName = fragmentSignupBinding.layoutSignup.edtFirstname.getText().toString().trim();
        String lastName = fragmentSignupBinding.layoutSignup.edtLastname.getText().toString().trim();
        String email = fragmentSignupBinding.layoutSignup.edtEmail.getText().toString().trim();
        String phone = fragmentSignupBinding.layoutSignup.edtMobile.getText().toString().trim();
        String password = fragmentSignupBinding.layoutSignup.edtPassword.getText().toString().trim();
//        String dob = fragmentSignupBinding.layoutSignup.edtBirthday.getText().toString().trim();
        String confirmPassword = fragmentSignupBinding.layoutSignup.edtConfirmPassword.getText().toString().trim();

        if (firstName.equals("")) {
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setText(getContext().getResources().getString(R.string.firstname_required));
        }
        if (firstName.length() < 2) {
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvFirstNameError.setText(getContext().getResources().getString(R.string.first_name_minimum_characters));
        }
        if (lastName.equals(""))
            fragmentSignupBinding.layoutSignup.tvLastNameError.setVisibility(View.VISIBLE);
        if (lastName.length() < 2) {
            fragmentSignupBinding.layoutSignup.tvLastNameError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvLastNameError.setText(getContext().getResources().getString(R.string.first_name_minimum_characters));
        }
        if (email.equals(""))
            fragmentSignupBinding.layoutSignup.tvEmailError.setVisibility(View.VISIBLE);
        if (phone.equals(""))
            fragmentSignupBinding.layoutSignup.tvMobileError.setVisibility(View.VISIBLE);
        if (password.equals(""))
            fragmentSignupBinding.layoutSignup.tvPasswordError.setVisibility(View.VISIBLE);
        fragmentSignupBinding.layoutSignup.tvPasswordError.setText(getContext().getResources().getString(R.string.password_required));
        if (password.length() < 6)
            fragmentSignupBinding.layoutSignup.tvPasswordError.setVisibility(View.VISIBLE);
        fragmentSignupBinding.layoutSignup.tvPasswordError.setText(getContext().getResources().getString(R.string.password_minimum_characters));
        if (confirmPassword.equals("")) {
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setText(getContext().getResources().getString(R.string.confirm_required));
        }
        if (confirmPassword.length() < 6) {
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setText(getContext().getResources().getString(R.string.password_minimum_characters));
        }
        if (!password.equals(confirmPassword)) {
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            fragmentSignupBinding.layoutSignup.tvConfirmPasswordError.setText(getContext().getResources().getString(R.string.same_pasword));
        }
        if (phone.length() != 8 || (!phone.startsWith("9") && !phone.startsWith("6") && !phone.startsWith("5"))) {
            try {
                GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), getContext().getResources().getString(R.string.enter_valid_mobile));
                generalDialog.show(signinActivity.getSupportFragmentManager(), "GENERAL_DIALOG");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!firstName.equals("") && firstName.length() >= 2 && lastName.length() >= 2 && !lastName.equals("") && !email.equals("") && !phone.equals("") && !password.equals("") && password.length() >= 6 && confirmPassword.length() >= 6 && !confirmPassword.equals("") && confirmPassword.equals(password))
            if (password.equals(confirmPassword)) {
                Map<String, Object> jsonParams = new ArrayMap<>();
                jsonParams.put("customerFirstName", firstName);
                jsonParams.put("customerLastName", lastName);
                jsonParams.put("mobileNo", phone);
                jsonParams.put("password", password);
                jsonParams.put("confirmPassword", confirmPassword);
                jsonParams.put("active", true);
                jsonParams.put("emailId", email);
                jsonParams.put("gender", selectedGender);
                jsonParams.put("nationality", "");
                jsonParams.put("dob", "");
                signinActivity.showProgressBar(true);
                signupViewModel.userRegistration(jsonParams).observe(getActivity(), signupResponse -> {
                    signinActivity.showProgressBar(false);
                    switch (signupResponse.status) {
                        case SUCCESS:
                            if (signupResponse.data.getStatusCode() == 200) {
                                PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                                try {
                                    if (signupResponse.data.getData().getToken() != null)
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, signupResponse.data.getData().getToken());
                                    if (signupResponse.data.getData().getuser() != null) {
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_CUSTOMER, new Gson().toJson(signupResponse.data.getData().getuser()));
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, signupResponse.data.getData().getuser().getCustomerId());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, signupResponse.data.getData().getuser().getCustomerFirstName());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, signupResponse.data.getData().getuser().getEmailId());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, signupResponse.data.getData().getuser().getPassword());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, signupResponse.data.getData().getuser().getGender());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, signupResponse.data.getData().getuser().getConfirmPassword());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, signupResponse.data.getData().getuser().getMobileNo());
                                        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);
                                        if (signupResponse.data.getData().getuser() != null) {
                                            signinActivity.getmFirebaseAnalytics().setUserProperty("email", signupResponse.data.getData().getuser().getEmailId());
                                            signinActivity.getmFirebaseAnalytics().setUserProperty("country", signupResponse.data.getData().getuser().getNationality());
                                            signinActivity.getmFirebaseAnalytics().setUserProperty("gender", signupResponse.data.getData().getuser().getGender());
                                            signinActivity.getmFirebaseAnalytics().setUserProperty("date_of_birth", signupResponse.data.getData().getuser().getDob());
                                            signinActivity.getmFirebaseAnalytics().setUserProperty("phone", signupResponse.data.getData().getuser().getMobileNo());
                                        }
                                        try {
//                                            Freshchat.resetUser(requireContext());
//                                            FreshchatUser freshchatUser = Freshchat.getInstance(requireContext()).getUser();
//                                            freshchatUser.setFirstName(signupResponse.data.getData().getuser().getCustomerFirstName());
//                                            freshchatUser.setEmail(signupResponse.data.getData().getuser().getEmailId());
//                                            Freshchat.getInstance(requireContext()).setUser(freshchatUser);
                                            Log.e("signUp", signupResponse.data.getData().getuser().getCustomerFirstName());

                                            InAppEvents.logUserEmailProperty(signupResponse.data.getData().getuser().getEmailId());
                                            InAppEvents.logUserPhoneProperty(signupResponse.data.getData().getuser().getMobileNo());
                                            InAppEvents.logUserGenderProperty(signupResponse.data.getData().getuser().getGender());
                                            InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                                            String loyalityJson = gson.toJson(signupResponse.data.getData().getuser().getLoyalty());
                                            InAppEvents.logUserLoyalityProperty(loyalityJson);
                                            InAppEvents.logUserLoyalityProperty(loyalityJson);
                                            try {
                                                InAppEvents.logUserLoyalityTag(signupResponse.data.getData().getuser().getLoyalty().getPoints());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e3) {
                                            e3.printStackTrace();
                                        }
                                        try {
                                            InAppEvents.logSignUpSuccessEvent(signupResponse.data.getData().getuser());
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    String date = DateTimeUtils.getCurrentStringDateTime();
                                    String country = "", gender = "";
                                    if (signupResponse.data.getData().getuser() != null) {
                                        country = signupResponse.data.getData().getuser().getNationality();
                                        gender = signupResponse.data.getData().getuser().getGender();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                RegistrationSuccessDialog registrationSuccessDialog = new RegistrationSuccessDialog(signinActivity);
                                registrationSuccessDialog.show(getParentFragmentManager(), "REG_SUCCESS_DIALOG");

                            } else {
                                try {
                                    GeneralDialog generalDialog = new GeneralDialog("Error", signupResponse.data.getMessage());
                                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case LOADING:
                            break;
                        case ERROR:
                            String error = signupResponse.message;
                            String date = "";
                            try {
                                date = DateTimeUtils.getCurrentStringDateTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(signinActivity, signupResponse.message, Toast.LENGTH_SHORT).show();
                            GeneralDialog generalDialog = new GeneralDialog("Error", error);
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                            try {
                                InAppEvents.logSignUpFailEvent();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            break;
                    }
                });

            } else {
                Toast.makeText(signinActivity, "Password did not match", Toast.LENGTH_SHORT).show();
            }

    }


    private void selectMale() {
        hideSoftKeyboard(signinActivity);
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.ace_theme_red_outlined_rounded_rectangle));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.ace_theme_color));
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.white_rounded_rectangle));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.black));
        selectedGender = "M";
    }

    private void selectFemale() {
        hideSoftKeyboard(signinActivity);
        fragmentSignupBinding.layoutSignup.constraintLayoutFemale.setBackground(getActivity().getResources().getDrawable(R.drawable.ace_theme_red_outlined_rounded_rectangle));
        fragmentSignupBinding.layoutSignup.txtFemale.setTextColor(getResources().getColor(R.color.ace_theme_color));
        fragmentSignupBinding.layoutSignup.constraintLayoutMale.setBackground(getActivity().getResources().getDrawable(R.drawable.white_rounded_rectangle));
        fragmentSignupBinding.layoutSignup.tvMale.setTextColor(getResources().getColor(R.color.black));
        selectedGender = "F";
    }

//    private void showDatePickerDialog() {
//        final Calendar cldr = Calendar.getInstance();
//        int day = cldr.get(Calendar.DAY_OF_MONTH);
//        int month = cldr.get(Calendar.MONTH);
//        int year = cldr.get(Calendar.YEAR);
//        // date picker dialog
//        datePickerDialog = new DatePickerDialog(getActivity(),
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        selectedDate = year + "-" + monthOfYear + "-" + dayOfMonth;
//                        fragmentSignupBinding.layoutSignup.edtBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                    }
//                }, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 365.25 * 16)));
//        datePickerDialog.show();
//    }

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
//        fragmentSignupBinding.layoutSignup.edtBirthday.setText(selectedDate);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.edt_birthday:
//                showDatePickerDialog();
//                break;
            case R.id.btn_signup:
                hideSoftKeyboard(signinActivity);
                userRegistration();
                break;
            case R.id.constraintLayoutMale:
                selectMale();
                break;
            case R.id.constraintLayoutFemale:
                selectFemale();
                break;
            case R.id.cv_background:
                hideSoftKeyboard(signinActivity);
                break;
            default:
                break;
        }
    }

}
