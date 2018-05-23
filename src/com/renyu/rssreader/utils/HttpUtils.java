package com.renyu.rssreader.utils;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by renyu on 2016/12/9.
 */
public class HttpUtils {

    private static volatile HttpUtils httpUtils;

    private OkHttpClient client;

    public static HttpUtils getIntance() {
        if (httpUtils==null) {
            synchronized (HttpUtils.class) {
                if (httpUtils==null) {
                    httpUtils=new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    private HttpUtils() {
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS);
        HttpsUtils.SSLParams sslParams=HttpsUtils.getSslSocketFactory(null, null, null);
        builder.hostnameVerifier((s, sslSession) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        client=builder.build();
    }

    public String get(String url, HashMap<String, String> head) {
        Request.Builder builder=new Request.Builder();
        if (head!=null) {
            Iterator<Map.Entry<String, String>> iterator=head.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry=iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request=builder.url(url).tag(url).build();
        try {
            Response response=client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String post(String url, HashMap<String, String> head, String json) {
        Request.Builder builder=new Request.Builder();
        if (head!=null) {
            Iterator<Map.Entry<String, String>> iterator=head.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry=iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request=builder.url(url).post(requestBody).tag(url).build();
        try {
            Response response=client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String put(String url, HashMap<String, String> head, String json) {
        Request.Builder builder=new Request.Builder();
        if (head!=null) {
            Iterator<Map.Entry<String, String>> iterator=head.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry=iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request=builder.url(url).put(requestBody).tag(url).build();
        try {
            Response response=client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
