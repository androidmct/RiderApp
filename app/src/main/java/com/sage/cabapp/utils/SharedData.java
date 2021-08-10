package com.sage.cabapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Maroof Ahmed Siddique on 21-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
public class SharedData {

    private static final String PREFS_NAME = "SAGE_RIDER";

    public SharedData() {
        super();
    }


    // For Strings

    public static void saveString(Context context, String key, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(key, text);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences settings;
        String text;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, null);
        return text;
    }


    // For Boolean

    public static void saveBool(Context context, String key, boolean value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBool(Context context, String key) {
        SharedPreferences settings;
        boolean text;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getBoolean(key, false);
        return text;
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.apply();
    }

    public static void removeString(Context context, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public static int getInt(Context context, String key) {
        SharedPreferences settings;
        int text;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getInt(key, 0);
        return text;
    }


    public static void saveDouble(Context context, String key, double value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }


    public static double getDouble(Context context, String key) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(settings.getLong(key, 0));
    }

    public static void saveLong(Context context, String key, long value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }


    public static long getLong(Context context, String key) {
        SharedPreferences settings;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, 0);
    }

}
