package com.alhasawi.acekuwait.utils.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutDialogRegSuccessBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;

public class RegistrationSuccessDialog extends DialogFragment {
    SigninActivity signinActivity;

    public RegistrationSuccessDialog(SigninActivity signinActivity) {
        this.signinActivity = signinActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutDialogRegSuccessBinding regSuccessBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_reg_success, null, true);
        regSuccessBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(signinActivity, DashboardActivity.class);
                startActivity(intent);
                signinActivity.finishAffinity();
            }
        });

        return regSuccessBinding.getRoot();
    }

}

