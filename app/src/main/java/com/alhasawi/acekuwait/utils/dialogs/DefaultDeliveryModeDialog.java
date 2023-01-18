package com.alhasawi.acekuwait.utils.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutDialogDefaultDeliveryModeBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;

public class DefaultDeliveryModeDialog extends DialogFragment {
    DefaultDeliveryModeInterface defaultDeliveryModeInterface;
    LayoutDialogDefaultDeliveryModeBinding dialogDefaultDeliveryModeBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dialogDefaultDeliveryModeBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_default_delivery_mode, null, false);
        dialogDefaultDeliveryModeBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (defaultDeliveryModeInterface != null)
                        defaultDeliveryModeInterface.onNo();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialogDefaultDeliveryModeBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (defaultDeliveryModeInterface != null)
                        defaultDeliveryModeInterface.onYes();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return dialogDefaultDeliveryModeBinding.getRoot();


    }


    @Override
    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void setClickListener(DefaultDeliveryModeInterface defaultDeliveryModeInterface) {
        this.defaultDeliveryModeInterface = defaultDeliveryModeInterface;
    }

    public void showDialog(BaseActivity activity) {
        try {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            show(ft, "Frag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowingCurrently() {
        return this.getDialog().isShowing();
    }

    public interface DefaultDeliveryModeInterface {
        void onYes();

        void onNo();
    }
}