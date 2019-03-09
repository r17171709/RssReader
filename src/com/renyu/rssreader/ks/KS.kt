package com.renyu.rssreader.ks

import com.renyu.commonlibrary.network.OKHttpHelper
import org.jsoup.Jsoup
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close
import java.io.*


fun main(args: Array<String>) {
    var page = 1
    var end = false
    var conut = 1
    while (!end) {
        page++
        val value = parse1("https://ksfan.net/story/shen-qi-tu-shu-guan-di-er-bu/?page=$page")
        if (value.isNullOrEmpty()) {
            end = true
        } else {
            val dir = "D://1//"
            val file = OKHttpHelper.getInstance().okHttpUtils.syncDownload(value, dir)
            val fileCount = File(dir, "$conut.mp3")
            fileCount.createNewFile()
            copy(file, fileCount)
            file.delete()
            conut++
            println(value)
        }
    }
}

private fun parse1(url: String): String? {
    val rootDoc = Jsoup.connect(url).get()
    val pageBody = Jsoup.parse(rootDoc.getElementsByClass("page-body").toString())
    val player = Jsoup.parse(pageBody.getElementsByClass("player").toString())
    val script = player.getElementsByTag("script")
    script.forEach {
        val data = it.data().toString().split("var")
        if (data[3].contains("//")) {
            return "http://www.${data[3].split("//")[1].split("'")[0]}"
        }
    }
    return null
}

private fun copy(source: File, dest: File) {
    var input: InputStream? = null
    var output: OutputStream? = null
    try {
        input = FileInputStream(source)
        output = FileOutputStream(dest)
        val buf = ByteArray(1024)
        var length = input.read(buf)
        while (length > 0) {
            output.write(buf, 0, length)
            length = input.read(buf)
        }
    } finally {
        input?.close()
        output?.close()
    }
}