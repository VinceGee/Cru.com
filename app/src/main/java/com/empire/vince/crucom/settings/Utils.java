package com.empire.vince.crucom.settings;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;


import com.empire.vince.crucom.R;

import java.util.HashMap;

/**
 * Created by VinceGee on 2/6/2016.
 */
public class Utils {

    private static String TAG = "Utils";

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void refreshWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String[] widgets = new String[]{
                "com.luk.timetable2.widget.dark.WidgetProvider",
                "com.luk.timetable2.widget.dark.red.WidgetProvider",
                "com.luk.timetable2.widget.dark.green.WidgetProvider",
                "com.luk.timetable2.widget.dark.blue.WidgetProvider",
                "com.luk.timetable2.widget.light.WidgetProvider",
                "com.luk.timetable2.widget.light.red.WidgetProvider",
                "com.luk.timetable2.widget.light.green.WidgetProvider",
                "com.luk.timetable2.widget.light.blue.WidgetProvider",
        };

        for (String widget : widgets) {
            try {
                Class<?> widgetClass = Class.forName(widget);
                ComponentName componentName = new ComponentName(context, widgetClass);

                /*for (int widgetID : appWidgetManager.getAppWidgetIds(componentName)) {
                    appWidgetManager.notifyAppWidgetViewDataChanged(widgetID, R.id.widget);
                }*/
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public static int getCurrentTheme(Activity activity) {
        HashMap<String, Integer> themes = new HashMap<>();

        themes.put("dark", R.style.AppTheme_Dark);
        themes.put("dark_red", R.style.AppTheme_Dark_Red);
        themes.put("dark_green", R.style.AppTheme_Dark_Green);
        themes.put("dark_blue", R.style.AppTheme_Dark_Blue);

        themes.put("light", R.style.AppTheme_Light);
        themes.put("light_red", R.style.AppTheme_Light_Red);
        themes.put("light_green", R.style.AppTheme_Light_Green);
        themes.put("light_blue", R.style.AppTheme_Light_Blue);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String theme = sharedPref.getString("theme", "dark");
        String themeAccent = sharedPref.getString("themeAccent", "normal");

        if (!themeAccent.equals("normal")) {
            return themes.get(String.format("%s_%s", theme, themeAccent));
        }

        return themes.get(theme);
    }

    public static Integer[] getWidgetColorsForVariant(String variant) {
        HashMap<String, Integer[]> variants = new HashMap<>();

        variants.put("dark", new Integer[]{               R.color.colorPrimary, R.color.colorWidgetBG_Dark, android.R.color.white});
        variants.put("dark_red", new Integer[]{                R.color.colorPrimary_Red, R.color.colorWidgetBG_Dark, android.R.color.white});
        variants.put("dark_green", new Integer[]{                R.color.colorPrimary_Green, R.color.colorWidgetBG_Dark, android.R.color.white});
        variants.put("dark_blue", new Integer[]{                R.color.colorPrimary_Blue, R.color.colorWidgetBG_Dark, android.R.color.white});

        variants.put("light", new Integer[]{                R.color.colorPrimary, R.color.colorWidgetBG_Light, android.R.color.black});
        variants.put("light_red", new Integer[]{                R.color.colorPrimary_Red, R.color.colorWidgetBG_Light, android.R.color.black});
        variants.put("light_green", new Integer[]{                R.color.colorPrimary_Green, R.color.colorWidgetBG_Light, android.R.color.black});
        variants.put("light_blue", new Integer[]{                R.color.colorPrimary_Blue, R.color.colorWidgetBG_Light, android.R.color.black});

        return variants.get(variant);
    }

    public static Integer[] getColorsForVariant(int variant) {
        HashMap<Integer, Integer[]> variants = new HashMap<>();

        variants.put(R.style.AppTheme_Dark, new Integer[]{R.color.colorPrimary,                android.R.color.white});
        variants.put(R.style.AppTheme_Dark_Red, new Integer[]{R.color.colorPrimary_Red,                android.R.color.white});
        variants.put(R.style.AppTheme_Dark_Green, new Integer[]{R.color.colorPrimary_Green,                android.R.color.white});
        variants.put(R.style.AppTheme_Dark_Blue, new Integer[]{R.color.colorPrimary_Blue,                android.R.color.white});

        variants.put(R.style.AppTheme_Light, new Integer[]{R.color.colorPrimary,                android.R.color.black});
        variants.put(R.style.AppTheme_Light_Red, new Integer[]{R.color.colorPrimary_Red,                android.R.color.black});
        variants.put(R.style.AppTheme_Light_Green, new Integer[]{R.color.colorPrimary_Green,                android.R.color.black});
        variants.put(R.style.AppTheme_Light_Blue, new Integer[]{R.color.colorPrimary_Blue,                android.R.color.black});

        return variants.get(variant);
    }
}
