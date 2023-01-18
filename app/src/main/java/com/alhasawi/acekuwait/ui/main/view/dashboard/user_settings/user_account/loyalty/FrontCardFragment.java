package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.loyalty;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentFrontCardBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;

public class FrontCardFragment extends BaseFragment {
    FragmentFrontCardBinding fragmentFrontCardBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_front_card;
    }

    @Override
    protected void setup() {
        fragmentFrontCardBinding = (FragmentFrontCardBinding) viewDataBinding;
    }
}
