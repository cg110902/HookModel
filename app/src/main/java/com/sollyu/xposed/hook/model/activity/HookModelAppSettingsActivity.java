package com.sollyu.xposed.hook.model.activity;

import android.app.Activity;
import android.os.Bundle;

import com.sollyu.xposed.hook.model.worker.HookModelAppSettingsWorker;

/**
 * Created by wangsy on 15/1/29.
 */
public class HookModelAppSettingsActivity extends Activity
{
    private HookModelAppSettingsWorker hookModelAppSettingsWorker = new HookModelAppSettingsWorker();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        hookModelAppSettingsWorker.onCreate(this, savedInstanceState);
    }
}
