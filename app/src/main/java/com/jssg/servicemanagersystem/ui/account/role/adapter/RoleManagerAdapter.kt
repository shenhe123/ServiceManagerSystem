package com.jssg.servicemanagersystem.ui.account.role.adapter

import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemRoleManagerLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class RoleManagerAdapter: BaseBindingAdapter<Role, ItemRoleManagerLayoutBinding>(ItemRoleManagerLayoutBinding::inflate) {

    init {
        addChildClickViewIds(R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemRoleManagerLayoutBinding>, item: Role) {
        holder.binding.tvRoleName.text = item.roleName
        holder.binding.tvRemark.text = item.remark ?: ""
        holder.binding.tvCreateTime.text = item.createTime

        holder.binding.groupRemark.isVisible = !item.remark.isNullOrEmpty()
    }

}