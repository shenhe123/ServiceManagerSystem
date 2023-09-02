package com.jssg.servicemanagersystem.ui.account.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/31.
 */
@Parcelize
data class Role(
    val admin: Boolean,
    val attachToApp: String,
    val deptIds: List<Long>,
    var menuIds: List<Long>,
    val roleId: String,
    val roleKey: String,
    val roleName: String,
    val roleSort: Int,
    val remark: String?
) : Parcelable