package org.aplas.mylibrary.models;

import android.content.Context;
import android.content.SharedPreferences;

import org.aplas.mylibrary.viewmodels.User;

public class UserData {
    private SharedPreferences pref;

    private final String SHARED_ID = "userdata";
    private final String tag_name = "name";
    private final String tag_country = "country";
    private final String tag_phone = "phone";
    private final String tag_color = "color";

    public UserData(Context c) {
        pref = c.getSharedPreferences(SHARED_ID, Context.MODE_PRIVATE);
    }

    public User getUserData() {
        int color = pref.getInt(tag_color, 255*255*255);
        String name = pref.getString(tag_name, "");
        String country = pref.getString(tag_country, "");
        String phone = pref.getString(tag_phone, "");
        return new User(name,country,phone,color);
    }

    public void saveUserData(String name, String country, String phone, int color) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(tag_name, name).apply();
        editor.putString(tag_country, country).apply();
        editor.putString(tag_phone, phone).apply();
        editor.putInt(tag_color, color).apply();
        editor.commit();
    }

    public void clearUserData() {
        pref.edit().clear().apply();
    }

    public boolean isUserDataExist() {
        return (pref.getAll().size() == 4) && (pref.contains(tag_name));
    }
}
