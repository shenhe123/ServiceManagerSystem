package com.jssg.servicemanagersystem.ui.account.logmanager.provider

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogChildEntity
import com.jssg.servicemanagersystem.utils.Utils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * Created by shenhe on 2020/7/6.
 */
class OptionLogChildProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.item_option_log_child_layout

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val childEntity: OptionLogChildEntity = item as OptionLogChildEntity
        helper.setText(R.id.tv_name, childEntity.operParam)
    }

    override fun onLongClick(
        helper: BaseViewHolder,
        view: View,
        data: BaseNode,
        position: Int
    ): Boolean {
        val childEntity = data as OptionLogChildEntity
        Utils.copyStringText(childEntity.operParam, context)
        ToastUtils.showToast("复制成功")
        return super.onLongClick(helper, view, data, position)
    }
}