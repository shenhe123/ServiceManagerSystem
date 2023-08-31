package com.jssg.servicemanagersystem.ui.account.role.provider

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleChildChildEntity
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity

/**
 * Created by shenhe on 2020/7/6.
 */
class RoleChildProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.item_add_role_child_layout

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val childEntity: RoleChildChildEntity = item as RoleChildChildEntity
        helper.setText(R.id.tv_name, childEntity.title)
        val checkBox = helper.getView<MaterialCheckBox>(R.id.mcb_check_child)
        checkBox.isChecked = childEntity.checked
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        val childEntity = data as RoleChildChildEntity
        childEntity.checked = !childEntity.checked

        getAdapter()?.let { adapter ->
            val parentNodeIndex = adapter.findParentNode(position)
            val roleParentEntity = adapter.data[parentNodeIndex] as RoleParentEntity
            if (!childEntity.checked) {
                roleParentEntity.checked = false
            } else {
                val allChildCheck = roleParentEntity.childNode?.all { (it as RoleChildChildEntity).checked }
                if (allChildCheck == true) {
                    roleParentEntity.checked = true
                }
            }

            adapter.notifyDataSetChanged()
        }


    }
}