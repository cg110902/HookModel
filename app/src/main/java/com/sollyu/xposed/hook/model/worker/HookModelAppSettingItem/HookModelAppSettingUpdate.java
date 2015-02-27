package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.widget.Toast;

import com.sollyu.xposed.hook.model.utils.HttpUtils;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by king.sollyu on 15/2/27.
 */
public class HookModelAppSettingUpdate
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingString(22);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingString(23);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingString(24);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public HookModelAppSettingWorker.PrefsFragment prefsFragment;
    public Preference preference = null;

    public HookModelAppSettingUpdate(final HookModelAppSettingWorker.PrefsFragment prefsFragment)
    {
        this.prefsFragment = prefsFragment;
        preference = prefsFragment.findPreference(XmlFlag);

        preference.setSummary(SummaryString);
        preference.setTitle(TitleString);
        preference.setOnPreferenceClickListener(onClickListener);
    }

    private Preference.OnPreferenceClickListener onClickListener = new Preference.OnPreferenceClickListener()
    {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            HookModelAppSettingUpdate.CheckUpdate(prefsFragment.getActivity(), true);
            return false;
        }
    };

    static public void CheckUpdate(final Context context, final Boolean showLast)
    {
        HttpUtils.GetHtml(HookModelAppListWorker.GetAppSettingString(25), null, new HttpUtils.HttpUtilsCallBack()
        {
            @Override
            public void OnFinish(final HttpResponse httpResponse)
            {
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            int currentVersionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                            final JSONObject result = new JSONObject(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));

                            if ( result.getInt("versionCode") > currentVersionCode)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(result.getString("updateContext"));
                                builder.setTitle(result.getString("updateTitle"));
                                builder.setNegativeButton("更新", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        try
                                        {
                                            ToolsHelper.OpenUrl(context, result.getString("downloadUrl") );
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                builder.setPositiveButton("取消", null);
                                builder.show();
                            }else
                            {
                                if (showLast) Toast.makeText(context, "当前版本已是最新", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, "更新失败：" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
