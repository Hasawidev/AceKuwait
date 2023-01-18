package com.alhasawi.acekuwait.ui.main.view.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.databinding.ActivitySplashBinding;
import com.alhasawi.acekuwait.ui.base.BaseActivity;
import com.alhasawi.acekuwait.ui.main.view.DashboardActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.SigninActivity;
import com.alhasawi.acekuwait.ui.main.view.signin.select_language.SelectLanguageActivity;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;


public class SplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";
    private static String SKU_ID = "productID";
    private static String MAIN_CATEGORY_ID = "mainCategoryID";
    private static String CATEGORY_ID = "categoryID";
    private static String SHOW_REGISTRATION = "showReg";
    ActivitySplashBinding activitySplashBinding;
    boolean isLoggedin, isLanguageAlreadyShown;
    Bundle dataBundle = new Bundle();
    private Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject linkProperties, BranchError error) {
            // do stuff with deep link data (nav to page, display content, etc)
            if (error == null) {
                PreferenceHandler preferenceHandler = new PreferenceHandler(SplashActivity.this, PreferenceHandler.TOKEN_LOGIN);

                try {
                    if (linkProperties.has("main_category_id")) {
                        String categoryId = linkProperties.getString("main_category_id");
//                        dataBundle.putString(MAIN_CATEGORY_ID, categoryId);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_MAIN_CATEGORY_ID, categoryId);

                    }
                    if (linkProperties.has("sku_id")) {
                        String productId = linkProperties.getString("sku_id");
//                        dataBundle.putString(SKU_ID, productId);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_PRODUCT_ID, productId);

                    }
                    if (linkProperties.has("category_id")) {
                        String cateogryId = linkProperties.getString("category_id");
//                        dataBundle.putString(CATEGORY_ID, cateogryId);
                        preferenceHandler.saveData(PreferenceHandler.LOGIN_BRANCH_CATEGORY_ID, cateogryId);

                    }
                    if (linkProperties.has("show_reg")) {
                        String showReg = linkProperties.getString("show_reg");
                        redirectToRegistration(showReg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void redirectToRegistration(String showReg) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        boolean isLoggedIn = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        if (!showReg.equals("")) {
            if (isLoggedIn) {
                dataBundle.putString(SHOW_REGISTRATION, showReg);
                deepLinkRedirection(dataBundle);
                preferenceHandler.saveData(PreferenceHandler.SHOW_REG_PAGE, true);
            } else {
                Intent intent = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(intent);
                this.finish();
            }
        } else {
            preferenceHandler.saveData(PreferenceHandler.SHOW_REG_PAGE, false);
        }
    }

    @Override
    protected void setup() {
        activitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        PreferenceHandler preferenceHandler = new PreferenceHandler(getApplicationContext(), PreferenceHandler.TOKEN_LOGIN);
        isLoggedin = preferenceHandler.getData(PreferenceHandler.LOGIN_STATUS, false);
        isLanguageAlreadyShown = preferenceHandler.getData(PreferenceHandler.HAS_LANGUAGE_PAGE_SHOWN, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleDeepLink();
            }
        }, 2000);
    }

    private void handleDeepLink() {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        redirectToHome(deepLink);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("dynamicLink", "getDynamicLink:onFailure", e);
                        redirectToHome(null);
                    }
                });
    }

    private void redirectToHome(@Nullable Uri deepLink) {
        if (deepLink != null) {
            if (deepLink.getBooleanQueryParameter("sku_id", false)) {
                String productID = deepLink.getQueryParameter("sku_id");
                dataBundle.putString(SKU_ID, productID);
                deepLinkRedirection(dataBundle);
            } else if (deepLink.getBooleanQueryParameter("category_id", false)) {
                String categoryId = deepLink.getQueryParameter("category_id");
                dataBundle.putString(CATEGORY_ID, categoryId);
                deepLinkRedirection(dataBundle);
            } else if (deepLink.getBooleanQueryParameter("show_reg", false)) {
                String showReg = deepLink.getQueryParameter("show_reg");
                redirectToRegistration(showReg);
            }

        } else {
            parsePushWooshDeeplinks();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
    }

    private void parsePushWooshDeeplinks() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            try {
                String categoryId = data.getQueryParameter("category_id");
                dataBundle.putString(CATEGORY_ID, categoryId);
                deepLinkRedirection(dataBundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String productId = data.getQueryParameter("sku_id");
                dataBundle.putString(SKU_ID, productId);
                deepLinkRedirection(dataBundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String showReg = data.getQueryParameter("show_reg");
                redirectToRegistration(showReg);

            } catch (Exception e) {
                e.printStackTrace();
                deepLinkRedirection(dataBundle);
            }
        } else {
            deepLinkRedirection(dataBundle);
        }


    }

    private void deepLinkRedirection(Bundle bundle) {
        if (isLanguageAlreadyShown) {
            Intent homeIntent = new Intent(SplashActivity.this, DashboardActivity.class);
            homeIntent.putExtras(bundle);
            startActivity(homeIntent);
            this.finish();
        } else {
            Intent homeIntent = new Intent(SplashActivity.this, SelectLanguageActivity.class);
            homeIntent.putExtras(bundle);
            startActivity(homeIntent);
            this.finish();
        }

    }

}