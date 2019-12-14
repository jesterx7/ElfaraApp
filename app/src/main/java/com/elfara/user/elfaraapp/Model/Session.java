package com.elfara.user.elfaraapp.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Session {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFERENCES_NAME = "elfaraApp";

    public Session(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setSession(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getSession(String key) {
        String value = preferences.getString(key, "");
        return value;
    }

    public void clearSession() {
        preferences.edit().clear().commit();
    }
}
