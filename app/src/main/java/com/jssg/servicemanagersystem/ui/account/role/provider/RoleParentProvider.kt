package com.jssg.servicemanagersystem.ui.account.role.provider

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleChildChildEntity
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity

/**
 * Created by shenhe on 2020/7/6.
 */
class RoleParentProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_add_role_parent_layout

    override fun convert(helper: BaseViewHolder, baseNode: BaseNode) {
        val parentEntity = baseNode as RoleParentEntity
        helper.setText(R.id.tv_name, parentEntity.title)
        val checkBox = helper.getView<MaterialCheckBox>(R.id.mcb_check)
        checkBox.isChecked = parentEntity.checked
        setArrowSpin(helper, baseNode, false)
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        for (payload in payloads) {
            if (payload is Int && payload == AddNewRoleAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, item, true)
            }
        }
    }

    private fun setArrowSpin(helper: BaseViewHolder, data: BaseNode, isAnimate: Boolean) {
        val entity = data as RoleParentEntity
        val imageView = helper.getView<ImageView>(R.id.iv_arrow)
        if (entity.isExpanded) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(-90f)
                    .start()
            } else {
                imageView.rotation = -90f
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                    .setInterpolator(DecelerateInterpolator())
                    .rotation(0f)
                    .start()
            } else {
                imageView.rotation = 0f
            }
        }
    }

    override fun onChildClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        super.onChildClick(helper, view, data, position)

        if (view.id == R.id.mcb_check) {
            val parentEntity =  data as RoleParentEntity
            parentEntity.checked = !parentEntity.checked
            //父节点check，子节点全部check
            parentEntity.childNode?.forEach {
                (it as RoleChildChildEntity).checked = parentEntity.checked
            }
            getAdapter()?.notifyItemChanged(helper.layoutPosition)
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()!!.expandOrCollapse(
            position,
            true,
            true,
            AddNewRoleAdapter.EXPAND_COLLAPSE_PAYLOAD
        )
    }
}