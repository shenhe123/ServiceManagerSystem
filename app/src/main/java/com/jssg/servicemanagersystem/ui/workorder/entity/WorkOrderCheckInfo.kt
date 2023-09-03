package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/3.
 */

@Parcelize
data class WorkOrderCheckInfo(
    val approverName: String?,
    val badNum: Int,
    val badPicNames: String,
    val batchPicNames: String,
    val billDetailNo: String,
    val billNo: String,
    val boxPicNames: String,
    val checkDate: String,
    val checkNum: Int,
    val checkerName: String?,
    val place: String,
    val reworkPicNames: String,
    val state: Int,
    val workOrderVo: WorkOrderInfo?,
    val remark: String?,
) : Parcelable