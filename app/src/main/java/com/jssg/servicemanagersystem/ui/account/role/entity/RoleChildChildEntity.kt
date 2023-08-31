package com.jssg.servicemanagersystem.ui.account.role.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import java.io.Serializable

/**
 * Created by shenhe on 2020/7/6.
 */
data class RoleChildChildEntity(
    var title: String, //menu权限
    var id: Long, // menu权限编号
    var checked: Boolean = false
) : BaseExpandNode(), Serializable {
    override val childNode: MutableList<BaseNode>?
        get() = null
}