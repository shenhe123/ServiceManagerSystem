package com.jssg.servicemanagersystem.ui.workorder.adapter

import android.view.Menu
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.core.AccountManager
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
        addChildClickViewIds(R.id.mbt_review, R.id.mbt_delete)
    }

    override fun convert(
        holder: VBViewHolder<ItemWorkOrderCheckLayoutBinding>,
        item: WorkOrderCheckInfo
    ) {
        holder.binding.tvOrderId.text = item.billNo
        holder.binding.tvBatchNo.text = item.batchNo ?: ""
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

        //只有提交状态，才可以审核
        val canReview: Boolean = item.state == 1
        holder.binding.mbtReview.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKDERDETAIL_APPROVE.printableName)
                    && canReview

        //排查工单删除
        // 1、自己单子&&保存状态
        // 2、非三方人员角色 并且有删除权限的 并且状态是保存和退文
        val deletePermission =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_REMOVE.printableName)

        val isMyOrder =
            deletePermission && AccountManager.instance.getUser()?.user?.userName.equals(item.createBy) && item.state == 0

        val canDeleteOrder =
            deletePermission && !AccountManager.instance.getUser()?.user?.userType.equals("end_user") && (item.state == 0 || item.state == 3)

        holder.binding.mbtDelete.isVisible = canDeleteOrder || isMyOrder

        //底部线
        holder.binding.vLineReview.isVisible =
            holder.binding.mbtReview.isVisible || holder.binding.mbtDelete.isVisible
    }

}