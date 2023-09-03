package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/3.
 */

@Parcelize
data class WorkDeptInfo(
    val ancestors: String,
    val createBy: String,
    val createTime: String,
    val delFlag: String,
    val deptId: Long,
    val deptName: String,
    val email: String,
    val leader: String,
    val orderNum: Int,
    val parentId: Long,
    val parentName: String,
    val phone: String,
    val status: String,
    val updateBy: String,
    val updateTime: String
) : Parcelable