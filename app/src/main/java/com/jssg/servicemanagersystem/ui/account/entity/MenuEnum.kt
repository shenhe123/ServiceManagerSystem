package com.jssg.servicemanagersystem.ui.account.entity

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/31.
 */
enum class MenuEnum(name: String) {
    LoginInfor_query("monitor:logininfor:query"), //登录日志查询
    User_query("system:user_query"),  //用户查询
    User_add("system:user_add"),  //用户新增
    User_edit("system:user_edit"),  //用户修改
    User_remove("system:user_remove"),  //用户删除
    User_resetPwd("system:user_resetPwd"),  //重置密码
    TripReport_add("qm:TripReport:add"), //出差报告新增
    TripReport_edit("qm:TripReport:edit"), //出差报告修改
    TripReport_query ("qm:TripReport:query"), //出差报告查询
    TripReport_remove ("qm:TripReport:remove"), //出差报告删除
    WorkOrder_add ("qm:WorkOrder:add"), //工单新增
    WorkOrder_approve ("qm:WorkOrder:approve"), //工单审核
    WorkOrder_edit ("qm:WorkOrder:edit"), //工单修改
    WorkOrder_export ("qm:WorkOrder:export"), //工单导出
    WorkOrder_query ("qm:WorkOrder:query"), //工单查询
    WorkOrder_remove ("qm:WorkOrder:remove"), //工单删除
    WorkderDetail_approve ("qm:WorkderDetail:approve"), //派工单明细审核
    WorkOrderDetail_add ("qm:WorkOrderDetail:add"), //派工单明细新增
    WorkOrderDetail_edit ("qm:WorkOrderDetail:edit"), //派工单明细修改
    WorkOrderDetail_export ("qm:WorkOrderDetail:export"), //派工单明细导出
    WorkOrderDetail_query ("qm:WorkOrderDetail:query"), //派工单明细查询
    WorkOrderDetail_remove ("qm:WorkOrderDetail:remove"), //派工单明细删除
    WorkOrderLog_add ("qm:WorkOrderLog:add"), //派工单变更日志新增
    WorkOrderLog_edit ("qm:WorkOrderLog:edit"), //派工单变更日志修改
//    WorkOrderLog_export ("qm:WorkOrderLog:export"), //派工单变更日志导出
    WorkOrderLog_query ("qm:WorkOrderLog:query"), //派工单变更日志查询
    WorkOrderLog_remov("qm:WorkOrderLog:remove"), //派工单变更日志删除
}