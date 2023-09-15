package com.jssg.servicemanagersystem.ui.account.entity

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/31.
 */
enum class MenuEnum(val printableName: String) {
    SYSTEM_USER_QUERY("system:user:query"), //用户查询
    SYSTEM_USER_ADD("system:user:add"), //    用户新增
    SYSTEM_USER_EDIT("system:user:edit"), //用户修改
    SYSTEM_USER_REMOVE("system:user:remove"), //用户删除
    SYSTEM_USER_RESETPWD("system:user:resetPwd"), //重置密码
    MONITOR_LOGININFOR_QUERY("monitor:logininfor:query"), //登录日志查询
    QM_TRIPREPORT_QUERY("qm:TripReport:query"), //出差报告查询
    QM_TRIPREPORT_ADD("qm:TripReport:add"), //出差报告新增
    QM_TRIPREPORT_EDIT("qm:TripReport:edit"), //出差报告修改
    QM_TRIPREPORT_REMOVE("qm:TripReport:remove"), //出差报告删除
    QM_TRIPREPORT_EXPORT("qm:TripReport:export"), //出差报告导出
    QM_WORKORDER_QUERY("qm:WorkOrder:query"), //工单查询
    QM_WORKORDER_ADD("qm:WorkOrder:add"), //工单新增
    QM_WORKORDER_EDIT("qm:WorkOrder:edit"), //工单修改
    QM_WORKORDER_REMOVE("qm:WorkOrder:remove"), //工单删除
    QM_WORKORDER_APPROVE("qm:WorkOrder:approve"), //工单审核
    QM_WORKORDER_FINISH("qm:WorkOrder:finish"), //工单结案
    QM_WORKDERDETAIL_APPROVE("qm:WorkOrderDetail:approve"), //派工单明细审核
    QM_WORKORDERDETAIL_QUERY("qm:WorkOrderDetail:query"), //派工单明细查询
    QM_WORKORDERDETAIL_ADD("qm:WorkOrderDetail:add"), //派工单明细新增
    QM_WORKORDERDETAIL_EDIT("qm:WorkOrderDetail:edit"), //派工单明细修改
    QM_WORKORDERDETAIL_REMOVE("qm:WorkOrderDetail:remove"), //派工单明细删除
    QM_WORKDERDETAIL_REPORT("qm:WorkOrderDetail:report"), //派工单明细报表
    QM_WORKORDERLOG_QUERY("qm:WorkOrderLog:query"), //派工单变更日志查询
    QM_WORKORDERLOG_ADD("qm:WorkOrderLog:add"), //派工单变更日志新增
    QM_WORKORDERLOG_EDIT("qm:WorkOrderLog:edit"), //派工单变更日志修改
    QM_WORKORDERLOG_REMOVE("qm:WorkOrderLog:remove"), //派工单变更日志删除
    SYSTEM_ROLE_QUERY("system:role:query"), //角色管理 查询
    SYSTEM_ROLE_ADD("system:role:add"), //角色管理 新增
    SYSTEM_ROLE_EDIT("system:role:edit"), //角色管理 编辑
    SYSTEM_ROLE_REMOVE("system:role:remove"), //角色管理 删除
}