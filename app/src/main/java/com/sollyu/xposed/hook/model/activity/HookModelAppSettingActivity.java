package com.sollyu.xposed.hook.model.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

/**
 * Created by wangsy on 15/1/30.
 */
public class HookModelAppSettingActivity extends Activity
{
    private HookModelAppSettingWorker hookModelAppSettingWorker = new HookModelAppSettingWorker();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        hookModelAppSettingWorker.onCreate(this, savedInstanceState);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        super.onMenuItemSelected(featureId, item);

        return hookModelAppSettingWorker.onMenuItemSelected(featureId, item);
    }
}
