package com.jssg.servicemanagersystem.ui.account.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/29.
 */
@Parcelize
data class UserInfo(
    val permissions: List<String>,
    val user: User,
) : Parcelable

@Parcelize
data class User(
    var address: String?,
    val admin: Boolean,
    val avatar: String,
    val createBy: String,
    val createTime: String,
    val dept: Dept?,
    val deptId: String?,
    val email: String,
    var idNo: String?,
    var nickName: String,
    var phonenumber: String,
    val remark: String?,
    val roleId: Long?,
    var roleIds: List<String>?,
    val sex: String,
    val status: String,
    val updateBy: String?,
    val updateTime: String?,
    val userId: Long,
    val userName: String,
    var expireDate: String
) : Parcelable

@Parcelize
data class Dept(
    val deptId: Long,
    val deptName: String,
) : Parcelable
