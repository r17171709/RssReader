package com.renyu.rssreader;

import com.renyu.rssreader.params.Params;
import com.renyu.rssreader.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by renyu on 2016/12/9.
 */
public class CreateTable {
    public static void main(String[] args) {
        String json = getTableString();

        if (json!=null) {
            HashMap<String, String> head=new HashMap<>();
            head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0");
            head.put("X-Bmob-Master-Key", "fe674c0175e0522360ac03bab0d5e6d9");

            String callback=HttpUtils.getIntance().post(Params.getBaseUrl() + "schemas/HomeLinkHouse", head, json);
            System.out.println(callback);
        }

//        if (json!=null) {
//            HashMap<String, String> head=new HashMap<>();
//            head.put("X-LC-Id", "wR64FWAvE7zrBIzeDTEucb6I-gzGzoHsz");
//            head.put("X-LC-Key", "mYA8RnhaVDNUgf5yLdsD09vi");
//
//            String callback=HttpUtils.getIntance().post("https://api.leancloud.cn/1.1/classes/Post?fetchWhenSave=true", head, json);
//            System.out.println(callback);
//        }
    }

    private static String getTableString() {
        String json=null;
        try {
            JSONObject object=new JSONObject();
            object.put("className", "HomeLinkHouse");

            JSONObject stringObjectType=new JSONObject();
            stringObjectType.put("type", "String");

            JSONObject numberObjectType=new JSONObject();
            numberObjectType.put("type", "Number");

            JSONObject fields=new JSONObject();
            fields.put("area", numberObjectType);
            fields.put("bedroom_hall_num", stringObjectType);
            fields.put("sell_time", stringObjectType);
            fields.put("orientation", stringObjectType);
            fields.put("community_name", stringObjectType);
            fields.put("cover_pic", stringObjectType);
            fields.put("feature_word", stringObjectType);
            fields.put("kv_house_type", stringObjectType);
            fields.put("title", stringObjectType);
            fields.put("unit_price", numberObjectType);
            fields.put("baidu_la", numberObjectType);
            fields.put("baidu_lo", numberObjectType);
            fields.put("price", numberObjectType);
            fields.put("house_code", stringObjectType);
            fields.put("tags", stringObjectType);
            fields.put("url", stringObjectType);
            fields.put("districtpinying", stringObjectType);

            object.put("fields", fields);

            json=object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
