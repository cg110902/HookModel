package com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem;

import android.preference.Preference;
import android.view.View;

import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.EditTextDialogPreference;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingsAdvancedImei
{
    public final String SaveFlag      = HookModelAppListWorker.GetAppSettingsString(4);
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingsString(8);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingsString(17);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingsString(34);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);
    public final String ButtonString  = HookModelAppListWorker.GetAppSettingsString(40);

    public final HookModelAppSettingsWorker.PrefsFragment parent;

    public EditTextDialogPreference editTextPreference = null;

    public HookModelAppSettingsAdvancedImei(HookModelAppSettingsWorker.PrefsFragment prefsFragment, String packageName, String parentKey)
    {
        parent = prefsFragment;
        editTextPreference = (EditTextDialogPreference) prefsFragment.findPreference(XmlFlag);
        editTextPreference.setKey(packageName + SaveFlag);

        String SummaryStringTemp = parent.getPreferenceManager().getSharedPreferences().getString(editTextPreference.getKey(), SummaryString);

        editTextPreference.setSummary(SummaryStringTemp.equals(EmptyString) ? SummaryString : SummaryStringTemp );
        editTextPreference.setTitle(TitleString);
        editTextPreference.setDialogTitle(TitleString);
        editTextPreference.setText(parent.getPreferenceManager().getSharedPreferences().getString(editTextPreference.getKey(), EmptyString));
        editTextPreference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        editTextPreference.setDependency(parentKey);
        editTextPreference.setButtonText(ButtonString);
        editTextPreference.setButtonOnClickListener(buttonOnClickListener);
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

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            editTextPreference.setText(ToolsHelper.RandomString(0, 9, 15));
        }
    };
}
