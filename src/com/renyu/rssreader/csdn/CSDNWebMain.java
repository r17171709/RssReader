package com.renyu.rssreader.csdn;

import com.renyu.rssreader.bean.RSSBean;
import com.renyu.rssreader.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/7/5.
 */
public class CSDNWebMain {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            String url="http://blog.csdn.net/mobile/newarticle.html";
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements=doc.getElementsByClass("blog_home_main");
                Document blog_home_main_doc = Jsoup.parse(elements.toString());
                Elements blog_l_elements=blog_home_main_doc.getElementsByClass("blog_l");
                Document blog_l_doc = Jsoup.parse(blog_l_elements.toString());
                Element listBot_element=blog_l_doc.getElementById("listBot");
                Document listBot_doc = Jsoup.parse(listBot_element.toString());
                Elements blog_list_elements=listBot_doc.select(".blog_list");

                System.out.println("获取到"+blog_list_elements.size()+"条数据");

                for (Element blog_list_element : blog_list_elements) {
                    Document blog_list_doc = Jsoup.parse(blog_list_element.toString());
                    Elements tracking_ad_elements=blog_list_doc.getElementsByClass("csdn-tracking-statistics");
                    Document tracking_ad_doc = Jsoup.parse(tracking_ad_elements.toString());
                    String title = tracking_ad_doc.getElementsByTag("a").text();
                    String url_ =tracking_ad_doc.getElementsByTag("a").attr("href");
                    RSSBean bean=new RSSBean(title, url_);
                    CSDNWebMain main=new CSDNWebMain();
                    if (!main.checkExists(bean)) {
                        main.update(bean);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 2, 60*60, TimeUnit.SECONDS);
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isExists;
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
