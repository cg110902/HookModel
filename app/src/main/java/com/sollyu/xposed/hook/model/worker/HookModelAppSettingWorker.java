package com.sollyu.xposed.hook.model.worker;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingActivity;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;

/**
 * Created by wangsy on 15/1/30.
 */
public class HookModelAppSettingWorker
{
    private static HookModelAppSettingActivity activity = null;
    private static Preference       m_hookModelSettingsCreateShortCutPreference;
    private static Preference       m_hookModelSettingsAboutPreference;
    private static SwitchPreference m_hookModelSettingShowSystemAppSwitchPreference;

    public void onCreate(HookModelAppSettingActivity hookModelAppSettingActivity, Bundle savedInstanceState)
    {
        HookModelAppSettingWorker.activity = hookModelAppSettingActivity;
        ToolsHelper.TranslucentStatus(activity, "#222222");

        activity.getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(HookModelAppListWorker.GetAppSettingString(1));
            addPreferencesFromResource(R.xml.hook_model_app_setting);

            m_hookModelSettingsCreateShortCutPreference     = findPreference(HookModelAppListWorker.GetAppSettingString(2));
            m_hookModelSettingsAboutPreference              = findPreference(HookModelAppListWorker.GetAppSettingString(8));
            m_hookModelSettingShowSystemAppSwitchPreference = (SwitchPreference) findPreference(HookModelAppListWorker.GetAppSettingString(3));

            // reset title
            m_hookModelSettingShowSystemAppSwitchPreference.setTitle(HookModelAppListWorker.GetAppSettingString(4));
            m_hookModelSettingsCreateShortCutPreference.setTitle(HookModelAppListWorker.GetAppSettingString(5));
            m_hookModelSettingsAboutPreference.setTitle(HookModelAppListWorker.GetAppSettingString(9));

            // reset summary
            m_hookModelSettingShowSystemAppSwitchPreference.setSummary(HookModelAppListWorker.GetAppSettingString(6));
            m_hookModelSettingsCreateShortCutPreference.setSummary(HookModelAppListWorker.GetAppSettingString(7));
            m_hookModelSettingsAboutPreference.setSummary(HookModelAppListWorker.GetAppSettingString(10));

            // set click listener
            m_hookModelSettingsCreateShortCutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference arg0)
                {
                    ToolsHelper.CreateShortcut(activity, activity.getString(R.string.app_name), HookModelAppSettingActivity.class, R.drawable.ic_launcher);
                    return false;
                }
            });
        }
    }
}
