package com.jssg.servicemanagersystem.ui.onsite

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOnsiteOptionsLayoutBinding
import com.jssg.servicemanagersystem.utils.DateUtil

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class OnsiteOptionsAdapter: BaseBindingAdapter<String, ItemOnsiteOptionsLayoutBinding>(ItemOnsiteOptionsLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemOnsiteOptionsLayoutBinding>, item: String) {
        holder.binding.tvApplyName.text = "申请人：shenhe"
        holder.binding.tvApplyDept.text = "申请部门：销售部"
        holder.binding.tvApplyFactory.text = "申请工厂：梦工厂"
        holder.binding.tvApplyDate.text = DateUtil.getDateyyMMdd(System.currentTimeMillis())
    }

}