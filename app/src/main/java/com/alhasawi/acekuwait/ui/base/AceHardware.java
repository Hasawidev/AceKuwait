package com.alhasawi.acekuwait.ui.base;

//import static com.alhasawi.acekuwait.ui.events.InAppEvents.ADJUST_APP_TOKEN;

import static com.alhasawi.acekuwait.ui.events.InAppEvents.logFirstAppOpenEvent;
import static com.alhasawi.acekuwait.ui.events.InAppEvents.logLastSessionEvent;
import static com.alhasawi.acekuwait.ui.events.InAppEvents.logStartSessionEvent;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;

import com.alhasawi.acekuwait.BuildConfig;
import com.alhasawi.acekuwait.data.api.retrofit.RetrofitApiClient;
import com.alhasawi.acekuwait.ui.events.InAppEvents;
import com.alhasawi.acekuwait.utils.AppConstants;
import com.alhasawi.acekuwait.utils.ForceUpdateChecker;
import com.alhasawi.acekuwait.utils.LanguageHelper;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.pushwoosh.Pushwoosh;

import java.util.HashMap;
import java.util.Map;

import io.branch.referral.Branch;

public class AceHardware extends MultiDexApplication implements LifecycleObserver {

    public static final String TAG = "acekuwait";
    public static Context appContext;
    public static FirebaseRemoteConfig firebaseRemoteConfig;
    //    private AppEventsLogger facebookEventLogger;
//    private Freshchat freshchat;
    private static AceHardware sInstance = null;
    private RetrofitApiClient retrofitApiClient;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static AceHardware getInstance() {
        return sInstance;
    }

    public RetrofitApiClient getRetrofitApiClient() {
        retrofitApiClient = RetrofitApiClient.getInstance();
        return retrofitApiClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        appContext = getApplicationContext();

        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

//        AppEventsLogger.activateApp(this);
//        facebookEventLogger = AppEventsLogger.newLogger(this);
        // Branch.io logging for debugging
        Branch.enableLogging();
//        Branch.setPlayStoreReferrerCheckTimeout(0);

        // Branch object initialization
        Branch.getAutoInstance(this);
        Pushwoosh.getInstance().registerForPushNotifications();

//        initialiseFreshchat();
        registerBroadcastReceiver();

//          <!-- This app has permission to register and receive data message. -->
//<!--    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />-->

//        initInsider(this, appContext);

//        initialiseAdjust();

        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.APP_NAME);

        String deviceName = Settings.Global.getString(getContentResolver(), "device_name");
        String os = Build.VERSION.RELEASE;
        String AppLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        mFirebaseAnalytics.setUserProperty("device", deviceName);
        mFirebaseAnalytics.setUserProperty("os", os);
        mFirebaseAnalytics.setUserProperty("language", AppLang);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, token);
//                        Freshchat.getInstance(appContext).setPushRegistrationToken(token);
                    }
                });

        appGetFirstTimeRun();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
//        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.8");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=com.alhasawi.acekuwait");

        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                    }
                });

        InAppEvents.logUserInitProperty();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onAppBackgrounded() {
        Log.d("ACE", "App in background");
        logLastSessionEvent();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onAppForegrounded() {
        Log.d("ACE", "App in foreground");
        logStartSessionEvent();
    }

    private void appGetFirstTimeRun() {
        //Check if App Start First Time
        SharedPreferences appPreferences = getSharedPreferences("ACE", 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt("app_first_time", 0);

        if (appLastBuildVersion == 0) {
            Log.e("appGetFirstTimeRun", "First Time");
            logFirstAppOpenEvent();
        } else if (appLastBuildVersion == appCurrentBuildVersion) {
            Log.e("appGetFirstTimeRun", "Already started before");
        } else {
            Log.e("appGetFirstTimeRun", "It has started once, but not that version , ie it is an update.");
        }

        appPreferences.edit().putInt("app_first_time", appCurrentBuildVersion).apply();
    }

    private void registerBroadcastReceiver() {
//        IntentFilter intentFilterUnreadMessagCount = new IntentFilter(Freshchat.FRESHCHAT_UNREAD_MESSAGE_COUNT_CHANGED);
//        IntentFilter intentFilterRestoreID = new IntentFilter(Freshchat.FRESHCHAT_USER_RESTORE_ID_GENERATED);
//        getLocalBroadcastManager().registerReceiver(restoreIdReceiver, intentFilterRestoreID);
//        getLocalBroadcastManager().registerReceiver(unreadCountChangeReceiver, intentFilterUnreadMessagCount);
    }

    public LocalBroadcastManager getLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        getLocalBroadcastManager().unregisterReceiver(unreadCountChangeReceiver);
//        getLocalBroadcastManager().unregisterReceiver(restoreIdReceiver);
    }

//    BroadcastReceiver unreadCountChangeReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Freshchat.getInstance(getApplicationContext()).getUnreadCountAsync(new UnreadCountCallback() {
//                @Override
//                public void onResult(FreshchatCallbackStatus freshchatCallbackStatus, int unreadCount) {
//                    //Toast.makeText(getApplicationContext(), "Unread message Count - " + unreadCount, Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    };

//    BroadcastReceiver restoreIdReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String restoreId = Freshchat.getInstance(getApplicationContext()).getUser().getRestoreId();
//            //Toast.makeText(context, "Restore id: " + restoreId, Toast.LENGTH_SHORT).show();
//        }
//
//    };

    private void initialiseFreshchat() {
//        FreshchatConfig freshchatConfig = new FreshchatConfig(getResources().getString(R.string.freshchat_app_id), getResources().getString(R.string.freshchat_app_key));
//        freshchatConfig.setDomain(getResources().getString(R.string.freshchat_domain));
//        freshchatConfig.setCameraCaptureEnabled(true);
//        freshchatConfig.setGallerySelectionEnabled(true);
//        freshchatConfig.setResponseExpectationEnabled(true);
//        getFreshchatInstance(getApplicationContext()).init(freshchatConfig);
    }

//    private Freshchat getFreshchatInstance(Context context) {
//        if (freshchat == null) {
//            freshchat = Freshchat.getInstance(context);
//        }
//        return freshchat;
//    }

//    private void initialiseAdjust() {
//
//        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
//
//        AdjustConfig config = new AdjustConfig(this, ADJUST_APP_TOKEN, environment);
//
//        config.setLogLevel(LogLevel.VERBOSE);
//
//        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
//            @Override
//            public void onAttributionChanged(AdjustAttribution attribution) {
//                Log.d("example", "Attribution callback called!");
//                Log.d("example", "Attribution: " + attribution.toString());
//            }
//        });
//        config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
//            @Override
//            public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
//                Log.d("example", "Event success callback called!");
//                Log.d("example", "Event success data: " + eventSuccessResponseData.toString());
//            }
//        });
//        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
//            @Override
//            public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
//                Log.d("example", "Event failure callback called!");
//                Log.d("example", "Event failure data: " + eventFailureResponseData.toString());
//            }
//        });
//        config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
//            @Override
//            public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess sessionSuccessResponseData) {
//                Log.d("example", "Session success callback called!");
//                Log.d("example", "Session success data: " + sessionSuccessResponseData.toString());
//            }
//        });
//        config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
//            @Override
//            public void onFinishedSessionTrackingFailed(AdjustSessionFailure sessionFailureResponseData) {
//                Log.d("example", "Session failure callback called!");
//                Log.d("example", "Session failure data: " + sessionFailureResponseData.toString());
//            }
//        });
//        config.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
//            @Override
//            public boolean launchReceivedDeeplink(Uri deeplink) {
//                Log.d("example", "Deferred deep link callback called!");
//                Log.d("example", "Deep link URL: " + deeplink);
//                return true;
//            }
//        });
//
//        config.setSendInBackground(true);
//
//        Adjust.onCreate(config);
//
//        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
//    }


//    private static final class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
//        @Override
//        public void onActivityResumed(Activity activity) {
//            Adjust.onResume();
//        }
//
//        @Override
//        public void onActivityPaused(Activity activity) {
//            Adjust.onPause();
//        }
//
//        @Override
//        public void onActivityStopped(Activity activity) {
//        }
//
//        @Override
//        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        }
//
//        @Override
//        public void onActivityDestroyed(Activity activity) {
//        }
//
//        @Override
//        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        }
//
//        @Override
//        public void onActivityStarted(Activity activity) {
//        }
//    }

    public void initAppLanguage(Context context) {
        PreferenceHandler preferenceHandler = new PreferenceHandler(this, PreferenceHandler.TOKEN_LOGIN);
        String selectedLanguageId = preferenceHandler.getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (selectedLanguageId.equals(""))
            selectedLanguageId = "en";
        LanguageHelper.changeLocale(context.getResources(), selectedLanguageId);
    }

}
