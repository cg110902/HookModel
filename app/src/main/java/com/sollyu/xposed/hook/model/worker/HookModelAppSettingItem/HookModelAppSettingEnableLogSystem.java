package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.preference.SwitchPreference;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

/**
 * Created by wangsy on 15/2/11.
 */
public class HookModelAppSettingEnableLogSystem
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingString(11);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingString(12);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingString(13);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public SwitchPreference editTextPreference = null;

    public HookModelAppSettingEnableLogSystem(final HookModelAppSettingWorker.PrefsFragment prefsFragment)
    {
        editTextPreference = (SwitchPreference) prefsFragment.findPreference(XmlFlag);

        editTextPreference.setSummary(SummaryString );
        editTextPreference.setTitle(TitleString);
    }
}
