package com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem;

import android.preference.Preference;
import android.view.View;

import com.sollyu.xposed.hook.model.worker.EditTextDialogPreference;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

import java.util.Random;

/**
 * Created by wangsy on 15/2/2.
 */
public class HookModelAppSettingsMacAddress
{
    public final String SaveFlag      = HookModelAppListWorker.GetAppSettingsString(29);
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingsString(30);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingsString(32);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingsString(31);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);
    public final String ButtonString  = HookModelAppListWorker.GetAppSettingsString(40);

    public final HookModelAppSettingsWorker.PrefsFragment parent;

    public EditTextDialogPreference editTextPreference = null;

    public HookModelAppSettingsMacAddress(HookModelAppSettingsWorker.PrefsFragment prefsFragment, String packageName, String parentKey)
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
            Random rand = new Random();
            byte[] macAddress = new byte[6];
            rand.nextBytes(macAddress);

            StringBuilder sb = new StringBuilder(18);
            for(byte b : macAddress){
                if(sb.length() > 0){
                    sb.append(":");
                }else{ //first byte, we need to set some options
                    b = (byte)(b | (byte)(0x01 << 6)); //locally adminstrated
                    b = (byte)(b | (byte)(0x00 << 7)); //unicast

                }
                sb.append(String.format("%02x", b));
            }

            editTextPreference.setText(sb.toString());
        }
    };
}
