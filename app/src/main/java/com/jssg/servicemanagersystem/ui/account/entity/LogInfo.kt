package com.jssg.servicemanagersystem.ui.account.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/16.
 */

@Parcelize
data class LogInfo(
    val browser: String,
    val infoId: String,
    val ipaddr: String,
    val loginLocation: String,
    val loginTime: String,
    val msg: String,
    val os: String,
    val params: Params,
    val status: String,
    val userName: String
) : Parcelable

@Parcelize
class Params : Parcelable