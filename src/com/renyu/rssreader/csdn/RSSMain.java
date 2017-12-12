package com.renyu.rssreader.csdn;

import com.renyu.rssreader.bean.RSSBean;
import com.renyu.rssreader.utils.HttpUtils;
import com.renyu.rssreader.utils.XmlParseUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Created by renyu on 2016/12/9.
 */
public class RSSMain {

    public static void main(String[] args) {
        ExecutorService uploadService=Executors.newFixedThreadPool(1);

        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            String value= HttpUtils.getIntance().get("http://blog.csdn.net/rss.html?type=Home&channel=mobile", null);
            if (value!=null) {
                ArrayList<RSSBean> beans=XmlParseUtils.getIntance().parse(value);
                System.out.println("获取到"+beans.size()+"条数据");
                for (RSSBean bean : beans) {
                    uploadService.execute(() -> {
                        RSSMain main=new RSSMain();
                        if (!main.checkExists(bean)) {
                            main.update(bean);
                        }
                        if (!main.checkExistsLeanCloud(bean)) {
                            main.updateLeanCloud(bean);
                        }
                    });
                }
            }
        }, 10, 60*120, TimeUnit.SECONDS);
    }

    public void update(RSSBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0");
        head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8");

        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getTile());
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!object.toString().equals("")) {
            String uploadResult=HttpUtils.getIntance().post("https://api.bmob.cn/1/classes/Rss", head, object.toString());
            System.out.println(uploadResult);
        }
    }

    public void updateLeanCloud(RSSBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-LC-Id", "wR64FWAvE7zrBIzeDTEucb6I-gzGzoHsz");
        head.put("X-LC-Key", "mYA8RnhaVDNUgf5yLdsD09vi");

        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getTile());
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!object.toString().equals("")) {
            String uploadResult=HttpUtils.getIntance().post("https://api.leancloud.cn/1.1/classes/Rss?fetchWhenSave=true", head, object.toString());
            System.out.println(uploadResult);
        }
    }

    public boolean checkExists(RSSBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0");
        head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8");

        JSONObject object=new JSONObject();
        try {
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String value=HttpUtils.getIntance().get("https://api.bmob.cn/1/classes/Rss?where="+object.toString(), head);
        boolean isExists=false;
        try {
            if (value==null) {
                isExists=true;
            }
            else {
                JSONObject jsonObject=new JSONObject(value);
                if (jsonObject.getJSONArray("results").length()>0) {
                    isExists=true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isExists;
    }

    public boolean checkExistsLeanCloud(RSSBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-LC-Id", "wR64FWAvE7zrBIzeDTEucb6I-gzGzoHsz");
        head.put("X-LC-Key", "mYA8RnhaVDNUgf5yLdsD09vi");

        JSONObject object=new JSONObject();
        try {
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String value=HttpUtils.getIntance().get("https://api.leancloud.cn/1.1/classes/Rss?where="+object.toString(), head);
        if (value==null) {
            return false;
        }
        boolean isExists=false;
        try {
            JSONObject object1=new JSONObject(value);
            if (object1.getJSONArray("results").length()!=0) {
                isExists=true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isExists;
    }
}
