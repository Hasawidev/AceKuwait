package com.alhasawi.acekuwait.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHandler {

    public static final int TOKEN_LOGIN = 0;
    public static final int TOKEN_PRE_LOGIN = 1;
    //    TOKEN_LOGIN
    public static final String FIREBASE_TOKEN = "firebase_token";
    public static final String LOGIN_TOKEN = "lg_tkn";
    public static final String LOGIN_STATUS = "login_status";
    public static final String LOGIN_USER_ID = "login_user_id";
    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_GENDER = "gender";
    public static final String LOGIN_PHONENUMBER = "phone_number";
    public static final String LOGIN_NATIONALITY = "nationality";
    public static final String LOGIN_EMAIL = "email";
    public static final String LOGIN_CATEGORY_ID = "category_id";
    public static final String LOGIN_BRANCH_CATEGORY_ID = "branch_category_id";
    public static final String LOGIN_MAIN_CATEGORY_ID = "main_category_id";
    public static final String SELECTED_SIZE = "selected_size";
    public static final String LOGIN_CATEGORY_NAME = "category_name";
    public static final String LOGIN_PASSWORD = "login_password";
    public static final String LOGIN_CONFIRM_PASSWORD = "login_confirm_password";
    public static final String NOTIFICATION_STATUS = "notification_status";

    public static final String LOGIN_ITEM_TO_BE_WISHLISTED = "item_to_be_wishlisted";
    public static final String LOGIN_ITEM_TO_BE_CARTED = "item_to_be_carted";
    public static final String LOGIN_USER_CART_ID = "cart_id";
    public static final String LOGIN_SELECTED_LANGUAGE_ID = "language_id";
    public static final String HAS_LANGUAGE_PAGE_SHOWN = "has_language_page_shown";
    public static final String LOGIN_CUSTOMER = "login_customer";
    public static final String LOGIN_PRODUCT_ID = "product_id";
    public static final String SHOW_REG_PAGE = "show_reg";
    public static final String HAS_UPDATE_DIALOG_SHOWN = "update_shown";
//    public static final String DEFAULT_SHIPPING_MODE="default_shipping_mode";

    private final Context context;
    String selected = "";

    public PreferenceHandler(Context context, int TOKEN_TYPE) {
        this.context = context;
        selected = getStringPreferenceName(TOKEN_TYPE);
    }

    public void saveData(String name, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        boolean b = sharedPreferences.edit().putBoolean(name, value).commit();
    }

    public void saveData(String name, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, value).commit();
    }

    public void saveData(String name, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(name, value).commit();
    }

    public String getData(String name, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, defaultValue);
    }

    public int getData(String name, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(selected, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, defaultValue);
    }

    public boolean getData(String name, boolean defaultValue) {
        return context.getSharedPreferences(selected, Context.MODE_PRIVATE).getBoolean(name, defaultValue);
    }

    public boolean contains(String name) {
        return context.getSharedPreferences(selected, Context.MODE_PRIVATE).contains(name);
    }

    public void deleteData(String name) {
        if (contains(name))
            try {
                context.getSharedPreferences(selected, Context.MODE_PRIVATE).edit().remove(name).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void deleteSession() {
        try {
            context.getSharedPreferences(selected, Context.MODE_PRIVATE).edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStringPreferenceName(int TOKEN) {
        String out = "";
        switch (TOKEN) {
            case TOKEN_LOGIN:
                out = "lg";
                break;
            case TOKEN_PRE_LOGIN:
                out = "pre";
        }
        return out;
    }
}
