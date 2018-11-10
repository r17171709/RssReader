package com.renyu.rssreader.wx;

import com.google.gson.Gson;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.rssreader.params.Params;
import com.renyu.rssreader.bean.WXBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/27.
 */
public class WXMain {
    // 当前加载的公众号信息
    private static int current=0;

    public static void main(String[] args) {
        OKHttpHelper okHttpHelper = OKHttpHelper.getInstance();
        String[] names={
//                "clock_life",
//                "androidtrending",
//                "QQ空间开发团队",
//                "刘桂林",
//                "伯特说",
//                "架构师必备",
//                "HenCoder",
                "码个蛋",
                "Android技术之家",
                "互扯程序",
                "JANiubility",
                "Android群英传",
                "non-famous-coder",
                "何俊林",
                "开发者技术前线",
                "code小生",
                "KotlinX",
                "秦子帅",
                "郭霖",
                "安卓开发精选",
                "Android编程精选",
                "玉刚说",
                "鸿洋",
                "程序员小乐",
                "编码美丽",
                "前端之巅",
                "安卓笔记侠",
                "DailyQueation",
                "AndroidChinaNet",
                "终端研发部",
                "刘望舒",
        };

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
            String searchValue;
            try {
                searchValue = okHttpHelper.getOkHttpUtils().syncGet(searchUrl, null).body().string();
                if (searchValue.contains(startSearchinfo)) {
                    searchValue=searchValue.substring(searchValue.indexOf(startSearchinfo)+startSearchinfo.length());
                    if (searchValue.contains(endSearchinfo)) {
                        searchValue=searchValue.substring(0, searchValue.indexOf(endSearchinfo));
                    }
                }
                if (!searchValue.equals("")) {
                    searchValue=searchValue.replace("amp;", "");
                    String value= okHttpHelper.getOkHttpUtils().syncGet(searchValue, null).body().string();
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
                            if (!checkExists(listBean)) {
                                update(listBean);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 2, 180, TimeUnit.SECONDS);
    }

    private static boolean checkExists(WXBean.ListBean bean) {
        OKHttpHelper okHttpHelper = OKHttpHelper.getInstance();
        JSONObject object=new JSONObject();
        try {
            object.put("title", bean.getApp_msg_ext_info().getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String value = okHttpHelper.getOkHttpUtils().syncGet(Params.getBaseUrl() + "classes/WX?where="+object.toString(), Params.head()).body().string();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void update(WXBean.ListBean bean) {
        OKHttpHelper okHttpHelper = OKHttpHelper.getInstance();
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
            String uploadResult;
            try {
                uploadResult = okHttpHelper.getOkHttpUtils().syncPostJson(Params.getBaseUrl() + "classes/WX", object.toString(), Params.head()).body().string();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(uploadResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
