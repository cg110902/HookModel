package com.sollyu.xposed.hook.model.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by king.sollyu on 15/2/27.
 */
public class HttpUtils
{
    static public void GetHtml(final String baseUrl, final List<BasicNameValuePair > params, final HttpUtilsCallBack httpUtilsCallBack)
    {
        new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                try
                {
                    List<BasicNameValuePair > params_ = params;
                    if (params_ == null) params_ = new LinkedList<BasicNameValuePair>();
                    String param = URLEncodedUtils.format ( params_, HTTP.UTF_8 );

                    HttpGet    httpGet      = new HttpGet ( baseUrl + "?" + param );
                    HttpClient httpClient   = new DefaultHttpClient ( );
                    httpUtilsCallBack.OnFinish ( httpClient.execute ( httpGet ) );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        } ).start ();
    }

    static public void PostHtml(final String baseUrl, final List<BasicNameValuePair > params, final HttpUtilsCallBack httpUtilsCallBack)
    {
        new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                try
                {
                    HttpPost httpPost     = new HttpPost ( baseUrl );
                    httpPost.setEntity ( new UrlEncodedFormEntity ( params, HTTP.UTF_8 ) );

                    HttpClient httpClient   = new DefaultHttpClient ( );
                    httpUtilsCallBack.OnFinish ( httpClient.execute ( httpPost ) );
                }
                catch ( Exception e )
                {
                    e.printStackTrace ();
                }
            }
        } ).start ();
    }

    static public interface HttpUtilsCallBack
    {
        public void OnFinish(final HttpResponse httpResponse);
    }
}
