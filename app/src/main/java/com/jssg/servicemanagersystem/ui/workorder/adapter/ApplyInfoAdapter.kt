package com.jssg.servicemanagersystem.ui.workorder.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemApplyInfoLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.ApplyInfoVos

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class ApplyInfoAdapter: BaseBindingAdapter<ApplyInfoVos, ItemApplyInfoLayoutBinding>(ItemApplyInfoLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemApplyInfoLayoutBinding>, item: ApplyInfoVos) {
//        holder.binding.tvApplyName.text = "${item.applyName}  ${item.createTime}"
        holder.binding.tvApplyInfo.text = "${item.remark}  ${item.applyName}  ${item.createTime}"
//        holder.binding.tvApplyResult.text = "审核结果：${item.remark}"
    }


}