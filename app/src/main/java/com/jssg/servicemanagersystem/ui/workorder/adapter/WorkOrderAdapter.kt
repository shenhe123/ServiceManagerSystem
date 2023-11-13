package com.jssg.servicemanagersystem.ui.workorder.adapter

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemWorkOrderLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.BigDecimalUtils.bigDecimal
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.Utils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class WorkOrderAdapter(isCloseCase: Boolean): BaseBindingAdapter<WorkOrderInfo, ItemWorkOrderLayoutBinding>(ItemWorkOrderLayoutBinding::inflate) {

    var isCloseCase: Boolean

    init {
        this.isCloseCase = isCloseCase
        addChildClickViewIds(R.id.mcb_check, R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemWorkOrderLayoutBinding>, item: WorkOrderInfo) {
        val orderId: String = item.oaBillNo?.ifEmpty { item.billNo } ?: item.billNo
        holder.binding.tvOrderId.text = orderId

        holder.binding.tvOrderId.setOnLongClickListener {
            Utils.copyStringText(orderId, context)
            ToastUtils.showToast("复制成功")
            true
        }

        holder.binding.tvApplyName.text = item.applyName
        holder.binding.tvApplyDept.text = item.applyDept
        holder.binding.tvApplyFactory.text = item.sysOrganizationVo?.orgShortName ?: item.orgService
        holder.binding.tvApplyDate.text = item.applyDate?.split(" ")?.get(0) ?: ""
        holder.binding.tvProductDesc.text = item.productDes
        holder.binding.tvProjectCode.text = item.projectCode ?: ""

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
            3 -> holder.binding.tvCheckState.text = "已手工提单"
        }

        holder.binding.mcbCheck.isVisible = isCloseCase && item.checkState < 2

        val hasPermission =
            RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_REMOVE.printableName)
        val notOa = "oa" != item.createBy
        holder.binding.groupDelete.isVisible = hasPermission && item.checkState == 0 && notOa
    }


}