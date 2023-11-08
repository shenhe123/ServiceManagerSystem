package com.jssg.servicemanagersystem.ui.account.logmanager.adapter

import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemLogInfoBinding
import com.jssg.servicemanagersystem.databinding.ItemOptionLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.OptionLogInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class OptionLogManagerAdapter: BaseBindingAdapter<OptionLogInfo, ItemOptionLogInfoBinding>(ItemOptionLogInfoBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemOptionLogInfoBinding>, item: OptionLogInfo) {
        holder.binding.tvLogId.text = item.operId
        holder.binding.tvIpLocation.text = item.operLocation
        holder.binding.tvIpAddress.text = item.operIp
        holder.binding.tvOptionName.text = item.title
        holder.binding.tvOptionStatus.text = if (item.status == 0) "成功" else "失败"
        //1：新增 2：修改 3：删除 4：授权 5：导出 7：强退 10：审核 0：其它
        holder.binding.tvOptionType.text = when(item.businessType) {
            0 -> "其它"
            1 -> "新增"
            2 -> "修改"
            3 -> "删除"
            4 -> "授权"
            5 -> "导出"
            7 -> "强退"
            10 -> "审核"
            else -> "其它"
        }
        holder.binding.tvOptionTime.text = item.operTime
    }

}