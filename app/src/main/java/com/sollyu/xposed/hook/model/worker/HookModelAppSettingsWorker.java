package com.sollyu.xposed.hook.model.worker;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingsActivity;
import com.sollyu.xposed.hook.model.utils.RootUtil;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsAdvancedImei;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsAdvancedSimSerialNumber;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsMacAddress;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsManufacturer;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsModel;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsOpenAdvanced;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsItem.HookModelAppSettingsOpenHook;

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

    public void onCreate(HookModelAppSettingsActivity hookModelAppSettingsActivity, Bundle savedInstanceState)
    {
        this.activity = hookModelAppSettingsActivity;

        ToolsHelper.TranslucentStatus(activity, "#222222");

        HookModelAppSettingsWorker.packageName = activity.getIntent().getStringExtra(HookModelAppListWorker.GetAppListString(3)).toString();
        HookModelAppSettingsWorker.appName     = activity.getIntent().getStringExtra(HookModelAppListWorker.GetAppListString(2)).toString();

        activity.setTitle(appName);
        activity.getActionBar().setLogo(HookModelAppListWorker.selectIconDrawable);

        activity.getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment
    {
        private int mBackKeyPressedTimes = 0;
        protected HookModelAppSettingsOpenHook                m_hookModelOpenHookSwitchPreference                  = null;
        protected HookModelAppSettingsOpenAdvanced            m_hookModelOpenAdvancedSwitchPreference              = null;
        protected HookModelAppSettingsManufacturer            m_hookModelManufacturerEditTextPreference            = null;
        protected HookModelAppSettingsModel                   m_hookModelModelEditTextPreference                   = null;
        protected HookModelAppSettingsAdvancedImei            m_hookModelAdvancedImeiEditTextPreference            = null;
        protected HookModelAppSettingsAdvancedSimSerialNumber m_hookModelAdvancedSimSerialNumberEditTextPreference = null;
        protected HookModelAppSettingsMacAddress              m_hookModelAdvancedMacAddressEditTextPreference      = null;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            this.getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            addPreferencesFromResource(R.xml.hook_model_app_settings);

            m_hookModelOpenHookSwitchPreference                  = new HookModelAppSettingsOpenHook(this, HookModelAppSettingsWorker.packageName);
            m_hookModelOpenAdvancedSwitchPreference              = new HookModelAppSettingsOpenAdvanced(this, HookModelAppSettingsWorker.packageName);
            m_hookModelManufacturerEditTextPreference            = new HookModelAppSettingsManufacturer(this, HookModelAppSettingsWorker.packageName);
            m_hookModelModelEditTextPreference                   = new HookModelAppSettingsModel(this, HookModelAppSettingsWorker.packageName);
            m_hookModelAdvancedImeiEditTextPreference            = new HookModelAppSettingsAdvancedImei(this, HookModelAppSettingsWorker.packageName, m_hookModelOpenAdvancedSwitchPreference.getKey());
            m_hookModelAdvancedSimSerialNumberEditTextPreference = new HookModelAppSettingsAdvancedSimSerialNumber(this, HookModelAppSettingsWorker.packageName, m_hookModelOpenAdvancedSwitchPreference.getKey());
            m_hookModelAdvancedMacAddressEditTextPreference      = new HookModelAppSettingsMacAddress(this, HookModelAppSettingsWorker.packageName, m_hookModelOpenAdvancedSwitchPreference.getKey());

            // get items
            m_hookModelAppInfoPreference                         = findPreference(HookModelAppListWorker.GetAppSettingsString(12));
            m_hookModelSelectModelsPreference                    = findPreference(HookModelAppListWorker.GetAppSettingsString(13));

            // reset parent
            m_hookModelSelectModelsPreference.setDependency(HookModelAppSettingsWorker.packageName);

            // reset title & icon
            m_hookModelAppInfoPreference.setTitle(HookModelAppSettingsWorker.appName);
            m_hookModelAppInfoPreference.setIcon(HookModelAppListWorker.selectIconDrawable);

            // reset summary
            m_hookModelAppInfoPreference.setSummary(HookModelAppSettingsWorker.packageName);
            m_hookModelSelectModelsPreference.setSummary(HookModelAppListWorker.GetAppSettingsString(27));

            // on click listener
            m_hookModelAppInfoPreference.setOnPreferenceClickListener(appInfoPreferenceClickListener);
            m_hookModelSelectModelsPreference.setOnPreferenceClickListener(selectModelPreferenceClickListener);
        }

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

                                            m_hookModelManufacturerEditTextPreference.setText(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(14)));
                                            m_hookModelModelEditTextPreference.setText(modeInfoJsonObject.getString(HookModelAppListWorker.GetAppSettingsString(15)));
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
