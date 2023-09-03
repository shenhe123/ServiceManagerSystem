package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/2.
 */
@Parcelize
data class WorkOrderInfo(

       /**
        * {
       "billNo": "1695069839463202817",
       "applyName": "刘志远1",
       "applyDate": "2023-08-01 00:00:00",
       "applyDept": "软件开发部1",
       "orgService": "廊坊工厂1",
       "tel": "15431652431",
       "servicePeriod": "3月到6月1",
       "unitPrice": 101,
       "totalPrice": 20001,
       "salesManager": "刘志远1",
       "serviceAdd": "河北省廊坊",
       "productCode": "P00011",
       "productDes": "有瑕疵1",
       "productNum": 1001,
       "remark": "有个备注1",
       "needBorrow": "没有1",
       "borrowMoney": 1001,
       "payer": "廊坊工厂1",
       "payee": "张三1",
       "openingBank": "11313113131",
       "checkState": 1,
       "account": "101131311",
       "sourceBill": "sss1",
       "checkNumTotal": 0,
       "badNumTotal": 0,
       "state": 2
       }
        */
       val billNo: String,   // 工单号",
       val applyName: String,   // 申请人",
       val applyDate: String,   // 申请时间",
       val applyDept: String,   // 申请部门",
       val orgService: String,   // 服务工厂",
       val tel: String,   // 电话",
       val servicePeriod: String,   // 服务周期",
       val unitPrice: String,   // 0, // 服务单价
       val totalPrice: String,   // 0, // 服务总价
       val salesManager: String,    // 销售经理
       val serviceAdd: String,   	// 服务地址
       val productCode: String,    // 产品编号
       val productDes: String,    // 产品描述
       val productNum: String,   // 0, // 问题产品数量
       val remark: String,    // 备注
       val needBorrow: String?,    // 是否借款
       val borrowMoney: String?,   // 0, // 借款金额
       val payer: String?,    // 付款单位
       val payee: String?,    // 收款人
       val openingBank: String?,    // 开户行
       val account: String?,    // 开户账号
       val sourceBill: String?,    // 原清单
       val state: Int,   // 状态,0保存 1提交
       val checkState: Int,   // 排查状态
       val checkNumTotal: Int,   // 排查数量
       val badNumTotal: Int,   // 不良数量
) : Parcelable