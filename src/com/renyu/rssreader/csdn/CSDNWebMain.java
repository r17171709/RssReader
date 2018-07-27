package com.renyu.rssreader.csdn;

import com.renyu.house.params.Params;
import com.renyu.rssreader.bean.RSSBean;
import com.renyu.rssreader.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Administrator on 2017/7/5.
 */
public class CSDNWebMain {
    public static void main(String[] args) {
        int page = 0;

        outer:
        while (true) {
            page++;
            String url="http://blog.csdn.net/mobile/newarticle.html?page="+page;
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

                    String time = blog_list_doc.getElementsByClass("blog_list_b_r fr").text();
                    if (time.contains("前天")) {
                        // 目标博客已经统计完成
                        break outer;
                    }

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
        }
    }

    private boolean checkExists(RSSBean bean) {
        JSONObject object=new JSONObject();
        try {
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String value=HttpUtils.getIntance().get("https://api.bmob.cn/1/classes/Rss?where="+object.toString(), Params.head());
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

    private void update(RSSBean bean) {
        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getTile());
            object.put("link", bean.getLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!object.toString().equals("")) {
            String uploadResult=HttpUtils.getIntance().post("https://api.bmob.cn/1/classes/Rss", Params.head(), object.toString());
            System.out.println(uploadResult);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
