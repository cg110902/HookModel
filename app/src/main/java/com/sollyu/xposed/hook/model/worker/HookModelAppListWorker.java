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

    public static native String GetAppListString(int nIndex);
    public static native String GetAppSettingsString(int nIndex);
    public static native String onCreate(Context context);

    static { System.loadLibrary("HookModel");}

    public void onCreate(HookModelAppListActivity hookModelAppListActivity, Bundle savedInstanceState)
    {
        activity = hookModelAppListActivity;
        activity.setContentView(R.layout.activity_hook_model_app_list);

        HookModelAppListWorker.onCreate(activity);

        appListView  = (ListView) activity.findViewById(R.id.model_app_list);
        appArrayList = new ArrayList<HashMap<String, Object>>();

        onReloadInstallPackages();
        onRefreshAppList("");

        appListView.setOnItemClickListener(onAppListItemClickListener);
    }

    public void onReloadInstallPackages()
    {
        installPackages = ToolsHelper.GetInstalledPackages(activity);
    }

    public void onRefreshAppList(String filter)
    {
        appArrayList.clear();
        Boolean isShowSystemPackage = HookModelSettings.GetShowSystemPackages(activity);
        for (PackageInfo installPackage : installPackages)
        {
            if (isShowSystemPackage == true || ((installPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (installPackage.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0))
            {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(GetAppListString(1), installPackage.applicationInfo.loadIcon(activity.getPackageManager()));
                map.put(GetAppListString(2), installPackage.applicationInfo.loadLabel(activity.getPackageManager()));
                map.put(GetAppListString(3), installPackage.applicationInfo.packageName);
                map.put(GetAppListString(4), activity.getSharedPreferences("com.sollyu.xposed.hook.model_preferences", Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS).getBoolean(installPackage.applicationInfo.packageName, false));
                appArrayList.add(map);
            }
        }

        appListView.setAdapter(new MyAdapter(activity, appArrayList, R.layout.list_item, new String[] { GetAppListString(1), GetAppListString(2), GetAppListString(3) }, new int[] { R.id.icon, R.id.appName, R.id.packageName }));
    }

    private AdapterView.OnItemClickListener onAppListItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            selectIconDrawable = (Drawable)appArrayList.get(i).get(GetAppListString(1));
            Intent intent = new Intent();
            intent.putExtra(GetAppListString(3), appArrayList.get(i).get(GetAppListString(3)).toString());
            intent.putExtra(GetAppListString(2), appArrayList.get(i).get(GetAppListString(2)).toString());
            intent.setClass(activity, HookModelAppSettingsActivity.class);
            activity.startActivity(intent);
        }
    };
}
