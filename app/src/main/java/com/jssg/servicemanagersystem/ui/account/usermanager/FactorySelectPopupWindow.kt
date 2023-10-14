package com.jssg.servicemanagersystem.ui.account.usermanager

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ItemPopupFactorySelectBinding
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchWorkOrderBinding
import com.jssg.servicemanagersystem.ui.main.MainActivity
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.widgets.popupwindow.BasePWControl
import java.util.Calendar

class FactorySelectPopupWindow(
    context: Context?,
    layoutParent: ViewGroup?,
    isSingleCheck: Boolean,
    source: MutableList<WorkFactoryInfo>,
) :
    BasePWControl(context, layoutParent) {

    private lateinit var factorySpinnerAdapter: FactorySpinnerAdapter
    private var source: MutableList<WorkFactoryInfo>
    private var isSingleCheck: Boolean
    private var listener: OnItemClick? = null
    private lateinit var binding: ItemPopupFactorySelectBinding

    init {
        this.isSingleCheck = isSingleCheck
        this.source = source
        initData()
    }

    override fun initView() {
        binding = ItemPopupFactorySelectBinding.bind(mView)

        binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
        factorySpinnerAdapter = FactorySpinnerAdapter(isSingleCheck)
        binding.recyclerView.adapter = factorySpinnerAdapter
        factorySpinnerAdapter.setOnItemClickListener { _, _, position ->
            val factoryInfo = source[position]
            if (isSingleCheck) {
                listener?.onSingleClick(factoryInfo)
                dismiss()
            } else {
                factoryInfo.isChecked = !factoryInfo.isChecked

                factorySpinnerAdapter.notifyItemChanged(position)

                listener?.onMultiClick(source)
            }
        }
    }

    private fun initData() {
        factorySpinnerAdapter.setList(source)
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