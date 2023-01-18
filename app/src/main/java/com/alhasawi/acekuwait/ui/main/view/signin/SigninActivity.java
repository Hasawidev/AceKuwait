package com.alhasawi.acekuwait.ui.main.view.signin;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.ActivitySigninBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SigninActivity extends BaseActivity {

    ActivitySigninBinding activitySigninBinding;
    FirebaseAnalytics mFirebaseAnalytics;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void setup() {
        activitySigninBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN).saveData(PreferenceHandler.SHOW_REG_PAGE, false);
    }

    public GoogleSignInAccount checksForAnExistingUser() {
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        return account;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void replaceFragment(Fragment fragment, Bundle bundle, Boolean toBeAddedToStack, Boolean clearHistory) {
        activitySigninBinding.fragmentReplacerSignin.setVisibility(View.VISIBLE);
        replaceFragment(R.id.fragment_replacer_signin, fragment, bundle, toBeAddedToStack, clearHistory);
    }


    public void showProgressBar(boolean shouldShow) {
        if (shouldShow)
            activitySigninBinding.progressBar.setVisibility(View.VISIBLE);
        else
            activitySigninBinding.progressBar.setVisibility(View.GONE);
    }

    public void hideFragment() {
        activitySigninBinding.fragmentReplacerSignin.setVisibility(View.GONE);
    }

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideFragment();
    }
}