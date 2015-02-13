package com.sollyu.xposed.hook.model.worker.HookModelAppSettingItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Message;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sollyu.xposed.hook.model.R;
import com.sollyu.xposed.hook.model.utils.ToolsHelper;
import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;
import com.sollyu.xposed.hook.model.worker.HookModelAppSettingWorker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    public ProgressDialog m_Dialog = null;

    private final String string001    = TitleString;
    private final String string002    = "取消";
    private final String string003    = "确定";


    private android.os.Handler handler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0: m_Dialog = ProgressDialog.show(prefsFragment.getActivity(), "请等待...", "正在为你登陆...",true);
                case 1: m_Dialog.dismiss();
                case 2:
                    String resString = (String) msg.obj;
                    Toast.makeText(prefsFragment.getActivity(), resString.equals("successful") ? "提交成功，感觉您的共享精神" : "分享失败，返回内容：" + resString, Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

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
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                handler.obtainMessage(0).sendToTarget();

                                try
                                {
                                    String urlString = "http://image.4sql.net/soft/hook-model/report_model.php";
                                    urlString += "?manufacturer=" + URLEncoder.encode(Build.MANUFACTURER, "utf-8");
                                    urlString += "&model=" + URLEncoder.encode(Build.MODEL, "utf-8");
                                    urlString += "&note=" + URLEncoder.encode(NoteEditText.getText().toString(), "utf-8");

                                    handler.obtainMessage(2, ToolsHelper.GetHtml(urlString)).sendToTarget();
                                    //
                                } catch (UnsupportedEncodingException e)
                                {
                                    e.printStackTrace();
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                handler.obtainMessage(1).sendToTarget();
                            }
                        }).start();
                    }
                });
                builder.create().show();

                return false;
            }
        });
    }
}
