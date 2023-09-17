package com.jssg.servicemanagersystem.ui.report.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderReportLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.BigDecimalUtils.bigDecimal

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class ReportAdapter(): BaseBindingAdapter<WorkOrderInfo, ItemWorkOrderReportLayoutBinding>(ItemWorkOrderReportLayoutBinding::inflate) {
    override fun convert(holder: VBViewHolder<ItemWorkOrderReportLayoutBinding>, item: WorkOrderInfo) {
        holder.binding.tvOrderId.text = item.billNo

        holder.binding.tvApplyName.text = item.applyName
        holder.binding.tvApplyDept.text = item.applyDept
        holder.binding.tvApplyFactory.text = item.sysOrganizationVo?.orgShortName ?: item.orgService
        holder.binding.tvApplyDate.text = item.applyDate
        holder.binding.tvProductDesc.text = item.productDes

//        when(item.state) {
//            0 -> holder.binding.tvOrderState.text = "已保存"
//            1 -> holder.binding.tvOrderState.text = "已提交"
//            2 -> holder.binding.tvOrderState.text = "已审核"
//            3 -> holder.binding.tvOrderState.text = "退回"
//            4 -> holder.binding.tvOrderState.text = "不同意"
//        }
        holder.binding.tvReviewNum.text = (item.waitCheckCount ?: 0).toString()

        holder.binding.tvWaitCheckNum.text = item.checkNum.bigDecimal().subtract(item.submitCheckCount.bigDecimal()).stripTrailingZeros().toPlainString()

        when(item.checkState) {
            0 -> holder.binding.tvCheckState.text = "未开始"
            1 -> holder.binding.tvCheckState.text = "排查中"
            2 -> holder.binding.tvCheckState.text = "已完成"
        }

    }
}