package com.example.psikologiku;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CurrentUser {
    private SharedPreferences pref;
    public CurrentUser(Context ctx){
        pref = PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public void setUsername(String username){
        pref.edit().putString("username",username).commit();
    }
    public String getUsername()
    {
        String username = pref.getString("text","");
        return username;
    }
}
