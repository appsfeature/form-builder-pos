package com.formbuilder.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


public class FBPreferences {

    public static final String IS_FORM_SUBMITTED = "fb_is_form_submitted";
    public static final String APP_VERSION_CODE = "fb_app_version_code";
    public static final String ADVERTISE_VERSION_CODE = "fb_adv_version_code";
    private static final String RECENT_FEATURE_DATA = "recent_feature_data";

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferenceObj(Context context){
        if (context != null && sharedPreferences == null )
            sharedPreferences = context.getSharedPreferences(context.getPackageName() , Context.MODE_PRIVATE);

        return sharedPreferences ;
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }
    public static String getString(Context context, String key, String defaultValue) {
        if (getSharedPreferenceObj(context) != null) {
            return decrypt(getSharedPreferenceObj(context).getString(encrypt(key), defaultValue));
        } else {
            return decrypt(defaultValue);
        }
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }
    public static int getInt(Context context, String key, int defaultValue) {
        if (getSharedPreferenceObj(context) != null) {
            return getSharedPreferenceObj(context).getInt(encrypt(key), defaultValue);
        } else {
            return (defaultValue);
        }
    }

    public static float getFloat(Context context, String key) {
        return getSharedPreferenceObj(context).getFloat(encrypt(key), 0);

    }
    public static long getLong(Context context, String key) {
        return getSharedPreferenceObj(context).getLong(encrypt(key), 0);
    }
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        if (getSharedPreferenceObj(context) != null) {
            return getSharedPreferenceObj(context).getBoolean(key, defaultValue);
        } else {
            return defaultValue;
        }
    }
    public static void setString(Context context, String key, String values) {
        if (getSharedPreferenceObj(context) != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
            if (editor != null) {
                editor.putString(encrypt(key), encrypt(values));
                editor.apply();
            }
        }
    }
    public static void setInt(Context context, String key, int value) {
        if (getSharedPreferenceObj(context) != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
            if (editor != null) {
                editor.putInt(encrypt(key), value);
                editor.apply();
            }
        }
    }
    public static void setFloat(Context context, String key, float value) {
        final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
        editor.putFloat(encrypt(key), value);
        editor.apply();
    }
    public static void setLong(Context context, String key, long value) {
        final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
        editor.putLong(encrypt(key), value);
        editor.apply();
    }
    public static void setBoolean(Context context, String key, boolean value) {
        if (getSharedPreferenceObj(context) != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
            if (editor != null) {
                editor.putBoolean(encrypt(key), value);
                editor.apply();
            }
        }
    }

    /**
     * Clear all preferences.
     */
    public static void clearPreferences(Context context) {
        final SharedPreferences.Editor editor = getSharedPreferenceObj(context).edit();
        editor.clear();
        editor.apply();
    }

    private static String encrypt(String input) {
        return input;
    }

    private static String decrypt(String input) {
        return input;
    }

    public static void setFormSubmitted(Context context, int formId, boolean value) {
        if(formId > 0) {
            setBoolean(context, IS_FORM_SUBMITTED + formId, value);
        }
    }

    public static boolean isFormSubmitted(Context context, int formId) {
        return getBoolean(context, IS_FORM_SUBMITTED + formId, false);
    }

    public static void setAppVersionCode(Context context, int value) {
        setInt(context, APP_VERSION_CODE, value);
    }

    public static int getAppVersionCode(Context context) {
        return getInt(context, APP_VERSION_CODE, 0);
    }

    public static void setAdvertiseVersionCode(Context context, int value) {
        setInt(context, ADVERTISE_VERSION_CODE, value);
    }

    public static int getAdvertiseVersionCode(Context context) {
        return getInt(context, ADVERTISE_VERSION_CODE, 0);
    }

    public static String getRecentFeatureData(Context context, String key) {
        return getString(context, RECENT_FEATURE_DATA + key, "");
    }

    public static void setRecentFeatureData(Context context, String key, String value) {
        setString(context, RECENT_FEATURE_DATA + key, value);
    }
}