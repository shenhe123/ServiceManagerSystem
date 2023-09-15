package com.jssg.servicemanagersystem.base.download

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/15.
 */
object UrlUtils {

    /**
     * 从url分割出BaseUrl
     */
    fun getBaseUrl(url: String): String {
        var mutableUrl = url
        var head = ""
        var index = mutableUrl.indexOf("://")
        if (index != -1) {
            head = mutableUrl.substring(0, index + 3)
            mutableUrl = mutableUrl.substring(index + 3)
        }
        index = mutableUrl.indexOf("/")
        if (index != -1) {
            mutableUrl = mutableUrl.substring(0, index + 1)
        }
        return head + mutableUrl
    }
}