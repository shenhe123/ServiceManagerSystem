package com.jssg.servicemanagersystem.ui.account.logmanager.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import java.io.Serializable

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/11/20.
 */
data class OptionLogChildEntity(
    val operParam: String,
) : BaseExpandNode(), Serializable {
    override var childNode: MutableList<BaseNode>? = null
}