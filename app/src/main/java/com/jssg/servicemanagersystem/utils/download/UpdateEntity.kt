package com.jssg.servicemanagersystem.utils.download

import android.os.Parcelable
import com.jssg.servicemanagersystem.BuildConfig
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/9.
 */

@Parcelize
data class UpdateEntity(
    val version: String,
    val versionCode: String,
    val downloadUrl: String,
    val force: Boolean,
    val updateInfo: String,
    var apkLocalPath: String,
) : Parcelable {
    fun hasUpdate(): Boolean {
        try {
            val nowVerCode: Int = BuildConfig.VERSION_NAME.replace(".", "").toInt()
            val serveVersion: Int = versionCode.toInt()
            return serveVersion > nowVerCode
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    val versionCodeStr: String
        get() = versionCode.toCharArray().joinToString(".")

}
