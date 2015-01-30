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

        HookModelAppSettingsWorker.packageName = activity.getIntent().getStringExtra("packageName").toString();
        HookModelAppSettingsWorker.appName     = activity.getIntent().getStringExtra("appName").toString();

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

            String hookModelManufacutrerKey                = HookModelAppSettingsWorker.packageName + ".hookModelManufacutrer";
            String hookModelModelKey                       = HookModelAppSettingsWorker.packageName + ".hookModelModel";
            String hookModelAdvancedSwitchKey              = HookModelAppSettingsWorker.packageName + ".hookModelAdvanced";
            String hookModelAdvancedImeiKey                = HookModelAppSettingsWorker.packageName + ".hookModelAdvancedImei";
            String hookModelAdvancedSimSerialNumberKey     = HookModelAppSettingsWorker.packageName + ".hookModelAdvancedSimSerialNumber";

            String hookModelManufacutrerSummary            = getPreferenceManager().getSharedPreferences().getString(hookModelManufacutrerKey, getString(R.string.hookModelAppSettingsManufacturerSummary));
            String hookModelModelSummary                   = getPreferenceManager().getSharedPreferences().getString(hookModelModelKey, getString(R.string.hookModelAppSettingsModelSummary));
            String hookModelAdvancedImeiSummary            = getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedImeiKey, getString(R.string.hookModelAppSettingsAdvancedImeiSummary));
            String hookModelAdvancedSimSerialNumberSummary = getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedSimSerialNumberKey, getString(R.string.hookModelAppSettingsAdvancedSimSerialNumber));



            // get items
            m_hookModelOpenHookSwitchPreference                  = (SwitchPreference) findPreference("hookModelOpenHookSwitch");
            m_hookModelAdvancedSwitchPreference                  = (SwitchPreference) findPreference("hookModelAdvancedSwitch");
            m_hookModelManufacutrerEditTextPreference            = (EditTextPreference) findPreference("hookModelManufacutrer");
            m_hookModelAdvancedImeiEditTextPreference            = (EditTextPreference) findPreference("hookModelAdvancedImei");
            m_hookModelModelEditTextPreference                   = (EditTextPreference) findPreference("hookModelModel");
            m_hookModelAdvancedSimSerialNumberEditTextPreference = (EditTextPreference) findPreference("hookModelAdvancedSimSerialNumber");
            m_hookModelAppInfoPreference                         = findPreference("hookModelAppInfo");
            m_hookModelSelectModelsPreference                    = findPreference("hookModelSelectModels");

            // reset key
            m_hookModelOpenHookSwitchPreference.setKey(HookModelAppSettingsWorker.packageName);
            m_hookModelManufacutrerEditTextPreference.setKey(hookModelManufacutrerKey);
            m_hookModelModelEditTextPreference.setKey(hookModelModelKey);
            m_hookModelAdvancedSwitchPreference.setKey(hookModelAdvancedSwitchKey);
            m_hookModelAdvancedImeiEditTextPreference.setKey(hookModelAdvancedImeiKey);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setKey(hookModelAdvancedSimSerialNumberKey);

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
            m_hookModelManufacutrerEditTextPreference.setSummary(hookModelManufacutrerSummary == "" ? getString(R.string.hookModelAppSettingsManufacturerSummary) : hookModelManufacutrerSummary);
            m_hookModelAdvancedImeiEditTextPreference.setSummary(hookModelAdvancedImeiSummary == "" ? getString(R.string.hookModelAppSettingsAdvancedImeiSummary) : hookModelAdvancedImeiSummary);
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setSummary(hookModelAdvancedSimSerialNumberSummary == "" ? getString(R.string.hookModelAppSettingsAdvancedSimSerialNumber) : hookModelAdvancedSimSerialNumberSummary);
            m_hookModelModelEditTextPreference.setSummary(hookModelModelSummary == "" ? getString(R.string.hookModelAppSettingsModelSummary) : hookModelModelSummary);
            m_hookModelAppInfoPreference.setSummary(HookModelAppSettingsWorker.packageName);

            // reset value
            m_hookModelOpenHookSwitchPreference.setChecked(getPreferenceManager().getSharedPreferences().getBoolean(HookModelAppSettingsWorker.packageName, false));
            m_hookModelAdvancedSwitchPreference.setChecked(getPreferenceManager().getSharedPreferences().getBoolean(hookModelAdvancedSwitchKey, false));
            m_hookModelManufacutrerEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelManufacutrerKey, ""));
            m_hookModelAdvancedImeiEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedImeiKey, ""));
            m_hookModelAdvancedSimSerialNumberEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelAdvancedSimSerialNumberKey, ""));
            m_hookModelModelEditTextPreference.setText(getPreferenceManager().getSharedPreferences().getString(hookModelModelKey, ""));

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
                String summaryString = "";

                if (arg0 == m_hookModelManufacutrerEditTextPreference)
                {
                    summaryString = arg1.equals("") ? activity.getString(R.string.hookModelAppSettingsManufacturerSummary) : arg1.toString();
                }
                else if (arg0 == m_hookModelModelEditTextPreference)
                {
                    summaryString = arg1.equals("") ? activity.getString(R.string.hookModelAppSettingsModelSummary) : arg1.toString();
                }
                else if (arg0 == m_hookModelAdvancedImeiEditTextPreference)
                {
                    summaryString = arg1.equals("") ? activity.getString(R.string.hookModelAppSettingsAdvancedImeiSummary) : arg1.toString();
                }
                else if (arg0 == m_hookModelAdvancedSimSerialNumberEditTextPreference)
                {
                    summaryString = arg1.equals("") ? activity.getString(R.string.hookModelAppSettingsAdvancedSimSerialNumber) : arg1.toString();
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
                    android.widget.Toast.makeText(activity, activity.getString(R.string.hookModelAppSettingsStartAppToast), android.widget.Toast.LENGTH_SHORT).show();
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
                    final JSONObject result = new JSONObject(activity.getString(R.string.model_app_settings_select_model_list));
                    final String[] arrayFruit = new String[result.length()];

                    int i = 0;
                    Iterator<?> it = result.keys();
                    while (it.hasNext())
                    {
                        arrayFruit[i++] = it.next().toString();
                    }

                    // show manufacturer alert dialog
                    android.app.AlertDialog.Builder manufacturerBuilder = new android.app.AlertDialog.Builder(activity);
                    manufacturerBuilder.setTitle(R.string.hookModelAppSettingsSelectModelTitle);
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
                                while (it.hasNext())
                                {
                                    modelStrings[i++] = it.next().toString();
                                }

                                // show model alert dialog
                                android.app.AlertDialog.Builder modelBuilder = new android.app.AlertDialog.Builder(activity);
                                modelBuilder.setTitle(R.string.hookModelAppSettingsSelectModelModel);
                                modelBuilder.setItems(modelStrings, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1)
                                    {
                                        try
                                        {
                                            JSONObject modeInfoJsonObject = manufacturerJsonObject.getJSONObject(modelStrings[arg1]);

                                            m_hookModelManufacutrerEditTextPreference.setText(modeInfoJsonObject.getString("manufacturer"));
                                            m_hookModelManufacutrerEditTextPreference.setSummary(modeInfoJsonObject.getString("manufacturer"));
                                            m_hookModelModelEditTextPreference.setText(modeInfoJsonObject.getString("model"));
                                            m_hookModelModelEditTextPreference.setSummary(modeInfoJsonObject.getString("model"));
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                modelBuilder.create().show();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
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
