package com.jssg.servicemanagersystem.ui.workorder.popup

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
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchWorkOrderDetailBinding
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckListFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import java.util.Calendar

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/21.
 */
class WorkOrderDetailSearchDialogFragment: BaseSearchDialogFragment() {

    private var searchParams: WorkOrderCheckListFragment.SearchParams? = null
    private lateinit var binding: ItemPopupSearchWorkOrderDetailBinding
    private lateinit var listener: OnSearchBtnClick

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemPopupSearchWorkOrderDetailBinding.inflate(inflater, container, false)
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


        binding.ivBatchNoClose.setOnClickListener {
            binding.etBatchNo.setText("")
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
//                    binding.rbSave.id -> "0"
                    binding.rbSubmit.id -> "1"
                    binding.rbAgree.id -> "2"
                    binding.rbResignation.id -> "3"
                    binding.rbAgreeNot.id -> "4"
                    else -> ""
                }

                val batchNo = binding.etBatchNo.text.toString()

                listener.onClick(
                    WorkOrderCheckListFragment.SearchParams(
                        batchNo,
                        state,
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
            it.state?.let { state ->
                when (state) {
//                    "0" -> holder.binding.tvOrderState.text = "已保存"
                    "1" -> binding.rbSubmit.isChecked = true
                    "2" -> binding.rbAgree.isChecked = true
                    "3" -> binding.rbResignation.isChecked = true
                    "4" -> binding.rbAgreeNot.isChecked = true
                }
            }

            val startDate = it.startDate?.split(" ")?.get(0)
            binding.tvStartDate.text = startDate
            val endDate = it.endDate?.split(" ")?.get(0)
            binding.tvEndDate.text = endDate

            binding.ivStartDateClose.isVisible = !startDate.isNullOrEmpty()
            binding.ivEndDateClose.isVisible = !endDate.isNullOrEmpty()
        }
    }

    private fun showSelectDateDialog(
        textView: TextView,
        index: Int,
        binding: ItemPopupSearchWorkOrderDetailBinding
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
        pvTime.show((requireContext() as WorkOrderDetailActivity).supportFragmentManager, "timepicker")
    }

    fun setOnClickListener(listener: OnSearchBtnClick): WorkOrderDetailSearchDialogFragment {
        this.listener = listener
        return this
    }

    interface OnSearchBtnClick {
        fun onClick(searchParams: WorkOrderCheckListFragment.SearchParams)
    }
    
    companion object {
        fun newInstance(searchParams: WorkOrderCheckListFragment.SearchParams?): WorkOrderDetailSearchDialogFragment {
            val args = Bundle()
            args.putParcelable("searchParams", searchParams)
            val fragment = WorkOrderDetailSearchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}