package com.jssg.servicemanagersystem.ui.account.logmanager.adapter

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogParentEntity
import com.jssg.servicemanagersystem.ui.account.logmanager.provider.OptionLogChildProvider
import com.jssg.servicemanagersystem.ui.account.logmanager.provider.OptionLogParentProvider


/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class OptionLogManagerAdapter: BaseNodeAdapter() {
    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        val baseNode = data[position]
        return if (baseNode is OptionLogParentEntity) {
            1
        } else 2
    }

    init {
        // 注册Provider，总共有如下三种方式

        // 需要占满一行的，使用此方法（例如section）
        addFullSpanNodeProvider(OptionLogParentProvider())
        // 普通的item provider
        addNodeProvider(OptionLogChildProvider())
        // 脚布局的 provider
//        addFooterNodeProvider(new RootFooterNodeProvider());

    }

    companion object {
        const val EXPAND_COLLAPSE_PAYLOAD = 130
    }

//    override fun convert(holder: VBViewHolder<ItemOptionLogInfoBinding>, item: OptionLogInfo) {
//        holder.binding.tvLogId.text = item.operId
//        holder.binding.tvIpLocation.text = item.operLocation
//        holder.binding.tvIpAddress.text = item.operIp
//        holder.binding.tvTitle.text = item.title
//        holder.binding.tvOptionName.text = "${item.nickName}(${item.operName})"
//        holder.binding.tvOptionStatus.text = if (item.status == 0) "成功" else "失败"
//        //1：新增 2：修改 3：删除 4：授权 5：导出 7：强退 10：审核 0：其它
//        holder.binding.tvOptionType.text = when(item.businessType) {
//            0 -> "其它"
//            1 -> "新增"
//            2 -> "修改"
//            3 -> "删除"
//            4 -> "授权"
//            5 -> "导出"
//            7 -> "强退"
//            10 -> "审核"
//            11 -> "结案"
//            else -> "其它"
//        }
//        holder.binding.tvOptionTime.text = item.operTime
//
//        //当此日志是派工单相关时
//        if (item.title == "工单") {
//            when(item.businessType) {
//                1 -> { //"新增"
//
//                    val fromJson = JsonParser.parseString(item.operParam).asJsonObject
//                    val billNo = fromJson["billNo"].asString
//                    val jsonElement = fromJson["oaBillNo"]
//                    val oaBillNo = if (jsonElement.isJsonNull) {
//                        ""
//                    } else {
//                        jsonElement.asString
//                    }
//                    if (oaBillNo.isNotEmpty()) {
//                        holder.binding.tvOrderId.text = "$billNo, $oaBillNo"
//                    } else {
//                        holder.binding.tvOrderId.text = billNo
//                    }
//                }
//                3 -> {//"删除"
//                    val lastOrderId = item.operUrl.split("/").last()
//                    holder.binding.tvOrderId.text = lastOrderId
//                }
//                11 -> { //"结案"
//                    val fromJson = JsonParser.parseString(item.operParam).asJsonArray
//                    val orderIds = fromJson.joinToString(", ")
//                    holder.binding.tvOrderId.text = orderIds.replace("\"", "")
//                }
//            }
//            holder.binding.groupOrderId.isVisible = true
//        } else {
//            holder.binding.groupOrderId.isVisible = false
//        }
//
//        holder.binding.tvOrderId.setOnLongClickListener {
//            Utils.copyStringText(holder.binding.tvOrderId.text.toString(), context)
//            ToastUtils.showToast("复制成功")
//            true
//        }
//    }



}