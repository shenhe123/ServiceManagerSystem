package com.jssg.servicemanagersystem.ui.account.usermanager

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
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.base.BaseSearchDialogFragment
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchUserBinding
import com.jssg.servicemanagersystem.databinding.ItemPopupSearchWorkOrderBinding
import com.jssg.servicemanagersystem.ui.main.MainActivity
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import java.util.Calendar

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/10/21.
 */
class UserSearchDialogFragment: BaseSearchDialogFragment() {

    private var listener: OnSearchBtnClick? = null
    private var searchParams: UserManagerActivity.SearchParams? = null
    private lateinit var binding: ItemPopupSearchUserBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemPopupSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchParams = arguments?.getParcelable("searchParams")
        
        initView()
        
        addListener()
    }

    private fun addListener() {
        binding.etUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivUserNameClose.isVisible = content.isNotEmpty()
            }
        })
        binding.etNickName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivNickNameClose.isVisible = content.isNotEmpty()
            }
        })
        binding.etPhoneNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivPhoneNumClose.isVisible = content.isNotEmpty()
            }
        })
        binding.etCreator.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivCreatorClose.isVisible = content.isNotEmpty()
            }
        })

        binding.etUserFactory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = s.toString()

                binding.ivUserFactoryClose.isVisible = content.isNotEmpty()
            }
        })

        binding.tvExpiredStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvExpiredStartDate, 0, binding)
        }

        binding.tvExpiredEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvExpiredEndDate, 1, binding)
        }

        binding.tvCreatorStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvCreatorStartDate, 2, binding)
        }

        binding.tvCreatorEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvCreatorEndDate, 3, binding)
        }

        binding.ivUserNameClose.setOnClickListener {
            binding.etUserName.setText("")
        }

        binding.ivNickNameClose.setOnClickListener {
            binding.etNickName.setText("")
        }

        binding.ivPhoneNumClose.setOnClickListener {
            binding.etPhoneNum.setText("")
        }

        binding.ivCreatorClose.setOnClickListener {
            binding.etCreator.setText("")
        }

        binding.ivUserFactoryClose.setOnClickListener {
            binding.etUserFactory.setText("")
        }

        binding.ivExpiredStartDateClose.setOnClickListener {
            binding.tvExpiredStartDate.text = ""
            binding.ivExpiredStartDateClose.isVisible = false
        }

        binding.ivExpiredEndDateClose.setOnClickListener {
            binding.tvExpiredEndDate.text = ""
            binding.ivExpiredEndDateClose.isVisible = false
        }

        binding.ivCreatorStartDateClose.setOnClickListener {
            binding.tvCreatorStartDate.text = ""
            binding.ivCreatorStartDateClose.isVisible = false
        }

        binding.ivCreatorEndDateClose.setOnClickListener {
            binding.tvCreatorEndDate.text = ""
            binding.ivCreatorEndDateClose.isVisible = false
        }

        binding.mbtSearch.setOnClickListener {
            listener?.let {
                var expiredStartDate = binding.tvExpiredStartDate.text.toString()
                var expiredEndDate = binding.tvExpiredEndDate.text.toString()
                expiredStartDate = if (expiredStartDate.equals("请选择日期") || expiredStartDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$expiredStartDate 00:00:00"
                }
                expiredEndDate = if (expiredEndDate.equals("请选择日期") || expiredEndDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$expiredEndDate 23:59:59"
                }

                if (expiredStartDate.isNotEmpty() && expiredEndDate.isNotEmpty() && !DateUtil.compareDate(expiredStartDate, expiredEndDate)) {
                    ToastUtils.showToast("过期终止日期不能小于起始日期")
                    return@setOnClickListener
                }

                var creatorStartDate = binding.tvCreatorStartDate.text.toString()
                var creatorEndDate = binding.tvCreatorEndDate.text.toString()
                creatorStartDate = if (creatorStartDate.equals("请选择日期") || creatorStartDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$creatorStartDate 00:00:00"
                }
                creatorEndDate = if (creatorEndDate.equals("请选择日期") || creatorEndDate.split(" ")[0].isEmpty()) {
                    ""
                } else {
                    "$creatorEndDate 23:59:59"
                }

                if (creatorStartDate.isNotEmpty() && creatorEndDate.isNotEmpty() && !DateUtil.compareDate(creatorStartDate, creatorEndDate)) {
                    ToastUtils.showToast("创建终止日期不能小于起始日期")
                    return@setOnClickListener
                }

                val state = when (binding.rgUserType.checkedRadioButtonId) {
                    binding.rbThird.id -> "end_user"
                    binding.rbFactory.id -> "factory_user"
                    binding.rbSystem.id -> "sys_user"
                    else -> ""
                }

                it.onClick(
                    UserManagerActivity.SearchParams(
                        binding.etUserName.text.toString(),
                        binding.etNickName.text.toString(),
                        binding.etPhoneNum.text.toString(),
                        binding.etUserFactory.text.toString(),
                        binding.etCreator.text.toString(),
                        expiredStartDate,
                        expiredEndDate,
                        creatorStartDate,
                        creatorEndDate,
                        state
                    )
                )
            }

            dismiss()
        }
    }

    private fun initView() {

        searchParams?.let {
            binding.etUserName.setText(it.userName)
            binding.etCreator.setText(it.creator)
            binding.etUserFactory.setText(it.factory)

            val expiredStartDate = it.expiredStartDate?.split(" ")?.get(0)
            binding.tvExpiredStartDate.text = expiredStartDate
            val expiredEndDate = it.expiredEndDate?.split(" ")?.get(0)
            binding.tvExpiredEndDate.text = expiredEndDate

            binding.ivExpiredStartDateClose.isVisible = !expiredStartDate.isNullOrEmpty()
            binding.ivExpiredEndDateClose.isVisible = !expiredEndDate.isNullOrEmpty()

            val creatorStartDate = it.creatorStartDate?.split(" ")?.get(0)
            binding.tvCreatorStartDate.text = creatorStartDate
            val creatorEndDate = it.creatorEndDate?.split(" ")?.get(0)
            binding.tvCreatorEndDate.text = creatorEndDate

            binding.ivCreatorStartDateClose.isVisible = !creatorStartDate.isNullOrEmpty()
            binding.ivCreatorEndDateClose.isVisible = !creatorEndDate.isNullOrEmpty()

            when(it.state) {
                "end_user" -> binding.rbThird.isChecked = true
                "factory_user" -> binding.rbFactory.isChecked = true
                "sys_user" -> binding.rbSystem.isChecked = true
            }
        }
    }

    private fun showSelectDateDialog(
        textView: TextView,
        index: Int,
        binding: ItemPopupSearchUserBinding
    ) {
        val calendar = Calendar.getInstance() //获取日期格式器对象

        //chose b
        val pvTime: TimeDialogFragment =
            TimePickerBuilder(
                requireContext()
            ) { date -> //选中事件回调
                textView.text = DateUtil.getDateyyMMdd(date.time)
                when(index) {
                    0 -> binding.ivExpiredStartDateClose.isVisible = true
                    1 -> binding.ivExpiredEndDateClose.isVisible = true
                    2 -> binding.ivCreatorStartDateClose.isVisible = true
                    3 -> binding.ivCreatorEndDateClose.isVisible = true
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
        pvTime.show((requireContext() as UserManagerActivity).supportFragmentManager, "timepicker")
    }

    fun setOnClickListener(listener: OnSearchBtnClick): UserSearchDialogFragment {
        this.listener = listener
        return this
    }

    interface OnSearchBtnClick {
        fun onClick(searchParams: UserManagerActivity.SearchParams)
    }

    companion object {
        fun newInstance(searchParams: UserManagerActivity.SearchParams?): UserSearchDialogFragment {
            val args = Bundle()
            args.putParcelable("searchParams", searchParams)
            val fragment = UserSearchDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}