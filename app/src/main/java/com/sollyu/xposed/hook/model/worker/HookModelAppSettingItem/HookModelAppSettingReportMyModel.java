package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.utils.HttpUtils;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangsy on 15/2/13.
 */
public class HookModelAppSettingReportMyModel
{
    public final String XmlFlag       = HookModelAppListWorker.GetAppSettingString(14);
    public final String SummaryString = HookModelAppListWorker.GetAppSettingString(15);
    public final String TitleString   = HookModelAppListWorker.GetAppSettingString(16);
    public final String EmptyString   = HookModelAppListWorker.GetAppListString(5);

    public HookModelAppSettingWorker.PrefsFragment prefsFragment;
    public Preference preference = null;

    private final String string001    = TitleString;
    private final String string002    = "取消";
    private final String string003    = "确定";

    public HookModelAppSettingReportMyModel(final HookModelAppSettingWorker.PrefsFragment prefsFragment)
    {
        this.prefsFragment = prefsFragment;
        preference = prefsFragment.findPreference(XmlFlag);

        preference.setSummary(SummaryString);
        preference.setTitle(TitleString);
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                View dialogView = LayoutInflater.from(prefsFragment.getActivity()).inflate(R.layout.hook_model_report_model, null);

                final EditText ManufacturerEditText = (EditText) dialogView.findViewById(R.id.HookModelAppSettingReportMyModelManufacturer);
                final EditText ModelEditText        = (EditText) dialogView.findViewById(R.id.HookModelAppSettingReportMyModelModel);
                final EditText NoteEditText         = (EditText) dialogView.findViewById(R.id.HookModelAppSettingReportMyModelNote);

                ManufacturerEditText.setText(Build.MANUFACTURER);
                ModelEditText.setText(Build.MODEL);

                AlertDialog.Builder builder = new AlertDialog.Builder(prefsFragment.getActivity());
                builder.setTitle(string001);
                builder.setView(dialogView);
                builder.setNegativeButton(string002, null);
                builder.setPositiveButton(string003, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        List<BasicNameValuePair > params = new LinkedList< BasicNameValuePair >();
                        params.add ( new BasicNameValuePair ( "manufacturer", ManufacturerEditText.getText ().toString () ) );
                        params.add ( new BasicNameValuePair ( "model", ModelEditText.getText ().toString () ) );
                        params.add ( new BasicNameValuePair ( "note" , NoteEditText.getText ().toString () ) );

                        Toast.makeText(prefsFragment.getActivity(), "正在提交...", Toast.LENGTH_LONG).show();
                        HttpUtils.GetHtml(HookModelAppListWorker.GetAppSettingString(21), params, new HttpUtils.HttpUtilsCallBack()
                        {
                            @Override
                            public void OnFinish(final HttpResponse httpResponse)
                            {
                                prefsFragment.getActivity().runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        try
                                        {
                                            String resultString = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                                            if (httpResponse.getStatusLine().getStatusCode() == 200 && resultString.equals("successful"))
                                            {
                                                Toast.makeText(prefsFragment.getActivity(), "提交成功, 感谢您的共享精神", Toast.LENGTH_LONG).show();
                                            } else
                                            {
                                                Toast.makeText(prefsFragment.getActivity(), "提交失败：" + resultString, Toast.LENGTH_LONG).show();
                                            }
                                        } catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                builder.create().show();

                return false;
            }
        });
    }
}
