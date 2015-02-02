package com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem;

import android.preference.SwitchPreference;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingsOpenHook
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingsString(6);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingsString(33);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingsString(23);

    public final SwitchPreference switchPreference;
    public HookModelAppSettingsOpenHook(HookModelAppSettingsWorker.PrefsFragment prefsFragment, String packageName)
    {
        switchPreference = (SwitchPreference) prefsFragment.findPreference(XmlFlag);

        switchPreference.setKey(packageName);
        switchPreference.setSummary(SummaryString);
        switchPreference.setTitle(TitleString);
        switchPreference.setChecked(prefsFragment.getPreferenceManager().getSharedPreferences().getBoolean(switchPreference.getKey(), false));
    }
}
