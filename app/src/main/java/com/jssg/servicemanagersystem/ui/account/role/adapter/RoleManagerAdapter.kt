package com.jssg.servicemanagersystem.ui.account.role.adapter

import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemRoleManagerLayoutBinding
import com.jssg.servicemanagersystem.databinding.ItemUserManagerLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class RoleManagerAdapter: BaseBindingAdapter<Role, ItemRoleManagerLayoutBinding>(ItemRoleManagerLayoutBinding::inflate) {

    init {
        addChildClickViewIds(R.id.card_layout, R.id.mbt_menu, R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemRoleManagerLayoutBinding>, item: Role) {
        holder.binding.tvId.text = "角色ID：${item.roleId}"
        holder.binding.tvName.text = "角色名称：${item.roleName}"
        holder.binding.tvRemark.text = "备注：${item.remark}"
    }

}