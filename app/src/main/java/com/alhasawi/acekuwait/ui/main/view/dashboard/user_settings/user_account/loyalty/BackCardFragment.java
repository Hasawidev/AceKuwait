package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.loyalty;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentBackCardBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;

public class BackCardFragment extends BaseFragment {
    FragmentBackCardBinding fragmentBackCardBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_back_card;
    }

    @Override
    protected void setup() {

        fragmentBackCardBinding = (FragmentBackCardBinding) viewDataBinding;
    }
}
