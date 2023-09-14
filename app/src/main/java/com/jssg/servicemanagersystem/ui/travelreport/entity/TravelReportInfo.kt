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
    var orgId: String?,   //工厂id",
    var orgName: String?,   //工厂name",
    var deptId: String?,   //部门id",
    var dept: String?,   //部门",
    var applyName: String,   //申请人",
    var partner: String,   //同行人员",
    var customer: String,   //客户",
    var productCode: String,   //产品编码",
    var projectCode: String,   //项目编码",
//    var placeFrom: String? = null,   //从地点",
    var placeTo: String,   //到地点",
    var address: String,   //地址",
    var startDate: String,   //2023-08-30 00:00:00",
    var endDate: String,   //2023-08-24 00:00:00",
    var purpose: String,   //目的",
    var mainTask: String,   //主要任务",
    var expectedResult: String,   //逾期结果",
    var schedule: String,   //待办任务"
) : Parcelable
