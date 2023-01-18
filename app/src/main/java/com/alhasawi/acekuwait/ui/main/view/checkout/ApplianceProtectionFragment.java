package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.view.View;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentApplianceProtectionBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class ApplianceProtectionFragment extends BaseFragment {
    FragmentApplianceProtectionBinding fragmentApplianceProtectionBinding;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_appliance_protection;
    }

    @Override
    protected void setup() {

        fragmentApplianceProtectionBinding = (FragmentApplianceProtectionBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();

        fragmentApplianceProtectionBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyCart_1_Fragment(), null, true, false);
            }
        });
    }
}
