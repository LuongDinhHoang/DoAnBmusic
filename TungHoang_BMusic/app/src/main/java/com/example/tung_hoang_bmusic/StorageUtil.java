package com.example.tung_hoang_bmusic;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.tung_hoang_bmusic.ui.setting.SettingFragment.THEME_NIGHT;

public class StorageUtil {
    private final String STORAGE = "STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public void storeThemeColor(boolean isNight){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(THEME_NIGHT, isNight);
        editor.apply();
    }

    public boolean loadThemeColor() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getBoolean(THEME_NIGHT, false);//return false if no data found
    }
}
