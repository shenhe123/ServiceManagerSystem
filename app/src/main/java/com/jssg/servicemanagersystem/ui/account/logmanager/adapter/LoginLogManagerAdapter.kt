package com.jssg.servicemanagersystem.ui.account.logmanager.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class LoginLogManagerAdapter: BaseBindingAdapter<LogInfo, ItemLogInfoBinding>(ItemLogInfoBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemLogInfoBinding>, item: LogInfo) {
        holder.binding.tvLogId.text = item.infoId
        holder.binding.tvIpLocation.text = item.loginLocation
        holder.binding.tvIpAddress.text = item.ipaddr
        holder.binding.tvLoginTime.text = item.loginTime
        holder.binding.tvUserName.text = item.userName
    }

}