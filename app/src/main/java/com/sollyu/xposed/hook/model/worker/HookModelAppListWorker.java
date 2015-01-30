package com.sollyu.xposed.hook.model.worker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppListActivity;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingsActivity;
import com.sollyu.xposed.hook.model.activity.MyAdapter;
import com.sollyu.xposed.hook.model.config.HookModelSettings;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangsy on 15/1/29.
 */
public class HookModelAppListWorker
{
    public static Drawable selectIconDrawable = null;

    private HookModelAppListActivity activity = null;
    private ArrayList<HashMap<String, Object>> appArrayList = null;
    private List<PackageInfo> installPackages = null;
    private ListView appListView = null;
    private String appPackageName = null;

    public native String GetString(int nIndex);
    static { System.loadLibrary("JniTest");}

    public void onCreate(HookModelAppListActivity hookModelAppListActivity, Bundle savedInstanceState)
    {
        activity = hookModelAppListActivity;
        activity.setContentView(R.layout.activity_hook_model_app_list);

        appPackageName = activity.getPackageName();

        appListView  = (ListView) activity.findViewById(R.id.model_app_list);
        appArrayList = new ArrayList<HashMap<String, Object>>();

        onReloadInstallPackages();
        onRefreshAppList("");

        appListView.setOnItemClickListener(onAppListItemClickListener);

        Toast.makeText(activity, GetString(0), Toast.LENGTH_LONG).show();
    }

    public void onReloadInstallPackages()
    {
        installPackages = ToolsHelper.GetInstalledPackages(activity);
    }

    public void onRefreshAppList(String filter)
    {
        if (!appPackageName.equals(ToolsHelper.GetMyAppPackage()))
            activity.finish();

        appArrayList.clear();
        Boolean isShowSystemPackage = HookModelSettings.GetShowSystemPackages(activity);
        for (PackageInfo installPackage : installPackages)
        {
            if (isShowSystemPackage == true || ((installPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (installPackage.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0))
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("icon", installPackage.applicationInfo.loadIcon(activity.getPackageManager()));
                map.put("appName", installPackage.applicationInfo.loadLabel(activity.getPackageManager()));
                map.put("packageName", installPackage.applicationInfo.packageName);
                map.put("isOpen", activity.getSharedPreferences("com.sollyu.xposed.hook.model_preferences", Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS).getBoolean(installPackage.applicationInfo.packageName, false));
                appArrayList.add(map);
            }
        }

        appListView.setAdapter(new MyAdapter(activity, appArrayList, R.layout.list_item, new String[] { "icon", "appName", "packageName" }, new int[] { R.id.icon, R.id.appName, R.id.packageName }));
    }

    private AdapterView.OnItemClickListener onAppListItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            selectIconDrawable = (Drawable)appArrayList.get(i).get("icon");
            Intent intent = new Intent();
            intent.putExtra("packageName", appArrayList.get(i).get("packageName").toString());
            intent.putExtra("appName", appArrayList.get(i).get("appName").toString());
            intent.setClass(activity, HookModelAppSettingsActivity.class);
            activity.startActivity(intent);
        }
    };
}
