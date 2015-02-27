package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.widget.Toast;

import com.sollyu.xposed.hook.model.utils.HttpUtils;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;

/**
 * Created by king.sollyu on 15/2/27.
 */
public class HookModelAppSettingUpdateRemoteModel
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingString(18);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingString(19);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingString(20);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public HookModelAppSettingWorker.PrefsFragment prefsFragment;
    public Preference preference = null;

    public HookModelAppSettingUpdateRemoteModel(final HookModelAppSettingWorker.PrefsFragment prefsFragment)
    {
        this.prefsFragment = prefsFragment;
        preference = prefsFragment.findPreference(XmlFlag);

        preference.setSummary(SummaryString);
        preference.setTitle(TitleString);
        preference.setOnPreferenceClickListener(onClickListener);
    }

    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0: Toast.makeText(prefsFragment.getActivity(), msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    private Preference.OnPreferenceClickListener onClickListener = new Preference.OnPreferenceClickListener()
    {
        @Override
        public boolean onPreferenceClick(Preference preference)
        {
            HttpUtils.GetHtml(HookModelAppListWorker.GetAppSettingString(21), null, new HttpUtils.HttpUtilsCallBack()
            {
                @Override
                public void OnFinish(HttpResponse httpResponse)
                {
                    FileOutputStream phone_outStream = null;
                    try
                    {
                        String resultString = EntityUtils.toString(httpResponse.getEntity(), "utf-8");

                        phone_outStream = prefsFragment.getActivity().openFileOutput("share_model.json", Context.MODE_PRIVATE);
                        phone_outStream.write(resultString.getBytes());
                        phone_outStream.close();

                        handler.obtainMessage(0, "更新成功").sendToTarget();
                    }
                    catch (Exception e)
                    {
                        handler.obtainMessage(0, "更新失败：" + e.toString()).sendToTarget();
                    }
                }
            });

            return false;
        }
    };
}
