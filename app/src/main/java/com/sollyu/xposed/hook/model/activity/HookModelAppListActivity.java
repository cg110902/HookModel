package com.sollyu.xposed.hook.model.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;


public class HookModelAppListActivity extends Activity {

    private HookModelAppListWorker hookModelAppListWorker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hookModelAppListWorker = new HookModelAppListWorker();
        hookModelAppListWorker.onCreate(HookModelAppListActivity.this, savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return hookModelAppListWorker.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return  hookModelAppListWorker.onOptionsItemSelected(item);
    }
}
