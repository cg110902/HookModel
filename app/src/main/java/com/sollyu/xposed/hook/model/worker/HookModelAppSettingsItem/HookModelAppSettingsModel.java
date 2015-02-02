package com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem;

import android.preference.EditTextPreference;
import android.preference.Preference;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingsModel
{
    public final String SaveFlag      = HookModelAppListWorker.GetAppSettingsString(2);
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingsString(9);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingsString(18);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingsString(23);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public final HookModelAppSettingsWorker.PrefsFragment parent;

    public EditTextPreference editTextPreference = null;

    public HookModelAppSettingsModel(HookModelAppSettingsWorker.PrefsFragment prefsFragment, String packageName)
    {
        parent = prefsFragment;
        editTextPreference = (EditTextPreference) prefsFragment.findPreference(XmlFlag);
        editTextPreference.setKey(packageName + SaveFlag);

        String SummaryStringTemp = parent.getPreferenceManager().getSharedPreferences().getString(editTextPreference.getKey(), SummaryString);

        editTextPreference.setTitle(TitleString);
        editTextPreference.setDialogTitle(TitleString);
        editTextPreference.setSummary( SummaryStringTemp.equals(EmptyString) ? SummaryString : SummaryStringTemp );
        editTextPreference.setText(parent.getPreferenceManager().getSharedPreferences().getString(editTextPreference.getKey(), EmptyString));
        editTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        editTextPreference.setDependency(packageName);
    }

    public void setText(java.lang.String text)
    {
        editTextPreference.setSummary(text);
        editTextPreference.setText(text);
    }

    private Preference.OnPreferenceChangeListener onPreferenceChangeListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object o)
        {
            editTextPreference.setSummary(o.toString().equals(EmptyString) ? SummaryString : o.toString());
            return true;
        }
    };
}
