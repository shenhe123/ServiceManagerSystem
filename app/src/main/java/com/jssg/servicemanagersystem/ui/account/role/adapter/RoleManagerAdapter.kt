package com.jssg.servicemanagersystem.ui.account.role.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemRoleManagerLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class RoleManagerAdapter: BaseBindingAdapter<Role, ItemRoleManagerLayoutBinding>(ItemRoleManagerLayoutBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemRoleManagerLayoutBinding>, item: Role) {
        holder.binding.tvName.text = item.roleName
        holder.binding.tvRemark.text = item.remark ?: ""
    }

}