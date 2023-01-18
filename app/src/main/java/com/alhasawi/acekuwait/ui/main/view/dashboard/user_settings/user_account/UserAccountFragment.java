package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentUserAccountBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.address_book.AddressBookFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.my_returns.MyReturnsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.order_history.OrderHistoryFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.profile.UserProfileFragment;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.LoginSignupDialog;
//import com.freshchat.consumer.sdk.Freshchat;

public class UserAccountFragment extends BaseFragment implements View.OnClickListener {
    FragmentUserAccountBinding fragmentUserAccountBinding;
    DashboardActivity dashboardActivity;
    boolean isLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_account;
    }

    @Override
    protected void setup() {
        fragmentUserAccountBinding = (FragmentUserAccountBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        dashboardActivity.handleActionMenuBar(true, false);
        dashboardActivity.handleActionBarIcons(false);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn) {
            String gender = preferenceHandler.getData(PreferenceHandler.LOGIN_GENDER, "");
            if (gender.equalsIgnoreCase("f"))
                fragmentUserAccountBinding.imageViewUser.setImageDrawable(getActivity().getDrawable(R.drawable.female));
            else if (gender.equalsIgnoreCase("m"))
                fragmentUserAccountBinding.imageViewUser.setImageDrawable(getActivity().getDrawable(R.drawable.male));
        }
        fragmentUserAccountBinding.tvMyProfile.setOnClickListener(this);
        fragmentUserAccountBinding.tvMyOrders.setOnClickListener(this);
        fragmentUserAccountBinding.tvMyReturns.setOnClickListener(this);
        fragmentUserAccountBinding.tvWishlist.setOnClickListener(this);
        fragmentUserAccountBinding.tvAddressBook.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewFacebook.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewInstagram.setOnClickListener(this);
//        fragmentUserAccountBinding.imageViewSnapChat.setOnClickListener(this);
//        fragmentUserAccountBinding.imageViewTiktok.setOnClickListener(this);
        fragmentUserAccountBinding.imageViewYoutube.setOnClickListener(this);
        fragmentUserAccountBinding.lvBack.setOnClickListener(this);
        fragmentUserAccountBinding.imageView25.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMyProfile:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new UserProfileFragment(), null, true, false);
                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
//            case R.id.constraintLayoutPreference:
//                dashboardActivity.handleActionMenuBar(true, false, "My Preference");
//                dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyPreferenceFragment(), null, true, false);
//                break;
            case R.id.tvMyOrders:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(false, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new OrderHistoryFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.tvMyReturns:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(false, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new MyReturnsFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.lv_back:
                dashboardActivity.onBackPressed();
                break;
//            case R.id.constraintLayoutReferEarn:
//                break;
            case R.id.tvWishlist:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new WishListFragment(), null, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.tvAddressBook:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, false);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("is_from_dashboard", true);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new AddressBookFragment(), bundle, true, false);

                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
//            case R.id.btn_loginSignup:
//                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
//                startActivity(intent);
//                dashboardActivity.finish();
//                break;
            case R.id.imageViewFacebook:
                launchBrowser(AppConstants.ACE_FACEBOOK, "FACEBOOK");
                break;
            case R.id.imageViewInstagram:
                launchBrowser(AppConstants.ACE_INSTAGRAM, "INSTAGRAM");
                break;
            case R.id.imageViewYoutube:
                launchBrowser(AppConstants.ACE_YOUTUBE, "YOUTUBE");
                break;
//            case R.id.imageViewSnapChat:
//                launchBrowser(AppConstants.ACE_SNAPCHAT);
//                break;
//            case R.id.imageViewTiktok:
//                launchBrowser(AppConstants.ACE_TIKTOK);
//                break;
            case R.id.imageView25:
//                startFreshChat();
                break;
            default:
                break;
        }
    }

    private void launchBrowser(String url, String channel) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        try {
            InAppEvents.logSocialEvent(
                    channel
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void startFreshChat() {
//        Freshchat.showConversations(requireView().getContext());
        try {
            InAppEvents.logContactUsEvent(
                    "chat"
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
