package com.jssg.servicemanagersystem.base.http

import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.base.http.interceptor.AuthInterceptor
import com.jssg.servicemanagersystem.base.http.interceptor.CookieInterceptor
import com.jssg.servicemanagersystem.core.AppApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Author shenhe
 * @create 2019-05-30 11:40
 */
object OkHttpClientFactory {
    const val DEFAULT_TIMEOUT: Long = 20000
    @JvmOverloads
    fun createOkHttpClient(
        hasCooking: Boolean,
        timeOut: Long,
        useOkHttpCache: Boolean = true,
        followRedirects: Boolean = true
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.retryOnConnectionFailure(true)
        httpClientBuilder.protocols(listOf(Protocol.HTTP_1_1))
        /***设置超时时间 */
        httpClientBuilder.connectTimeout(timeOut, TimeUnit.MILLISECONDS)
        httpClientBuilder.writeTimeout(timeOut, TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(timeOut, TimeUnit.MILLISECONDS)
        httpClientBuilder.callTimeout(timeOut, TimeUnit.MILLISECONDS)

        //设定公共的加密参数
        if (hasCooking) {
            httpClientBuilder.addInterceptor(CookieInterceptor())
            httpClientBuilder.addInterceptor(AuthInterceptor())
        }
        //        SslUtils.INSTANCE.sslConfig(httpClientBuilder);
        if (BuildConfig.DEBUG) {
            //log 拦截器
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        //设置缓存
        if (useOkHttpCache) {
            val httpCacheDirectory = File(AppApplication.get().cacheDir, "OkHttpCache")
            httpClientBuilder.cache(Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong()))
        }
        httpClientBuilder.followRedirects(followRedirects)
        return httpClientBuilder.build()
    }
}