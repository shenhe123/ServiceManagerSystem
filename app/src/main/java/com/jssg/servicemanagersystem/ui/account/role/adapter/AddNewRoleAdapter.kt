package com.jssg.servicemanagersystem.ui.account.role.adapter

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity
import com.jssg.servicemanagersystem.ui.account.role.provider.RoleChildProvider
import com.jssg.servicemanagersystem.ui.account.role.provider.RoleParentProvider

/**
 * Created by shenhe on 2021/11/16.
 */
class AddNewRoleAdapter : BaseNodeAdapter() {
    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        val baseNode = data[position]
        return if (baseNode is RoleParentEntity) {
            1
        } else 2
    }

    init {
        // 注册Provider，总共有如下三种方式

        // 需要占满一行的，使用此方法（例如section）
        addFullSpanNodeProvider(RoleParentProvider())
        // 普通的item provider
        addNodeProvider(RoleChildProvider())
        // 脚布局的 provider
//        addFooterNodeProvider(new RootFooterNodeProvider());
    }

    companion object {
        const val EXPAND_COLLAPSE_PAYLOAD = 110
    }
}