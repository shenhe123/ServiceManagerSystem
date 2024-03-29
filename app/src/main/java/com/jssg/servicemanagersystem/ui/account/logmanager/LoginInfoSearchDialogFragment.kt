package com.jssg.servicemanagersystem.ui.account.logmanager

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
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseSearchDialogFragment
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchLogInfoBinding
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.LoginLogFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import java.util.Calendar

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/11/17.
 */
class LoginInfoSearchDialogFragment: BaseSearchDialogFragment() {

    private var searchParams: LoginLogFragment.SearchParams? = null
    private lateinit var listener: OnSearchBtnClick
    private lateinit var binding: ItemPopupSearchLogInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemPopupSearchLogInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListener()

        initData()
    }

    private fun addListener() {
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

        binding.tvStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvStartDate, 0, binding)
        }

        binding.tvEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvEndDate, 1, binding)
        }

        binding.ivApplyNameClose.setOnClickListener {
            binding.etApplyName.setText("")
        }

        binding.ivStartDateClose.setOnClickListener {
            binding.tvStartDate.text = "请选择日期"
            binding.ivStartDateClose.isVisible = false
        }

        binding.ivEndDateClose.setOnClickListener {
            binding.tvEndDate.text = "请选择日期"
            binding.ivEndDateClose.isVisible = false
        }

        binding.mbtReset.setOnClickListener {
            binding.etApplyName.setText("")
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

                listener.onClick(
                    LoginLogFragment.SearchParams(
                        binding.etApplyName.text.toString(),
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

        searchParams?.let {
            binding.etApplyName.setText(it.userName)

            val startDate = it.startDate?.split(" ")?.get(0)
            binding.tvStartDate.text = startDate ?: "请选择日期"
            val endDate = it.endDate?.split(" ")?.get(0)
            binding.tvEndDate.text = endDate ?: "请选择日期"

            binding.ivStartDateClose.isVisible = !startDate.isNullOrEmpty()
            binding.ivEndDateClose.isVisible = !endDate.isNullOrEmpty()
        }
    }

    private fun showSelectDateDialog(
        textView: TextView,
        index: Int,
        binding: ItemPopupSearchLogInfoBinding
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
                .setSubmitText(requireContext().getString(R.string.app_confirm)) //确认按钮文字
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

    fun setOnClickListener(listener: OnSearchBtnClick): LoginInfoSearchDialogFragment {
        this.listener = listener
        return this
    }

    interface OnSearchBtnClick {
        fun onClick(searchParams: LoginLogFragment.SearchParams)
    }

    companion object {
        fun newInstance(searchParams: LoginLogFragment.SearchParams?): LoginInfoSearchDialogFragment {
            val args = Bundle()
            args.putParcelable("searchParams", searchParams)
            val fragment = LoginInfoSearchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}