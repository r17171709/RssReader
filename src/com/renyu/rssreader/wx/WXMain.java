package com.renyu.rssreader.wx;

import com.google.gson.Gson;
import com.renyu.rssreader.bean.WXBean;
import com.renyu.rssreader.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WXMain {

    // 当前加载的公众号信息
    static int current=0;

    public static void main(String[] args) {

        String[] names={
                "鸿洋",
                "郭霖",
                "APP架构师",
                "KotlinX",
                "沪江技术学院",
                "KotlinThree",
                "Android科技前沿",
                "Android开发666",
                "Android编程精选",
                "移动开发前线",
                "安卓笔记侠",
                "Android开发中文站",
                "何俊林",
                "终端研发部",
                "吴小龙同学",
//                "手机淘宝技术团队MTT",
                "WeMobileDev",
//                "技术实验室",
                "非著名程序员",
                "Android技术之家",
                "QQ空间开发团队",
                "优雅的程序员",
                "HenCoder",
                "技术视界",
                "Android群英传",
                "Android程序员",
//                "Android面试专栏",
                "开发者技术前线",
                "刘桂林",
                "code小生",
                "Android进阶之旅",
                "KotlinX",
                "Android程序员",
                "码个蛋",
                "ASCE1885"
        };

        ExecutorService uploadService= Executors.newFixedThreadPool(1);

        ScheduledExecutorService scheduledExecutorService= Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {

            // 找到当前需要加载的公众号站点名称
            String searchUrl="http://weixin.sogou.com/weixin?type=1&s_from=input&query="+names[current]+"&ie=utf8&_sug_=n&_sug_type_=";
            if (current==names.length-1) {
                current=0;
            }
            else {
                current++;
            }

            // 查找公众号地址
            String startSearchinfo="uigs=\"account_image_0\" href=\"";
            String endSearchinfo="\"><span>";
            String searchValue=HttpUtils.getIntance().get(searchUrl, null);
            if (searchValue.indexOf(startSearchinfo)!=-1) {
                searchValue=searchValue.substring(searchValue.indexOf(startSearchinfo)+startSearchinfo.length());
                if (searchValue.indexOf(endSearchinfo)!=-1) {
                    searchValue=searchValue.substring(0, searchValue.indexOf(endSearchinfo));
                }
            }
            if (!searchValue.equals("")) {
                searchValue=searchValue.replace("amp;", "");
                String value= HttpUtils.getIntance().get(searchValue, null);
                if (value!=null) {
                    int start=value.indexOf("{\"list\":[");
                    int end=value.indexOf("]};");
                    if (start==-1 || end==-1) {
                        System.out.println("请网页访问输入验证码");
                        return;
                    }
                    value=value.substring(start, end+2);
                    Gson gson=new Gson();
                    WXBean bean=gson.fromJson(value, WXBean.class);
                    List<WXBean.ListBean> beans= bean.getList();
                    System.out.println("获取到"+beans.size()+"条数据");
                    for (WXBean.ListBean listBean : beans) {
                        uploadService.execute(() -> {
                            if (!checkExists(listBean)) {
                                update(listBean);
                            }
                        });
                    }
                }
            }
        }, 2, 60*20, TimeUnit.SECONDS);
    }

    private static boolean checkExists(WXBean.ListBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0");
        head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8");

        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getApp_msg_ext_info().getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String value=HttpUtils.getIntance().get("https://api.bmob.cn/1/classes/WX?where="+object.toString(), head);
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

    private static void update(WXBean.ListBean bean) {
        HashMap<String, String> head=new HashMap<>();
        head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0");
        head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8");

        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getApp_msg_ext_info().getTitle());
            object.put("link", "");
            object.put("cover", bean.getApp_msg_ext_info().getCover());
            object.put("author", bean.getApp_msg_ext_info().getAuthor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!object.toString().equals("")) {
            String uploadResult=HttpUtils.getIntance().post("https://api.bmob.cn/1/classes/WX", head, object.toString());
            System.out.println(uploadResult);
        }
    }
}
