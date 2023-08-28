package com.jssg.servicemanagersystem.base.http.interceptor

import android.text.TextUtils
import com.jssg.servicemanagersystem.core.AccountManager.Companion.instance
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/28.
 */
class CookieInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val resp = chain.proceed(request)
        val cookies = resp.headers("Set-Cookie")

        if (cookies.isNotEmpty()) {
            var cookieStr: String? = ""
            for (header in cookies) {
                if (header.contains("authtoken")) {
                    cookieStr = header
                }
            }
            if (!TextUtils.isEmpty(cookieStr)) {
                instance.saveCookie(cookieStr!!)
            }
        }
        return resp
    }
}