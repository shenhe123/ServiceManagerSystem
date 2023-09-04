package com.jssg.servicemanagersystem.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.baidu.location.LocationClient
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.utils.LogUtil
import com.jssg.servicemanagersystem.utils.toast.ToastTool
import com.jssg.servicemanagersystem.utils.toast.style.BitMaxStyle
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import io.reactivex.plugins.RxJavaPlugins
import java.util.LinkedList
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.base.refresh.RefreshLayoutManager
import java.lang.IllegalStateException

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AppApplication: BaseCoreApplication() {

    var isRunInBackground = false
    private var appCount = 0
    private val activityList = LinkedList<Activity>()

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        RetrofitService.init()

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

        ToastTool.init(this, BitMaxStyle(false))

        try {
            MMKV.initialize(this)
        } catch (e: UnsatisfiedLinkError) {
            //在一些低端机型会有小概率问题 isuse:02c72e5de1d6971970aa87d61ab0ab71
        }

        CrashReport.initCrashReport(applicationContext, "6ade02869d", BuildConfig.DEBUG);

        RefreshLayoutManager.init()

        lookHomeUI()
    }

    private fun lookHomeUI() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityList.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                appCount++

                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                appCount--
                if (appCount == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                activityList.remove(activity)
            }
        })
    }

    /**
     * 从后台回到前台 或者第一次进入app都会进入改方法
     */
    private fun back2App(activity: Activity) {
        isRunInBackground = false
    }

    /**
     * 离开应用 压入后台或者退出应用
     */
    private fun leaveApp() {
        isRunInBackground = true
    }

    fun getLastActivity(): Activity {
        return activityList.last
    }

    companion object {
        fun get(): AppApplication {
            return _instance as AppApplication
        }
    }
}