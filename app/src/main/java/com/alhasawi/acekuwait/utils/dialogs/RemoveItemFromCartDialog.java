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
import com.alhasawi.acekuwait.databinding.LayoutDialogRemoveItemFromCartBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;

public class RemoveItemFromCartDialog extends DialogFragment {
    RemoveItemDialogInterface removeItemDialogInterface;
    LayoutDialogRemoveItemFromCartBinding removeItemFromCartBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        removeItemFromCartBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_remove_item_from_cart, null, false);
        removeItemFromCartBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (removeItemDialogInterface != null)
                        removeItemDialogInterface.onNo();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        removeItemFromCartBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (removeItemDialogInterface != null)
                        removeItemDialogInterface.onYes();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return removeItemFromCartBinding.getRoot();


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

    public void setClickListener(RemoveItemDialogInterface removeItemDialogInterface) {
        this.removeItemDialogInterface = removeItemDialogInterface;
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

    public interface RemoveItemDialogInterface {
        void onYes();

        void onNo();
    }
}