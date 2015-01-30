package com.sollyu.xposed.hook.model.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sollyu.xposed.hook.model.worker.HookModelAppListWorker;

import java.util.List;
import java.util.Map;

/**
 * Created by wangsy on 15/1/29.
 */
public class MyAdapter extends SimpleAdapter
{
    private int            appResource    = 0;
    private int[]          appTo          = null;
    private String[]       appFrom        = null;
    private LayoutInflater layoutInflater = null;
    private List<? extends Map<String, ?>> appData;

    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);

        appTo          = to;
        appData        = data;
        appFrom        = from;
        appResource    = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = onCreateViewFromResource(position, convertView, parent, appResource);
        final Map<String, ?> dataSet = appData.get(position);
        if ((Boolean)dataSet.get(HookModelAppListWorker.GetAppListString(4)))
            view.setBackgroundColor(android.graphics.Color.rgb(131,175,155));
        else
            view.setBackgroundColor(0x00000000);

        return view;
    }

    private View onCreateViewFromResource(int pos, View convert, ViewGroup parent, int resource)
    {
        View view = null;

        if (convert == null)
        {
            view = layoutInflater.inflate(resource, parent ,false);
            View holder[] = new View[appTo.length];

            for (int i = 0 ; i < appTo.length; i++)
            {
                holder[i] = view.findViewById(appTo[i]);
            }

            view.setTag(holder);
        }else
        {
            view = convert;
        }

        bindView(pos, view);

        return view;
    }

    private void bindView(int position, View view)
    {
        final Map<String, ?> dataSet = appData.get(position);

        if (dataSet == null)
        {
            return;
        }

        final View[] holder = (View[]) view.getTag();
        final String[] from = appFrom;
        final int[] to = appTo;
        final int count = to.length;

        for (int i = 0; i < count; i++)
        {
            final View v = holder[i];

            if (v != null)
            {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();

                if (text == null)
                {
                    text = "";
                }

                if (v instanceof TextView)
                {
                    setViewText((TextView) v, text);
                }
                else if (v instanceof ImageView)
                {
                    ((ImageView) v).setImageDrawable((Drawable) data);
                }
            }
        }
    }
}
