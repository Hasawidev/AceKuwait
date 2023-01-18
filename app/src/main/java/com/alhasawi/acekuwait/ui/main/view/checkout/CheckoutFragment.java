package com.alhasawi.acekuwait.ui.main.view.checkout;

import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentPlaceOrderBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.viewmodel.PlaceOrderViewModel;

public class CheckoutFragment extends BaseFragment implements View.OnClickListener {
    FragmentPlaceOrderBinding fragmentPlaceOrderBinding;
    DashboardActivity dashboardActivity;
    PlaceOrderViewModel placeOrderViewModel;
    boolean disableShippingPaymentAfterReview = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_place_order;
    }

    @Override
    protected void setup() {

        fragmentPlaceOrderBinding = (FragmentPlaceOrderBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        placeOrderViewModel = new ViewModelProvider(this).get(PlaceOrderViewModel.class);
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(false, true);
        dashboardActivity.handleActionBarIcons(false);
//        fragmentPlaceOrderBinding.tvShipping.setOnClickListener(this);
        fragmentPlaceOrderBinding.tvPayment.setOnClickListener(this);
        replaceFragment(R.id.fragment_replacer_checkout, new OrderPaymentFragment(this), null, false, true);
        handleTabView("payment");
//        setupViewPager(fragmentPlaceOrderBinding.viewpagerCheckout);
//        fragmentPlaceOrderBinding.tabLayoutCheckout.setupWithViewPager(fragmentPlaceOrderBinding.viewpagerCheckout);
//        fragmentPlaceOrderBinding.tabLayoutCheckout.setTabTextColors(ContextCompat.getColorStateList(getContext(), R.color.txt_clr_blue));
//        fragmentPlaceOrderBinding.tabLayoutCheckout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.red));
    }

    public void handleTabView(String fragment) {
//        if (fragment_notify_me.xml.equalsIgnoreCase("shipping")) {
//            fragmentPlaceOrderBinding.viewShipping.setVisibility(View.VISIBLE);
//            fragmentPlaceOrderBinding.tvShipping.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
//            fragmentPlaceOrderBinding.tvPayment.setTextColor(getActivity().getResources().getColor(R.color.cart_grey));
//            fragmentPlaceOrderBinding.viewPayment.setVisibility(View.GONE);
//            fragmentPlaceOrderBinding.tvReview.setTextColor(getActivity().getResources().getColor(R.color.cart_grey));
//            fragmentPlaceOrderBinding.viewReview.setVisibility(View.GONE);
//        }
        if (fragment.equalsIgnoreCase("payment")) {
//            fragmentPlaceOrderBinding.viewShipping.setVisibility(View.GONE);
//            fragmentPlaceOrderBinding.tvShipping.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
            fragmentPlaceOrderBinding.tvPayment.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
            fragmentPlaceOrderBinding.viewPayment.setVisibility(View.VISIBLE);
            fragmentPlaceOrderBinding.tvReview.setTextColor(getActivity().getResources().getColor(R.color.cart_grey));
            fragmentPlaceOrderBinding.viewReview.setVisibility(View.GONE);
        }
        if (fragment.equalsIgnoreCase("review")) {
            disableShippingPaymentAfterReview = true;
//            fragmentPlaceOrderBinding.viewShipping.setVisibility(View.GONE);
//            fragmentPlaceOrderBinding.tvShipping.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
            fragmentPlaceOrderBinding.tvPayment.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
            fragmentPlaceOrderBinding.viewPayment.setVisibility(View.GONE);
            fragmentPlaceOrderBinding.tvReview.setTextColor(getActivity().getResources().getColor(R.color.ace_theme_color));
            fragmentPlaceOrderBinding.viewReview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvShipping:
//                replaceFragment(R.id.fragment_replacer_checkout, new OrderShippingFragment(this), null, false, true);
//                handleTabView("shipping");
//                break;
            case R.id.tvPayment:
                if (!disableShippingPaymentAfterReview) {
                    replaceFragment(R.id.fragment_replacer_checkout, new OrderPaymentFragment(this), null, false, true);
                    handleTabView("payment");
                }
                break;
            default:
                break;
        }
    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getParentFragmentManager());
//        adapter.addFragment(new OrderShippingFragment(), "");
//        adapter.addFragment(new OrderPaymentFragment(), "");
//        adapter.addFragment(new OrderFragment(), "");
//        viewPager.setAdapter(adapter);
//    }


//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment_notify_me.xml, String name) {
//            mFragmentList.add(fragment_notify_me.xml);
//            mFragmentTitleList.add(name);
//
//        }
//    }
}
