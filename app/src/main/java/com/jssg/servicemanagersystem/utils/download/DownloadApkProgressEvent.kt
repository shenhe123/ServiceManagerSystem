package com.jssg.servicemanagersystem.utils.download

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/9.
 */
@Parcelize
data class DownloadApkProgressEvent(
    val current: Int,
    val total: Int,
    var status: Int
) : Parcelable {
    fun getProgress():Int{
        if (total<=0)return 1
        return current * 100 / total
    }

    override fun toString(): String {
        return "DownloadApkProgressEvent(current=$current, total=$total, status=$status)"
    }
}
