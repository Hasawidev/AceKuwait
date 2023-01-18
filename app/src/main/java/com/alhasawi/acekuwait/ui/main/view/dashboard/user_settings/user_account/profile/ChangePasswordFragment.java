package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.profile;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentChangePasswordBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.UserProfileViewModel;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.GeneralDialog;

public class ChangePasswordFragment extends BaseFragment {
    FragmentChangePasswordBinding fragmentChangePasswordBinding;
    UserProfileViewModel userProfileViewModel;
    DashboardActivity dashboardActivity;
    private String languageId = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected void setup() {
        fragmentChangePasswordBinding = (FragmentChangePasswordBinding) viewDataBinding;
        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.setTitle("Change Password");
        dashboardActivity.handleActionBarIcons(false);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        languageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        fragmentChangePasswordBinding.btnUpdatePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPswd = fragmentChangePasswordBinding.edtOldPswd.getText().toString();
                String newPswd = fragmentChangePasswordBinding.edtNewPswd.getText().toString();
                String reenteredNewPswd = fragmentChangePasswordBinding.edtReenterNewPswd.getText().toString();

                if (newPswd.equals(reenteredNewPswd)) {
                    callUpdatePasswordApi(oldPswd, newPswd);
                } else {
                    Toast.makeText(getActivity(), "Password did not match", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    private void callUpdatePasswordApi(String oldPswd, String newPswd) {

        PreferenceHandler preferenceHandler = new PreferenceHandler(getActivity(), PreferenceHandler.TOKEN_LOGIN);
        String userId = preferenceHandler.getData(PreferenceHandler.LOGIN_USER_ID, "");
        String sessiontoken = preferenceHandler.getData(PreferenceHandler.LOGIN_TOKEN, "");
        fragmentChangePasswordBinding.progressBar.setVisibility(View.VISIBLE);
        userProfileViewModel.changePassword(userId, oldPswd, newPswd, sessiontoken, languageId).observe(getActivity(), changePasswordResponseResource -> {
            switch (changePasswordResponseResource.status) {
                case SUCCESS:
                    if (changePasswordResponseResource.data.getStatusCode() == 200) {
                        Toast.makeText(dashboardActivity, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStackImmediate();
                    } else {
                        GeneralDialog generalDialog = new GeneralDialog("Error", changePasswordResponseResource.data.getMessage());
                        generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    }

                    break;
                case LOADING:
                    break;
                case ERROR:
                    GeneralDialog generalDialog = new GeneralDialog("Error", changePasswordResponseResource.message);
                    generalDialog.show(getParentFragmentManager(), "GENERAL_DIALOG");
                    break;
            }
            fragmentChangePasswordBinding.progressBar.setVisibility(View.GONE);
        });
    }
}
