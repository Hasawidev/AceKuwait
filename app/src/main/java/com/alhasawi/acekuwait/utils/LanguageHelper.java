package com.alhasawi.acekuwait.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public static void changeLocale(Resources res, String locale) {

        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch (locale) {
            case "ar":
                Locale arLocale = new Locale("ar");
//                Locale.setDefault(arLocale);
                config.locale = arLocale;
                config.setLayoutDirection(arLocale);
                break;
            case "en":
                Locale enLocale = Locale.ENGLISH;
//                Locale.setDefault(enLocale);
                config.locale = enLocale;
                config.setLayoutDirection(enLocale);
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());

        /*Context context = AceHardware.appContext;
        if (!locale.equals("en") && !locale.equals("ar")) {
            locale = "en";
        }
        Locale localeToSwitchTo =  new Locale(locale);
        Configuration configuration  = res.getConfiguration();
        Locale.setDefault(localeToSwitchTo);
        configuration.setLocale(localeToSwitchTo);
        configuration.setLayoutDirection(localeToSwitchTo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(configuration);
        }
        res.updateConfiguration(configuration, res.getDisplayMetrics());*/
    }

    public static Locale getLocale(Resources res) {
        Configuration config;
        config = new Configuration(res.getConfiguration());
        return config.locale;
    }
}