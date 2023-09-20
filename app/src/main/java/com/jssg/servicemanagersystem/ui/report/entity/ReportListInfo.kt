package com.jssg.servicemanagersystem.ui.report.entity

import android.os.Parcelable
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/17.
 */
@SmartTable(name = "报表预览")
@Parcelize
data class ReportListInfo(
    @SmartColumn(id = 1,name ="工单号")
    val billNo: String?,
    @SmartColumn(id = 2,name ="申请人")
    val applyName: String,
    @SmartColumn(id = 3,name ="申请日期")
    val applyDate: String,
    @SmartColumn(id = 4,name ="申请部门")
    val applyDept: String,
    @SmartColumn(id = 5,name ="申请工厂")
    val orgService: String,
    @SmartColumn(id = 6,name ="产品名称")
    val productDes: String,
    @SmartColumn(id = 7,name ="产品编码")
    val productCode: String,
    @SmartColumn(id = 8,name ="内容描述")
    val remark: String,
    @SmartColumn(id = 9,name ="服务人员名称")
    val checkerName: String?,
    @SmartColumn(id = 10,name ="排查数量")
    val checkNum: Int,
    @SmartColumn(id = 11,name ="服务单价")
    val unitPrice: Int,
    @SmartColumn(id = 12,name ="预估总费用")
    val totalPrice: Int,
    @SmartColumn(id = 13,name ="服务周期")
    val servicePeriod: String,
    @SmartColumn(id = 14,name ="服务地点")
    val serviceAdd: String?,
    @SmartColumn(id = 15,name ="排查开始日期")
    val checkBegin: String?,
    @SmartColumn(id = 16,name ="排查结束日期")
    val checkEnd: String?,
    @SmartColumn(id = 17,name ="工厂CQE工程师")
    val reviewer: String?,
//    @SmartColumn(id = 18,name ="审核意见")
//    val applyRemark: String?,
    @SmartColumn(id = 18,name ="排查数量总（至今）")
    val checkNumTotal: Int,
    @SmartColumn(id = 19,name ="不良数量总（至今）")
    val badNumTotal: Int,
    @SmartColumn(id = 20,name ="工单状态")
    var checkStateName: String?,
    val badNum: Int,



    val approverName: String?,
    val badPicNames: String,
    val batchPicNames: String,
    val boxPicNames: String,
    val checkDate: String,
    val checkState: Int,
    val index: Int?,
    val place: String,
    val productNum: Int,
    val reworkPicNames: String
) : Parcelable