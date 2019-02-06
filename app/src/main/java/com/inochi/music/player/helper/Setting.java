package com.inochi.music.player.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
    private SharedPreferences settings;
    private Context context;
    private String name;

    public Setting(Context context, String name){
        this.context = context;
        this.name = name;
        settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public Setting(Context context){
        this(context, context.getPackageName());
    }

    public void setSetting(String key, String value){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void deleteSetting(){
        settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public String getSetting(String key, String defVal){
        return settings.getString(key, defVal);
    }

    public String getSetting(String key){
        return settings.getString(key, "");
    }

    public int getIntSetting(String strKey, int defVal){
        String strValue = String.valueOf(defVal);
        String strSetting = getSetting(strKey, strValue);

        return Integer.parseInt(strSetting);
    }

    public int getIntSetting(String strKey){
        String strSetting = getSetting(strKey);
        return Integer.parseInt(strSetting);
    }

    public void setIntSetting(String strKey, int defVal){
        String strValue = String.valueOf(defVal);
        setSetting(strKey, strValue);
    }

    public short getShortSetting(String strKey, short defVal){
        String strValue = String.valueOf(defVal);
        String strSetting = getSetting(strKey, strValue);

        return Short.parseShort(strSetting);
    }

    public void setShortSetting(String strKey, short defVal){
        String strValue = String.valueOf(defVal);
        setSetting(strKey, strValue);
    }
}

