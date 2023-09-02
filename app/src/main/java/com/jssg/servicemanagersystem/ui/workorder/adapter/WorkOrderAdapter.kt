package com.jssg.servicemanagersystem.ui.workorder.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOnsiteOptionsLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.DateUtil

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderAdapter: BaseBindingAdapter<WorkOrderInfo, ItemWorkOrderLayoutBinding>(ItemWorkOrderLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemWorkOrderLayoutBinding>, item: WorkOrderInfo) {
        holder.binding.tvOrderId.text = item.billNo

        holder.binding.tvApplyName.text = item.applyName
        holder.binding.tvApplyDept.text = item.applyDept
        holder.binding.tvApplyFactory.text = item.orgService
        holder.binding.tvApplyDate.text = item.applyDate
    }

}