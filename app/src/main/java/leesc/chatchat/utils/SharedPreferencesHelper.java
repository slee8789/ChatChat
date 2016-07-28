package leesc.chatchat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static SharedPreferences getSharedPreference(Context context, String args) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        return prefs;
    }

    public static String getValue(Context context, String args, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static int getValue(Context context, String args, String key, int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }

    public static boolean getValue(Context context, String args, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }

    public static boolean setValue(Context context, String args, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(key, value);
        return ed.commit();
    }

    public static boolean setValue(Context context, String args, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt(key, value);
        return ed.commit();
    }

    public static boolean setValue(Context context, String args, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(args, Activity.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean(key, value);
        return ed.commit();
    }
}
