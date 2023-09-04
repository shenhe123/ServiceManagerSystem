package com.jssg.servicemanagersystem.ui.travelreport.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/4.
 */
@Parcelize
data class TravelReportInfo(
    val billNo: String,   //1692528600129953793",
    val orgId: String,   //工厂name",
    val dept: String,   //部门",
    val applyName: String,   //申请人",
    val partner: String,   //同行人员",
    val customer: String,   //客户",
    val productCode: String,   //产品编码",
    val projectCode: String,   //项目编码",
    val placeFrom: String,   //从地点",
    val placeTo: String,   //到地点",
    val address: String,   //地址",
    val startDate: String,   //2023-08-30 00:00:00",
    val endDate: String,   //2023-08-24 00:00:00",
    val purpose: String,   //目的",
    val mainTask: String,   //主要任务",
    val expectedResult: String,   //逾期结果",
    val schedule: String,   //待办任务"
) : Parcelable
