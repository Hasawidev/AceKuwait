package com.alhasawi.acekuwait.ui.events;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.alhasawi.acekuwait.data.api.model.User;
import com.alhasawi.acekuwait.data.api.model.pojo.Order;
import com.alhasawi.acekuwait.data.api.model.pojo.PaymentMode;
import com.alhasawi.acekuwait.data.api.model.pojo.Product;
import com.alhasawi.acekuwait.data.api.model.pojo.ShippingMode;
import com.alhasawi.acekuwait.data.api.model.pojo.ShoppingCartItem;
import com.alhasawi.acekuwait.ui.base.AceHardware;
import com.alhasawi.acekuwait.utils.DateTimeUtils;
import com.alhasawi.acekuwait.utils.PreferenceHandler;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pushwoosh.Pushwoosh;
import com.pushwoosh.inapp.InAppManager;
import com.pushwoosh.tags.Tags;
import com.pushwoosh.tags.TagsBundle;

import java.util.ArrayList;
import java.util.List;

public class InAppEvents {

    //Event Names
    public static final String EVENT_APP_DOWLOADED = "app_download";
    public static final String EVENT_APP_INSTALLED = "app_install";
    public static final String EVENT_FIRST_APP_OPEN = "first_app_open";
    public static final String EVENT_APP_SESSION_START = "start_session";
    public static final String EVENT_APP_LAST_SESSION = "last_session";
    public static final String EVENT_PRODUCT_VIEW = "product_view";
    public static final String EVENT_MAIN_CATEGORY_VIEW = "main_category_view";
    public static final String EVENT_CATEGORY_VIEW = "category_view";
    public static final String EVENT_TOP_BANNER_CLICK = "top_banner_click";
    public static final String EVENT_HERO_BANNER_CLICK = "hero_banner_click";
    public static final String EVENT_BANNER_CLICK = "banner_click";
    public static final String EVENT_BEST_SELLING_PRODUCT_CLICK = "best_selling_products_click";
    public static final String EVENT_NEW_PRODUCT_CLICK = "new_products_click";
    public static final String EVENT_UPCOMING_PRODUCT_CLICK = "upcoming_products_click";
    public static final String EVENT_TOP_SELLING_PRODUCT_CLICK = "top_selling_products_click";
    public static final String EVENT_TRENDING_PRODUCT_CLICK = "trending_products_click";
    public static final String EVENT_TODAY_DEAL_CLICK = "today_deals_click";
    public static final String EVENT_TRENDING_CATEGORY_CLICK = "trending_categories_click";
    public static final String EVENT_SEARCH = "search";
    public static final String EVENT_REGISTRATION_SUCCESS = "registration_success";
    public static final String EVENT_REGISTRATION_FAILED = "registration_failed";
    public static final String EVENT_REGISTRATION_DATE = "registration_date";
    public static final String EVENT_ADD_TO_CART = "add_to_cart";
    public static final String EVENT_ITEMS_IN_CART = "items_in_cart";
    public static final String EVENT_ITEM_REMOVED_FROM_CART = "item_removed_from_cart";
    public static final String EVENT_PROCEED_TO_CHECKOUT = "proceed_to_checkout";
    public static final String EVENT_PROMO_CODE = "promo_code";
    public static final String EVENT_ORDER_CONFIRMATION = "order_confirmation";
    public static final String EVENT_ORDER_FAILED = "order_failed";
    public static final String EVENT_PAYMENT_METHOD = "payment_method";
    public static final String EVENT_PAYMENT_FAILED = "payment_failed";
    public static final String EVENT_PAYMENT_SUCCESSFUL = "payment_success";
    public static final String EVENT_CONTACT_US = "contact_us";
    public static final String EVENT_SOCIAL_MEDIA_ICON = "social_media_icon";
    public static final String EVENT_REFUND = "refund_event";
    public static final String EVENT_REFUND_VALUE = "refund_value";
    public static final String EVENT_REFUND_PRODUCTS = "refund_products";
    public static final String EVENT_APP_SHARE = "app_share";
    public static final String EVENT_PRODUCT_SHARE = "product_share";
    public static final String EVENT_ADD_ADDRESS = "add_address";
    public static final String EVENT_ADD_SHIPPING_INFO = "add_shipping_info";
    public static final String EVENT_WISHLIST = "wishlist";
    public static final String EVENT_NOTIFY_ME = "notify_me";
    public static final String EVENT_RELATED_PRODUCT = "related_product";
    public static final String EVENT_FORGOT_PASSWORD = "forgot_password";
    public static final String EVENT_STORE_LOCATION = "store_location";
    public static final String EVENT_BARCODE_SCAN = "barcode_scan";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_PAYMENT_METHOD = "payment_method";
    private static final String EVENT_PAYMENT_DATE = "payment_date";
    private static final String KEY_REFUND_VALUE = "refund_value";
    private static final String KEY_SHARED_CHANNEL = "shared_channel";
    private static final String EVENT_VIEW_CART = "view_cart";
    private static final String EVENT_PURCHASE = "purchase";
    private static final String EVENT_REVENUE = "revenue";
    //Common
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_OS = "os";
    private static final String KEY_COUNT = "count";

    //Adjust Tokens
//    public static final String ADJUST_APP_TOKEN = "cqqq2qg9fnk0";

    //    private static final String ADJUST_KEY_APP_DOWLOADED = "yvto0r";
//    private static final String ADJUST_KEY_APP_INSTALLED = "zhnbay";
//    private static final String ADJUST_KEY_FIRST_APP_OPEN = "5ydc84";
//    private static final String ADJUST_KEY_APP_SESSION_START = "e3mhvx";
//    private static final String ADJUST_KEY_APP_LAST_SESSION = "lu2l2z";
//    private static final String ADJUST_KEY_PRODUCT_VIEW = "6p1xq4";
//    private static final String ADJUST_KEY_MAIN_CATEGORY_VIEW = "ijpsje";
//    private static final String ADJUST_KEY_CATEGORY_VIEW = "hjrsdv";
//    private static final String ADJUST_KEY_TOP_BANNER_CLICK = "toncsa";
//    private static final String ADJUST_KEY_HERO_BANNER_CLICK = "y3xwpu";
//    private static final String ADJUST_KEY_BANNER_CLICK = "nfthvp";
//    private static final String ADJUST_KEY_BEST_SELLING_PRODUCT_CLICK = "e3zm29";
//    private static final String ADJUST_KEY_NEW_PRODUCT_CLICK = "7c1h22";
//    private static final String ADJUST_KEY_UPCOMING_PRODUCT_CLICK = "7azill";
//    private static final String ADJUST_KEY_TOP_SELLING_PRODUCT_CLICK = "kuv7hz";
//    private static final String ADJUST_KEY_TRENDING_PRODUCT_CLICK = "7buplw";
//    private static final String ADJUST_KEY_TODAY_DEAL_CLICK = "g2ed5s";
//    private static final String ADJUST_KEY_TRENDING_CATEGORY_CLICK = "kws446";
//    private static final String ADJUST_KEY_SEARCH = "7gq0c7";
//    private static final String ADJUST_KEY_REGISTRATION_SUCCESS = "gc8kfa";
//    private static final String ADJUST_KEY_REGISTRATION_FAILED = "pg7c4d";
//    private static final String ADJUST_KEY_REGISTRATION_DATE = "8e5jt1";
//    private static final String ADJUST_KEY_ADD_TO_CART = "80e5fs";
//    private static final String ADJUST_KEY_ITEMS_IN_CART = "oxrufn";
//    private static final String ADJUST_KEY_ITEM_REMOVED_FROM_CART = "q5rq2c";
//    private static final String ADJUST_KEY_PROCEED_TO_CHECKOUT = "crjgar";
//    private static final String ADJUST_KEY_PROMO_CODE = "miv51l";
//    private static final String ADJUST_KEY_ORDER_CONFIRMATION = "qflnyb";
//    private static final String ADJUST_KEY_PURCHASE = "lx023s";
//    private static final String ADJUST_KEY_REVENUE = "qflneb";
//    private static final String ADJUST_KEY_ORDER_FAILED = "4shz27";
//    private static final String ADJUST_KEY_PAYMENT_METHOD = "v87g06";
//    private static final String ADJUST_KEY_PAYMENT_FAILED = "jm7km5";
//    private static final String ADJUST_KEY_PAYMENT_SUCCESSFUL = "w6q7kr";
//    private static final String ADJUST_KEY_CONTACT_US = "57zj48";
//    private static final String ADJUST_KEY_SOCIAL_MEDIA_ICON = "lvtbi6";
//    private static final String ADJUST_KEY_REFUND = "o2m4p1";
//    private static final String ADJUST_KEY_APP_SHARE = "9rxwjc";
//    private static final String ADJUST_KEY_PRODUCT_SHARE = "n9ylnb";
//    private static final String ADJUST_KEY_ADD_ADDRESS = "c3teoj";
//    private static final String ADJUST_KEY_ADD_SHIPPING_INFO = "vhiygd";
//    private static final String ADJUST_KEY_WISHLIST = "f7f376";
//    private static final String ADJUST_KEY_NOTIFY_ME = "2hle17";
//    private static final String ADJUST_KEY_RELATED_PRODUCT = "3bwftg";
//    private static final String ADJUST_KEY_FORGOT_PASSWORD = "qkp5wy";
//    private static final String ADJUST_KEY_STORE_LOCATION = "tn20aq";
//    private static final String ADJUST_KEY_BARCODE_SCAN = "z58z2v";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_MAIN_CATEGORY_ID = "main_category_id";
    private static final String KEY_MAIN_CATEGORY_NAME = "main_category_name";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_BANNER_ID = "banner_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_ITEM_LIST = "cart_item_list";
    private static final String KEY_PROMO_CODE = "promocode";
    private static final String KEY_DISCOUNT_AMOUNT = "discount_amount";
    private static final String KEY_PRICE = "price";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_NAME = "name";
    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_LIST_PRODUCT = "list_product";
    private static final String KEY_CART_COUNT = "cart_count";
    private static final String KEY_DEVICE = "device";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_AGE = "age";
    private static final String KEY_DOB = "date_of_birth";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PHONE_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NOTIFICATION_STATUS = "notification_status";
    private static final String KEY_LOYALTY_POINTS = "loyalty_points";
    private static final String KEY_UPDATED_PROFILE = "updated_profile";
    private static final String KEY_DEVICE_NAME = "device_name";
    private static final String VALUE_COUNT = "1";
    private static final String VALUE_OS_NAME = "Android";
    private static final String VALUE_CURRENCY = "KWD";
    private static final String VALUE_CONTENT_TYPE = "product";
    //Pushwoosh Constants
    private static final String PUSHWOOSH_EVENT_ADD_TO_CART = "Add to cart";
    private static final String PUSHWOOSH_EVENT_ADD_TO_WISHLIST = "Add to wishlist";
    private static final String PUSHWOOSH_EVENT_NOTIFY_ME = "Notify me";
    private static final String PUSHWOOSH_EVENT_RELATED_PRODUCT = "Related product";
    private static final String PUSHWOOSH_EVENT_APP_UPDATE = "update";
    private static final String PUSHWOOSH_EVENT_USER_REG = "Registration in the app";
    private static final String PUSHWOOSH_TAG_ADDRESS = "Address";
    private static final String PUSHWOOSH_TAG_ADD_PAYMENT_INFO = "Add Payment Info";
    private static final String PUSHWOOSH_TAG_ADD_TO_CART = "Add to Cart";
    private static final String PUSHWOOSH_TAG_ADD_TO_CART_PRODUCT = "Add to Cart Product";
    private static final String PUSHWOOSH_TAG_ADD_TO_WISHLIST = "Add to Wishlist";
    private static final String PUSHWOOSH_TAG_ADD_TO_WISHLIST_PRODUCT = "Add to Wishlist Product";
    private static final String PUSHWOOSH_TAG_BANNER_CLICK = "Banner Click";
    private static final String PUSHWOOSH_TAG_BEGIN_CHECKOUT = "Begin Checkout";
    private static final String PUSHWOOSH_TAG_BEST_SELLING_PRODUCT = "Best Selling Products Click";
    private static final String PUSHWOOSH_TAG_CATEGORY_VIEW = "Category View";
    private static final String PUSHWOOSH_TAG_PRODUCT_VIEW = "Product View";
    private static final String PUSHWOOSH_TAG_PROMO_CODE = "Promo code";
    private static final String PUSHWOOSH_TAG_LOYALTY_POINTS = "Loyalty Points";
    private static final String PUSHWOOSH_TAG_NOTIFY_ME_PRODUCT = "Notify Me Product";
    private static final String PUSHWOOSH_TAG_ORDER_FAILED = "Order Failed";
    private static final String PUSHWOOSH_TAG_PAYMENT_METHOD = "Payment Method";
    private static final String PUSHWOOSH_TAG_PRODUCT_SHARE = "Product Share";
    private static final String PUSHWOOSH_TAG_ORDER_SUCCESS = "Order Success";
    private static final String PUSHWOOSH_TAG_ORDER_VALUE = "Order Value";
    private static final String PUSHWOOSH_TAG_CUSTOMER_LIFETIME_VALUE = "Customer Lifetime Value";
    private static final String PUSHWOOSH_TAG_REGISTRATION_DATE = "Registration Date";
    private static final String PUSHWOOSH_TAG_REGISTRATION_FAILED = "Registration Failed";
    private static final String PUSHWOOSH_TAG_REGISTRATION_SUCCESS = "Registration Success";
    private static final String PUSHWOOSH_TAG_STORE_LOCATION = "Store Location";
    private static final String PUSHWOOSH_TAG_APP_LOCATION = "App Location";
    private static final String PUSHWOOSH_TAG_LAST_CATEGORY_VIEW = "Last Category View";
    private static final String PUSHWOOSH_TAG_PURCHASE_REVENUE = "Purchase Revenue";
    private static final String PUSHWOOSH_TAG_PURCHASE_CATEGORY = "Purchase Category";
    private static final String PUSHWOOSH_TAG_PURCHASE_LAST_DATE = "Purchase Last Date";
    private static final String PUSHWOOSH_TAG_DEFAULT_PURCHASE_LAST_DATE = "Last In-App Purchase Date";
    private static final String PUSHWOOSH_PARAM_PRODUCT_ID = "product_id";
    private static final String PUSHWOOSH_PARAM_PRODUCT_NAME = "product_name";
    private static final String PUSHWOOSH_PARAM_CURRENCY = "currency";
    private static final String PUSHWOOSH_PARAM_PRICE = "price";
    //CartItemCopy
    public static List<ShoppingCartItem> cartItemsList = new ArrayList<>();
    public static Double cartTotalAmount = 0d;
    public static String productId = "";
    private static Gson gson = new Gson();

    private static Context getAppContext() {
        return AceHardware.appContext;
    }

    private static FirebaseAnalytics getFireBaseAnalytics() {
        return FirebaseAnalytics.getInstance(getAppContext());
    }

//    private static AppEventsLogger getFacebookEventsLogger() {
//        return AppEventsLogger.newLogger(getAppContext());
//    }

    private static InAppManager getPushwooshInstance() {
        return InAppManager.getInstance();
    }

    /*private static AppEventsLogger getFbEventLogger() {
        return AppEventsLogger.newLogger(getAppContext());
    }*/

    private static PreferenceHandler getPreferenceHandler() {
        return new PreferenceHandler(getAppContext(), PreferenceHandler.TOKEN_LOGIN);
    }

    private static boolean isUserLoggedIn() {
        return getPreferenceHandler().getData(PreferenceHandler.LOGIN_STATUS, false);
    }

    private static String getSelectedLangId() {
        String langId = "en";
        String lang = getPreferenceHandler().getData(PreferenceHandler.LOGIN_SELECTED_LANGUAGE_ID, "");
        if (lang != null && !lang.equals("")) {
            langId = lang;
        }
        return langId;
    }

    private static String getSelectedLanguage() {
        String langId = getSelectedLangId();
        String language = "English";
        if (langId.equals("ar")) {
            language = "Arabic";
        }
        return language;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        return Settings.Secure.getString(getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    private static String getDeviceName() {
        if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            return Build.MODEL + "(API " + VERSION.SDK_INT + ")";
        } else {
            return Build.MANUFACTURER + " " + Build.MODEL + "(API " + VERSION.SDK_INT + ")";
        }
    }

    private static String getUserId() {
        String userId = getDeviceId();
        if (isUserLoggedIn()) {
            userId = getPreferenceHandler().getData(PreferenceHandler.LOGIN_USER_ID, getDeviceId());
        }
        return userId;
    }


    private static String getGender() {
        String gender = "";
        if (isUserLoggedIn()) {
            gender = getPreferenceHandler().getData(PreferenceHandler.LOGIN_GENDER, "");
        }
        return gender;
    }

    private static String getMobile() {
        String mobile = "";
        if (isUserLoggedIn()) {
            mobile = getPreferenceHandler().getData(PreferenceHandler.LOGIN_PHONENUMBER, "");
        }
        return mobile;
    }

    private static String getMail() {
        String mail = "";
        if (isUserLoggedIn()) {
            mail = getPreferenceHandler().getData(PreferenceHandler.LOGIN_EMAIL, "");
        }
        return mail;
    }

    private static void setDefaultFireBaseParams(Bundle bundle) {
        bundle.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
        bundle.putString(KEY_USER_ID, getUserId());
        bundle.putString(KEY_DEVICE_ID, getDeviceId());
        bundle.putString(KEY_OS, VALUE_OS_NAME);
        bundle.putString(KEY_COUNT, VALUE_COUNT);
        bundle.putString(KEY_GENDER, getGender());
        bundle.putString(KEY_EMAIL, getMail());
        bundle.putString(KEY_PHONE_NO, getMobile());
        bundle.putString(KEY_LANGUAGE, getSelectedLanguage());
    }

//    private static void setDefaultInsiderParams(InsiderEvent tagEvent) {
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_DEVICE_ID, getDeviceId());
//        tagEvent.addParameterWithString(KEY_OS, VALUE_OS_NAME);
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.addParameterWithString(KEY_LANGUAGE, getSelectedLanguage());
//    }

//    private static void setDefaultAdjustParams(AdjustEvent event) {
//
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_DEVICE_ID, getDeviceId());
//        event.addCallbackParameter(KEY_OS, VALUE_OS_NAME);
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//        event.addCallbackParameter(KEY_LANGUAGE, getSelectedLanguage());
//
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_DEVICE_ID, getDeviceId());
//        event.addPartnerParameter(KEY_OS, VALUE_OS_NAME);
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//        event.addPartnerParameter(KEY_LANGUAGE, getSelectedLanguage());
//
//    }

    public static void handleFCMNotification(Context context, RemoteMessage remoteMessage) {
//        Insider.Instance.handleFCMNotification(context, remoteMessage);
    }

    public static void initInsider(Application application, Context appContext) {
//        Insider.Instance.init(application, appContext.getResources().getString(R.string.insider_key));
//        Insider.Instance.registerInsiderCallback(new InsiderCallback() {
//            @Override
//            public void doAction(JSONObject data, InsiderCallbackType callbackType) {
//                switch (callbackType) {
//                    case NOTIFICATION_OPEN:
//                        Log.d("[INSIDER]", "[NOTIFICATION_OPEN]: " + data);
//                        break;
//                    case INAPP_BUTTON_CLICK:
//                        Log.d("[INSIDER]", "[INAPP_BUTTON_CLICK]: " + data);
//                        break;
//                    case TEMP_STORE_PURCHASE:
//                        Log.d("[INSIDER]", "[TEMP_STORE_PURCHASE]: " + data);
//                        break;
//                    case TEMP_STORE_ADDED_TO_CART:
//                        Log.d("[INSIDER]", "[TEMP_STORE_ADDED_TO_CART]: " + data);
//                        break;
//                    case TEMP_STORE_CUSTOM_ACTION:
//                        Log.d("[INSIDER]", "[TEMP_STORE_CUSTOM_ACTION]: " + data);
//                        break;
//                }
//            }
//        });
        //Insider.Instance.startTrackingGeofence();
//        Insider.Instance.enableIDFACollection(false);
//        Insider.Instance.setSplashActivity(SplashActivity.class);
    }

    public static void logFirstAppOpenEvent() {
        Bundle bundle = new Bundle();
        setDefaultFireBaseParams(bundle);

        getFireBaseAnalytics().logEvent(EVENT_FIRST_APP_OPEN, bundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_FIRST_APP_OPEN);
//        setDefaultAdjustParams(event);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_FIRST_APP_OPEN);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.build();
    }

    public static void logStartSessionEvent() {
        Bundle bundle = new Bundle();
        setDefaultFireBaseParams(bundle);

        getFireBaseAnalytics().logEvent(EVENT_APP_SESSION_START, bundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_APP_SESSION_START);
//        setDefaultAdjustParams(event);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_APP_SESSION_START);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.build();
    }

    public static void logLastSessionEvent() {
        Bundle bundle = new Bundle();
        setDefaultFireBaseParams(bundle);

        getFireBaseAnalytics().logEvent(EVENT_APP_LAST_SESSION, bundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_APP_LAST_SESSION);
//        setDefaultAdjustParams(event);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_APP_LAST_SESSION);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.build();
    }

    public static void logProductViewEvent(String productID, String productName, Product product) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
        Bundle item1 = new Bundle();
        item1.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        item1.putString(FirebaseAnalytics.Param.ITEM_ID, productID);
        item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        item1.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
        analyticsBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Bundle[]{item1});
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_ITEM, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PRODUCT_VIEW);
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//        event.addCallbackParameter(KEY_PRODUCT_ID, productID);
//        event.addCallbackParameter(KEY_PRODUCT_NAME, productName);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_PRICE, product.getOriginalPrice().toString());
//        event.addCallbackParameter(KEY_CURRENCY, VALUE_CURRENCY);
//
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//        event.addPartnerParameter(KEY_PRODUCT_ID, productID);
//        event.addPartnerParameter(KEY_PRODUCT_NAME, productName);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_PRICE, product.getOriginalPrice().toString());
//        event.addPartnerParameter(KEY_CURRENCY, VALUE_CURRENCY);

//        Adjust.trackEvent(event);

        String imageString = "";
        String[] catArray = new String[product.getCategories().size()];
        try {
            imageString = product.getProductConfigurables().get(0).getProductImages().get(0).getImageUrl();
            for (int i = 0; i < product.getCategories().size(); i++) {
                catArray[i] = product.getCategories().get(i).getCategoryId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Insider insider = Insider.Instance;
//        Insider.Instance.visitProductDetailPage(
//                insider.createNewProduct(
//                        productID, productName, catArray,
//                        imageString, product.getOriginalPrice(), "KWD"));
//
//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PRODUCT_VIEW);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, productID);
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, productName);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_PRICE, product.getOriginalPrice().toString());
//        tagEvent.addParameterWithString(KEY_CURRENCY, VALUE_CURRENCY);
//        tagEvent.build();

        //Logging Facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productID);
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(product));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, product.getOriginalPrice(), params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_PRODUCT_VIEW, productID), callback -> {
            Log.e("logProductView", callback.isSuccess() + "");
        });

    }

    public static void logMainCategoryViewEvent(String catID, String catName) {
        Bundle fireItemBundle = new Bundle();
        try {
            setDefaultFireBaseParams(fireItemBundle);
            fireItemBundle.putString(KEY_CATEGORY_ID, catID);
            fireItemBundle.putString(KEY_NAME, catName);
        } catch (Exception e) {
            //
        }
        getFireBaseAnalytics().logEvent(EVENT_MAIN_CATEGORY_VIEW, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_MAIN_CATEGORY_VIEW);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_CATEGORY_ID, catID);
//        event.addCallbackParameter(KEY_NAME, catName);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_CATEGORY_ID, catID);
//        event.addPartnerParameter(KEY_NAME, catName);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);

//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_MAIN_CATEGORY_VIEW);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_CATEGORY_ID, catID);
//        tagEvent.addParameterWithString(KEY_NAME, catName);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_CATEGORY_ID, catID);
//        params.putString(KEY_CATEGORY_NAME, catName);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product_group");
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, catID);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, catName);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_MAIN_CATEGORY_VIEW, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_LAST_CATEGORY_VIEW, catID), callback -> {
            Log.e("logMainCategory", callback.isSuccess() + "");
        });//PUSHWOOSH_TAG_CATEGORY_VIEW

    }

    public static void logCategoryViewEvent(String mainCatID, String catID, String catName) {
        Bundle fireItemBundle = new Bundle();
        try {
            setDefaultFireBaseParams(fireItemBundle);
            fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
            fireItemBundle.putString(KEY_CATEGORY_ID, catID);
            fireItemBundle.putString(KEY_CATEGORY_NAME, catName);
            fireItemBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "category");
            fireItemBundle.putString(FirebaseAnalytics.Param.ITEM_ID, catID);
        } catch (Exception e) {
            //
        }
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_CATEGORY_VIEW);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_CATEGORY_NAME, catName);
//        event.addCallbackParameter(KEY_CATEGORY_ID, catID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_CATEGORY_NAME, catName);
//        event.addPartnerParameter(KEY_CATEGORY_ID, catID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_CATEGORY_VIEW);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_CATEGORY_NAME, catName);
//        tagEvent.addParameterWithString(KEY_CATEGORY_ID, catID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_CATEGORY_ID, catID);
//        params.putString(KEY_CATEGORY_NAME, catName);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "product_group");
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, catID);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, catName);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_CATEGORY_VIEW, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_CATEGORY_VIEW, catID), callback -> {
            Log.e("logCategory", callback.isSuccess() + "");
        });

    }

    public static void logTopBannerClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_TOP_BANNER_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_TOP_BANNER_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_TOP_BANNER_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_TOP_BANNER_CLICK, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_BANNER_CLICK, bannerID), callback -> {
            Log.e("logTopBannerClickEvent", callback.isSuccess() + "");
        });
    }

    public static void logHeroBannerClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_HERO_BANNER_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_HERO_BANNER_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_HERO_BANNER_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_HERO_BANNER_CLICK, params);

        /*Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_BANNER_CLICK, bannerID), callback -> {
            Log.e("logHeroBannerClickEvent", callback.isSuccess() + "");
        });*/
    }

    public static void logBannerClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_BANNER_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_BANNER_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_BANNER_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_BANNER_CLICK, params);

        /*Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_BANNER_CLICK, bannerID), callback -> {
            Log.e("logBannerClickEvent", callback.isSuccess() + "");
        });*/
    }

    public static void logBestSellingProductClickEvent(String mainCatID, String bannerID, String productId) {
        /*Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_BEST_SELLING_PRODUCT_CLICK, fireItemBundle);

        AdjustEvent event = new AdjustEvent(ADJUST_KEY_BEST_SELLING_PRODUCT_CLICK);
        setDefaultAdjustParams(event);
        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
        event.addCallbackParameter(KEY_USER_ID, getUserId());
        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);

        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
        event.addPartnerParameter(KEY_USER_ID, getUserId());
        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);

        Adjust.trackEvent(event);

        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_BEST_SELLING_PRODUCT_CLICK);
        setDefaultInsiderParams(tagEvent);
        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
        tagEvent.build();

        //Logging facebook event
        Bundle params = new Bundle();
        params.putString(KEY_BANNER_ID, bannerID);
        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        params.putString(KEY_USER_ID, getUserId());
        params.putString(KEY_COUNT, VALUE_COUNT);
        getFacebookEventsLogger().logEvent(EVENT_BEST_SELLING_PRODUCT_CLICK, params);*/

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_BEST_SELLING_PRODUCT, productId), callback -> {
            Log.e("logTagBestSellPro", callback.isSuccess() + "");
        });
    }

    public static void logNewProductClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_NEW_PRODUCT_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_NEW_PRODUCT_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_NEW_PRODUCT_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_NEW_PRODUCT_CLICK, params);
    }

    public static void logUpComingProductClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_UPCOMING_PRODUCT_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_UPCOMING_PRODUCT_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_UPCOMING_PRODUCT_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_UPCOMING_PRODUCT_CLICK, params);
    }

    public static void logTopSellingProductClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_TOP_SELLING_PRODUCT_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_TOP_SELLING_PRODUCT_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_TOP_SELLING_PRODUCT_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_TOP_SELLING_PRODUCT_CLICK, params);
    }

    public static void logTrendingProductClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_TRENDING_PRODUCT_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_TRENDING_PRODUCT_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_TRENDING_PRODUCT_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_TRENDING_PRODUCT_CLICK, params);
    }

    public static void logTodayDealClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_TODAY_DEAL_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_TODAY_DEAL_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_TODAY_DEAL_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_TODAY_DEAL_CLICK, params);
    }

    public static void logTrendingCategoryClickEvent(String mainCatID, String bannerID) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_BANNER_ID, bannerID);
        fireItemBundle.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
        getFireBaseAnalytics().logEvent(EVENT_TRENDING_CATEGORY_CLICK, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_TRENDING_CATEGORY_CLICK);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_BANNER_ID, bannerID);
//        event.addCallbackParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_BANNER_ID, bannerID);
//        event.addPartnerParameter(KEY_MAIN_CATEGORY_ID, mainCatID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_TRENDING_CATEGORY_CLICK);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_BANNER_ID, bannerID);
//        tagEvent.addParameterWithString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_BANNER_ID, bannerID);
//        params.putString(KEY_MAIN_CATEGORY_ID, mainCatID);
//        params.putString(KEY_USER_ID, getUserId());
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_TRENDING_CATEGORY_CLICK, params);
    }

    public static void logSearchProductEvent(String productID, String productName, String searchQuery) {

        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        Bundle item1 = new Bundle();
        item1.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        item1.putString(FirebaseAnalytics.Param.ITEM_ID, productID);
        analyticsBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Bundle[]{item1});
        analyticsBundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_SEARCH);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_SEARCH_QUERY, searchQuery);
//        event.addCallbackParameter(KEY_PRODUCT_ID, productID);
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//
//        event.addPartnerParameter(KEY_SEARCH_QUERY, searchQuery);
//        event.addPartnerParameter(KEY_PRODUCT_ID, productID);
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_SEARCH);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_SEARCH_QUERY, searchQuery);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, productID);
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.build();

        //Logging Facebook event
//        Bundle fbParams = new Bundle();
//        fbParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        fbParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productID);
//        fbParams.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, searchQuery);
//        fbParams.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, 1);
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, fbParams);
    }

    public static void logSearchEvent(String searchQuery) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_SEARCH);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
//
//        event.addPartnerParameter(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_SEARCH);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery);
//        tagEvent.build();


    }

    public static void logSignUpSuccessEvent(User user) {
        Bundle fireItemBundle = new Bundle();
        fireItemBundle.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
        fireItemBundle.putString(KEY_USER_ID, user.getCustomerId());
        fireItemBundle.putString(KEY_OS, VALUE_OS_NAME);
        fireItemBundle.putString(KEY_COUNT, VALUE_COUNT);
        fireItemBundle.putString(KEY_DEVICE_ID, getDeviceId());
        getFireBaseAnalytics().logEvent(EVENT_REGISTRATION_SUCCESS, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REGISTRATION_SUCCESS);
//
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addCallbackParameter(KEY_USER_ID, user.getCustomerId());
//        event.addCallbackParameter(KEY_OS, VALUE_OS_NAME);
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//        event.addCallbackParameter(KEY_DEVICE_ID, getDeviceId());
//        //event.addCallbackParameter(KEY_USER_NAME, user.getCustomerFirstName());
//        //event.addCallbackParameter(KEY_EMAIL, user.getEmailId());
//
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addPartnerParameter(KEY_USER_ID, user.getCustomerId());
//        event.addPartnerParameter(KEY_OS, VALUE_OS_NAME);
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//        event.addPartnerParameter(KEY_DEVICE_ID, getDeviceId());
//        //event.addPartnerParameter(KEY_USER_NAME, user.getCustomerFirstName());
//        //event.addPartnerParameter(KEY_EMAIL, user.getEmailId());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REGISTRATION_SUCCESS);
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        tagEvent.addParameterWithString(KEY_USER_ID, user.getCustomerId());
//        tagEvent.addParameterWithString(KEY_OS, VALUE_OS_NAME);
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.addParameterWithString(KEY_DEVICE, getDeviceId());
//        tagEvent.build();
//
//        InsiderIdentifiers identifiers = new InsiderIdentifiers();
//        identifiers.addEmail(user.getEmailId());
//        identifiers.addPhoneNumber(user.getMobileNo());
//        identifiers.addUserID(user.getCustomerId());
//        Insider.Instance.getCurrentUser().login(identifiers);

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        params.putString(KEY_USER_ID, user.getCustomerId());
//        params.putString(KEY_OS, VALUE_OS_NAME);
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        params.putString(KEY_DEVICE_ID, getDeviceId());
//        getFacebookEventsLogger().logEvent(EVENT_REGISTRATION_SUCCESS, params);

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_REGISTRATION_DATE, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logRegisterDate", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_REGISTRATION_SUCCESS, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logRegisterSuccess", callback.isSuccess() + "");
        });
    }

    public static void logSignUpFailEvent() {
        Bundle fireItemBundle = new Bundle();
        fireItemBundle.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
        //fireItemBundle.putString(KEY_USER_ID, user.getCustomerId());
        fireItemBundle.putString(KEY_OS, VALUE_OS_NAME);
        fireItemBundle.putString(KEY_COUNT, VALUE_COUNT);
        fireItemBundle.putString(KEY_DEVICE_ID, getDeviceId());
        getFireBaseAnalytics().logEvent(EVENT_REGISTRATION_FAILED, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REGISTRATION_FAILED);
//
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        //event.addCallbackParameter(KEY_USER_ID, user.getCustomerId());
//        event.addCallbackParameter(KEY_OS, VALUE_OS_NAME);
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//        event.addCallbackParameter(KEY_DEVICE_ID, getDeviceId());
//        //event.addCallbackParameter(KEY_USER_NAME, user.getCustomerFirstName());
//        //event.addCallbackParameter(KEY_EMAIL, user.getEmailId());
//
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        //event.addPartnerParameter(KEY_USER_ID, user.getCustomerId());
//        event.addPartnerParameter(KEY_OS, VALUE_OS_NAME);
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//        event.addPartnerParameter(KEY_DEVICE_ID, getDeviceId());
//        //event.addPartnerParameter(KEY_USER_NAME, user.getCustomerFirstName());
//        //event.addPartnerParameter(KEY_EMAIL, user.getEmailId());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REGISTRATION_FAILED);
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        //tagEvent.addParameterWithString(KEY_USER_ID, user.getCustomerId());
//        tagEvent.addParameterWithString(KEY_OS, VALUE_OS_NAME);
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.addParameterWithString(KEY_DEVICE, getDeviceId());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        params.putString(KEY_OS, VALUE_OS_NAME);
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        params.putString(KEY_DEVICE_ID, getDeviceId());
//        getFacebookEventsLogger().logEvent(EVENT_REGISTRATION_FAILED, params);

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_REGISTRATION_DATE, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logRegisterDate", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_REGISTRATION_FAILED, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logRegisterFailed", callback.isSuccess() + "");
        });
    }

    public static void logRegistrationDateEvent(User user) {
        Bundle fireItemBundle = new Bundle();
        fireItemBundle.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
        fireItemBundle.putString(KEY_USER_ID, user.getCustomerId());
        fireItemBundle.putString(KEY_DEVICE_ID, getDeviceId());
        getFireBaseAnalytics().logEvent(EVENT_REGISTRATION_DATE, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REGISTRATION_DATE);
//
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addCallbackParameter(KEY_USER_ID, user.getCustomerId());
//        event.addCallbackParameter(KEY_DEVICE_ID, getDeviceId());
//
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addPartnerParameter(KEY_USER_ID, user.getCustomerId());
//        event.addPartnerParameter(KEY_DEVICE_ID, getDeviceId());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REGISTRATION_DATE);
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        tagEvent.addParameterWithString(KEY_USER_ID, user.getCustomerId());
//        tagEvent.addParameterWithString(KEY_DEVICE, getDeviceId());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        params.putString(KEY_USER_ID, user.getCustomerId());
//        params.putString(KEY_DEVICE_ID, getDeviceId());
//        getFacebookEventsLogger().logEvent(EVENT_REGISTRATION_DATE, params);
    }

    public static void logAddCartEvent(String productID, String productName, Double price, String productSku) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, price);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        Bundle item1 = new Bundle();
        item1.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        item1.putString(FirebaseAnalytics.Param.ITEM_ID, productID);
        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Bundle[]{item1});
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_TO_CART, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ADD_TO_CART);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_PRODUCT_NAME, productName);
//        event.addCallbackParameter(KEY_PRODUCT_ID, productID);
//        event.addCallbackParameter(KEY_PRICE, price.toString());
//        event.addCallbackParameter(KEY_CURRENCY, VALUE_CURRENCY);
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addCallbackParameter(KEY_USER_ID, getUserId());
//        event.addCallbackParameter(KEY_DEVICE_ID, getDeviceId());
//
//        event.addPartnerParameter(KEY_PRODUCT_NAME, productName);
//        event.addPartnerParameter(KEY_PRODUCT_ID, productID);
//        event.addPartnerParameter(KEY_PRICE, price.toString());
//        event.addPartnerParameter(KEY_CURRENCY, VALUE_CURRENCY);
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addPartnerParameter(KEY_USER_ID, getUserId());
//        event.addPartnerParameter(KEY_DEVICE_ID, getDeviceId());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ADD_TO_CART);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, productName);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, productID);
//        tagEvent.addParameterWithDouble(KEY_PRICE, price);
//        tagEvent.addParameterWithString(KEY_CURRENCY, VALUE_CURRENCY);
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        tagEvent.addParameterWithString(KEY_USER_ID, getUserId());
//        tagEvent.addParameterWithString(KEY_DEVICE_ID, getDeviceId());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productID);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, productName);
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price, params);

        //Logging pushwoosh event
        TagsBundle attributes = new TagsBundle.Builder()
                .putString(PUSHWOOSH_PARAM_PRODUCT_NAME, productName)
                .putString(PUSHWOOSH_PARAM_PRODUCT_ID, productID)
                .putString(PUSHWOOSH_PARAM_CURRENCY, VALUE_CURRENCY)
                .putInt(PUSHWOOSH_PARAM_PRICE, Integer.parseInt(price + ""))
                .build();
        getPushwooshInstance().postEvent(PUSHWOOSH_EVENT_ADD_TO_CART, attributes);

        Pushwoosh.getInstance().setTags(Tags.booleanTag(PUSHWOOSH_TAG_ADD_TO_CART, true), callback -> {
            Log.e("logAddCartEvent", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_ADD_TO_CART_PRODUCT, productSku), callback -> {
            Log.e("logAddCartEvent", callback.isSuccess() + "");
        });

    }

    public static void logItemsInCartEvent(List<ShoppingCartItem> itemList, double totalCartPrice) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Bundle item1 = new Bundle();
            try {
                item1.putString(FirebaseAnalytics.Param.ITEM_ID, itemList.get(i).getProduct().getProductId());
                item1.putString(FirebaseAnalytics.Param.ITEM_NAME, itemList.get(i).getProduct().getDescriptions().get(0).getProductName());
                item1.putDouble(FirebaseAnalytics.Param.VALUE, itemList.get(i).getProduct().getOriginalPrice());
                item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            } catch (Exception e) {
                //
            }
            itemArray[i] = item1;
        }
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, totalCartPrice);
        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        fireItemBundle.putInt(KEY_CART_COUNT, itemArray.length);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, fireItemBundle);

        String productListString = gson.toJson(itemList);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ITEMS_IN_CART);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_LIST_PRODUCT, productListString);
//        event.addCallbackParameter(KEY_CART_COUNT, String.valueOf(itemArray.length));
//
//        event.addPartnerParameter(KEY_LIST_PRODUCT, productListString);
//        event.addPartnerParameter(KEY_CART_COUNT, String.valueOf(itemArray.length));
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ITEMS_IN_CART);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_LIST_PRODUCT, productListString);
//        tagEvent.addParameterWithInt(KEY_CART_COUNT, itemArray.length);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(itemList));
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        getFacebookEventsLogger().logEvent(EVENT_ITEMS_IN_CART, totalCartPrice, params);
    }

    public static void logViewCartEvent(List<ShoppingCartItem> itemList, double totalCartPrice) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Bundle item1 = new Bundle();
            try {
                item1.putString(FirebaseAnalytics.Param.ITEM_ID, itemList.get(i).getProduct().getProductId());
                item1.putString(FirebaseAnalytics.Param.ITEM_NAME, itemList.get(i).getProduct().getDescriptions().get(0).getProductName());
                item1.putDouble(FirebaseAnalytics.Param.VALUE, itemList.get(i).getProduct().getOriginalPrice());
                item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            } catch (Exception e) {
                //
            }
            itemArray[i] = item1;
        }
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, totalCartPrice);
        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        fireItemBundle.putInt(KEY_CART_COUNT, itemArray.length);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.VIEW_CART, fireItemBundle);

        String productListString = gson.toJson(itemList);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ITEMS_IN_CART);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_LIST_PRODUCT, productListString);
//        event.addCallbackParameter(KEY_CART_COUNT, String.valueOf(itemArray.length));
//
//        event.addPartnerParameter(KEY_LIST_PRODUCT, productListString);
//        event.addPartnerParameter(KEY_CART_COUNT, String.valueOf(itemArray.length));
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_VIEW_CART);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_LIST_PRODUCT, productListString);
//        tagEvent.addParameterWithInt(KEY_CART_COUNT, itemArray.length);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(itemList));
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        getFacebookEventsLogger().logEvent(EVENT_VIEW_CART, totalCartPrice, params);
    }

    public static void logRemoveCartEvent(String productID, String productName, Double price) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle item1 = new Bundle();
        item1.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
        item1.putString(FirebaseAnalytics.Param.ITEM_ID, productID);
        item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        item1.putDouble(FirebaseAnalytics.Param.VALUE, price);
        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Bundle[]{item1});
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, price);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ITEM_REMOVED_FROM_CART);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_PRODUCT_NAME, productName);
//        event.addCallbackParameter(KEY_PRODUCT_ID, productID);
//        event.addCallbackParameter(KEY_PRICE, price.toString());
//        event.addCallbackParameter(KEY_CURRENCY, VALUE_CURRENCY);
//
//        event.addPartnerParameter(KEY_PRODUCT_NAME, productName);
//        event.addPartnerParameter(KEY_PRODUCT_ID, productID);
//        event.addPartnerParameter(KEY_PRICE, price.toString());
//        event.addPartnerParameter(KEY_CURRENCY, VALUE_CURRENCY);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ITEM_REMOVED_FROM_CART);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, productName);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, productID);
//        tagEvent.addParameterWithDouble(KEY_PRICE, price);
//        tagEvent.addParameterWithString(KEY_CURRENCY, VALUE_CURRENCY);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productID);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, productName);
//        getFacebookEventsLogger().logEvent(EVENT_ITEM_REMOVED_FROM_CART, price, params);

        Pushwoosh.getInstance().setTags(Tags.booleanTag(PUSHWOOSH_TAG_ADD_TO_CART, false), callback -> {
            Log.e("logRemoveCartEvent", callback.isSuccess() + "");
        });
    }

    public static void logProceedCheckOutEvent(List<ShoppingCartItem> itemList, double totalPrice) {
        cartItemsList = itemList;
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Bundle item1 = new Bundle();
            item1.putString(FirebaseAnalytics.Param.ITEM_ID, itemList.get(i).getProduct().getProductId());
            item1.putString(FirebaseAnalytics.Param.ITEM_NAME, itemList.get(i).getProduct().getDescriptions().get(0).getProductName());
            item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            item1.putDouble(FirebaseAnalytics.Param.VALUE, itemList.get(i).getOneTimePrice());
            itemArray[i] = item1;
        }
        fireItemBundle.putInt(KEY_CART_COUNT, itemList.size());
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, totalPrice);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, fireItemBundle);

        String productListString = gson.toJson(itemList);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PROCEED_TO_CHECKOUT);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_ITEM_LIST, productListString);
//        event.addCallbackParameter(KEY_CART_COUNT, itemList.size() + "");
//        event.addCallbackParameter(KEY_PRICE, totalPrice + "");
//        event.addCallbackParameter(KEY_CURRENCY, "KWD");
//        event.addPartnerParameter(KEY_PRODUCTS, productListString);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PROCEED_TO_CHECKOUT);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCTS, productListString);
//        tagEvent.addParameterWithInt(KEY_CART_COUNT, itemList.size());
//        tagEvent.addParameterWithString(KEY_PRICE, totalPrice + "");
//        tagEvent.addParameterWithString(KEY_CURRENCY, "KWD");
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(cartItemsList));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, itemList.size());
//        params.putInt(AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE, 0);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, totalPrice, params);

        Pushwoosh.getInstance().setTags(Tags.booleanTag(PUSHWOOSH_TAG_BEGIN_CHECKOUT, true), callback -> {
            Log.e("logProceedCheckOutEvent", callback.isSuccess() + "");
        });

    }

    public static void logPromoCodeEvent(String promoCode, Double discountAmount) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_PROMO_CODE, promoCode);
        fireItemBundle.putDouble(KEY_DISCOUNT_AMOUNT, discountAmount);
        getFireBaseAnalytics().logEvent(EVENT_PROMO_CODE, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PROMO_CODE);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_PROMO_CODE, promoCode);
//        event.addCallbackParameter(KEY_DISCOUNT_AMOUNT, "" + discountAmount);
//
//        event.addPartnerParameter(KEY_PROMO_CODE, promoCode);
//        event.addPartnerParameter(KEY_DISCOUNT_AMOUNT, "" + discountAmount);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PROMO_CODE);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PROMO_CODE, promoCode);
//        tagEvent.addParameterWithString(KEY_DISCOUNT_AMOUNT, "" + discountAmount);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_PROMO_CODE, promoCode);
//        params.putDouble(KEY_DISCOUNT_AMOUNT, discountAmount);
//        getFacebookEventsLogger().logEvent(EVENT_PROMO_CODE, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_PROMO_CODE, promoCode), callback -> {
            Log.e("logPromoCode", callback.isSuccess() + "");
        });
    }

    public static void logOrderConfirmedEvent(List<ShoppingCartItem> itemList, Double amount, String orderId) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Bundle item1 = new Bundle();
            item1.putString(FirebaseAnalytics.Param.ITEM_ID, itemList.get(i).getProduct().getProductId());
            item1.putString(FirebaseAnalytics.Param.ITEM_NAME, itemList.get(i).getProduct().getDescriptions().get(0).getProductName());
            item1.putDouble(FirebaseAnalytics.Param.VALUE, itemList.get(i).getProduct().getOriginalPrice());
            item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            itemArray[i] = item1;
        }

        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        fireItemBundle.putString(KEY_ORDER_ID, orderId);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        getFireBaseAnalytics().logEvent(EVENT_ORDER_CONFIRMATION, fireItemBundle);


        String productListString = gson.toJson(itemList);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ORDER_CONFIRMATION);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_PRODUCTS, productListString);
//        event.addCallbackParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addCallbackParameter(KEY_CURRENCY, "KWD");
//        event.addCallbackParameter(KEY_ORDER_ID, orderId);
//
//        event.addPartnerParameter(KEY_PRODUCTS, productListString);
//        event.addPartnerParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addPartnerParameter(KEY_CURRENCY, "KWD");
//        event.addPartnerParameter(KEY_ORDER_ID, orderId);
//        Adjust.trackEvent(event);

        for (int i = 0; i < itemList.size(); i++) {
            String imageString = "";
            String[] catArray = new String[itemList.get(i).getProduct().getCategories().size()];
            try {
                imageString = itemList.get(i).getProduct().getProductConfigurables().get(0).getProductImages().get(0).getImageUrl();
                for (int j = 0; j < itemList.get(i).getProduct().getCategories().size(); j++) {
                    catArray[j] = itemList.get(i).getProduct().getCategories().get(j).getCategoryId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            Insider insider = Insider.Instance;
//            InsiderProduct insiderProduct = insider.createNewProduct(
//                    itemList.get(i).getProduct().getProductId(), itemList.get(i).getProduct().getSku(), catArray,
//                    imageString, itemList.get(i).getProduct().getOriginalPrice(), "KWD");
//
//            Insider insider2 = Insider.Instance;
//            insider2.itemPurchased(orderId, insiderProduct);
        }

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ORDER_CONFIRMATION);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCTS, productListString);
//        tagEvent.addParameterWithDouble(KEY_TOTAL_PRICE, amount);
//        tagEvent.addParameterWithString(KEY_CURRENCY, "KWD");
//        tagEvent.addParameterWithString(KEY_ORDER_ID, orderId);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, orderId);
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(cartItemsList));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, itemList.size());
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(EVENT_ORDER_CONFIRMATION, amount, params);

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_ORDER_SUCCESS, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logTagOrderSuccess", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.intTag(PUSHWOOSH_TAG_ORDER_VALUE, amount.intValue()), callback -> {
            Log.e("logTagOrderValue", callback.isSuccess() + "");
        });
    }

    public static void logPurchaseEvent(List<ShoppingCartItem> cartItemsList, Double amount, String orderId) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[cartItemsList.size()];
        for (int i = 0; i < cartItemsList.size(); i++) {
            Bundle item1 = new Bundle();
            item1.putString(FirebaseAnalytics.Param.ITEM_ID, cartItemsList.get(i).getProduct().getProductId());
            item1.putString(FirebaseAnalytics.Param.ITEM_NAME, cartItemsList.get(i).getProduct().getDescriptions().get(0).getProductName());
            item1.putDouble(FirebaseAnalytics.Param.VALUE, cartItemsList.get(i).getProduct().getOriginalPrice());
            item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            itemArray[i] = item1;
        }

        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.PURCHASE, fireItemBundle);


//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PURCHASE);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addCallbackParameter(KEY_CURRENCY, "KWD");
//
//        event.addPartnerParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addPartnerParameter(KEY_CURRENCY, "KWD");
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PURCHASE);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithDouble(KEY_TOTAL_PRICE, amount);
//        tagEvent.addParameterWithString(KEY_CURRENCY, "KWD");
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, cartItemsList.size());
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, orderId);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logPurchase(BigDecimal.valueOf(amount), Currency.getInstance(VALUE_CURRENCY), params);

        String items = "";
        try {
            items = new Gson().toJson(cartItemsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TagsBundle attributes = new TagsBundle.Builder()
                .putString("items", items)
                .putInt("item_count", cartItemsList.size())
                .putString("order_id", orderId)
                .putInt("__amount", Integer.parseInt(amount + ""))
                .putString("__currency", VALUE_CURRENCY)
                .build();

        getPushwooshInstance().postEvent("purchase", attributes);
    }

    public static void logRevenueEvent(Double amount) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
        getFireBaseAnalytics().logEvent(EVENT_REVENUE, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REVENUE);
//        setDefaultAdjustParams(event);
//        event.setRevenue(amount, "KWD");
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REVENUE);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithDouble(KEY_TOTAL_PRICE, amount);
//        tagEvent.addParameterWithString(KEY_CURRENCY, "KWD");
//        tagEvent.build();

//        Log.e("logRevenueEvent", "KWD " + amount);
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(EVENT_REVENUE, amount, params);

        Pushwoosh.getInstance().setTags(Tags.intTag(PUSHWOOSH_TAG_ORDER_VALUE, amount.intValue()), callback -> {
            Log.e("logTagOrderValue", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_PURCHASE_LAST_DATE, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logPurchaseDate", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_DEFAULT_PURCHASE_LAST_DATE, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logPurchaseDate", callback.isSuccess() + "");
        });

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_PURCHASE_REVENUE, "KWD \\ " + amount), callback -> {
            Log.e("logPurchaseDate", callback.isSuccess() + "");
        });
    }

    public static void logOrderFailedEvent(List<ShoppingCartItem> itemList, Double amount) {
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        Bundle[] itemArray = new Bundle[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            Bundle item1 = new Bundle();
            item1.putString(FirebaseAnalytics.Param.ITEM_ID, itemList.get(i).getProduct().getProductId());
            item1.putString(FirebaseAnalytics.Param.ITEM_NAME, itemList.get(i).getProduct().getDescriptions().get(0).getProductName());
            item1.putDouble(FirebaseAnalytics.Param.VALUE, itemList.get(i).getProduct().getOriginalPrice());
            item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            itemArray[i] = item1;
        }

        fireItemBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        fireItemBundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
        fireItemBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        getFireBaseAnalytics().logEvent(EVENT_ORDER_FAILED, fireItemBundle);

        String productListString = gson.toJson(itemList);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ORDER_FAILED);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter(KEY_PRODUCTS, productListString);
//        event.addCallbackParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addPartnerParameter(KEY_TOTAL_PRICE, amount + "");
//        event.addCallbackParameter(KEY_CURRENCY, "KWD");
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ORDER_FAILED);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCTS, productListString);
//        tagEvent.addParameterWithDouble(KEY_TOTAL_PRICE, amount);
//        tagEvent.addParameterWithString(KEY_CURRENCY, "KWD");
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(cartItemsList));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, itemList.size());
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(EVENT_ORDER_FAILED, amount, params);

        Pushwoosh.getInstance().setTags(Tags.dateTag(PUSHWOOSH_TAG_ORDER_FAILED, DateTimeUtils.getCurrentUTCDate()), callback -> {
            Log.e("logOrderFailed", callback.isSuccess() + "");
        });
    }

    public static void logPaymentMethodEvent(PaymentMode selectedPaymentMode) {
        String method = selectedPaymentMode.getName();
        Bundle fireItemBundle = new Bundle();
        setDefaultFireBaseParams(fireItemBundle);
        fireItemBundle.putString(KEY_PAYMENT_METHOD, method);
        getFireBaseAnalytics().logEvent(EVENT_PAYMENT_METHOD, fireItemBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PAYMENT_METHOD);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_PAYMENT_METHOD, method);
//        event.addPartnerParameter(KEY_PAYMENT_METHOD, method);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PAYMENT_METHOD);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PAYMENT_METHOD, method);
//        tagEvent.build();

        //Logging facebbok event
//        Bundle params = new Bundle();
//        params.putString(KEY_PAYMENT_METHOD, method);
//        getFacebookEventsLogger().logEvent(EVENT_PAYMENT_METHOD, params);

        TagsBundle attributes = new TagsBundle.Builder()
                .putString("type", method)
                .putString("user_id", getUserId())
                .build();

        getPushwooshInstance().postEvent("Payment method is changed", attributes);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_PAYMENT_METHOD, method), callback -> {
            Log.e("logPaymentMethod", callback.isSuccess() + "");
        });

    }

    public static void logAddPaymentInfoFirebaseEvent(PaymentMode selectedPaymentMode, boolean paymentSuccess) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString(KEY_PAYMENT_METHOD, selectedPaymentMode.getName());
//        try {
//            analyticsBundle.putString(FirebaseAnalytics.Param.END_DATE, DateTimeUtils.getCurrentStringDateTime());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        analyticsBundle.putInt(FirebaseAnalytics.Param.SUCCESS, paymentSuccess ? 1 : 0);
//        analyticsBundle.putString("payment_status", paymentSuccess ? "success" : "failed");
//        analyticsBundle.putDouble(KEY_PRICE, amount);
//        analyticsBundle.putString(KEY_CURRENCY, VALUE_CURRENCY);
        if (paymentSuccess)
            getFireBaseAnalytics().logEvent(EVENT_PAYMENT_SUCCESSFUL, analyticsBundle);
        else
            getFireBaseAnalytics().logEvent(EVENT_PAYMENT_FAILED, analyticsBundle);


        String eventKey = "";
        String insiderKey = "";
        if (paymentSuccess) {
//            eventKey = ADJUST_KEY_PAYMENT_SUCCESSFUL;
            insiderKey = EVENT_PAYMENT_SUCCESSFUL;
        } else {
//            eventKey = ADJUST_KEY_PAYMENT_FAILED;
            insiderKey = EVENT_PAYMENT_FAILED;
        }

//        AdjustEvent event = new AdjustEvent(eventKey);
//        setDefaultAdjustParams(event);

//        event.addCallbackParameter(KEY_PAYMENT_METHOD, selectedPaymentMode.getName());

//        event.addPartnerParameter(KEY_PAYMENT_METHOD, selectedPaymentMode.getName());

//        Adjust.trackEvent(event);
//
//        InsiderEvent tagEvent = Insider.Instance.tagEvent(insiderKey);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PAYMENT_METHOD, selectedPaymentMode.getName());
//        tagEvent.build();

        //Logging Facebook event
//        Bundle fbParams = new Bundle();
//        fbParams.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, paymentSuccess ? 1 : 0);
//        fbParams.putString(KEY_PAYMENT_METHOD, selectedPaymentMode.getName());
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, fbParams);
    }

    public static void logPaymentInfo(double amount, String paymentMethod) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, amount);
        analyticsBundle.putString(FirebaseAnalytics.Param.PAYMENT_TYPE, paymentMethod);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, analyticsBundle);

        Pushwoosh.getInstance().setTags(Tags.booleanTag(PUSHWOOSH_TAG_ADD_PAYMENT_INFO, true), callback -> {
            Log.e("logPaymentInfo", callback.isSuccess() + "");
        });


    }

    public static void logPaymentDateEvent() {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        getFireBaseAnalytics().logEvent(EVENT_PAYMENT_DATE, analyticsBundle);
    }

    public static void logContactUsEvent(String method) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString("medium", method);
        getFireBaseAnalytics().logEvent(EVENT_CONTACT_US, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_CONTACT_US);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("medium", method);
//
//        event.addPartnerParameter("medium", method);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_CONTACT_US);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("medium", method);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString("medium", method);
//        getFacebookEventsLogger().logEvent(EVENT_CONTACT_US, params);
    }

    public static void logSocialEvent(String channel) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString("sm_channel", channel);
        getFireBaseAnalytics().logEvent(EVENT_SOCIAL_MEDIA_ICON, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_SOCIAL_MEDIA_ICON);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("sm_channel", channel);
//        event.addPartnerParameter("sm_channel", channel);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_SOCIAL_MEDIA_ICON);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("sm_channel", channel);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString("sm_channel", channel);
//        getFacebookEventsLogger().logEvent(EVENT_SOCIAL_MEDIA_ICON, params);
    }

    public static void logRefundEvent() {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        getFireBaseAnalytics().logEvent(EVENT_REFUND, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REFUND);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("count", "1");
//        event.addPartnerParameter("count", "1");
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REFUND);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithInt("count", 1);
////        tagEvent.addParameterWithString(FirebaseAnalytics.Param.TRANSACTION_ID, selectedOrder.getOrderId());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        getFacebookEventsLogger().logEvent(EVENT_REFUND, params);
    }

    public static void logRefundedValueEvent(Double amount) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putDouble(KEY_REFUND_VALUE, amount);
        getFireBaseAnalytics().logEvent(EVENT_REFUND_VALUE, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REFUND);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_REFUND_VALUE, amount + "");
//        event.addPartnerParameter(KEY_REFUND_VALUE, amount + "");
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REFUND_VALUE);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithDouble(KEY_REFUND_VALUE, amount);
////        tagEvent.addParameterWithString(FirebaseAnalytics.Param.TRANSACTION_ID, selectedOrder.getOrderId());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putDouble(KEY_REFUND_VALUE, amount);
//        getFacebookEventsLogger().logEvent(EVENT_REFUND_VALUE, params);
    }

    public static void logRefundedProductsEvent(Order selectedOrder) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        Bundle[] itemArray = new Bundle[selectedOrder.getOrderProducts().size()];
        for (int i = 0; i < selectedOrder.getOrderProducts().size(); i++) {
            Bundle item1 = new Bundle();
            try {
                item1.putString(FirebaseAnalytics.Param.ITEM_ID, selectedOrder.getOrderProducts().get(i).getOrderProductId());
                item1.putString(FirebaseAnalytics.Param.ITEM_NAME, selectedOrder.getOrderProducts().get(i).getProductName());
                item1.putDouble(FirebaseAnalytics.Param.VALUE, selectedOrder.getOrderProducts().get(i).getOneTimePrice());
                item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            } catch (Exception e) {
                //
            }
            itemArray[i] = item1;
        }
        analyticsBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, itemArray);
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, selectedOrder.getTotal());
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        analyticsBundle.putString(FirebaseAnalytics.Param.COUPON, selectedOrder.getCouponCode());
        analyticsBundle.putDouble(FirebaseAnalytics.Param.SHIPPING, selectedOrder.getShippingCharge());
        analyticsBundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, selectedOrder.getOrderId());
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.REFUND, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_REFUND);
//        setDefaultAdjustParams(event);
//
//        String productListString = gson.toJson(itemArray);
//        event.addCallbackParameter(KEY_PRODUCTS, productListString);
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_REFUND_PRODUCTS);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCTS, productListString);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, selectedOrder.getOrderId());
//        try {
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, new Gson().toJson(selectedOrder));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, selectedOrder.getOrderProducts().size());
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(EVENT_REFUND_PRODUCTS, selectedOrder.getTotal(), params);
    }

    public static void logDiscountedPurchase(Double price, Double discountAmount, String couponCode) {
        TagsBundle attributes = new TagsBundle.Builder()
                .putInt("discount_amount", Integer.parseInt(discountAmount + ""))
                .putString("currency", VALUE_CURRENCY)
                .putInt("price", Integer.parseInt(price + ""))
                .putString("coupon_value", couponCode)
                .build();

        getPushwooshInstance().postEvent("Discount purchase", attributes);
    }

    public static void userLoginEvent(String userId) {
        TagsBundle attributes = new TagsBundle.Builder()
                .putString("type", "LOCAL")
                .putString("user_id", userId)
                .build();

        getPushwooshInstance().postEvent("Log in", attributes);
    }


    public static void logAppShareEvent(String channel, String itemId) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "app_share");
        analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        analyticsBundle.putString(FirebaseAnalytics.Param.METHOD, channel);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.SHARE, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_APP_SHARE);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(FirebaseAnalytics.Param.ITEM_ID, itemId);
//        event.addCallbackParameter(FirebaseAnalytics.Param.METHOD, channel);
//
//        event.addPartnerParameter(FirebaseAnalytics.Param.ITEM_ID, itemId);
//        event.addPartnerParameter(FirebaseAnalytics.Param.METHOD, channel);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_APP_SHARE);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(FirebaseAnalytics.Param.ITEM_ID, itemId);
//        tagEvent.addParameterWithString(FirebaseAnalytics.Param.METHOD, channel);
//        tagEvent.build();
    }

    public static void logProductShareEvent(String channel, String itemId) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
//        analyticsBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "product_share");
//        analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
//        analyticsBundle.putString(FirebaseAnalytics.Param.METHOD, channel);
//        analyticsBundle.putString(KEY_SHARED_CHANNEL, channel);
//        analyticsBundle.putString(KEY_PRODUCT_ID, itemId);
//        analyticsBundle.putString("language", getSelectedLanguage());
//        getFireBaseAnalytics().logEvent(EVENT_PRODUCT_SHARE, analyticsBundle);

        Bundle firebaseBundle = new Bundle();
        setDefaultFireBaseParams(firebaseBundle);
        firebaseBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Product");
        firebaseBundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        firebaseBundle.putString(FirebaseAnalytics.Param.METHOD, channel);
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.SHARE, firebaseBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_PRODUCT_SHARE);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_PRODUCT_ID, itemId);
//        event.addCallbackParameter(KEY_SHARED_CHANNEL, channel);
//        event.addCallbackParameter("language", getSelectedLanguage());
//
//        event.addPartnerParameter(KEY_PRODUCT_ID, itemId);
//        event.addPartnerParameter(KEY_SHARED_CHANNEL, channel);
//        event.addCallbackParameter("language", getSelectedLanguage());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_PRODUCT_SHARE);
//        setDefaultInsiderParams(tagEvent);
//
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, itemId);
//        tagEvent.addParameterWithString(KEY_SHARED_CHANNEL, channel);
//        tagEvent.addParameterWithString("language", getSelectedLanguage());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_SHARED_CHANNEL, channel);
//        params.putString(KEY_PRODUCT_ID, itemId);
//        params.putString("language", getSelectedLanguage());
//        getFacebookEventsLogger().logEvent(EVENT_PRODUCT_SHARE, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_PRODUCT_SHARE, itemId), callback -> {
            Log.e("logProductShare", callback.isSuccess() + "");
        });
    }

    public static void logShippingAreaEvent(String area) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString("shipment_area", area);
        getFireBaseAnalytics().logEvent(EVENT_ADD_ADDRESS, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ADD_ADDRESS);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("shipment_area", area);
//
//        event.addPartnerParameter("shipment_area", area);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ADD_ADDRESS);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("shipment_area", area);
//        tagEvent.build();

//        Bundle params = new Bundle();
//        params.putString("shipment_area", area);
//        getFacebookEventsLogger().logEvent(EVENT_ADD_ADDRESS, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_ADDRESS, area), callback -> {
            Log.e("logTagAddress", callback.isSuccess() + "");
        });
    }

    /*public static void logTagAddress(String address) {

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_ADDRESS, address), callback -> {
            Log.e("logTagAddress", callback.isSuccess() + "");
        });
    }*/


    public static void logShippingInfoEvent(ShippingMode shippingMode) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, shippingMode.getCharge());
        analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
        analyticsBundle.putString(FirebaseAnalytics.Param.SHIPPING_TIER, shippingMode.getName());
        getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_ADD_SHIPPING_INFO);
//        setDefaultAdjustParams(event);
//        event.addCallbackParameter("shipping_mode", shippingMode.getName());
//        event.addPartnerParameter("shipping_mode", shippingMode.getName());
//        event.addCallbackParameter("shipping_charge", shippingMode.getCharge() + "");
//        event.addPartnerParameter("shipping_charge", shippingMode.getCharge() + "");
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_ADD_SHIPPING_INFO);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("shipping_mode", shippingMode.getName());
//        tagEvent.addParameterWithDouble("shipping_charge", shippingMode.getCharge());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString("shipping_mode", shippingMode.getName());
//        params.putDouble("shipping_charge", shippingMode.getCharge());
//        getFacebookEventsLogger().logEvent(EVENT_ADD_SHIPPING_INFO, params);
    }

    public static void logAddToWishlistEvent(String productName, String productId, String catId, Double price, String productSku) {
        Bundle analyticsBundle = new Bundle();
        try {
            setDefaultFireBaseParams(analyticsBundle);
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, price);
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            Bundle item1 = new Bundle();
            item1.putString(FirebaseAnalytics.Param.ITEM_NAME, productName);
            item1.putString(FirebaseAnalytics.Param.ITEM_ID, productId);
            item1.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            item1.putDouble(FirebaseAnalytics.Param.VALUE, price);
            analyticsBundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, new Bundle[]{item1});
            getFireBaseAnalytics().logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_WISHLIST);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_PRODUCT_ID, productId);
//        event.addCallbackParameter(KEY_PRODUCT_NAME, productName);
//        event.addCallbackParameter(KEY_PRICE, "" + price);
//
//        event.addPartnerParameter(KEY_PRODUCT_ID, productId);
//        event.addPartnerParameter(KEY_PRODUCT_NAME, productName);
//        event.addPartnerParameter(KEY_PRICE, "" + price);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_WISHLIST);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, productId);
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, productName);
//        tagEvent.addParameterWithString(KEY_PRICE, "" + price);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, productId);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, productName);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//        getFacebookEventsLogger().logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, price, params);

        //Logging pushwoosh events

        TagsBundle attributes = new TagsBundle.Builder()
                .putString(PUSHWOOSH_PARAM_CURRENCY, VALUE_CURRENCY)
                .putString(PUSHWOOSH_PARAM_PRODUCT_NAME, productName)
                .putString(PUSHWOOSH_PARAM_PRODUCT_ID, productId)
                .putInt(PUSHWOOSH_PARAM_PRICE, Integer.parseInt(price + ""))
                .build();

        getPushwooshInstance().postEvent(PUSHWOOSH_EVENT_ADD_TO_WISHLIST, attributes);

        Pushwoosh.getInstance().setTags(Tags.booleanTag(PUSHWOOSH_TAG_ADD_TO_WISHLIST, true), callback -> {
            Log.e("logAddToWishlistEvent", callback.isSuccess() + "");
        });

        /*Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_ADD_TO_WISHLIST_PRODUCT, productId), callback -> {
            Log.e("logAddToWishlistEvent", callback.isSuccess() + "");
        });*/

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_ADD_TO_WISHLIST_PRODUCT, productSku), callback -> {
            Log.e("logAddToWishlistEvent", callback.isSuccess() + "");
        });
    }

    public static void logNotifyMeEvent(Product product) {
        Bundle analyticsBundle = new Bundle();
        try {
            setDefaultFireBaseParams(analyticsBundle);
            analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.getProductId());
            analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
            getFireBaseAnalytics().logEvent(EVENT_NOTIFY_ME, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_NOTIFY_ME);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_PRODUCT_ID, product.getProductId());
//        event.addCallbackParameter(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        event.addCallbackParameter(KEY_PRICE, "" + product.getOriginalPrice());
//        event.addCallbackParameter(KEY_CURRENCY, VALUE_CURRENCY);
//
//        event.addPartnerParameter(KEY_PRODUCT_ID, product.getProductId());
//        event.addPartnerParameter(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        event.addPartnerParameter(KEY_PRICE, "" + product.getOriginalPrice());
//        event.addCallbackParameter(KEY_CURRENCY, VALUE_CURRENCY);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_NOTIFY_ME);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, product.getProductId());
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        tagEvent.addParameterWithString(KEY_PRICE, "" + product.getOriginalPrice());
//        tagEvent.addParameterWithString(KEY_CURRENCY, VALUE_CURRENCY);
//        tagEvent.build();

        //Logging facebook events
//        try {
//            Bundle params = new Bundle();
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, product.getProductId());
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, product.getDescriptions().get(0).getProductName());
//            params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//            getFacebookEventsLogger().logEvent(EVENT_NOTIFY_ME, product.getOriginalPrice(), params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Log pushwoosh event
        TagsBundle attributes = new TagsBundle.Builder()
                .putInt(PUSHWOOSH_PARAM_PRICE, Integer.parseInt(product.getOriginalPrice() + ""))
                .putString(PUSHWOOSH_PARAM_CURRENCY, VALUE_CURRENCY)
                .putString(PUSHWOOSH_PARAM_PRODUCT_ID, product.getProductId())
                .putString(PUSHWOOSH_PARAM_PRODUCT_NAME, product.getDescriptions().get(0).getProductName())
                .build();

        getPushwooshInstance().postEvent(PUSHWOOSH_EVENT_NOTIFY_ME, attributes);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_NOTIFY_ME_PRODUCT, product.getSku()), callback -> {
            Log.e("logNotifyMe", callback.isSuccess() + "");
        });
    }

    public static void logLikeProductEvent(Product product) {
        Bundle analyticsBundle = new Bundle();
        try {
            setDefaultFireBaseParams(analyticsBundle);
            analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.getProductId());
            analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, VALUE_CURRENCY);
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
            getFireBaseAnalytics().logEvent(EVENT_RELATED_PRODUCT, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Bundle itemBundle = new Bundle();
//        try {
//            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
//            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product.getCategories().get(0).getDescriptions().get(0).getCategoryName());
//            itemBundle.putString("product_id", product.getProductId());
//            ArrayList<Bundle> parcelabeList = new ArrayList<>();
//            parcelabeList.add(itemBundle);
//            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, parcelabeList);

//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_RELATED_PRODUCT);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter(KEY_PRODUCT_ID, product.getProductId());
//        event.addCallbackParameter(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        event.addCallbackParameter(KEY_PRICE, "" + product.getOriginalPrice());
//
//        event.addPartnerParameter(KEY_PRODUCT_ID, product.getProductId());
//        event.addPartnerParameter(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        event.addPartnerParameter(KEY_PRICE, "" + product.getOriginalPrice());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_RELATED_PRODUCT);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString(KEY_PRODUCT_ID, product.getProductId());
//        tagEvent.addParameterWithString(KEY_PRODUCT_NAME, product.getDescriptions().get(0).getProductName());
//        tagEvent.addParameterWithString(KEY_PRICE, "" + product.getOriginalPrice());
//        tagEvent.build();

        //Logging facebook events
//        try {
//            Bundle params = new Bundle();
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, VALUE_CONTENT_TYPE);
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, product.getProductId());
//            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, product.getDescriptions().get(0).getProductName());
//            params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, VALUE_CURRENCY);
//            getFacebookEventsLogger().logEvent(EVENT_RELATED_PRODUCT, product.getOriginalPrice(), params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void logForgotPasswordEvent(String userId) {
        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
        analyticsBundle.putString(KEY_USER_ID, userId);
        analyticsBundle.putString(KEY_OS, VALUE_OS_NAME);
        analyticsBundle.putString(KEY_COUNT, VALUE_COUNT);
        analyticsBundle.putString(KEY_LANGUAGE, getSelectedLanguage());
        getFireBaseAnalytics().logEvent(EVENT_FORGOT_PASSWORD, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_FORGOT_PASSWORD);
//
//        event.addCallbackParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addCallbackParameter(KEY_USER_ID, userId);
//        event.addCallbackParameter(KEY_OS, VALUE_OS_NAME);
//        event.addCallbackParameter(KEY_COUNT, VALUE_COUNT);
//        event.addCallbackParameter(KEY_LANGUAGE, getSelectedLanguage());
//
//        event.addPartnerParameter(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        event.addPartnerParameter(KEY_USER_ID, userId);
//        event.addPartnerParameter(KEY_OS, VALUE_OS_NAME);
//        event.addPartnerParameter(KEY_COUNT, VALUE_COUNT);
//        event.addPartnerParameter(KEY_LANGUAGE, getSelectedLanguage());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_FORGOT_PASSWORD);
//        tagEvent.addParameterWithString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        tagEvent.addParameterWithString(KEY_USER_ID, userId);
//        tagEvent.addParameterWithString(KEY_OS, VALUE_OS_NAME);
//        tagEvent.addParameterWithString(KEY_COUNT, VALUE_COUNT);
//        tagEvent.addParameterWithString(KEY_LANGUAGE, getSelectedLanguage());
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString(KEY_TIME_STAMP, DateTimeUtils.getCurrentStringDateTime());
//        params.putString(KEY_USER_ID, userId);
//        params.putString(KEY_OS, VALUE_OS_NAME);
//        params.putString(KEY_COUNT, VALUE_COUNT);
//        params.putString(KEY_LANGUAGE, getSelectedLanguage());
//        getFacebookEventsLogger().logEvent(EVENT_FORGOT_PASSWORD, params);
    }

    public static void logStoreNameEvent(String storeName) {
        Bundle analyticsBundle = new Bundle();
        setDefaultFireBaseParams(analyticsBundle);
        analyticsBundle.putString("store_name", storeName);
        getFireBaseAnalytics().logEvent(EVENT_STORE_LOCATION, analyticsBundle);

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_STORE_LOCATION);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("store_name", storeName);
//
//        event.addPartnerParameter("store_name", storeName);
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_STORE_LOCATION);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("store_name", storeName);
//        tagEvent.build();

        //Logging facebook event
//        Bundle params = new Bundle();
//        params.putString("store_name", storeName);
//        getFacebookEventsLogger().logEvent(EVENT_STORE_LOCATION, params);

        Pushwoosh.getInstance().setTags(Tags.stringTag(PUSHWOOSH_TAG_STORE_LOCATION, storeName), callback -> {
            Log.e("logTagStore", callback.isSuccess() + "");
        });
    }

    public static void logBarcodeScanEvent(Product product) {
        Bundle analyticsBundle = new Bundle();
        try {
            analyticsBundle.putString(FirebaseAnalytics.Param.CURRENCY, "KWD");
            analyticsBundle.putDouble(FirebaseAnalytics.Param.VALUE, product.getOriginalPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle itemBundle = new Bundle();
        try {
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getDescriptions().get(0).getProductName());
            itemBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product.getCategories().get(0).getDescriptions().get(0).getCategoryName());
            itemBundle.putString("product_id", product.getProductId());
            ArrayList<Bundle> parcelabeList = new ArrayList<>();
            parcelabeList.add(itemBundle);
            analyticsBundle.putParcelableArrayList(FirebaseAnalytics.Param.ITEMS, parcelabeList);
            getFireBaseAnalytics().logEvent(EVENT_BARCODE_SCAN, analyticsBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        AdjustEvent event = new AdjustEvent(ADJUST_KEY_BARCODE_SCAN);
//        setDefaultAdjustParams(event);
//
//        event.addCallbackParameter("product_id", product.getProductId());
//        event.addCallbackParameter("product_name", product.getDescriptions().get(0).getProductName());
//        event.addCallbackParameter("product_price", "" + product.getOriginalPrice());
//
//        event.addPartnerParameter("product_id", product.getProductId());
//        event.addPartnerParameter("product_name", product.getDescriptions().get(0).getProductName());
//        event.addPartnerParameter("product_price", "" + product.getOriginalPrice());
//
//        Adjust.trackEvent(event);

//        InsiderEvent tagEvent = Insider.Instance.tagEvent(EVENT_BARCODE_SCAN);
//        setDefaultInsiderParams(tagEvent);
//        tagEvent.addParameterWithString("product_id", product.getProductId());
//        tagEvent.addParameterWithString("product_name", product.getDescriptions().get(0).getProductName());
//        tagEvent.addParameterWithString("product_price", "" + product.getOriginalPrice());
//        tagEvent.build();
    }

    public static void logAppUpdateEvent() {
        TagsBundle attributes = new TagsBundle.Builder()
                .putString("time_stamp", DateTimeUtils.getCurrentStringDateTime())
                .putString("version", getAppVersionNumber())
                .putString("user_id", getUserId())
                .putString("os", VALUE_OS_NAME)
                .build();

        getPushwooshInstance().postEvent(PUSHWOOSH_EVENT_APP_UPDATE, attributes);
    }

    public static void logUserInitProperty() {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_DEVICE, getDeviceId());
            getFireBaseAnalytics().setUserProperty(KEY_DEVICE_ID, getDeviceId());
            getFireBaseAnalytics().setUserProperty(KEY_DEVICE_NAME, getDeviceName());
            getFireBaseAnalytics().setUserProperty(KEY_OS, VALUE_OS_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserGenderProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_GENDER, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserEmailProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_EMAIL, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserPhoneProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_PHONE_NO, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserNotificationProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_NOTIFICATION_STATUS, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserLoyalityProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_LOYALTY_POINTS, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserLoyalityTag(int value) {
        try {
            Pushwoosh.getInstance().setTags(Tags.intTag(PUSHWOOSH_TAG_LOYALTY_POINTS, value), callback -> {
                Log.e("logUserLoyality", callback.isSuccess() + "");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logUserUpdateProfileProperty(String value) {
        try {
            getFireBaseAnalytics().setUserProperty(KEY_UPDATED_PROFILE, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppVersionNumber() {
        try {
            PackageInfo pInfo = getAppContext().getPackageManager().getPackageInfo(getAppContext().getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


}
