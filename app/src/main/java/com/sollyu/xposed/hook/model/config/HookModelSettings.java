package com.sollyu.xposed.hook.model.config;

import android.content.Context;

import com.sollyu.xposed.hook.model.R;

/**
 * Created by wangsy on 15/1/29.
 */
public class HookModelSettings
{
    public static Boolean GetShowSystemPackages(Context context)
    {
        return context.getSharedPreferences("ModelSettings", Context.MODE_PRIVATE).getBoolean( context.getString(R.string.model_preferences_setting_show_system_app_key) , false);
    }
}
