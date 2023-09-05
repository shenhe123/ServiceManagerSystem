package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchWorkOrderBinding
import com.jssg.servicemanagersystem.ui.MainActivity
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.widgets.popupwindow.BasePWControl
import java.util.Calendar

class SearchFilterPopupWindow(context: Context?, layoutParent: ViewGroup?) :
    BasePWControl(context, layoutParent) {

    private lateinit var listener: OnSearchBtnClick
    private lateinit var binding: ItemPopupSearchWorkOrderBinding

    override fun initView() {
        binding = ItemPopupSearchWorkOrderBinding.bind(mView)
        binding.layoutRoot.setOnClickListener { v: View? -> dismiss() }
        binding.etProductDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivProductDescClose.isVisible = content.isNotEmpty()
            }
        })

        binding.etProductCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivProductCodeClose.isVisible = content.isNotEmpty()
            }
        })

        binding.etOrderId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivOrderIdClose.isVisible = content.isNotEmpty()
            }
        })

        binding.tvStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvStartDate, 0, binding)
        }

        binding.tvEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvEndDate, 1, binding)
        }

        binding.ivProductDescClose.setOnClickListener {
            binding.etProductDesc.setText("")
        }

        binding.ivProductCodeClose.setOnClickListener {
            binding.etProductCode.setText("")
        }

        binding.ivStartDateClose.setOnClickListener {
            binding.tvStartDate.text = ""
            binding.ivStartDateClose.isVisible = false
        }

        binding.ivEndDateClose.setOnClickListener {
            binding.tvEndDate.text = ""
            binding.ivEndDateClose.isVisible = false
        }

        binding.ivOrderIdClose.setOnClickListener {
            binding.etOrderId.setText("")
        }

        binding.mbtSearch.setOnClickListener {
            if (this::listener.isInitialized) {
                var startDate = binding.tvStartDate.text.toString()
                var endDate = binding.tvEndDate.text.toString()
                if (startDate.equals("请选择日期")) {
                    startDate = ""
                }
                if (endDate.equals("请选择日期")) {
                    endDate = ""
                }
                listener.onClick(
                    binding.etProductDesc.text.toString(),
                    binding.etProductCode.text.toString(),
                    startDate,
                    endDate,
                    binding.etOrderId.text.toString(),
                )
            }

            dismiss()
        }
    }

    private fun showSelectDateDialog(textView: TextView, index: Int, binding: ItemPopupSearchWorkOrderBinding) {
        val calendar = Calendar.getInstance() //获取日期格式器对象

        //chose b
        val pvTime: TimeDialogFragment =
            TimePickerBuilder(
                mContext
            ) { date -> //选中事件回调
                textView.text = DateUtil.getFullData(date.time)
                if (index == 0) {
                    binding.ivStartDateClose.isVisible = true
                } else {
                    binding.ivEndDateClose.isVisible = true
                }
            }
                .setType(booleanArrayOf(true, true, true, true, true, true)) //默认全部显示
                .setGravity(
                    intArrayOf(
                        Gravity.CENTER,
                        Gravity.CENTER,
                        Gravity.CENTER,
                        Gravity.CENTER,
                        Gravity.CENTER,
                        Gravity.CENTER
                    )
                )
                .setCancelText("取消") //取消按钮文字
                .setSubmitText(mContext.getString(com.jssg.servicemanagersystem.R.string.app_confirm)) //确认按钮文字
                .setContentTextSize(18) //滚轮文字大小
                .setTitleSize(18) //标题文字大小
                .setTitleText("选择过期时间") //标题文字
                .isCyclic(true) //是否循环滚动
                .setTextColorCenter(mContext.getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //设置选中项的颜色
                .setTitleColor(mContext.getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //标题文字颜色
                .setSubmitColor(mContext.getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //确定按钮文字颜色
                .setCancelColor(mContext.getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //取消按钮文字颜色
                .setTitleBgColor(mContext.getColor(com.jssg.servicemanagersystem.R.color.white)) //标题背景颜色 Night mode
                .setBgColor(mContext.getColor(com.jssg.servicemanagersystem.R.color.white)) //滚轮背景颜色 Night mode
                .setDate(calendar) // 如果不设置的话，默认是系统时间*/
                .setLabel(
                    "年",
                    "月",
                    "日",
                    "时",
                    "分",
                    "秒"
                )
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build()
        pvTime.show((mContext as MainActivity).supportFragmentManager, "timepicker")
    }

    override fun injectLayout(): Int {
        return R.layout.item_popup_search_work_order
    }

    override fun injectAnimationStyle(): Int {
        return -1
    }

    override fun injectParamsHeight(): Int {
        return LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    }

    override fun injectParamsWight(): Int {
        return LinearLayoutCompat.LayoutParams.MATCH_PARENT
    }

    fun setOnClickListener(listener: OnSearchBtnClick) {
        this.listener = listener
    }

    interface OnSearchBtnClick {
        fun onClick(productDesc: String?, productCode: String?, startDate: String?, endDate: String?, oaBillNo: String?)
    }
}