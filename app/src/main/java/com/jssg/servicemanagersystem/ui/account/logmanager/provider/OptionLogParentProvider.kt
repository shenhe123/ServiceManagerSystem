package com.jssg.servicemanagersystem.ui.account.logmanager.provider

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.JsonParser
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogParentEntity
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.utils.Utils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * Created by shenhe on 2020/7/6.
 */
class OptionLogParentProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_option_log_parent_layout

    override fun convert(helper: BaseViewHolder, baseNode: BaseNode) {
        val parentEntity = baseNode as OptionLogParentEntity
        helper.setText(R.id.tv_log_id, parentEntity.operId)
        helper.setText(R.id.tv_ip_location, parentEntity.operLocation)
        helper.setText(R.id.tv_ip_address, parentEntity.operIp)
        helper.setText(R.id.tv_oper_url, parentEntity.operUrl)
        helper.setText(R.id.tv_title, parentEntity.title)
        helper.setText(R.id.tv_option_name, "${parentEntity.nickName}(${parentEntity.operName})")
        helper.setText(R.id.tv_option_status, if (parentEntity.status == 0) "成功" else "失败")
        val businessType = when(parentEntity.businessType) {
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
        helper.setText(R.id.tv_option_type, businessType)
        helper.setText(R.id.tv_option_time, parentEntity.operTime)

        //当此日志是派工单相关时
        if (parentEntity.title == "工单") {
            when(parentEntity.businessType) {
                1 -> { //"新增"

                    val fromJson = JsonParser.parseString(parentEntity.operParam).asJsonObject
                    val billNo = fromJson["billNo"].asString
                    val jsonElement = fromJson["oaBillNo"]
                    val oaBillNo = if (jsonElement.isJsonNull) {
                        ""
                    } else {
                        jsonElement.asString
                    }
                    if (oaBillNo.isNotEmpty()) {
                        helper.setText(R.id.tv_order_id, "$billNo, $oaBillNo")
                    } else {
                        helper.setText(R.id.tv_order_id, billNo)
                    }
                }
                3 -> {//"删除"
                    val lastOrderId = parentEntity.operUrl.split("/").last()
                    helper.setText(R.id.tv_order_id, lastOrderId)
                }
                11 -> { //"结案"
                    val fromJson = JsonParser.parseString(parentEntity.operParam).asJsonArray
                    val orderIds = fromJson.joinToString(", ")
                    helper.setText(R.id.tv_order_id, orderIds.replace("\"", ""))
                }
            }
            helper.setGone(R.id.group_order_id, false)
        } else if (parentEntity.title == "派工单明细") {
            when(parentEntity.businessType) {
                1,2,10 -> { //"新增"、"修改"
                    val fromJson = JsonParser.parseString(parentEntity.operParam).asJsonObject

                    val jsonElement = fromJson["billDetailNo"]
                    if (jsonElement.isJsonNull) {
                        helper.setText(R.id.tv_order_id, "")
                    } else {
                        val billDetailNo = jsonElement.asString
                        helper.setText(R.id.tv_order_id, billDetailNo)
                    }
                }
                3 -> {//"删除"
                    val lastOrderId = parentEntity.operUrl.split("/").last()
                    helper.setText(R.id.tv_order_id, lastOrderId)
                }
//                10 -> { //"审核"
//                    val fromJson = JsonParser.parseString(parentEntity.operParam).asJsonArray
//                    val orderIds = fromJson.joinToString(", ")
//                    helper.setText(R.id.tv_order_id, orderIds.replace("\"", ""))
//                }
            }
            helper.setGone(R.id.group_order_id, false)
        } else if (parentEntity.title == "用户管理") {
            when(parentEntity.businessType) {
                1,2 -> { //"新增"、"修改"
                    val fromJson = JsonParser.parseString(parentEntity.operParam).asJsonObject

                    val userNameElement = fromJson["userName"]
                    val nickNameElement = fromJson["nickName"]
                    val userName = if (userNameElement.isJsonNull) "" else userNameElement.asString
                    val nickName = if (nickNameElement.isJsonNull) "" else nickNameElement.asString
                    helper.setText(R.id.tv_user_name, "$nickName($userName)")
                }
                3 -> {//"删除"
                    val lastOrderId = parentEntity.operUrl.split("/").last()
                    helper.setText(R.id.tv_user_name, lastOrderId)
                }
            }
            helper.setGone(R.id.group_user_name, false)
        } else {
            helper.setGone(R.id.group_order_id, true)
            helper.setGone(R.id.group_user_name, true)
        }

        val tvOrderId = helper.getView<TextView>(R.id.tv_order_id)
        tvOrderId.setOnLongClickListener {
            Utils.copyStringText(tvOrderId.text.toString(), context)
            ToastUtils.showToast("复制成功")
            true
        }

//        setArrowSpin(helper, baseNode, false)
    }

//    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
//        for (payload in payloads) {
//            if (payload is Int && payload == AddNewRoleAdapter.EXPAND_COLLAPSE_PAYLOAD) {
//                // 增量刷新，使用动画变化箭头
//                setArrowSpin(helper, item, true)
//            }
//        }
//    }

//    private fun setArrowSpin(helper: BaseViewHolder, data: BaseNode, isAnimate: Boolean) {
//        val entity = data as OptionLogParentEntity
//        val imageView = helper.getView<ImageView>(R.id.iv_arrow)
//        if (entity.isExpanded) {
//            if (isAnimate) {
//                ViewCompat.animate(imageView).setDuration(200)
//                    .setInterpolator(DecelerateInterpolator())
//                    .rotation(-90f)
//                    .start()
//            } else {
//                imageView.rotation = -90f
//            }
//        } else {
//            if (isAnimate) {
//                ViewCompat.animate(imageView).setDuration(200)
//                    .setInterpolator(DecelerateInterpolator())
//                    .rotation(0f)
//                    .start()
//            } else {
//                imageView.rotation = 0f
//            }
//        }
//    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()!!.expandOrCollapse(
            position,
            true,
            true,
            AddNewRoleAdapter.EXPAND_COLLAPSE_PAYLOAD
        )
    }
}