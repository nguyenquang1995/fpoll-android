package com.framgia.fpoll.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.framgia.fpoll.data.model.authorization.User;
import com.google.gson.Gson;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.framgia.fpoll.util.Constant.PreferenceConstant.PREF_IS_LOGIN;
import static com.framgia.fpoll.util.Constant.PreferenceConstant.PREF_LANGUAGE;
import static com.framgia.fpoll.util.Constant.PreferenceConstant.PREF_TOKEN;
import static com.framgia.fpoll.util.Constant.PreferenceConstant.PREF_USER;

/**
 * Created by Nhahv0902 on 3/16/2017.
 * <></>
 */
public class SharePreferenceUtil {
    private static final String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    private static SharePreferenceUtil sIntances;
    private SharedPreferences mPreferences;

    private SharePreferenceUtil(Context context) {
        mPreferences = context.getSharedPreferences(SHARE_PREFERENCE, MODE_PRIVATE);
    }

    public static SharePreferenceUtil getIntances(Context context) {
        if (sIntances == null) sIntances = new SharePreferenceUtil(context);
        return sIntances;
    }

    public void writeUser(User user) {
        mPreferences.edit().putString(PREF_USER, new Gson().toJson(user)).apply();
    }

    public User getUser() {
        User user = new Gson().fromJson(mPreferences.getString(PREF_USER, null), User.class);
        return user != null ? user : new User();
    }

    public void writeToken(String token) {
        mPreferences.edit().putString(PREF_TOKEN, token).apply();
    }

    public String getToken() {
        return mPreferences.getString(PREF_TOKEN, null);
    }

    public void writeLogin(boolean isLogin) {
        mPreferences.edit().putBoolean(PREF_IS_LOGIN, isLogin).apply();
    }

    public boolean isLogin() {
        return mPreferences.getBoolean(PREF_IS_LOGIN, false);
    }

    public void saveLanguage(String lang) {
        mPreferences.edit().putString(PREF_LANGUAGE, lang).apply();
    }

    public String loadLanguage() {
        return mPreferences.getString(PREF_LANGUAGE, Locale.getDefault().getDisplayLanguage());
    }

    public void writePreference(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public void writePreference(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public String getString(String key) {
        return mPreferences.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public void clearKey(String key) {
        mPreferences.edit().remove(key).apply();
    }
}
