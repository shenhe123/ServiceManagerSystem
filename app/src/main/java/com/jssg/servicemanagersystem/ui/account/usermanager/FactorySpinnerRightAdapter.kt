package com.jssg.servicemanagersystem.ui.account.usermanager

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemFactorySpinnerBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/14.
 */
class FactorySpinnerRightAdapter(isSingleCheck: Boolean): BaseBindingAdapter<WorkFactoryInfo, ItemFactorySpinnerBinding>(ItemFactorySpinnerBinding::inflate) {

    private var isSingleCheck: Boolean

    init {
        this.isSingleCheck = isSingleCheck
    }

    override fun convert(holder: VBViewHolder<ItemFactorySpinnerBinding>, item: WorkFactoryInfo) {
        holder.binding.tvName.text = item.orgShortName
        holder.binding.checkBox.isChecked = item.isChecked

        holder.binding.checkBox.isVisible = !isSingleCheck
    }

    override fun convert(
        holder: VBViewHolder<ItemFactorySpinnerBinding>,
        item: WorkFactoryInfo,
        payloads: List<Any>
    ) {
        for (payload in payloads) {
            if (payload is Int && payload == 1) {
                holder.binding.checkBox.isChecked = item.isChecked
            }
        }
    }
}