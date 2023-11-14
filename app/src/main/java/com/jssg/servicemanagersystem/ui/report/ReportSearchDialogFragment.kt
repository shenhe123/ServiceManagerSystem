package com.jssg.servicemanagersystem.ui.report

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.base.BaseSearchDialogFragment
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchReportBinding
import com.jssg.servicemanagersystem.ui.main.MainActivity
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchDialogFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import java.util.Calendar

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/21.
 */
class ReportSearchDialogFragment: BaseSearchDialogFragment() {

    private lateinit var listener: OnSearchBtnClick
    private lateinit var binding: ItemPopupSearchReportBinding
    private var searchParams: WorkOrderFragment.SearchParams? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemPopupSearchReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchParams = arguments?.getParcelable("searchParams")
        
        initView()
        
        initData()
    }

    private fun initView() {
        binding.root.setOnClickListener { dismiss() }

        binding.layoutApplyName.isVisible = !AccountManager.instance.isEndUser
        binding.layoutBatchNo.isVisible = !AccountManager.instance.isEndUser
        binding.layoutProductDesc.isVisible = !AccountManager.instance.isEndUser
        binding.layoutProductCode.isVisible = !AccountManager.instance.isEndUser
        binding.layoutApplyDate.isVisible = !AccountManager.instance.isEndUser
        binding.layoutFactory.isVisible = !AccountManager.instance.isEndUser
        binding.layoutCheckState.isVisible = !AccountManager.instance.isEndUser

        binding.layoutRoot.setOnClickListener { v: View? -> dismiss() }
        binding.etApplyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivApplyNameClose.isVisible = content.isNotEmpty()
            }
        })

        binding.etBatchNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivBatchNoClose.isVisible = content.isNotEmpty()
            }
        })

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

        binding.etFactory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivFactoryClose.isVisible = content.isNotEmpty()
            }
        })

        binding.ivBatchNoClose.setOnClickListener {
            binding.etBatchNo.setText("")
        }

        binding.tvStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvStartDate, 0, binding)
        }

        binding.tvEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvEndDate, 1, binding)
        }

        binding.ivApplyNameClose.setOnClickListener {
            binding.etApplyName.setText("")
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

        binding.ivFactoryClose.setOnClickListener {
            binding.etFactory.setText("")
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

                val state = when (binding.rgCheckState.checkedRadioButtonId) {
                    binding.rbStart.id -> "0"
                    binding.rbChecking.id -> "1"
                    binding.rbFinish.id -> "2"
                    else -> ""
                }

                listener.onClick(
                    WorkOrderFragment.SearchParams(
                        binding.etApplyName.text.toString(),
                        binding.etProductDesc.text.toString(),
                        binding.etProductCode.text.toString(),
                        startDate,
                        endDate,
                        binding.etOrderId.text.toString(),
                        binding.etFactory.text.toString(),
                        state,
                        binding.etBatchNo.text.toString()
                    )
                )
            }

            dismiss()
        }
    }

    private fun initData() {
        searchParams?.let {
            binding.etApplyName.setText(it.applyName)
            binding.etBatchNo.setText(it.batchNo ?: "")
            binding.etProductDesc.setText(it.productDesc)
            binding.etProductCode.setText(it.productCode)
            binding.etOrderId.setText(it.oaBillNo)
            binding.etFactory.setText(it.factory)
            val startDate = it.startDate?.split(" ")?.get(0)
            binding.tvStartDate.text = startDate
            val endDate = it.endDate?.split(" ")?.get(0)
            binding.tvEndDate.text = endDate

            binding.ivStartDateClose.isVisible = !startDate.isNullOrEmpty()
            binding.ivEndDateClose.isVisible = !endDate.isNullOrEmpty()

            when(it.state) {
                "0" -> binding.rbStart.isChecked = true
                "1" -> binding.rbChecking.isChecked = true
                "2" -> binding.rbFinish.isChecked = true
            }
        }
    }

    private fun showSelectDateDialog(
        textView: TextView,
        index: Int,
        binding: ItemPopupSearchReportBinding
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
                .setTitleText("选择过期时间") //标题文字
                .isCyclic(true) //是否循环滚动
                .setTextColorCenter(requireContext().getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //设置选中项的颜色
                .setTitleColor(requireContext().getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //标题文字颜色
                .setSubmitColor(requireContext().getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //确定按钮文字颜色
                .setCancelColor(requireContext().getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //取消按钮文字颜色
                .setTitleBgColor(requireContext().getColor(com.jssg.servicemanagersystem.R.color.white)) //标题背景颜色 Night mode
                .setBgColor(requireContext().getColor(com.jssg.servicemanagersystem.R.color.white)) //滚轮背景颜色 Night mode
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
        pvTime.show((requireContext() as MainActivity).supportFragmentManager, "timepicker")
    }

    fun setOnClickListener(listener: OnSearchBtnClick): ReportSearchDialogFragment {
        this.listener = listener
        return this
    }

    interface OnSearchBtnClick {
        fun onClick(searchParams: WorkOrderFragment.SearchParams)
    }
    
    companion object {
        fun newInstance(searchParams: WorkOrderFragment.SearchParams?): ReportSearchDialogFragment {
            val args = Bundle()
            args.putParcelable("searchParams", searchParams)
            val fragment = ReportSearchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}