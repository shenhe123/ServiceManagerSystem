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
    val roles: List<String>,
    val user: User,
) : Parcelable

@Parcelize
data class User(
    val address: String?,
    val admin: Boolean,
    val avatar: String,
    val createBy: String,
    val createTime: String,
    val delFlag: String,
    val dept: String?,
    val deptId: String?,
    val email: String,
    val idNo: String?,
    val loginDate: String,
    val loginIp: String,
    val nickName: String,
    val phonenumber: String,
    val postIds: String?,
    val remark: String,
    val roleId: String?,
    val roleIds: String?,
    val roles: List<Role>,
    val sex: String,
    val status: String,
    val updateBy: String?,
    val updateTime: String?,
    val userId: Int,
    val userName: String,
    val userType: String
) : Parcelable

@Parcelize
data class Role(
    val admin: Boolean,
    val attachToApp: String?,
    val createBy: String?,
    val createTime: String?,
    val dataScope: String,
    val delFlag: String?,
    val deptCheckStrictly: String?,
    val deptIds: String?,
    val flag: Boolean,
    val menuCheckStrictly: String?,
    val menuIds: String?,
    val remark: String?,
    val roleId: Int,
    val roleKey: String,
    val roleName: String,
    val roleSort: Int,
    val status: String,
    val updateBy: String?,
    val updateTime: String?
) : Parcelable
