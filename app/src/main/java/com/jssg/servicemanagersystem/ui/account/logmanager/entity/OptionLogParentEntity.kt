package com.jssg.servicemanagersystem.ui.account.logmanager.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.ui.account.entity.Params
import java.io.Serializable

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/11/20.
 */
data class OptionLogParentEntity(
    val businessType: Int,
//    val businessTypes: Any,
    val deptName: String,
    val errorMsg: String,
    val jsonResult: String,
    val method: String,
    val operId: String,
    val operIp: String,
    val operLocation: String,
    val operName: String,
    val nickName: String,
    val operParam: String,
    val operTime: String,
    val operUrl: String,
    val operatorType: Int, //1：新增 2：修改 3：删除 4：授权 5：导出 7：强退 10：审核 0：其它
    val params: Params,
    val requestMethod: String,
    val status: Int, //0:成功 1:失败
    val title: String
) : BaseExpandNode(), Serializable {
    override var childNode: MutableList<BaseNode>? = null
}