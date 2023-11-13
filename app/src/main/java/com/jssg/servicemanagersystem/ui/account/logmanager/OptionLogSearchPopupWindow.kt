package com.jssg.servicemanagersystem.ui.account.logmanager

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchLogInfoBinding
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchOptionLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.LoginLogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.OptionLogFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.widgets.popupwindow.BasePWControl
import java.util.Calendar

class OptionLogSearchPopupWindow(
    context: Context?,
    layoutParent: ViewGroup?,
    searchParams: OptionLogFragment.SearchParams?
) :
    BasePWControl(context, layoutParent), View.OnClickListener {

    private var optionType: String? = null
    private var searchParams: OptionLogFragment.SearchParams?
    private lateinit var listener: OnSearchBtnClick
    private lateinit var binding: ItemPopupSearchOptionLogInfoBinding

    init {
        this.searchParams = searchParams
        initData()
    }

    override fun initView() {
        binding = ItemPopupSearchOptionLogInfoBinding.bind(mView)

        binding.layoutRoot.setOnClickListener { v: View? -> dismiss() }
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivTitleClose.isVisible = content.isNotEmpty()
            }
        })
        binding.etOptionName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivOptionNameClose.isVisible = content.isNotEmpty()
            }
        })

        binding.tvStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvStartDate, 0, binding)
        }

        binding.tvEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvEndDate, 1, binding)
        }

        binding.ivTitleClose.setOnClickListener {
            binding.etTitle.setText("")
        }

        binding.ivOptionNameClose.setOnClickListener {
            binding.etOptionName.setText("")
        }

        binding.ivStartDateClose.setOnClickListener {
            binding.tvStartDate.text = ""
            binding.ivStartDateClose.isVisible = false
        }

        binding.ivEndDateClose.setOnClickListener {
            binding.tvEndDate.text = ""
            binding.ivEndDateClose.isVisible = false
        }

        binding.rb1.setOnClickListener(this)
        binding.rb2.setOnClickListener(this)
        binding.rb3.setOnClickListener(this)
        binding.rb4.setOnClickListener(this)
        binding.rb5.setOnClickListener(this)
        binding.rb7.setOnClickListener(this)
        binding.rb10.setOnClickListener(this)
        binding.rb0.setOnClickListener(this)

        binding.mbtSearch.setOnClickListener {
            if (this::listener.isInitialized) {
                var startDate = binding.tvStartDate.text.toString()
                var endDate = binding.tvEndDate.text.toString()
                startDate = if (startDate.equals("请选择日期") || startDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$startDate 00:00:00"
                }
                endDate = if (endDate.equals("请选择日期") || endDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$endDate 23:59:59"
                }

                val optionStatus = when {
                    binding.rbSuccess.isChecked -> "0"
                    binding.rbFailed.isChecked -> "1"
                    else -> null
                }

                listener.onClick(
                    OptionLogFragment.SearchParams(
                        binding.etTitle.text.toString(),
                        optionType,
                        binding.etOptionName.text.toString(),
                        optionStatus,
                        startDate,
                        endDate,
                    )
                )
            }

            dismiss()
        }
    }

    private fun initData() {
        searchParams?.let {
            binding.etTitle.setText(it.title)
            binding.etOptionName.setText(it.operName)

            if (it.status.isNullOrEmpty()) {
                binding.rbSuccess.isChecked = false
                binding.rbFailed.isChecked = false
            } else {
                if (it.status == "0") {
                    binding.rbSuccess.isChecked = true
                } else {
                    binding.rbFailed.isChecked = true
                }
            }

            when(it.businessType) {
                "0" -> binding.rb0.isChecked = true
                "1" -> binding.rb1.isChecked = true
                "2" -> binding.rb2.isChecked = true
                "3" -> binding.rb3.isChecked = true
                "4" -> binding.rb4.isChecked = true
                "5" -> binding.rb5.isChecked = true
                "7" -> binding.rb7.isChecked = true
                "10" -> binding.rb10.isChecked = true
                else -> resetRadioChecked()
            }

            val startDate = it.beginTime?.split(" ")?.get(0)
            binding.tvStartDate.text = startDate
            val endDate = it.endTime?.split(" ")?.get(0)
            binding.tvEndDate.text = endDate

            binding.ivStartDateClose.isVisible = !startDate.isNullOrEmpty()
            binding.ivEndDateClose.isVisible = !endDate.isNullOrEmpty()
        }
    }

    private fun showSelectDateDialog(
        textView: TextView,
        index: Int,
        binding: ItemPopupSearchOptionLogInfoBinding
    ) {
        val calendar = Calendar.getInstance() //获取日期格式器对象

        //chose b
        val pvTime: TimeDialogFragment =
            TimePickerBuilder(
                mContext
            ) { date -> //选中事件回调
                textView.text = DateUtil.getDateyyMMdd(date.time)
                if (index == 0) {
                    binding.ivStartDateClose.isVisible = true
                } else {
                    binding.ivEndDateClose.isVisible = true
                }
            }
                .setType(booleanArrayOf(true, true, true, false, false, false)) //默认全部显示
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
                .setTitleText("选择登录日志时间") //标题文字
                .isCyclic(true) //是否循环滚动
                .setTextColorCenter(mContext.getColor(R.color.purple_700)) //设置选中项的颜色
                .setTitleColor(mContext.getColor(R.color.x_text_01)) //标题文字颜色
                .setSubmitColor(mContext.getColor(R.color.purple_700)) //确定按钮文字颜色
                .setCancelColor(mContext.getColor(R.color.x_text_01)) //取消按钮文字颜色
                .setTitleBgColor(mContext.getColor(R.color.white)) //标题背景颜色 Night mode
                .setBgColor(mContext.getColor(R.color.white)) //滚轮背景颜色 Night mode
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
        pvTime.show((mContext as LogManagerActivity).supportFragmentManager , "timepicker")
    }

    override fun injectLayout(): Int {
        return R.layout.item_popup_search_option_log_info
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
        fun onClick(searchParams: OptionLogFragment.SearchParams)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.rb_1, R.id.rb_0, R.id.rb_2, R.id.rb_3, R.id.rb_4, R.id.rb_5, R.id.rb_7, R.id.rb_10 -> {
                resetRadioChecked()
                val checkedRadio = v as AppCompatRadioButton
                checkedRadio.isChecked = true
                optionType = checkedRadio.tag.toString()
            }
        }
    }

    private fun resetRadioChecked() {
        binding.chipGroupOptionType.children.forEach { radio ->
            (radio as AppCompatRadioButton).isChecked = false
        }
    }
}