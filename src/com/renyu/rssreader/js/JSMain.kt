package com.renyu.rssreader.js

import com.renyu.rssreader.bean.WXBean
import com.renyu.rssreader.utils.HttpUtils
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {

    val urlList: Array<String> = arrayOf("d1591c322c89", "5139d555c94d", "58b4c20abf2f", "3fde3b545a35", "ddfd0f9bb992", "38d96caffb2f", "7d70f1739deb", "d79a6385bded", "35da7ac56db8", "98aaef9f5d2f")

    val uploadService = Executors.newFixedThreadPool(1)

    val scheduledExecutorService = Executors.newScheduledThreadPool(1)
    scheduledExecutorService.scheduleAtFixedRate({
        println("开始")
        for (url in urlList) {
            var avatars: MutableList<String> = MutableList(0) {""}
            var names: MutableList<String> = MutableList(0) {""}
            var textUrls: MutableList<String> = MutableList(0) {""}
            var titles: MutableList<String> = MutableList(0) {""}

            val newUrl = "http://www.jianshu.com/c/"+url
            val doc: Document = Jsoup.connect(newUrl).get()
            val note_list_element: Elements = doc.getElementsByClass("note-list")
            val note_list_document: Document = Jsoup.parse(note_list_element.toString())
            val author_element: Elements = note_list_document.getElementsByClass("author")
            for (element in author_element) {
                val author_document: Document = Jsoup.parse(element.toString())
                val avatar: String = "http:"+author_document.getElementsByTag("img").attr("src")
                avatars.add(avatar)
                val blue_link_element: Elements = author_document.getElementsByClass("blue-link")
                val blue_link_document: Document = Jsoup.parse(blue_link_element.toString())
                val name = (blue_link_document.childNodes()[0] as Element).text()
                names.add(name)
            }
            val title_element: Elements = note_list_document.getElementsByClass("title")
            for (element in title_element) {
                val title_document: Document = Jsoup.parse(element.toString())
                val textUrl = "http://www.jianshu.com"+title_document.getElementsByTag("a").attr("href")
                textUrls.add(textUrl)
                val title = (title_document.childNodes()[0] as Element).text()
                titles.add(title)
            }
            for (i in 0..9) {
                var listBean: WXBean.ListBean = WXBean.ListBean()
                listBean.app_msg_ext_info = WXBean.ListBean.AppMsgExtInfoBean()
                listBean.app_msg_ext_info.title = titles[i]
                listBean.app_msg_ext_info.author = names[i]
                listBean.app_msg_ext_info.source_url = textUrls[i]
                listBean.app_msg_ext_info.cover = avatars[i]
                uploadService.execute {
                    if (!checkExists(listBean)) {
                        update(listBean)
                    }
                }
            }
        }
        println("完成")
    }, 2, 60*60, TimeUnit.SECONDS)

//    checkExists2()
}

//fun checkExists2() {
//    val head = HashMap<String, String>()
//    head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0")
//    head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8")
//
//    val params = "bql=select * from JianShu where title = 'Android - 网络请求之 Retrofit2 + RxJava'"
//    val uploadResult= HttpUtils.getIntance().get("https://api.bmob.cn/1/cloudQuery?"+params, head)
//
//    println(uploadResult)
//}

fun checkExists(bean: WXBean.ListBean) : Boolean {
    val head = HashMap<String, String>()
    head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0")
    head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8")

    val jsonObject = JSONObject()
    jsonObject.put("title", bean.app_msg_ext_info.title)
    val value = HttpUtils.getIntance().get("https://api.bmob.cn/1/classes/JianShu?where="+jsonObject.toString(), head)
    var isExists=false
    if (value==null) {
        isExists=true
    }
    else {
        val jsonObjectValue = JSONObject(value)
        if (jsonObjectValue.getJSONArray("results").length()>0) {
            isExists=true
        }
    }
    return isExists
}

fun update(bean: WXBean.ListBean) {
    val head = HashMap<String, String>()
    head.put("X-Bmob-Application-Id", "43199c324d3bcb01bacdbd0914277ef0")
    head.put("X-Bmob-REST-API-Key", "d4ac4f967651b0a0053a9d3c45c3efa8")
    val jsonObject = JSONObject()
    jsonObject.put("title", bean.app_msg_ext_info.title)
    jsonObject.put("link", bean.app_msg_ext_info.source_url )
    jsonObject.put("cover", bean.app_msg_ext_info.cover)
    jsonObject.put("author", bean.app_msg_ext_info.author)
    val uploadResult: String? = HttpUtils.getIntance().post("https://api.bmob.cn/1/classes/JianShu", head, jsonObject.toString())
    println(uploadResult)
}