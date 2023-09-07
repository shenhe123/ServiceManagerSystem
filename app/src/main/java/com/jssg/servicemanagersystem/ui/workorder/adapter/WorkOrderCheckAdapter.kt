package com.jssg.servicemanagersystem.ui.workorder.adapter

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOnsiteOptionsLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderCheckLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderCheckAdapter :
    BaseBindingAdapter<WorkOrderCheckInfo, ItemWorkOrderCheckLayoutBinding>(
        ItemWorkOrderCheckLayoutBinding::inflate
    ) {

    init {
        addChildClickViewIds(R.id.mbt_review)
    }

    override fun convert(
        holder: VBViewHolder<ItemWorkOrderCheckLayoutBinding>,
        item: WorkOrderCheckInfo
    ) {
        holder.binding.tvOrderId.text = item.billNo

        holder.binding.tvPlace.text = item.place
        holder.binding.tvCheckDate.text = item.checkDate
        holder.binding.tvBadNum.text = item.badNum.toString()
        holder.binding.tvCheckNum.text = item.checkNum.toString()

        when (item.state) {
            0 -> holder.binding.tvOrderState.text = "已保存"
            1 -> holder.binding.tvOrderState.text = "已提交"
            2 -> holder.binding.tvOrderState.text = "已审核"
            3 -> holder.binding.tvOrderState.text = "退文"
            4 -> holder.binding.tvOrderState.text = "不同意"
        }

        val canReview: Boolean = !(item.state == 0 || item.state == 2 || item.state == 4)
        holder.binding.groupReview.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKDERDETAIL_APPROVE.printableName)
                    && canReview
    }

}