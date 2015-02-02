package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.preference.Preference;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingActivity;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingShortCut
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingString(2);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingString(7);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingString(5);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public Preference editTextPreference = null;

    public HookModelAppSettingShortCut(final HookModelAppSettingWorker.PrefsFragment prefsFragment)
    {
        editTextPreference = prefsFragment.findPreference(XmlFlag);

        String SummaryStringTemp = prefsFragment.getPreferenceManager().getSharedPreferences().getString(editTextPreference.getKey(), SummaryString);

        editTextPreference.setSummary(SummaryStringTemp.equals(EmptyString) ? SummaryString : SummaryStringTemp );
        editTextPreference.setTitle(TitleString);
        editTextPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference arg0)
            {
                ToolsHelper.CreateShortcut(prefsFragment.getActivity(), prefsFragment.getActivity().getString(R.string.app_name), HookModelAppSettingActivity.class, R.drawable.ic_launcher);
                return false;
            }
        });
    }
}
