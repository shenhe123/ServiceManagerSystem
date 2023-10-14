package com.jssg.servicemanagersystem.core

import com.jssg.servicemanagersystem.BuildConfig

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/28.
 */
object Constants {
    val Test_Host = "http://qm.tiaodou21.cn/"
    val Release_Host = "http://123.249.25.5:19888/"
    val hostArray =
        if (BuildConfig.IS_TEST_HOST) arrayOf(Test_Host) else arrayOf(Release_Host)
}