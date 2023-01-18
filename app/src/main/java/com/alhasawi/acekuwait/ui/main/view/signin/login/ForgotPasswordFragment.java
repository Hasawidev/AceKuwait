package com.alhasawi.acekuwait.ui.main.view.signin.login;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentForgotPasswordBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ForgotPasswordViewModel;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;


public class ForgotPasswordFragment extends BaseFragment {

    FragmentForgotPasswordBinding forgotPasswordBinding;
    ForgotPasswordViewModel forgotPasswordViewModel;
    SigninActivity signinActivity;
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    protected void setup() {
        forgotPasswordBinding = (FragmentForgotPasswordBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        forgotPasswordViewModel = new ViewModelProvider(getActivity()).get(ForgotPasswordViewModel.class);
        forgotPasswordBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = forgotPasswordBinding.edtEmail.getText().toString().trim();
                callForgotPasswordApi(emailId);
                hideSoftKeyboard(signinActivity);

            }
        });
    }

    private void callForgotPasswordApi(String email) {

        signinActivity.showProgressBar(true);
        forgotPasswordViewModel.forgotPassword(email).observe(getActivity(), forgotPasswordResponseResource -> {
            signinActivity.showProgressBar(false);
            //Log.e("forgotPassResponse", gson.toJson(forgotPasswordResponseResource));
            switch (forgotPasswordResponseResource.status) {
                case SUCCESS:
                    if (forgotPasswordResponseResource.data.getStatusCode() == 200) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        try {
                            InAppEvents.logForgotPasswordEvent(forgotPasswordResponseResource.data.getData().getCustomer().getCustomerId());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        signinActivity.replaceFragment(new VerifyEmailFragment(), bundle, false, false);
                    } else {
                        try {
                            GeneralDialog generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.data.getMessage());
                            generalDialog.show(getParentFragmentManager(), "GENERAL DIALOG");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case LOADING:
                    break;
                case ERROR:
                    try {
                        GeneralDialog generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.message);
                        generalDialog.show(getParentFragmentManager(), "GENERAL DIALOG");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

    }
}
