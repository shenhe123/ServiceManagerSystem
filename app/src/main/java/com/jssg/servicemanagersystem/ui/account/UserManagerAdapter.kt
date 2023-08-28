package com.jssg.servicemanagersystem.ui.account

import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemUserManagerLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class UserManagerAdapter: BaseBindingAdapter<String, ItemUserManagerLayoutBinding>(ItemUserManagerLayoutBinding::inflate) {

    init {
        addChildClickViewIds(R.id.card_layout, R.id.mbt_permission, R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemUserManagerLayoutBinding>, item: String) {
        holder.binding.tvId.text = "用户ID：1001"
        holder.binding.tvName.text = "用户名：用户001"
        holder.binding.tvPassword.text = "密码：123456"
        holder.binding.tvPhone.text = "13256789040"
    }

}