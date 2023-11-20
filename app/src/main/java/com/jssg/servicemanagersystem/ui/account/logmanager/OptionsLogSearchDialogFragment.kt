package com.jssg.servicemanagersystem.ui.account.logmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.children
import androidx.core.view.isVisible
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseSearchDialogFragment
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchOptionLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.OptionLogFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import java.util.Calendar

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/11/17.
 */
class OptionsLogSearchDialogFragment: BaseSearchDialogFragment() {

    private var optionTypeArray = arrayOf("选择操作类型","新增", "修改", "删除", "授权", "导出", "强退", "审核", "结案", "其他")
    private var optionTitleArray = arrayOf("选择模块名称","工单", "派工单明细", "用户管理", "OSS对象存储", "导出", "强退", "审核", "结案", "其他")
    private var optionType: String? = null
    private var optionTitle: String? = null
    private var searchParams: OptionLogFragment.SearchParams? = null
    private lateinit var listener: OnSearchBtnClick
    private lateinit var binding: ItemPopupSearchOptionLogInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemPopupSearchOptionLogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListener()

        initData()
    }

    private fun addListener() {
        binding.layoutRoot.setOnClickListener { v: View? -> dismiss() }

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

        binding.ivOptionNameClose.setOnClickListener {
            binding.etOptionName.setText("")
        }

        binding.ivOrderIdClose.setOnClickListener {
            binding.etOrderId.setText("")
        }

        binding.ivStartDateClose.setOnClickListener {
            binding.tvStartDate.text = ""
            binding.ivStartDateClose.isVisible = false
        }

        binding.ivEndDateClose.setOnClickListener {
            binding.tvEndDate.text = ""
            binding.ivEndDateClose.isVisible = false
        }

        binding.asOptionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                optionType = when(position) {
                    0 -> null
                    1 -> "1"
                    2 -> "2"
                    3 -> "3"
                    4 -> "4"
                    5 -> "5"
                    6 -> "7"
                    7 -> "10"
                    8 -> "11"
                    9 -> "0"
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.asTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                optionTitle = if (position == 0) {
                    null
                } else {
                    optionTitleArray[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.mbtReset.setOnClickListener {
            binding.asTitle.setSelection(0)
            binding.etOptionName.setText("")
            binding.etOrderId.setText("")
            binding.asOptionType.setSelection(0)
            binding.rgOptionStatus.clearCheck()
            binding.tvStartDate.text = "请选择日期"
            binding.ivStartDateClose.isVisible = false
            binding.tvEndDate.text = "请选择日期"
            binding.ivEndDateClose.isVisible = false
        }

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
                        optionTitle,
                        optionType,
                        binding.etOptionName.text.toString(),
                        binding.etOrderId.text.toString(),
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
        searchParams = arguments?.getParcelable("searchParams")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.simple_spinner_left_item, optionTypeArray
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_left_item)
        binding.asOptionType.adapter = adapter

        val adapter2: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), R.layout.simple_spinner_left_item, optionTitleArray
        )
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_left_item)
        binding.asTitle.adapter = adapter2

        searchParams?.let {

            if (optionTitleArray.contains(it.title)) {
                val pos = optionTitleArray.indexOf(it.title)
                binding.asTitle.setSelection(pos)
            } else {
                binding.asTitle.setSelection(0)
            }

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
                "0" -> binding.asOptionType.setSelection(9)
                "1" -> binding.asOptionType.setSelection(1)
                "2" -> binding.asOptionType.setSelection(2)
                "3" -> binding.asOptionType.setSelection(3)
                "4" -> binding.asOptionType.setSelection(4)
                "5" -> binding.asOptionType.setSelection(5)
                "7" -> binding.asOptionType.setSelection(6)
                "10" -> binding.asOptionType.setSelection(7)
                "11" -> binding.asOptionType.setSelection(8)
                else -> binding.asOptionType.setSelection(0)
            }

            val startDate = it.beginTime?.split(" ")?.get(0)
            binding.tvStartDate.text = startDate ?: "请选择日期"
            val endDate = it.endTime?.split(" ")?.get(0)
            binding.tvEndDate.text = endDate ?: "请选择日期"

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
                requireContext()
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
                .setSubmitText(requireContext().getString(com.jssg.servicemanagersystem.R.string.app_confirm)) //确认按钮文字
                .setContentTextSize(18) //滚轮文字大小
                .setTitleSize(18) //标题文字大小
                .setTitleText("选择登录日志时间") //标题文字
                .isCyclic(true) //是否循环滚动
                .setTextColorCenter(requireContext().getColor(R.color.purple_700)) //设置选中项的颜色
                .setTitleColor(requireContext().getColor(R.color.x_text_01)) //标题文字颜色
                .setSubmitColor(requireContext().getColor(R.color.purple_700)) //确定按钮文字颜色
                .setCancelColor(requireContext().getColor(R.color.x_text_01)) //取消按钮文字颜色
                .setTitleBgColor(requireContext().getColor(R.color.white)) //标题背景颜色 Night mode
                .setBgColor(requireContext().getColor(R.color.white)) //滚轮背景颜色 Night mode
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
        pvTime.show(childFragmentManager , "timepicker")
    }

    fun setOnClickListener(listener: OnSearchBtnClick): OptionsLogSearchDialogFragment {
        this.listener = listener
        return this
    }

    interface OnSearchBtnClick {
        fun onClick(searchParams: OptionLogFragment.SearchParams)
    }

    companion object {
        fun newInstance(searchParams: OptionLogFragment.SearchParams?): OptionsLogSearchDialogFragment {
            val args = Bundle()
            args.putParcelable("searchParams", searchParams)
            val fragment = OptionsLogSearchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}