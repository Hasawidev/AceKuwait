package com.alhasawi.acekuwait.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutDialogDisabledCartBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class DisabledCartDialog extends DialogFragment {

    DashboardActivity dashboardActivity;

    public DisabledCartDialog(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutDialogDisabledCartBinding dialogDisabledCartBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_disabled_cart, null, true);
        dialogDisabledCartBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialogDisabledCartBinding.getRoot();
    }


}
