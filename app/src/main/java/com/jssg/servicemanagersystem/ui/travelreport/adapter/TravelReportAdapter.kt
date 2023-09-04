package com.jssg.servicemanagersystem.ui.travelreport.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemTravelReportLayoutBinding
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class TravelReportAdapter: BaseBindingAdapter<TravelReportInfo, ItemTravelReportLayoutBinding>(ItemTravelReportLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemTravelReportLayoutBinding>, item: TravelReportInfo) {
        holder.binding.tvApplyName.text = item.applyName
        holder.binding.tvDept.text = item.dept
        holder.binding.tvCustomer.text = item.customer
        holder.binding.tvPlace.text = "${item.placeFrom} - ${item.placeTo}"
        holder.binding.tvPurpose.text = item.purpose
        holder.binding.tvStartDate.text = item.startDate
        holder.binding.tvEndDate.text = item.endDate

    }


}