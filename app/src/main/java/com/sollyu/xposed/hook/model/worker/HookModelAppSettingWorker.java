package com.sollyu.xposed.hook.model.worker;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingActivity;
import com.sollyu.xposed.hook.model.utils.SystemBarTintManager;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem.HookModelAppSettingShortCut;

/**
 * Created by wangsy on 15/1/30.
 */
public class HookModelAppSettingWorker
{
    private static HookModelAppSettingActivity activity = null;
    private static Preference       m_hookModelSettingsAboutPreference;
    private static SwitchPreference m_hookModelSettingShowSystemAppSwitchPreference;

    public void onCreate(HookModelAppSettingActivity hookModelAppSettingActivity, Bundle savedInstanceState)
    {
        HookModelAppSettingWorker.activity = hookModelAppSettingActivity;
        ToolsHelper.TranslucentStatus(activity, "#1958b7");

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
        tintManager.setStatusBarTintColor(android.graphics.Color.parseColor("#1958b7"));
        tintManager.setNavigationBarTintColor(android.graphics.Color.parseColor("#1958b7"));
        tintManager.setNavigationBarAlpha(0.5f);
        activity.getActionBar().setBackgroundDrawable( new android.graphics.drawable.ColorDrawable(android.graphics.Color.parseColor("#1958b7")) );
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        activity.getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home: activity.finish(); break;
        }

        return true;
    }

    public static class PrefsFragment extends PreferenceFragment
    {
        protected HookModelAppSettingShortCut m_hookModelSettingsCreateShortCutPreference = null;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(HookModelAppListWorker.GetAppSettingString(1));
            addPreferencesFromResource(R.xml.hook_model_app_setting);

            // 注：因开市启桌面图标，所以去掉创建桌面快捷方式
            // m_hookModelSettingsCreateShortCutPreference = new HookModelAppSettingShortCut(this);

            m_hookModelSettingsAboutPreference              = findPreference(HookModelAppListWorker.GetAppSettingString(8));
            m_hookModelSettingShowSystemAppSwitchPreference = (SwitchPreference) findPreference(HookModelAppListWorker.GetAppSettingString(3));

            // reset title
            m_hookModelSettingShowSystemAppSwitchPreference.setTitle(HookModelAppListWorker.GetAppSettingString(4));
            m_hookModelSettingsAboutPreference.setTitle(HookModelAppListWorker.GetAppSettingString(9));

            // reset summary
            m_hookModelSettingShowSystemAppSwitchPreference.setSummary(HookModelAppListWorker.GetAppSettingString(6));
            m_hookModelSettingsAboutPreference.setSummary(HookModelAppListWorker.GetAppSettingString(10));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.findViewById(android.R.id.list).setFitsSystemWindows(true);
            return view;
        }
    }
}
