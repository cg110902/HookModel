package com.sollyu.xposed.hook.model.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sollyu.xposed.hook.model.R;
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hook_model_app_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
