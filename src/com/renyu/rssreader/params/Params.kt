package com.renyu.rssreader.params

import java.util.HashMap

object Params {
    @JvmStatic
    fun head() : HashMap<String, String> {
        val head = HashMap<String, String>()
        head["X-Bmob-Application-Id"] = "36ff001c70ee14091e08bf9dac74f365"
        head["X-Bmob-REST-API-Key"] = "a2dbed09c1f79a8344d36cb21429d1e0"
        return head
    }

    @JvmStatic
    val baseUrl = "https://api2.bmob.cn/1/"
}