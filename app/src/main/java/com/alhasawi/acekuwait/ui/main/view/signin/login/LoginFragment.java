package com.alhasawi.acekuwait.ui.main.view.signin.login;

import android.content.Intent;
import android.content.res.Resources;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentLoginBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.signup.SignupFragment;
import com.alhasawi.acekuwait.ui.main.viewmodel.LoginViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.Map;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private static final int GOOGLE_SIGN_IN = 100;
    private static final String TAG = "SIGN IN ACTIVITY";
    SigninActivity signinActivity;
    //    CallbackManager callbackManager;
//    AccessToken facebookAccessToken;
//    GraphRequest facebookGraphRequest;
//    FacebookCallback<LoginResult> facebookCallback;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding fragmentLoginBinding;
    //    private AccessTokenTracker accessTokenTracker;
    private Gson gson = new Gson();

    public static com.alhasawi.acekuwait.ui.main.view.signin.login.LoginFragment newInstance() {
        return new com.alhasawi.acekuwait.ui.main.view.signin.login.LoginFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void setup() {
        fragmentLoginBinding = (FragmentLoginBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        fragmentLoginBinding.btnLogin.setOnClickListener(this);
        fragmentLoginBinding.imageViewGoogle.setOnClickListener(this);
        fragmentLoginBinding.imageViewFacebook.setOnClickListener(this);
        fragmentLoginBinding.tvsignup.setOnClickListener(this);
        fragmentLoginBinding.tvResetPassword.setOnClickListener(this);

//        //Facbook Signin
//        FacebookSdk.sdkInitialize(getActivity());
//        callbackManager = CallbackManager.Factory.create();
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//        facebookCallback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("LoginActivity", response.toString());
//
//                                // Application code
//                                try {
//                                    Log.d("tttttt", object.getString("id"));
//                                    String birthday = "";
//                                    if (object.has("birthday")) {
//                                        birthday = object.getString("birthday"); // 01/31/1980 format
//                                    }
//
//                                    String first_name = object.getString("first_name");
//                                    String last_name = object.getString("last_name");
//                                    String mail = object.getString("email");
//                                    String gender = object.getString("gender");
//                                    String fid = object.getString("id");
//                                    String picture = "https://graph.facebook.com/" + fid + "/picture?type=large";
//
//                                    userRegistration(first_name, last_name, mail);
////                                    tvdetails.setText("Name: "+fnm+" "+lnm+" \n"+"Email: "+mail+" \n"+"Gender: "+gender+" \n"+"ID: "+fid+" \n"+"Birth Date: "+birthday);
////                                    aQuery.id(ivpic).image("https://graph.facebook.com/" + fid + "/picture?type=large");
////                                    https://graph.facebook.com/143990709444026/picture?type=large
//                                    Log.d("aswwww", "https://graph.facebook.com/" + fid + "/picture?type=large");
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        };
//        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//            }
//        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewGoogle:
                Intent signInIntent = signinActivity.getmGoogleSignInClient().getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                break;
            case R.id.imageViewFacebook:
//                if (signinActivity.isAlreadyLoggedinWithFacebbok()) {
//
//                } else
//                    facebookSignin();
                break;
            case R.id.btn_login:
                hideSoftKeyboard(signinActivity);
                userAuthentication();
                break;
            case R.id.tvsignup:
                signinActivity.replaceFragment(new SignupFragment(), null, true, false);
                break;
            case R.id.tvResetPassword:
                signinActivity.replaceFragment(new ForgotPasswordFragment(), null, true, false);
                break;
            default:
                break;


        }
    }

    private void userAuthentication() {
        signinActivity.showProgressBar(true);
        String username = fragmentLoginBinding.edtEmail.getText().toString().trim();
        String password = fragmentLoginBinding.edtPassword.getText().toString().trim();
        if (!username.equals("") && !password.equals("")) {
            Map<String, Object> jsonParams = new ArrayMap<>();
            jsonParams.put("emailId", username);
            jsonParams.put("password", password);
            loginViewModel.userLogin(jsonParams).observe(getActivity(), loginResponse -> {
                signinActivity.showProgressBar(false);
                switch (loginResponse.status) {
                    case SUCCESS:
                        try {
                            if (loginResponse.data.getStatusCode() == 200) {
                                Toast.makeText(signinActivity, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, loginResponse.data.getData().getToken());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, loginResponse.data.getData().getCustomer().getCustomerId());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, loginResponse.data.getData().getCustomer().getCustomerFirstName());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, loginResponse.data.getData().getCustomer().getEmailId());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_GENDER, loginResponse.data.getData().getCustomer().getGender());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, loginResponse.data.getData().getCustomer().getMobileNo());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, loginResponse.data.getData().getCustomer().getPassword());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, loginResponse.data.getData().getCustomer().getConfirmPassword());
                                preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);

                                preferenceHandler.saveData(PreferenceHandler.LOGIN_CUSTOMER, new Gson().toJson(loginResponse.data.getData().getCustomer()));
                                redirectToHomePage();
                                try {
//                                    Freshchat.resetUser(requireContext());
//                                    FreshchatUser freshchatUser = Freshchat.getInstance(requireContext()).getUser();
//                                    freshchatUser.setFirstName(loginResponse.data.getData().getCustomer().getCustomerFirstName());
//                                    freshchatUser.setEmail(loginResponse.data.getData().getCustomer().getEmailId());
//                                    Freshchat.getInstance(requireContext()).setUser(freshchatUser);
                                    Log.e("signIn", loginResponse.data.getData().getCustomer().getCustomerFirstName());

                                    InAppEvents.logUserEmailProperty(loginResponse.data.getData().getCustomer().getEmailId());
                                    InAppEvents.logUserPhoneProperty(loginResponse.data.getData().getCustomer().getMobileNo());
                                    InAppEvents.logUserGenderProperty(loginResponse.data.getData().getCustomer().getGender());
                                    InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                                    String loyalityJson = gson.toJson(loginResponse.data.getData().getCustomer().getLoyalty());
                                    InAppEvents.logUserLoyalityProperty(loyalityJson);
                                    InAppEvents.userLoginEvent(loginResponse.data.getData().getCustomer().getCustomerId());
                                    try {
                                        InAppEvents.logUserLoyalityTag(loginResponse.data.getData().getCustomer().getLoyalty().getPoints());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }

                            } else {
                                try {
                                    GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), loginResponse.data.getMessage());
                                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case ERROR:
                        try {
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), loginResponse.message);
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }

            });
        } else {
            signinActivity.showProgressBar(false);
            Toast.makeText(getContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
        }

    }

    private void redirectToHomePage() {
//        signinActivity.hideFragment();
        Intent intent = new Intent(signinActivity, DashboardActivity.class);
        startActivity(intent);
        signinActivity.finish();
    }

    @Override
    public void onActivityResult(int resultCode, int requestCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            redirectToHomePage();
            String email = account.getEmail();
            String name = account.getGivenName();
            String lastName = account.getFamilyName();
            String id = account.getId();
            userRegistration(name, lastName, email);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(signinActivity, "Signin Failed. Please Try Again", Toast.LENGTH_SHORT).show();
//            updateUI(null);
        }
    }

    private void facebookSignin() {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday"));
    }


    private void userRegistration(String firstName, String lastName, String email) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("customerFirstName", firstName);
        jsonParams.put("customerLastName", lastName);
//                jsonParams.put("mobileNo", phone);
        jsonParams.put("active", true);
        jsonParams.put("emailId", email);
//                jsonParams.put("gender", selectedGender);
//                jsonParams.put("nationality", selectedNationality);
//                jsonParams.put("dob", selectedDate);
        signinActivity.showProgressBar(true);
        loginViewModel.userRegistration(jsonParams).observe(getActivity(), signupResponse -> {
            signinActivity.showProgressBar(false);
            switch (signupResponse.status) {
                case SUCCESS:
                    if (signupResponse.data.getStatusCode() == 200) {
                        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_TOKEN, signupResponse.data.getData().getToken());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_USER_ID, signupResponse.data.getData().getuser().getCustomerId());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_USERNAME, signupResponse.data.getData().getuser().getCustomerFirstName());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_EMAIL, signupResponse.data.getData().getuser().getEmailId());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PASSWORD, signupResponse.data.getData().getuser().getPassword());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_CONFIRM_PASSWORD, signupResponse.data.getData().getuser().getConfirmPassword());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PHONENUMBER, signupResponse.data.getData().getuser().getMobileNo());
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_STATUS, true);

                        signinActivity.getmFirebaseAnalytics().setUserProperty("email", signupResponse.data.getData().getuser().getEmailId());
                        signinActivity.getmFirebaseAnalytics().setUserProperty("country", signupResponse.data.getData().getuser().getNationality());
                        signinActivity.getmFirebaseAnalytics().setUserProperty("gender", signupResponse.data.getData().getuser().getGender());
                        signinActivity.getmFirebaseAnalytics().setUserProperty("date_of_birth", signupResponse.data.getData().getuser().getDob().toString());
                        signinActivity.getmFirebaseAnalytics().setUserProperty("phone", signupResponse.data.getData().getuser().getMobileNo());

                        try {
//                            Freshchat.resetUser(requireContext());
//                            FreshchatUser freshchatUser = Freshchat.getInstance(requireContext()).getUser();
//                            freshchatUser.setFirstName(signupResponse.data.getData().getuser().getCustomerFirstName());
//                            freshchatUser.setEmail(signupResponse.data.getData().getuser().getEmailId());
//                            Freshchat.getInstance(requireContext()).setUser(freshchatUser);
                            Log.e("signIn", signupResponse.data.getData().getuser().getCustomerFirstName());

                            InAppEvents.logUserEmailProperty(signupResponse.data.getData().getuser().getEmailId());
                            InAppEvents.logUserPhoneProperty(signupResponse.data.getData().getuser().getMobileNo());
                            InAppEvents.logUserGenderProperty(signupResponse.data.getData().getuser().getGender());
                            InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                            String loyalityJson = gson.toJson(signupResponse.data.getData().getuser().getLoyalty());
                            InAppEvents.logUserLoyalityProperty(loyalityJson);
                            try {
                                InAppEvents.logUserLoyalityTag(signupResponse.data.getData().getuser().getLoyalty().getPoints());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }

                        Intent intent = new Intent(signinActivity, DashboardActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), signupResponse.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog(getContext().getResources().getString(R.string.error), signupResponse.message);
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

    }

}



