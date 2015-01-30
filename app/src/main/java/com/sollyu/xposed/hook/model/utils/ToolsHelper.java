package com.sollyu.xposed.hook.model.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.sollyu.xposed.hook.model.R;

import java.util.List;

/**
 * Created by wangsy on 15/1/29.
 */
public class ToolsHelper
{
    public static List<PackageInfo> GetInstalledPackages(Context context)
    {
        return context.getPackageManager().getInstalledPackages(0);
    }

    public static void ShowAlertDialogOk(Context context, int title, int message)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.model_string_alert_ok, null);
        builder.create().show();
    }
    public static void ShowAlertDialogOk(Context context, String title, String message)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.model_string_alert_ok, null);
        builder.create().show();
    }

    public static void ShowAlertDialogYesNo(Context context, int title, int message, int buttonYes, int buttonNo, DialogInterface.OnClickListener yesClickListener, DialogInterface.OnClickListener noClickListener )
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonNo, noClickListener);
        builder.setNegativeButton(buttonYes,yesClickListener);
        builder.create().show();
    }

    public static void ShowAlertDialogYesNo(Context context, String title, String message, String buttonYes, String buttonNo, DialogInterface.OnClickListener yesClickListener, DialogInterface.OnClickListener noClickListener )
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonNo, noClickListener);
        builder.setNegativeButton(buttonYes,yesClickListener);
        builder.create().show();
    }

    public static void OpenUrl(Context context, String url)
    {
        android.content.Intent intent = new android.content.Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(android.net.Uri.parse(url));
        context.startActivity(intent);
    }

    public static void OpenUrl(Context context, int url)
    {
        android.content.Intent intent = new android.content.Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(android.net.Uri.parse(context.getString(url)));
        context.startActivity(intent);
    }

    public static void StartActivity(Context context, Class<?> class1)
    {
        Intent intent = new Intent();
        intent.setClass(context, class1);
        context.startActivity(intent);
    }

    public static void OpenApp(Context context, String packageName_)
    {
        try
        {
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(context.getPackageManager().getPackageInfo(packageName_, 0).packageName);

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null)
            {
                String packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void CreateShortcut(Context context, String appName, Class<?> startClass, int icon)
    {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        shortcut.putExtra("duplicate", false);// 设置是否重复创建
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, startClass);// 设置第一个页面
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        context.sendBroadcast(shortcut);
    }

    public static String GetMyAppPackage()
    {
        return new String(new byte[]{ 99, 111, 109, 46, 115, 111, 108, 108, 121, 117, 46, 120, 112, 111, 115, 101, 100, 46, 104, 111, 111, 107, 46, 109, 111, 100, 101, 108 });
    }
}
