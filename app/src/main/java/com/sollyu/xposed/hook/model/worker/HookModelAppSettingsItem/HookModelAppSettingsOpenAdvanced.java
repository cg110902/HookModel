package com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem;

import android.preference.SwitchPreference;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingsOpenAdvanced
{
    public final String SaveFlag      = HookModelAppListWorker.GetAppSettingsString(3);
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingsString(11);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingsString(26);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingsString(25);

    public final SwitchPreference switchPreference;
    public HookModelAppSettingsOpenAdvanced(HookModelAppSettingsWorker.PrefsFragment prefsFragment, String packageName)
    {
        switchPreference = (SwitchPreference) prefsFragment.findPreference(XmlFlag);

        switchPreference.setKey(packageName + SaveFlag);
        switchPreference.setSummary(SummaryString);
        switchPreference.setTitle(TitleString);
        switchPreference.setDependency(packageName);
        switchPreference.setChecked(prefsFragment.getPreferenceManager().getSharedPreferences().getBoolean(switchPreference.getKey(), false));
    }

    public java.lang.String getKey()
    {
        return  switchPreference.getKey();
    }

}
