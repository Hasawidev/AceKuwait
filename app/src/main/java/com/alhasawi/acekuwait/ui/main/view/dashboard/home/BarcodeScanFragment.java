package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentBarcodeScannerBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class BarcodeScanFragment extends BaseFragment {
    private FragmentBarcodeScannerBinding fragmentBarcodeScannerBinding;
    private DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_barcode_scanner;
    }

    @Override
    protected void setup() {

        fragmentBarcodeScannerBinding = (FragmentBarcodeScannerBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();

    }
}
