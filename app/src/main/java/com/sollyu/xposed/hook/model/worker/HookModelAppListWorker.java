package com.sollyu.xposed.hook.model.worker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.activity.HookModelAppListActivity;
import com.sollyu.xposed.hook.model.activity.HookModelAppListAdapter;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingActivity;
import com.sollyu.xposed.hook.model.activity.HookModelAppSettingsActivity;
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
    private HookModelAppListAdapter appListAdapter = null;

    private EditText  searchEditText  = null;
    private ImageView deleteImageView = null;

    public static native String GetAppListString(int nIndex);
    public static native String GetAppSettingsString(int nIndex);
    public static native String GetAppSettingString(int nIndex);
    public static native String onCreate(Context context);

    static { System.loadLibrary("HookModel");}

    public void onCreate(HookModelAppListActivity hookModelAppListActivity, Bundle savedInstanceState)
    {
        activity = hookModelAppListActivity;
        activity.setContentView(R.layout.hook_model_activity_app_list);

        ToolsHelper.TranslucentStatus(activity, "#222222");

        HookModelAppListWorker.onCreate(activity);

        appListView     = (ListView) activity.findViewById(R.id.model_app_list);
        searchEditText  = (EditText) activity.findViewById(R.id.etSearch);
        deleteImageView = (ImageView) activity.findViewById(R.id.ivDeleteText);
        appArrayList    = new ArrayList<HashMap<String, Object>>();

        onReloadInstallPackages();
        onRefreshAppList("");

        appListView.setOnItemClickListener(onAppListItemClickListener);
        deleteImageView.setOnClickListener(deleteImageViewOnClickListener);
        searchEditText.addTextChangedListener(searchTextChangedListener);
    }

    public void onReloadInstallPackages()
    {
        installPackages = ToolsHelper.GetInstalledPackages(activity);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, HookModelAppListWorker.GetAppListString(7 )).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 1, 1, HookModelAppListWorker.GetAppListString(8 )).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, 2, HookModelAppListWorker.GetAppListString(9 )).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 3, 3, HookModelAppListWorker.GetAppListString(10)).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case 0: ToolsHelper.StartActivity(activity, HookModelAppSettingActivity.class); break;
            case 1: onRefreshAppList(HookModelAppListWorker.GetAppListString(5)); break;
            case 2: ToolsHelper.OpenUrl(activity, HookModelAppListWorker.GetAppListString(12));break;
            case 3: ToolsHelper.ShowAlertDialogOk(activity, HookModelAppListWorker.GetAppListString(10), HookModelAppListWorker.GetAppListString(11)); break;
        }
        return true;
    }

    public void onRefreshAppList(String filter)
    {
        appArrayList.clear();
        Boolean isShowSystemPackage = HookModelSettings.GetShowSystemPackages(activity);
        for (PackageInfo installPackage : installPackages)
        {
            if (isShowSystemPackage == true || ((installPackage.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (installPackage.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0))
            {
                String appLabel   = installPackage.applicationInfo.loadLabel(activity.getPackageManager()).toString().toLowerCase();
                String appPackage = installPackage.applicationInfo.packageName.toLowerCase();

                filter = filter.toLowerCase();

                if (filter.equals(HookModelAppListWorker.GetAppListString(5)) || appLabel.contains(filter) || appPackage.contains(filter))
                {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put(GetAppListString(1), installPackage.applicationInfo.loadIcon(activity.getPackageManager()));
                    map.put(GetAppListString(2), installPackage.applicationInfo.loadLabel(activity.getPackageManager()));
                    map.put(GetAppListString(3), installPackage.applicationInfo.packageName);
                    map.put(GetAppListString(4), activity.getSharedPreferences("com.sollyu.xposed.hook.model_preferences", Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS).getBoolean(installPackage.applicationInfo.packageName, false));
                    appArrayList.add(map);
                }
            }
        }
        appListAdapter = new HookModelAppListAdapter(activity, appArrayList, R.layout.hook_model_app_list_item, new String[] { GetAppListString(1), GetAppListString(2), GetAppListString(3) }, new int[] { R.id.icon, R.id.appName, R.id.packageName });
        appListView.setAdapter(appListAdapter);
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

    private View.OnClickListener deleteImageViewOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            searchEditText.setText("");
        }
    };

    private TextWatcher searchTextChangedListener = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            onRefreshAppList(editable.toString());
            deleteImageView.setVisibility(editable.length() == 0 ? View.GONE : View.VISIBLE);
            appListAdapter.notifyDataSetChanged();
        }
    };
}
