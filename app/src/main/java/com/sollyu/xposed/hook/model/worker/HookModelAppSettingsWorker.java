package com.sollyu.xposed.hook.model.worker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingsActivity;
import com.sollyu.xposed.hook.model.utils.RootUtil;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by wangsy on 15/1/29.
 */
public class HookModelAppSettingsWorker
{
    protected static HookModelAppSettingsActivity activity = null;
    protected static String packageName = null;
    protected static String appName = null;

    protected static Preference m_hookModelAppInfoPreference;
    protected static Preference m_hookModelSelectModelsPreference;
    protected static SwitchPreference m_hookModelOpenHookSwitchPreference;
    protected static SwitchPreference m_hookModelAdvancedSwitchPreference;
    protected static EditTextPreference m_hookModelManufacutrerEditTextPreference;
    protected static EditTextPreference m_hookModelModelEditTextPreference;
    protected static EditTextPreference m_hookModelAdvancedImeiEditTextPreference;
    protected static EditTextPreference m_hookModelAdvancedSimSerialNumberEditTextPreference;

    public void onCreate(HookModelAppSettingsActivity hookModelAppSettingsActivity, Bundle savedInstanceState)
    {
        this.activity = hookModelAppSettingsActivity;

        HookModelAppSettingsWorker.packageName = activity.getIntent().getStringExtra(HookModelAppListWorker.GetAppListString(3)).toString();
        HookModelAppSettingsWorker.appName     = activity.getIntent().getStringExtra(HookModelAppListWorker.GetAppListString(2)).toString();

        activity.setTitle(appName);
        activity.getActionBar().setLogo(HookModelAppListWorker.selectIconDrawable);

        activity.getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment
    {
        private int mBackKeyPressedTimes = 0;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            this.getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            addPreferencesFromResource(R.xml.hook_model_app_settings);
    
            String hookModelManufacutrerKey                = HookModelAppSettingsWorker.packageName + HookModelAppListWorker.GetAppSettingsString(1);
            String hookModelModelKey                       = HookModelAppSettingsWorker.packageName + HookModelAppListWorker.GetAppSettingsString(2);
            String hookModelAdvancedSwitchKey              = HookModelAppSettingsWorker.packageName + HookModelAppListWorker.GetAppSettingsString(3);
            String hookModelAdvancedImeiKey                = HookModelAppSettingsWorker.packageName + HookModelAppListWorker.GetAppSettingsString(4);
            String hookModelAdvancedSimSerialNumberKey     = HookModelAppSettingsWorker.packageName + HookModelAppListWorker.GetAppSettingsString(5);

            String hookModelManufacutrerSummary            = getPreferenceManager().getSharedPreferences().getString(hookModelManufacutrerKey, HookModelAppListWorker.GetAppSettingsString(16));
            String hookModelModelSummary                   = getPreferenceManager().getSharedPreferences().getString(hookModelModelKey, HookModelAppListWorker.GetAppSettingsString(18));
            String hookModelAdvancedImeiSummary            = getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedImeiKey, HookModelAppListWorker.GetAppSettingsString(17));
            String hookModelAdvancedSimSerialNumberSummary = getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedSimSerialNumberKey, HookModelAppListWorker.GetAppSettingsString(19));

            // get items
            m_hookModelOpenHookSwitchPreference                  = (SwitchPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(6));
            m_hookModelAdvancedSwitchPreference                  = (SwitchPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(11));
            m_hookModelManufacutrerEditTextPreference            = (EditTextPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(7));
            m_hookModelAdvancedImeiEditTextPreference            = (EditTextPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(8));
            m_hookModelModelEditTextPreference                   = (EditTextPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(9));
            m_hookModelAdvancedSimSerialNumberEditTextPreference = (EditTextPreference) findPreference(HookModelAppListWorker.GetAppSettingsString(10));
            m_hookModelAppInfoPreference                         = findPreference(HookModelAppListWorker.GetAppSettingsString(12));
            m_hookModelSelectModelsPreference                    = findPreference(HookModelAppListWorker.GetAppSettingsString(13));

            // reset key
            m_hookModelOpenHookSwitchPreference.setKey(HookModelAppSettingsWorker.packageName);
            m_hookModelManufacutrerEditTextPreference.setKey(hookModelManufacutrerKey);
            m_hookModelModelEditTextPreference.setKey(hookModelModelKey);
            m_hookModelAdvancedSwitchPreference.setKey(hookModelAdvancedSwitchKey);
            m_hookModelAdvancedImeiEditTextPreference.setKey(hookModelAdvancedImeiKey);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setKey(hookModelAdvancedSimSerialNumberKey);

            // reset title
            m_hookModelManufacutrerEditTextPreference.setTitle(HookModelAppListWorker.GetAppSettingsString(22));
            m_hookModelModelEditTextPreference.setTitle(HookModelAppListWorker.GetAppSettingsString(23));
            m_hookModelAdvancedSwitchPreference.setTitle(HookModelAppListWorker.GetAppSettingsString(25));

            // reset parent
            m_hookModelManufacutrerEditTextPreference.setDependency(HookModelAppSettingsWorker.packageName);
            m_hookModelModelEditTextPreference.setDependency(HookModelAppSettingsWorker.packageName);
            m_hookModelSelectModelsPreference.setDependency(HookModelAppSettingsWorker.packageName);
            m_hookModelAdvancedSwitchPreference.setDependency(HookModelAppSettingsWorker.packageName);
            m_hookModelAdvancedImeiEditTextPreference.setDependency(hookModelAdvancedSwitchKey);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setDependency(hookModelAdvancedSwitchKey);

            // reset title & icon
            m_hookModelAppInfoPreference.setTitle(HookModelAppSettingsWorker.appName);
            m_hookModelAppInfoPreference.setIcon(HookModelAppListWorker.selectIconDrawable);

            // reset summary
            m_hookModelManufacutrerEditTextPreference.setSummary(hookModelManufacutrerSummary.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(16) : hookModelManufacutrerSummary);
            m_hookModelAdvancedImeiEditTextPreference.setSummary(hookModelAdvancedImeiSummary.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(17) : hookModelAdvancedImeiSummary);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setSummary(hookModelAdvancedSimSerialNumberSummary.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(19) : hookModelAdvancedSimSerialNumberSummary);
            m_hookModelModelEditTextPreference.setSummary(hookModelModelSummary.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(18) : hookModelModelSummary);
            m_hookModelAppInfoPreference.setSummary(HookModelAppSettingsWorker.packageName);
            m_hookModelAdvancedSwitchPreference.setSummary(HookModelAppListWorker.GetAppSettingsString(26));
            m_hookModelSelectModelsPreference.setSummary(HookModelAppListWorker.GetAppSettingsString(27));

            // reset value
            m_hookModelOpenHookSwitchPreference.setChecked(getPreferenceManager().getSharedPreferences().getBoolean(HookModelAppSettingsWorker.packageName, false));
            m_hookModelAdvancedSwitchPreference.setChecked(getPreferenceManager().getSharedPreferences().getBoolean(hookModelAdvancedSwitchKey, false));
            m_hookModelManufacutrerEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelManufacutrerKey, HookModelAppListWorker.GetAppListString(5)));
            m_hookModelAdvancedImeiEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedImeiKey, HookModelAppListWorker.GetAppListString(5)));
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedSimSerialNumberKey, HookModelAppListWorker.GetAppListString(5)));
            m_hookModelModelEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelModelKey, HookModelAppListWorker.GetAppListString(5)));

            // on change listener
            m_hookModelManufacutrerEditTextPreference.setOnPreferenceChangeListener(editPreferenceChangeListener);
            m_hookModelAdvancedImeiEditTextPreference.setOnPreferenceChangeListener(editPreferenceChangeListener);
            m_hookModelModelEditTextPreference.setOnPreferenceChangeListener(editPreferenceChangeListener);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setOnPreferenceChangeListener(editPreferenceChangeListener);

            // on click listener
            m_hookModelAppInfoPreference.setOnPreferenceClickListener(appInfoPreferenceClickListener);
            m_hookModelSelectModelsPreference.setOnPreferenceClickListener(selectModelPreferenceClickListener);
        }

        private Preference.OnPreferenceChangeListener editPreferenceChangeListener = new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference arg0, Object arg1)
            {
                String summaryString = HookModelAppListWorker.GetAppListString(5);

                if (arg0 == m_hookModelManufacutrerEditTextPreference)
                {
                    summaryString = arg1.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(16) : arg1.toString();
                }
                else if (arg0 == m_hookModelModelEditTextPreference)
                {
                    summaryString = arg1.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(18) : arg1.toString();
                }
                else if (arg0 == m_hookModelAdvancedImeiEditTextPreference)
                {
                    summaryString = arg1.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(17) : arg1.toString();
                }
                else if (arg0 == m_hookModelAdvancedSimSerialNumberEditTextPreference)
                {
                    summaryString = arg1.equals(HookModelAppListWorker.GetAppListString(5)) ? HookModelAppListWorker.GetAppSettingsString(19) : arg1.toString();
                }

                arg0.setSummary(summaryString);

                return true;
            }
        };

        private Preference.OnPreferenceClickListener appInfoPreferenceClickListener = new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference arg0)
            {
                if (mBackKeyPressedTimes == 0)
                {
                    android.widget.Toast.makeText(activity, HookModelAppListWorker.GetAppSettingsString(24), android.widget.Toast.LENGTH_SHORT).show();
                    mBackKeyPressedTimes = 1;
                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            finally
                            {
                                mBackKeyPressedTimes = 0;
                            }
                        }
                    }.start();
                }
                else
                {
                    RootUtil.killProcess(packageName);
                    ToolsHelper.OpenApp(activity, packageName);
                }

                return false;
            }
        };

        private Preference.OnPreferenceClickListener selectModelPreferenceClickListener = new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference arg0)
            {
                try
                {
                    final JSONObject result = new JSONObject(HookModelAppListWorker.GetAppSettingsString(28));
                    final String[] arrayFruit = new String[result.length()];

                    int i = 0;
                    Iterator<?> it = result.keys();
                    while (it.hasNext()) { arrayFruit[i++] = it.next().toString(); }

                    // show manufacturer alert dialog
                    android.app.AlertDialog.Builder manufacturerBuilder = new android.app.AlertDialog.Builder(activity);
                    manufacturerBuilder.setTitle(HookModelAppListWorker.GetAppSettingsString(20));
                    manufacturerBuilder.setItems(arrayFruit, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            try
                            {
                                final JSONObject manufacturerJsonObject = result.getJSONObject(arrayFruit[arg1]);
                                final String[] modelStrings = new String[manufacturerJsonObject.length()];

                                // get model list
                                int i = 0;
                                Iterator<?> it = manufacturerJsonObject.keys();
                                while (it.hasNext()) { modelStrings[i++] = it.next().toString(); }

                                // show model alert dialog
                                android.app.AlertDialog.Builder modelBuilder = new android.app.AlertDialog.Builder(activity);
                                modelBuilder.setTitle(HookModelAppListWorker.GetAppSettingsString(21));
                                modelBuilder.setItems(modelStrings, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1)
                                    {
                                        try
                                        {
                                            JSONObject modeInfoJsonObject = manufacturerJsonObject.getJSONObject(modelStrings[arg1]);

                                            m_hookModelManufacutrerEditTextPreference.setText(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(14)));
                                            m_hookModelManufacutrerEditTextPreference.setSummary(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(14)));
                                            m_hookModelModelEditTextPreference.setText(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(15)));
                                            m_hookModelModelEditTextPreference.setSummary(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(15)));
                                        }
                                        catch (JSONException e) { e.printStackTrace(); }
                                    }
                                });
                                modelBuilder.create().show();
                            }
                            catch (JSONException e) { e.printStackTrace(); }
                        }
                    });
                    manufacturerBuilder.create().show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        };

    }
}
