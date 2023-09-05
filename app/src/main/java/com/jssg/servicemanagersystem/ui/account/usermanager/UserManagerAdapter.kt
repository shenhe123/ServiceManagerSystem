package com.jssg.servicemanagersystem.ui.account.usermanager

import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemUserManagerLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.User

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class UserManagerAdapter: BaseBindingAdapter<User, ItemUserManagerLayoutBinding>(ItemUserManagerLayoutBinding::inflate) {

    init {
        addChildClickViewIds(R.id.card_layout, R.id.mbt_permission, R.id.mbt_delete)
    }

    override fun convert(holder: VBViewHolder<ItemUserManagerLayoutBinding>, item: User) {
        holder.binding.tvName.text = "用户名：${item.nickName}"
        holder.binding.tvPhone.text = "联系方式：${item.phonenumber}"
        holder.binding.tvAddress.text = "居住地址：${item.address}"
        holder.binding.tvExpiredDate.text = "有效期至：${item.expireDate}"
    }

}