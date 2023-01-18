package com.alhasawi.acekuwait.ui.main.view.signin.login;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentResetPasswordBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.ForgotPasswordViewModel;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;
import com.google.gson.Gson;

public class ResetPasswordFragment extends BaseFragment {
    FragmentResetPasswordBinding fragmentResetPasswordBinding;
    SigninActivity signinActivity;
    ForgotPasswordViewModel forgotPasswordViewModel;
    String email = "";
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_reset_password;
    }

    @Override
    protected void setup() {

        fragmentResetPasswordBinding = (FragmentResetPasswordBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        forgotPasswordViewModel = new ViewModelProvider(getActivity()).get(ForgotPasswordViewModel.class);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("email"))
                email = bundle.getString("email");
        }

        fragmentResetPasswordBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(signinActivity);
                String newPassword = fragmentResetPasswordBinding.edtNewPassword.getText().toString();
                String confirmPassword = fragmentResetPasswordBinding.edtConfirmPassword.getText().toString();
                if (newPassword.length() < 6)
                    fragmentResetPasswordBinding.tvPasswordMinCharacterError.setVisibility(View.VISIBLE);
                else
                    fragmentResetPasswordBinding.tvPasswordMinCharacterError.setVisibility(View.GONE);
                if (!newPassword.equals(confirmPassword))
                    fragmentResetPasswordBinding.tvConfirmPswdError.setVisibility(View.VISIBLE);
                else
                    fragmentResetPasswordBinding.tvConfirmPswdError.setVisibility(View.GONE);

                if (confirmPassword.equals("") && newPassword.equals("")) {
                    GeneralDialog generalDialog = new GeneralDialog(getResources().getString(R.string.error), getResources().getString(R.string.password_field_empty));
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                }
                if (newPassword.length() >= 6 && newPassword.equals(confirmPassword)) {
                    callResetPasswordApi(email, newPassword, confirmPassword);
                    hideSoftKeyboard(signinActivity);
                }
            }
        });

    }


    private void callResetPasswordApi(String email, String newPassword, String confirmPassword) {

        signinActivity.showProgressBar(true);
        forgotPasswordViewModel.resetPassword(email, newPassword, confirmPassword).observe(getActivity(), forgotPasswordResponseResource -> {
            //Log.e("callResetPassword", gson.toJson(forgotPasswordResponseResource));
            signinActivity.showProgressBar(false);
            switch (forgotPasswordResponseResource.status) {
                case SUCCESS:
                    if (forgotPasswordResponseResource.data.getStatusCode() == 200) {
                        GeneralDialog generalDialog = new GeneralDialog("Success", "Password Changed Successfully !. Login Now");
                        generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                        signinActivity.replaceFragment(new LoginFragment(), null, true, true);
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", forgotPasswordResponseResource.message);
                    generalDialog.show(getChildFragmentManager(), "GENERAL DIALOG");
                    break;
            }
        });

    }
}
