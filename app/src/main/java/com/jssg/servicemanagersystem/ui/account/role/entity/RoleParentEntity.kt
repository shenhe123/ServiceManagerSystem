package com.jssg.servicemanagersystem.ui.account.role.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import java.io.Serializable

/**
 * Created by gdy on 2019/1/17
 * Describe:
 */
data class RoleParentEntity(
    var title: String,
    var checked: Boolean = false,
) : BaseExpandNode(), Serializable {
    override var childNode: MutableList<BaseNode>? = null
}