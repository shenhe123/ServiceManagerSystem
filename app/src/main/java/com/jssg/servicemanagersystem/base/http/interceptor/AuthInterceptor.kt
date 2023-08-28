package com.jssg.servicemanagersystem.base.http.interceptor

import android.os.Build
import android.text.TextUtils
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.utils.LogUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/28.
 */
class AuthInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        builder.addHeader("content-type", "application/json;charset:utf-8")

        val auth = "Bearer ${AccountManager.instance.getAuthorization()}"

        if (!TextUtils.isEmpty(auth)) {
            builder.addHeader("Authorization", auth)
        }
        builder.addHeader("Agent-Type", Build.BRAND + "-" + Build.MODEL)
        builder.addHeader("ExchVer", BuildConfig.VERSION_NAME)
        builder.addHeader("Platform", "android")
        builder.addHeader("Connection", "keep-alive")

        if (TextUtils.isEmpty(UA)) {
            UA = "Exch/" + BuildConfig.VERSION_NAME + "(Android;" + Build.VERSION.RELEASE + ";" + Build.BRAND + "-" + Build.MODEL + ";" + Locale.getDefault()
                    .toLanguageTag() + ")"
        }

        builder.addHeader("User-Agent", UA)

        val cookieStr = AccountManager.instance.getCookie()
        builder.addHeader("Cookie", cookieStr ?: "")
        return if (BuildConfig.DEBUG) {
            try {
                chain.proceed(builder.build())
            } catch (e: Exception) {
                LogUtil.e("intercept", "error:${chain.request().url}$e".trimIndent())
                throw e
            }
        } else {
            chain.proceed(builder.build())
        }
    }

    companion object {
        var UA = ""
    }
}