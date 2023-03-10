package com.alhasawi.acekuwait.utils.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutDialogLoginSignupBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;

public class LoginSignupDialog extends DialogFragment {

    DashboardActivity dashboardActivity;

    public LoginSignupDialog(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutDialogLoginSignupBinding loginSignupBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_login_signup, null, true);
        loginSignupBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        loginSignupBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                dismiss();
                //dashboardActivity.finish();
            }
        });

        return loginSignupBinding.getRoot();
    }

}
