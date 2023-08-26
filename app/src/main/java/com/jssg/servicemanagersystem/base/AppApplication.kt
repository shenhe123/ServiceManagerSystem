package com.jssg.servicemanagersystem.base

import android.app.Application
import com.baidu.location.LocationClient
import com.bumptech.glide.Glide
import com.jssg.servicemanagersystem.utils.LogUtil
import io.reactivex.plugins.RxJavaPlugins

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    private fun init() {
        //RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            //fix isuse:a9c0574286c01bac6c02bc99704a62e0
            LogUtil.e(
                "RxJava_ErrorHandler",
                "rxJavaPlugins_" + throwable.message
            )
        }

        //setAgreePrivacy接口需要在LocationClient实例化之前调用
        //如果setAgreePrivacy接口参数设置为了false，则定位功能不会实现
        //true，表示用户同意隐私合规政策
        //false，表示用户不同意隐私合规政策
        LocationClient.setAgreePrivacy(true)

    }
}