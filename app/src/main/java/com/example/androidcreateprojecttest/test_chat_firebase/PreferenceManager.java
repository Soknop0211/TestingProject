package com.example.androidcreateprojecttest.test_chat_firebase;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static void saveString(String key, String value, Context activity) {
        SharedPreferences aSharedPreferences = activity.getSharedPreferences(Constant.Companion.getKEY_PREFERENCE(), Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences.edit();

        aSharedPreferencesEdit.putString(key, value);
        aSharedPreferencesEdit.apply();
    }

    public static String getString(String key, Context activity) {
        SharedPreferences aSharedPreferences = activity.getSharedPreferences(Constant.Companion.getKEY_PREFERENCE(), Context.MODE_PRIVATE);
        return aSharedPreferences.getString(key, "");
    }

    public static void saveBoolean(String key, boolean value, Context activity) {
        SharedPreferences aSharedPreferences = activity.getSharedPreferences(Constant.Companion.getKEY_PREFERENCE(), Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences.edit();

        aSharedPreferencesEdit.putBoolean(key, value);
        aSharedPreferencesEdit.apply();
    }

    public static boolean getBoolean(String key, Context activity) {
        SharedPreferences aSharedPreferences = activity.getSharedPreferences(Constant.Companion.getKEY_PREFERENCE(), Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean(key, false);
    }

    public static void clear(Context activity) {
        SharedPreferences preferences = activity.getSharedPreferences(Constant.Companion.getKEY_PREFERENCE(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
