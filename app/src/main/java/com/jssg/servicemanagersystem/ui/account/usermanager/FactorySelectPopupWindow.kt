package com.jssg.servicemanagersystem.ui.account.usermanager

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ItemPopupFactorySelectBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.widgets.popupwindow.BasePWControl

class FactorySelectPopupWindow(
    context: Context?,
    layoutParent: ViewGroup?,
    isLayoutRight: Boolean,
    isSingleCheck: Boolean,
    source: MutableList<WorkFactoryInfo>,
) :
    BasePWControl(context, layoutParent) {

    private var isLayoutRight: Boolean
    private var source: MutableList<WorkFactoryInfo>
    private var isSingleCheck: Boolean
    private var listener: OnItemClick? = null
    private lateinit var binding: ItemPopupFactorySelectBinding

    init {
        this.isLayoutRight = isLayoutRight
        this.isSingleCheck = isSingleCheck
        this.source = source
        initData()
    }

    override fun initView() {
        binding = ItemPopupFactorySelectBinding.bind(mView)

        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    private fun initData() {

        val factorySpinnerRightAdapter = if (isLayoutRight) FactorySpinnerRightAdapter(isSingleCheck) else FactorySpinnerLeftAdapter(isSingleCheck)
        binding.recyclerView.adapter = factorySpinnerRightAdapter
        factorySpinnerRightAdapter.setOnItemClickListener { _, _, position ->
            val factoryInfo = source[position]
            if (isSingleCheck) {
                listener?.onSingleClick(factoryInfo)
                dismiss()
            } else {
                factoryInfo.isChecked = !factoryInfo.isChecked

                factorySpinnerRightAdapter.notifyItemChanged(position)

                listener?.onMultiClick(source)
            }
        }

        factorySpinnerRightAdapter.setList(source)
    }

    override fun injectLayout(): Int {
        return R.layout.item_popup_factory_select
    }

    override fun injectAnimationStyle(): Int {
        return -1
    }

    override fun injectParamsHeight(): Int {
        return LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    }

    override fun injectParamsWight(): Int {
        return DpPxUtils.dip2px(mContext, 310f)
    }

    fun setOnClickListener(listener: OnItemClick) {
        this.listener = listener
    }

    interface OnItemClick {
        fun onSingleClick(factoryInfo: WorkFactoryInfo)
        fun onMultiClick(factoryInfos: MutableList<WorkFactoryInfo>)
    }
}