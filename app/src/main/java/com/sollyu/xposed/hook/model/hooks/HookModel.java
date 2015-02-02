package com.sollyu.xposed.hook.model.hooks;

import android.os.Environment;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookModel implements IXposedHookLoadPackage
{
    public static Boolean DEBUG_MODEL = false;
    public static String LOG_TAG = "=== MODEL_HOOK ===";
    public static XSharedPreferences pref = null;

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable
    {
         try
        {
            int nTryCount = 0;
            File file = new File(Environment.getDataDirectory(), "data/com.sollyu.xposed.hook.model/shared_prefs/com.sollyu.xposed.hook.model_preferences.xml");
            while ( pref == null && nTryCount++ < 10 && !file.exists() )
            {
                file = new File(Environment.getDataDirectory(), "data/com.sollyu.xposed.hook.model/shared_prefs/com.sollyu.xposed.hook.model_preferences.xml");
                LogString("try load :" + file.getAbsolutePath());
                Thread.sleep(100);
            }

            if (nTryCount == 10)
            {
                LogString("NOT FOUNT : " + file.getAbsolutePath());
                return;
            }

            pref = new XSharedPreferences(file);

            // refresh shared preferences
            XposedHelpers.findAndHookMethod((Class<?>) android.app.Activity.class, "onResume", new XC_MethodHook()
            {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable
                {
                    LogString(param.getClass().toString() + " : === pref.reload() ===");
                    pref.reload();
                }
            });

            // debug model
            LogString( lpparam.packageName + ":" + pref.getBoolean(lpparam.packageName, false) );

            // replace settings.
            if (pref.getBoolean(lpparam.packageName, false))
            {
                final String hookModelManufacutrerKey            = lpparam.packageName + ".hookModelManufacutrer";
                final String hookModelModelKey                   = lpparam.packageName + ".hookModelModel";
                final String hookModelAdvancedSwitchKey          = lpparam.packageName + ".hookModelAdvanced";
                final String hookModelAdvancedImeiKey            = lpparam.packageName + ".hookModelAdvancedImei";
                final String hookModelAdvancedSimSerialNumberKey = lpparam.packageName + ".hookModelAdvancedSimSerialNumber";
                final String hookModelAdvancedMacAddressKey      = lpparam.packageName + ".hookModelAdvancedMacAddress";
                final String hookModelAdvancedAndroidIdKey       = lpparam.packageName + ".hookModelAdvancedAndroidId";

                Class<?> classBuild = XposedHelpers.findClass("android.os.Build", lpparam.classLoader);

                XposedHelpers.setStaticObjectField(classBuild, "MANUFACTURER", pref.getString(hookModelManufacutrerKey, android.os.Build.MANUFACTURER));
                XposedHelpers.setStaticObjectField(classBuild, "MODEL", pref.getString(hookModelModelKey, android.os.Build.MODEL));

                // hook advanced
                if (pref.getBoolean(hookModelAdvancedSwitchKey, false))
                {
                    Class<?> classTelephonyManager = XposedHelpers.findClass("android.telephony.TelephonyManager", lpparam.classLoader);
                    Class<?> classWifiInfo         = XposedHelpers.findClass("android.net.wifi.WifiInfo", lpparam.classLoader);
                    Class<?> classSettingsSecure   = XposedHelpers.findClass("android.provider.Settings.Secure", lpparam.classLoader);

                    XposedHelpers.findAndHookMethod(classTelephonyManager, "getDeviceId", new XC_MethodHook()
                    {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable
                        {
                            String saveValue = pref.getString(hookModelAdvancedImeiKey, "null");
                            if (!saveValue.equals("null")) param.setResult(saveValue);
                            super.afterHookedMethod(param);
                        }
                    });

                    XposedHelpers.findAndHookMethod(classTelephonyManager, "getSimSerialNumber", new XC_MethodHook()
                    {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable
                        {
                            String saveValue = pref.getString(hookModelAdvancedSimSerialNumberKey, "null");
                            if (!saveValue.equals("null")) param.setResult(saveValue);
                            super.afterHookedMethod(param);
                        }
                    });

                    XposedHelpers.findAndHookMethod(classWifiInfo, "getMacAddress", new XC_MethodHook()
                    {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable
                        {
                            String saveValue = pref.getString(hookModelAdvancedMacAddressKey, "null");
                            if (!saveValue.equals("null")) param.setResult(saveValue);
                            super.afterHookedMethod(param);
                        }
                    });

                    XposedHelpers.findAndHookMethod(classSettingsSecure, "getString", android.content.ContentResolver.class , java.lang.String.class, new XC_MethodHook()
                    {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable
                        {
                            String saveValue = pref.getString(hookModelAdvancedAndroidIdKey, "null");
                            if (param.args[1].equals("android_id") && !saveValue.equals("null")) param.setResult(saveValue);
                            super.afterHookedMethod(param);
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            LogString(e.toString());
        }
    }

    public void LogString(Object log)
    {
        if (DEBUG_MODEL)
        {
            android.util.Log.e(LOG_TAG, log.toString());
            // XposedBridge.log(log);
        }
    }
}
