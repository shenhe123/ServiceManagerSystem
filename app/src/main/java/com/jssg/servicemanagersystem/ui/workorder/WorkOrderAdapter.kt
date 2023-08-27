package com.jssg.servicemanagersystem.ui.workorder

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOnsiteOptionsLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.utils.DateUtil

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderAdapter: BaseBindingAdapter<String, ItemWorkOrderLayoutBinding>(ItemWorkOrderLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemWorkOrderLayoutBinding>, item: String) {
        holder.binding.tvOrderId.text = "工单号：编号101"
        holder.binding.tvApplyName.text = "申请人：客户A"
        holder.binding.tvApplyDept.text = "申请部门：武装部"
        holder.binding.tvApplyFactory.text = "申请工厂：工厂B"
        holder.binding.tvApplyDate.text = DateUtil.getFullData(System.currentTimeMillis())
    }

}