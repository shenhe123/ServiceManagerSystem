package com.jssg.servicemanagersystem.ui.report.adapter

import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.BigDecimalUtils.bigDecimal

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderReportAdapter(isAll: Boolean): BaseBindingAdapter<WorkOrderInfo, ItemWorkOrderLayoutBinding>(ItemWorkOrderLayoutBinding::inflate) {

    var isAll: Boolean

    init {
        this.isAll = isAll
        addChildClickViewIds(R.id.mcb_check)
    }

    override fun convert(holder: VBViewHolder<ItemWorkOrderLayoutBinding>, item: WorkOrderInfo) {
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

        holder.binding.mcbCheck.isChecked = isAll || item.isChecked
    }

    override fun convert(
        holder: VBViewHolder<ItemWorkOrderLayoutBinding>,
        item: WorkOrderInfo,
        payloads: List<Any>
    ) {
        for (payload in payloads) {
            if (payload is Int && payload == 1) {
                holder.binding.mcbCheck.isChecked = isAll || item.isChecked
            }
        }
    }

    fun toggleCheckedState() {
        isAll = !isAll
        notifyItemRangeChanged(0, data.size - 1, 1)
    }
}