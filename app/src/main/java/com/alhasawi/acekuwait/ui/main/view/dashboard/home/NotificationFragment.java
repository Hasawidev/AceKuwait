package com.alhasawi.acekuwait.ui.main.view.dashboard.home;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentNotificationsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;

public class NotificationFragment extends BaseFragment {
    FragmentNotificationsBinding fragmentNotificationsBinding;
    DashboardActivity dashboardActivity;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected void setup() {
        fragmentNotificationsBinding = (FragmentNotificationsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        dashboardActivity.handleActionMenuBar(true, true);
    }
}
