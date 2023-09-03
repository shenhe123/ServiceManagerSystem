package com.jssg.servicemanagersystem.ui.workorder.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOnsiteOptionsLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderCheckLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.DateUtil

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderCheckAdapter: BaseBindingAdapter<WorkOrderCheckInfo, ItemWorkOrderCheckLayoutBinding>(ItemWorkOrderCheckLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemWorkOrderCheckLayoutBinding>, item: WorkOrderCheckInfo) {
        holder.binding.tvOrderId.text = item.billNo

        holder.binding.tvPlace.text = item.place
        holder.binding.tvCheckDate.text = item.checkDate
        holder.binding.tvBadNum.text = item.badNum.toString()
        holder.binding.tvCheckNum.text = item.checkNum.toString()

        when(item.state) {
            0 -> holder.binding.tvOrderState.text = "未开始"
            1 -> holder.binding.tvOrderState.text = "排查中"
            2 -> holder.binding.tvOrderState.text = "已完成"
        }
    }

}