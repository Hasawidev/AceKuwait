package com.alhasawi.acekuwait.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.LayoutCartDialogNewDesignBinding;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.checkout.MyCart_1_Fragment;

public class CartDialog extends DialogFragment {

    DashboardActivity dashboardActivity;

    public CartDialog(DashboardActivity dashboardActivity) {
        this.dashboardActivity = dashboardActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutCartDialogNewDesignBinding cartDialogNewDesignBinding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_dialog_new_design, null, true);
        cartDialogNewDesignBinding.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cartDialogNewDesignBinding.btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.handleActionMenuBar(true, true);
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
                dismiss();
            }
        });
        return cartDialogNewDesignBinding.getRoot();
    }

//    @Override
//    public Dialog onCreateDialog(final Bundle savedInstanceState) {
//        LayoutInflater inflater = getLayoutInflater();
//        LayoutCartDialogNewDesignBinding cartDialogNewDesignBinding = DataBindingUtil.inflate(inflater, R.layout.layout_cart_dialog_new_design, null, true);
//
//        cartDialogNewDesignBinding.btnContinueShopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        cartDialogNewDesignBinding.btnViewCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(dashboardActivity, MyCartActivity.class);
//                startActivity(intent);
//                dismiss();
//            }
//        });
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(cartDialogNewDesignBinding.getRoot());
//        return builder.create();
//    }
}
