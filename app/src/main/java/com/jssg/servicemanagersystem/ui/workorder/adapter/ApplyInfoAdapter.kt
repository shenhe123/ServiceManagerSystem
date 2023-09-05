package com.jssg.servicemanagersystem.ui.workorder.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemApplyInfoLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.ApplyInfoVos
import com.jssg.servicemanagersystem.utils.DateUtil

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class ApplyInfoAdapter: BaseBindingAdapter<ApplyInfoVos, ItemApplyInfoLayoutBinding>(ItemApplyInfoLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemApplyInfoLayoutBinding>, item: ApplyInfoVos) {
//        holder.binding.tvApplyName.text = "${item.applyID} ${item.applyID}"
        holder.binding.tvApplyName.text = "审核人 ${DateUtil.getFullData(System.currentTimeMillis())}"
        holder.binding.tvApplyInfo.text = item.remark
    }


}