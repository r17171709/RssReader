package com.renyu.rssreader.js

import com.renyu.rssreader.params.Params
import com.renyu.rssreader.bean.WXBean
import com.renyu.rssreader.utils.HttpUtils
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {

    //      "98aaef9f5d2f"  "3fde3b545a35"
    val urlList: Array<String> = arrayOf("0659acfdce32","ebc9d2e84214", "38d96caffb2f", "7d70f1739deb", "NEt52a", "58b4c20abf2f", "d1591c322c89", "5139d555c94d", "ddfd0f9bb992", "0dc880a2c73c", "383594e9265f")

    val scheduledExecutorService = Executors.newScheduledThreadPool(1)
    scheduledExecutorService.scheduleAtFixedRate({
        println("开始")
        for (url in urlList) {
            val names: MutableList<String> = MutableList(0) {""}
            val textUrls: MutableList<String> = MutableList(0) {""}
            val titles: MutableList<String> = MutableList(0) {""}

            val newUrl = "http://www.jianshu.com/c/$url"
            val doc: Document = Jsoup.connect(newUrl).get()
            val note_list_element: Elements = doc.getElementsByClass("note-list")
            val note_list_document: Document = Jsoup.parse(note_list_element.toString())
            val content_element: Elements = note_list_document.getElementsByClass("content")
            for (element in content_element) {
                val title_document: Document = Jsoup.parse(element.toString())
                val name = title_document.getElementsByClass("nickname").text()
                names.add(name)
                val textUrl = "http://www.jianshu.com"+title_document.getElementsByClass("title").attr("href")
                textUrls.add(textUrl)
                val title = title_document.getElementsByClass("title").text()
                titles.add(title)
            }
            for (i in 0..9) {
                val listBean: WXBean.ListBean = WXBean.ListBean()
                listBean.app_msg_ext_info = WXBean.ListBean.AppMsgExtInfoBean()
                listBean.app_msg_ext_info.title = titles[i]
                listBean.app_msg_ext_info.author = names[i]
                listBean.app_msg_ext_info.source_url = textUrls[i]
                if (!checkExists(listBean)) {
                    update(listBean)
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

private fun checkExists(bean: WXBean.ListBean) : Boolean {
    val jsonObject = JSONObject()
    jsonObject.put("title", bean.app_msg_ext_info.title)
    val value = HttpUtils.getIntance().get(Params.baseUrl + "classes/JianShu?where="+jsonObject.toString(), Params.head())
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
    Thread.sleep(2000)
    return isExists
}

private fun update(bean: WXBean.ListBean) {
    val jsonObject = JSONObject()
    jsonObject.put("title", bean.app_msg_ext_info.title)
    jsonObject.put("link", bean.app_msg_ext_info.source_url )
    jsonObject.put("author", bean.app_msg_ext_info.author)
    val uploadResult: String? = HttpUtils.getIntance().post(Params.baseUrl + "classes/JianShu", Params.head(), jsonObject.toString())
    println(uploadResult)
    Thread.sleep(2000)
}