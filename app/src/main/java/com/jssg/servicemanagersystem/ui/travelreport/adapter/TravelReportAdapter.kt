package com.jssg.servicemanagersystem.ui.travelreport.adapter

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ItemTravelReportLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class TravelReportAdapter: BaseBindingAdapter<TravelReportInfo, ItemTravelReportLayoutBinding>(ItemTravelReportLayoutBinding::inflate) {

    private var hasPermission: Boolean = false

    init {
        hasPermission =
            RolePermissionUtils.hasPermission(MenuEnum.QM_TRIPREPORT_EXPORT.printableName)

        addChildClickViewIds(R.id.mbt_export)
    }

    override fun convert(holder: VBViewHolder<ItemTravelReportLayoutBinding>, item: TravelReportInfo) {
        holder.binding.tvApplyName.text = item.applyName
        holder.binding.tvDept.text = item.dept
        holder.binding.tvCustomer.text = item.customer
        holder.binding.tvPlace.text = item.placeTo
        holder.binding.tvPurpose.text = item.purpose
        holder.binding.tvStartDate.text = item.startDate.split(" ")[0]
        holder.binding.tvEndDate.text = item.endDate.split(" ")[0]

        holder.binding.groupExport.isVisible = hasPermission
    }


}