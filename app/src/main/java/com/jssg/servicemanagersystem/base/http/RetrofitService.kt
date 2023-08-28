package com.jssg.servicemanagersystem.base.http

import android.webkit.URLUtil
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.base.http.apiservice.ApiService
import com.jssg.servicemanagersystem.core.Constants
import com.jssg.servicemanagersystem.utils.LogUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays

/**
 * retrofit
 * Created by  on 2018/9/25.
 */
object RetrofitService {
    private var httpApi: ApiService? = null
    private var retrofit: Retrofit? = null
    private lateinit var currentClient: OkHttpClient


    fun init() {
        retrofit = createRetrofit()
        httpApi = createApiService(ApiService::class.java)
    }

    /**
     * 重启网络框架
     */
    fun recreateRetrofit() {
        retrofit = createRetrofit()
        httpApi = createApiService(ApiService::class.java)
    }

    fun <T> createApiService(tClass: Class<T>): T {
        if (retrofit == null) {
            retrofit = createRetrofit()
        }
        return retrofit!!.create(tClass)
    }

    fun createRetrofit(): Retrofit {
        evictAllConnection()
        currentClient = OkHttpClientFactory.createOkHttpClient(
            true,
            OkHttpClientFactory.DEFAULT_TIMEOUT
        )
        val url: String = if (BuildConfig.IS_TEST_HOST) Constants.Test_Host else Constants.Release_Host
        if (!URLUtil.isHttpsUrl(url)) {
            LogUtil.e(RetrofitService::class.simpleName, "http url  不合法$url".trimIndent())
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .client(currentClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val apiService: ApiService
        get() {
            if (httpApi == null) {
                httpApi = createApiService(ApiService::class.java)
            }
            return httpApi!!
        }

    /**
     * http1.1 支持 TCP 通道复用机制，http2.0 还支持了多路复用机制，所以有时候明明有网络，但是接口却返回 SocketTimeoutException，UnknownHostException，
     * 一般都是后台接口没有严格按照http1.1协议和http2.0协议来，导致服务器Socket关了，但是没有通知客户端，客户端下次请求，复用链路导致 SocketTimeoutException。
     * 三种思路解决：
     *
     *
     * 第一种：服务器端修改。
     *
     *
     * 第二种：客户端关闭连接池 OkHttpClient.connectionPool().evictAll()。
     *
     *
     * 第三种：客户端加重试机制，失败重新请求一次。推荐这种方式，失败重试可以解决很多其他网络偶然问题，比如快速切网的时候。
     */
    fun evictAllConnection() {
        if (this::currentClient.isInitialized ) {
            Thread { currentClient.connectionPool.evictAll() }.start()
        }
    }
}