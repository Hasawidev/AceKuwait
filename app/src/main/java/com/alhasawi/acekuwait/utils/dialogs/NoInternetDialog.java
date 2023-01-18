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
import com.alhasawi.acekuwait.databinding.LayoutDialogNoInternetBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;

public class NoInternetDialog extends DialogFragment {
    NoInternetDialogInterface noInternetDialogInterface;
    LayoutDialogNoInternetBinding dialogNoInternetBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        dialogNoInternetBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_no_internet, null, false);
        dialogNoInternetBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (noInternetDialogInterface != null)
                        noInternetDialogInterface.onCancel();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialogNoInternetBinding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (noInternetDialogInterface != null)
                        noInternetDialogInterface.onRetry();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return dialogNoInternetBinding.getRoot();


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

    public void setClickListener(NoInternetDialogInterface dialogGeneralInterface) {
        this.noInternetDialogInterface = dialogGeneralInterface;
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

    public interface NoInternetDialogInterface {
        void onCancel();

        void onRetry();
    }
}