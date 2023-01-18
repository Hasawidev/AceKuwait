package com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.FragmentUserSettingsBinding;
import com.alhasawi.acekuwait.ui.base.BaseFragment;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.change_language.ChangeLanguageFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general.AboutUsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general.ContactUsFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general.FAQFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.general.PrivatePolicyFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.UserAccountFragment;
import com.alhasawi.acekuwait.ui.main.view.dashboard.user_settings.user_account.loyalty.LoyaltyFragment;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.alhasawi.acekuwait.utils.dialogs.LoginSignupDialog;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserSettingsFragment extends BaseFragment implements View.OnClickListener {
    FragmentUserSettingsBinding fragmentUserSettingsBinding;
    DashboardActivity dashboardActivity;
    boolean isLoggedIn = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_settings;
    }

    @Override
    protected void setup() {
        fragmentUserSettingsBinding = (FragmentUserSettingsBinding) viewDataBinding;
        dashboardActivity = (DashboardActivity) getActivity();
        PreferenceHandler preferenceHandler = new PreferenceHandler(getContext(), PreferenceHandler.TOKEN_LOGIN);
        String username = preferenceHandler.getData(PreferenceHandler.LOGIN_USERNAME, "");
        fragmentUserSettingsBinding.tvUserName.setText(username);
        isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (isLoggedIn) {
            String gender = preferenceHandler.getData(PreferenceHandler.LOGIN_GENDER, "");
            if (gender.equalsIgnoreCase("f"))
                fragmentUserSettingsBinding.imageViewUser.setImageDrawable(getActivity().getDrawable(R.drawable.female));
            else if (gender.equalsIgnoreCase("m"))
                fragmentUserSettingsBinding.imageViewUser.setImageDrawable(getActivity().getDrawable(R.drawable.male));
        }
        if (isLoggedIn) {
            fragmentUserSettingsBinding.btnLoginSignup.setVisibility(View.GONE);
            fragmentUserSettingsBinding.tvUserName.setVisibility(View.VISIBLE);
            fragmentUserSettingsBinding.tvSignout.setVisibility(View.VISIBLE);
        } else {
            fragmentUserSettingsBinding.btnLoginSignup.setVisibility(View.VISIBLE);
            fragmentUserSettingsBinding.tvUserName.setVisibility(View.GONE);
            fragmentUserSettingsBinding.tvSignout.setVisibility(View.GONE);
        }

        fragmentUserSettingsBinding.tvChangeLanguage.setOnClickListener(this);
        fragmentUserSettingsBinding.btnLoginSignup.setOnClickListener(this);
        fragmentUserSettingsBinding.tvMyAccount.setOnClickListener(this);
        fragmentUserSettingsBinding.tvMyLoyalty.setOnClickListener(this);
        fragmentUserSettingsBinding.tvFaq.setOnClickListener(this);
        fragmentUserSettingsBinding.tvAboutUs.setOnClickListener(this);
        fragmentUserSettingsBinding.tvPrivatePolicy.setOnClickListener(this);
        fragmentUserSettingsBinding.tvContactus.setOnClickListener(this);
        fragmentUserSettingsBinding.tvSignout.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewFacebook.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewYoutube.setOnClickListener(this);
        fragmentUserSettingsBinding.imageViewInstagram.setOnClickListener(this);
//        fragmentUserSettingsBinding.imageViewSnapChat.setOnClickListener(this);
//        fragmentUserSettingsBinding.imageViewTiktok.setOnClickListener(this);
//        fragmentUserSettingsBinding.imageViewSupport.setOnClickListener(this);

        boolean isNotificationEnabled = preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false);
        if (isNotificationEnabled)
            fragmentUserSettingsBinding.swOnOff.setChecked(true);
        else
            fragmentUserSettingsBinding.swOnOff.setChecked(false);

        fragmentUserSettingsBinding.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    preferenceHandler.saveData(PreferenceHandler.NOTIFICATION_STATUS, true);
                    FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);
                    InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                } else {
                    preferenceHandler.saveData(PreferenceHandler.NOTIFICATION_STATUS, false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(AppConstants.APP_NAME);
                    InAppEvents.logUserNotificationProperty("" + preferenceHandler.getData(PreferenceHandler.NOTIFICATION_STATUS, false));
                }
            }
        });
        fragmentUserSettingsBinding.lvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashboardActivity.onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentUserSettingsBinding.fragmentReplacerSettings.setVisibility(View.GONE);
        dashboardActivity.handleActionMenuBar(true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginSignup:
                Intent intent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(intent);
                break;
            case R.id.tvMyAccount:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new UserAccountFragment(), null, true, false);
                break;
            case R.id.tvMyLoyalty:
                if (isLoggedIn) {
                    dashboardActivity.handleActionMenuBar(true, false);
                    dashboardActivity.replaceFragment(R.id.fragment_replacer, new LoyaltyFragment(), null, true, false);
                } else {
                    LoginSignupDialog loginSignupDialog = new LoginSignupDialog(dashboardActivity);
                    loginSignupDialog.show(getParentFragmentManager(), "LOGIN_SIGNUP_DIALOG");
                }
                break;
            case R.id.tvChangeLanguage:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new ChangeLanguageFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.tvFaq:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new FAQFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.tvAboutUs:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new AboutUsFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.tvPrivatePolicy:
                dashboardActivity.replaceFragment(R.id.fragment_replacer, new PrivatePolicyFragment(), null, true, false);
                dashboardActivity.hideToolBar();
                break;
            case R.id.tvContactus:
                fragmentUserSettingsBinding.fragmentReplacerSettings.setVisibility(View.VISIBLE);
                dashboardActivity.replaceFragment(R.id.fragment_replacer_settings, new ContactUsFragment(), null, true, false);
                break;
            case R.id.tvSignout:
//                if (dashboardActivity.isAlreadyLoggedinWithFacebbok())
//                    dashboardActivity.disconnectFromFacebook();
                dashboardActivity.clearPreferences();
                Intent signoutIntent = new Intent(dashboardActivity, SigninActivity.class);
                startActivity(signoutIntent);
                dashboardActivity.finish();
                break;
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
            case R.id.imageViewSupport:
                /*if (dashboardActivity.zendeskMessagingEnabled)
                    Messaging.instance().showMessaging(dashboardActivity);*/
                startFreshChat();
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

