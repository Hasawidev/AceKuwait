package com.alhasawi.acekuwait.ui.base;

import static com.alhasawi.acekuwait.utils.AppConstants.ACE_DYNAMIC_URL;
import static com.alhasawi.acekuwait.utils.AppConstants.ACE_EMAIL;
import static com.alhasawi.acekuwait.utils.AppConstants.ACE_IOS_APP_STORE_ID;
import static com.alhasawi.acekuwait.utils.AppConstants.ACE_IOS_BUNDLE_ID;
import static com.alhasawi.acekuwait.utils.AppConstants.ACE_PHONE;
import static com.alhasawi.acekuwait.utils.AppConstants.ACE_WEBSITE_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alhasawi.acekuwait.R;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.ui.main.service.ApplicationSelectorReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {

    static boolean isAppRunning = true;
    private static boolean isAppStopped = false;
    protected LayoutInflater inflater;
    private InputMethodManager inputMethodManager;
    private boolean isForceUpdate = false;

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null) {
                final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (activity.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isIsAppStopped() {
        return isAppStopped;
    }

    protected abstract void setup();

    @Override
    public void onPause() {
        super.onPause();
        isAppRunning = false;
        isAppStopped = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isAppRunning = true;
        isAppStopped = false;

    }


    @Override
    public void onStop() {
        super.onStop();
        isAppStopped = true;
        isAppRunning = false;

    }

    @Override
    public void onDestroy() {
        try {
            AceHardware.getInstance().initAppLanguage(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        isAppStopped = true;
        isAppRunning = false;
    }

    @Override
    public void onBackPressed() {
        if (!isForceUpdate) {
            super.onBackPressed();
        }
    }

    /*Method called  To hide keyboard*/
    public boolean hideSoftKeyboard() {
        try {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Method called  To check Internet Connection */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * This method is used to replace fragment_notify_me.xml .
     *
     * @param newFragment :replace an existing fragment_notify_me.xml with new fragment_notify_me.xml.
     * @param args        :pass bundle data from one fragment_notify_me.xml to another fragment_notify_me.xml
     */
    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory) {
        replaceFragment(frame_layout, newFragment, args, toBeAddedInStack, clearAllHistory, R.anim.slide_in_up, R.anim.slide_out_up, null);
    }

    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory, int enter, int exit) {
        replaceFragment(frame_layout, newFragment, args, toBeAddedInStack, clearAllHistory, enter, exit, null);
    }

    public void replaceFragment(int frame_layout, Fragment newFragment, Bundle args, boolean toBeAddedInStack, boolean clearAllHistory, int enter, int exit, ArrayList<View> viewList) {
        hideSoftKeyboard(newFragment.getActivity());
        FragmentManager manager = getSupportFragmentManager();
//        manager.popBackStack();
        if (clearAllHistory) {
            if (manager.getBackStackEntryCount() != 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        if (args != null)
            newFragment.setArguments(args);
        FragmentTransaction transaction = manager.beginTransaction();
        if (viewList != null) {
            for (int i = 0; i < viewList.size(); i++) {
                transaction.addSharedElement(viewList.get(i), "name" + viewList.get(i));
            }
        }
        // Replace whatever is in the fragment_container view with this fragment_notify_me.xml,
        // and add the transaction to the back stack so the user can navigate back
//        if (newFragment instanceof SearchPickupFragment || newFragment instanceof SearchDropFragment)
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.replace(frame_layout, newFragment);
        if (toBeAddedInStack)
            transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void replaceFragment(int frame_layout, Fragment fragment, Bundle bundle, boolean toBeAddedInStack) {

        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(frame_layout, fragment);

        int enter = R.anim.slide_in_up;
        int exit = R.anim.slide_out_up;
        fragmentTransaction.setCustomAnimations(enter, exit, enter, exit);

        if (toBeAddedInStack)
            fragmentTransaction.addToBackStack(null);

        fragmentManager.popBackStack();

        fragmentTransaction.commit();
    }

    public void popBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() != 0) {
            manager.popBackStack();
        }
    }

    public Animation inFromBottomAnimation(long durationMillis) {

        Animation inFromBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        inFromBottom.setDuration(durationMillis);
        return inFromBottom;
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    public Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup();
        isAppRunning = true;
        isAppStopped = false;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        float scale = 1;
        adjustFontScale(getResources().getConfiguration(), scale);
//        if (loadingIndicator == null)
//            loadingIndicator = new LoadingIndicator();


    }

    // To change size of font in settings according to the convenience of the user. This function determines the scaling
    public void adjustFontScale(Configuration configuration, float scale) {

        float systemScale = Settings.System.getFloat(getContentResolver(), Settings.System.FONT_SCALE, 1f);
        configuration.fontScale = scale * systemScale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

//    public boolean isAlreadyLoggedinWithFacebbok() {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        return isLoggedIn;
//    }


    public void disconnectFromFacebook() {

//        if (AccessToken.getCurrentAccessToken() == null) {
//            return; // already logged out
//        }
//
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//
//                LoginManager.getInstance().logOut();
//
//            }
//        }).executeAsync();
    }

    public void sendEmailToCustomerCare() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{ACE_EMAIL});
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
        try {
            InAppEvents.logContactUsEvent(
                    "email"
            );
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void shareProductInfo(Product currentSelectedProduct) {

        try {
            //String productId = currentSelectedProduct.getProductId();
            String productId = currentSelectedProduct.getSku();
            InAppEvents.productId = productId;
            //String customShareUrl = ACE_DYNAMIC_URL + "/?product_id=" + productId;
            String customShareUrl = ACE_WEBSITE_URL + "/?sku_id=" + productId;
            String imageUrl = currentSelectedProduct.getProductConfigurables().get(0).getProductImages().get(0).getImageUrl();

            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(customShareUrl))
                    .setDomainUriPrefix(ACE_DYNAMIC_URL)
                    .setAndroidParameters(
                            new DynamicLink.AndroidParameters.Builder(AceHardware.appContext.getPackageName())
                                    .setMinimumVersion(9)
                                    .build())
                    .setIosParameters(
                            new DynamicLink.IosParameters.Builder(ACE_IOS_BUNDLE_ID)
                                    .setAppStoreId(ACE_IOS_APP_STORE_ID)
                                    .setMinimumVersion("1.2")
                                    .build())
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle(currentSelectedProduct.getDescriptions().get(0).getProductName())
                                    .setDescription(currentSelectedProduct.getDescriptions().get(0).getProductDescription())
                                    .setImageUrl(Uri.parse(imageUrl))
                                    .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)//ShortDynamicLink.Suffix.SHORT
                    .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                        @Override
                        public void onSuccess(ShortDynamicLink shortDynamicLink) {
                            Uri mShareUrl = shortDynamicLink.getShortLink();
                            if (mShareUrl != null) {
                                String shareLink = mShareUrl.toString();
                                shareProductIntent(shareLink);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BaseActivity.this, "Share link not generated", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    });
                    /*.addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();
                                if (shortLink != null) {
                                    String shareLink = shortLink.toString();
                                    shareProductIntent(shareLink);
                                }
                            } else {
                                if (task.getException() != null) {
                                    task.getException().printStackTrace();
                                }
                                Log.e("dynamicLink", "error generating link");
                            }
                        }
                    });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareProductIntent(String productLink) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Share Product :" + productLink);
            sendIntent.setType("text/plain");
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                Intent receiver = new Intent(this, ApplicationSelectorReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooser = Intent.createChooser(sendIntent, null, pendingIntent.getIntentSender());
                startActivity(chooser);
            } else {
                startActivity(sendIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAceCustomerCare() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + ACE_PHONE));
        startActivity(callIntent);
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        super.applyOverrideConfiguration(getBaseContext().getResources().getConfiguration());
    }
}
