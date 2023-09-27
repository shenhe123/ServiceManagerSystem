package com.jssg.servicemanagersystem.base.loadmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel

/**
 * Created by shenhe on 2020/7/6.
 *
 * Application context aware [ViewModel].
 *
 *
 * Subclasses must have a constructor which accepts [Application] as the only parameter.
 *
 *
 */
open class AndroidViewModel(@field:SuppressLint("StaticFieldLeak") private val mApplication: Application) :
    ViewModel() {
    /**
     * Return the application.
     */
    fun <T : Application?> getApplication(): T {
        return mApplication as T
    }
}