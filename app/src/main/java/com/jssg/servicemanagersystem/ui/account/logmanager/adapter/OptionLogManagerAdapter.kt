package com.jssg.servicemanagersystem.ui.account.logmanager.adapter

import androidx.core.view.isVisible
import com.google.gson.JsonParser
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemOptionLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.entity.OptionLogInfo
import com.jssg.servicemanagersystem.utils.Utils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils


/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class OptionLogManagerAdapter: BaseBindingAdapter<OptionLogInfo, ItemOptionLogInfoBinding>(ItemOptionLogInfoBinding::inflate) {

    override fun convert(holder: VBViewHolder<ItemOptionLogInfoBinding>, item: OptionLogInfo) {
        holder.binding.tvLogId.text = item.operId
        holder.binding.tvIpLocation.text = item.operLocation
        holder.binding.tvIpAddress.text = item.operIp
        holder.binding.tvTitle.text = item.title
        holder.binding.tvOptionName.text = "${item.nickName}(${item.operName})"
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
            11 -> "结案"
            else -> "其它"
        }
        holder.binding.tvOptionTime.text = item.operTime

        //当此日志是派工单相关时
        if (item.title == "工单") {
            when(item.businessType) {
                1 -> { //"新增"

                    val fromJson = JsonParser.parseString(item.operParam).asJsonObject
                    val billNo = fromJson["billNo"].asString
                    val jsonElement = fromJson["oaBillNo"]
                    val oaBillNo = if (jsonElement.isJsonNull) {
                        ""
                    } else {
                        jsonElement.asString
                    }
                    if (oaBillNo.isNotEmpty()) {
                        holder.binding.tvOrderId.text = "$billNo, $oaBillNo"
                    } else {
                        holder.binding.tvOrderId.text = billNo
                    }
                }
                3 -> {//"删除"
                    val lastOrderId = item.operUrl.split("/").last()
                    holder.binding.tvOrderId.text = lastOrderId
                }
                11 -> { //"结案"
                    val fromJson = JsonParser.parseString(item.operParam).asJsonArray
                    val orderIds = fromJson.joinToString(", ")
                    holder.binding.tvOrderId.text = orderIds.replace("\"", "")
                }
            }
            holder.binding.groupOrderId.isVisible = true
        } else {
            holder.binding.groupOrderId.isVisible = false
        }

        holder.binding.tvOrderId.setOnLongClickListener {
            Utils.copyStringText(holder.binding.tvOrderId.text.toString(), context)
            ToastUtils.showToast("复制成功")
            true
        }
    }



}