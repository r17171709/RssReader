package com.renyu.rssreader.juejin

import com.google.gson.Gson
import com.renyu.commonlibrary.network.OKHttpHelper
import com.renyu.rssreader.bean.JuejinBean
import com.renyu.rssreader.js.regEx
import com.renyu.rssreader.params.Params
import org.json.JSONObject

fun main(args: Array<String>) {
    val okHttpHelper = OKHttpHelper.getInstance()
    for (page in 0..5) {
        val url = "https://timeline-merger-ms.juejin.im/v1/get_tag_entry?" +
                "src=web&" +
                "uid=57720a530a2b5800581d5a49&" +
                "device_id=${Params.device_id}&" +
                "token=${Params.juejinToken}&" +
                "tagId=56c40b1d2e958a0059a1b719&" +
                "page=$page&" +
                "pageSize=20&" +
                "sort=createdAt"
        val response = okHttpHelper.okHttpUtils.syncGet(url).body().string()
        val beans = Gson().fromJson<JuejinBean>(response, JuejinBean::class.java)
        beans.d.entrylist.forEach {
            println(it.title)
            val username = when {
                it.user.username != null -> it.user.username
                it.user.community != null -> when {
                    it.user.community.github != null -> it.user.community.github.username
                    it.user.community.weibo != null -> it.user.community.weibo.username
                    else -> ""
                }
                else -> ""
            }
            if (!checkExists(it.title)) {
                update(it.title, it.user.avatarLarge, username, it.originalUrl, it.screenshot, it.summaryInfo)
            }
        }
    }

}

private fun checkExists(title: String) : Boolean {
    val okHttpHelper = OKHttpHelper.getInstance()
    val jsonObject = JSONObject()
    jsonObject.put("temp", title.replace(Regex(regEx),""))
    val value = okHttpHelper.okHttpUtils.syncGet(Params.baseUrl + "classes/Kotlin?where="+jsonObject.toString(), Params.head()).body().string()
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

private fun update(title: String, avatarLarge: String?, username: String, originalUrl: String, screenshot: String?, summaryInfo: String) {
    val okHttpHelper = OKHttpHelper.getInstance()
    val jsonObject = JSONObject()
    jsonObject.put("title", title)
    jsonObject.put("temp", title.replace(Regex(regEx),""))
    jsonObject.put("avatarLarge", avatarLarge?:"")
    jsonObject.put("username", username)
    jsonObject.put("originalUrl", originalUrl)
    jsonObject.put("screenshot", screenshot?:"")
    jsonObject.put("summaryInfo", summaryInfo)
    okHttpHelper.okHttpUtils.syncPostJson(Params.baseUrl + "classes/Kotlin", jsonObject.toString(), Params.head())
}