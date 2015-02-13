package com.sollyu.xposed.hook.model.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.sollyu.xposed.hook.model.R;

import java.lang.reflect.Field;
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

    public static void TranslucentStatus(Activity activity, String color)
    {
        if (android.os.Build.VERSION.SDK_INT > 18)
        {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // 获取手机状态栏高度
    public static int GetStatusBarHeight(Activity activity)
    {
        int x = 0, statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    // 获取ActionBar的高度
    public static int GetActionBarHeight(Activity activity)
    {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static Drawable GetActionBarBackground(Context context)
    {
        int[] android_styleable_ActionBar = { android.R.attr.background };

        // Need to get resource id of style pointed to from actionBarStyle
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarStyle, outValue, true);
        // Now get action bar style values...
        TypedArray abStyle = context.getTheme().obtainStyledAttributes(outValue.resourceId, android_styleable_ActionBar);
        try
        {
            return abStyle.getDrawable(0);
        }
        finally
        {
            abStyle.recycle();
        }
    }

    public static long Random(int nMin, int nMax)
    {
        return Math.round(Math.random()*(nMax-nMin)+nMin);
    }

    public static String RandomString(int nMin, int nMax, int nLen)
    {
        String randomString = "";
        for (int i=0; i<nLen; i++)
        {
            int rand=(int)Math.round(Math.random()*(nMax-nMin)+nMin);
            randomString += String.valueOf(rand);
        }
        return  randomString;
    }

    public static String GetHtml(String Url) throws Exception
    {
        java.net.URL url = new java.net.URL(Url);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);

        java.io.InputStream inStream = conn.getInputStream();

        java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1)
            outStream.write(buffer, 0, len);

        inStream.close();

        String html = new String(outStream.toByteArray());
        return html;
    }
}
