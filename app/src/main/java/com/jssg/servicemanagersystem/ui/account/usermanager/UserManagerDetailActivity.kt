package com.jssg.servicemanagersystem.ui.account.usermanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SpinnerAdapter
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityUserManagerDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.popup.WorkOrderSearchPopupWindow
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

class UserManagerDetailActivity : BaseActivity() {
    private var userType: String? = null
    private var userTypeArray: Array<String> = arrayOf()
    private var inputData: InputData? = null
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<DeptInfo>? = null
    private var factoryInfos: MutableList<WorkFactoryInfo> = mutableListOf()
    private var user: User? = null
    private lateinit var accountViewModel: AccountViewModel
    private var editable: Boolean = false
    private lateinit var binding: ActivityUserManagerDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserManagerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        inputData = intent?.getParcelableExtra("input")
        user = inputData?.user
        user?.let {
            updateUserInfo(it)
        }

        //sys_user 都不隐藏, 角色信息、工厂、部门这几项非必填
        //main_factor_cqe, 这个角色角色信息、工厂显示, 部门隐藏；工厂必填，角色非必填
        //else 角色信息、工厂、部门隐藏
        if (AccountManager.instance.isSysUser) {
            binding.layoutUserType.isVisible = true
            userTypeArray = resources.getStringArray(R.array.sys_user)

            binding.layoutFactoryRoot.isVisible = true
            binding.layoutDept.isVisible = true
        } else if (AccountManager.instance.isMainFactorCqe) {
            binding.layoutUserType.isVisible = true
            userTypeArray = resources.getStringArray(R.array.main_factor_cqe)

            binding.layoutFactoryRoot.isVisible = true
            binding.layoutDept.isVisible = false
        } else {
            binding.layoutUserType.isVisible = false

            binding.layoutFactoryRoot.isVisible = false
            binding.layoutDept.isVisible = false
        }

        if (userTypeArray.isNotEmpty()) {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.simple_spinner_right_item, userTypeArray
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_right_item)
            binding.asUserType.adapter = adapter

            if (user?.userType.equals("sys_user")) {
                binding.asUserType.setSelection(userTypeArray.indexOf("集团用户"))
            } else if (user?.userType.equals("factory_user")) {
                binding.asUserType.setSelection(userTypeArray.indexOf("工厂用户"))
            } else if (user?.userType.equals("end_user")) {
                binding.asUserType.setSelection(userTypeArray.indexOf("三方用户"))
            }
        }

        initView()

        initViewModel()
    }
    private fun updateUserInfo(it: User) {
        binding.etUsername.setText(it.userName)
        binding.etNickname.setText(it.nickName)
        binding.etCardId.setText(it.idNo)
        binding.etPhoneNum.setText(it.phonenumber)
        binding.etAddress.setText(it.address)
        binding.tvExpiredDate.text = it.expireDate
        binding.etCreatePerson.setText(it.createBy)
        binding.etCreateTime.setText(it.createTime)
        binding.etUpdatePerson.setText(it.updateBy)
        binding.etUpdateTime.setText(it.updateTime)

        binding.tvEdit.isVisible = it.userId >= 100
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.updateUserInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                ToastUtils.showToast("更新成功")

                user?.let {
                    accountViewModel.getUserInfo(it.userId)
                }

                binding.tvDept.text = binding.asDept.prompt

                toggleEdit()

                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("output", inputData)
                })
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        accountViewModel.userInfoLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                result.data?.let {
                    updateUserInfo(it.user)

                    binding.tvFactory.text = it.user.sysOrganizationVos?.joinToString(",") { newFactory ->
                        newFactory.orgShortName
                    }

                    it.user.dept?.let { newDept ->
                        binding.tvDept.text = newDept.deptName

                    }
                }
            }
        }


        accountViewModel.factoryInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    factoryInfos.addAll(it)


                    user?.sysOrganizationVos?.forEach  {
                        val pos = factoryInfos.indexOfFirst { item -> item.orgShortName == it.orgShortName }
                        if (pos != -1) {
                            factoryInfos[pos].isChecked = true
                            binding.tvFactory.text = factoryInfos[pos].orgShortName

                            orgId = factoryInfos[pos].orgId
                            binding.ivFactoryClose.isVisible = true && editable
                        }
                    }

                }
            }
        }

        accountViewModel.deptInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                deptInfos = result.data

                val list = mutableListOf<String>()
                list.add("请选择部门")
                deptInfos?.let {
                    list.addAll(it.map { info -> info.label })
                }
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, R.layout.simple_spinner_right_item, list
                )
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_right_item)
                binding.asDept.adapter = adapter

                user?.dept?.deptName?.let {
                    val pos = list.indexOf(it)
                    if (pos > 0) {
                        binding.asDept.setSelection(pos, false)
                        deptId = deptInfos?.get(pos - 1)?.id
                    } else {
                        binding.asDept.setSelection(0, false)
                    }
                }

                binding.tvDept.text = user?.dept?.deptName ?: "请选择部门"
            }
        }

        accountViewModel.getFactoryInfo()
        accountViewModel.getDeptInfo()

        user?.let {
            accountViewModel.getUserInfo(it.userId)
        }
    }

    private fun initView() {

        updateEditWidgets()

        binding.tvEdit.setOnClickListener {
            toggleEdit()
        }

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.etUsername.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutUsername.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutUsername.background = drawable
            if (hasFocus) {
                setLast(binding.etUsername)
            }
        }

        binding.etNickname.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutNickname.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutNickname.background = drawable
            if (hasFocus) {
                setLast(binding.etNickname)
            }
        }

        binding.etPhoneNum.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutPhoneNum.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutPhoneNum.background = drawable
            if (hasFocus) {
                setLast(binding.etPhoneNum)
            }
        }

        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutPassword.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutPassword.background = drawable
            if (hasFocus) {
                setLast(binding.etPassword)
            }
        }

        binding.etAddress.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutAddress.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutAddress.background = drawable
            if (hasFocus) {
                setLast(binding.etAddress)
            }
        }

        binding.etCardId.setOnFocusChangeListener { v, hasFocus ->
            binding.layoutCardId.isSelected = hasFocus
            val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
            binding.layoutCardId.background = drawable
            if (hasFocus) {
                setLast(binding.etCardId)
            }
        }

        binding.layoutExpiredDate.setOnClickListener {
            val calendar = Calendar.getInstance() //获取日期格式器对象

            //chose b
            val pvTime: TimeDialogFragment =
                TimePickerBuilder(this@UserManagerDetailActivity
                ) { date -> //选中事件回调
                    binding.tvExpiredDate.text = DateUtil.getFullData(date.time)
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
                    .setSubmitText(getString(R.string.app_confirm)) //确认按钮文字
                    .setContentTextSize(18) //滚轮文字大小
                    .setTitleSize(18) //标题文字大小
                    .setTitleText("选择过期时间") //标题文字
                    .isCyclic(true) //是否循环滚动
                    .setTextColorCenter(getColor(R.color.purple_700)) //设置选中项的颜色
                    .setTitleColor(getColor(R.color.x_text_01)) //标题文字颜色
                    .setSubmitColor(getColor(R.color.purple_700)) //确定按钮文字颜色
                    .setCancelColor(getColor(R.color.x_text_01)) //取消按钮文字颜色
                    .setTitleBgColor(getColor(R.color.white)) //标题背景颜色 Night mode
                    .setBgColor(getColor(R.color.white)) //滚轮背景颜色 Night mode
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
            pvTime.show(supportFragmentManager, "timepicker")
        }

        binding.asUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val userTypeValue = if (position == 0) {
                    null
                } else {
                    userTypeArray[position]
                }

                if (userTypeValue.equals("集团用户")) {
                    userType = "sys_user"
                } else if (userTypeValue.equals("工厂用户")) {
                    userType = "factory_user"
                } else if (userTypeValue.equals("三方用户")) {
                    userType = "end_user"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.tvUserType.setOnClickListener {  }

        binding.layoutFactory.setOnClickListener {
            showFactoryPopup(binding.layoutFactory, !AccountManager.instance.isMultiFactory)
        }

        binding.ivFactoryClose.setOnClickListener {
            binding.ivFactoryClose.isVisible = false
            binding.tvFactory.text = "请选择工厂"
            orgId = ""

            factoryInfos.forEach { it.isChecked = false }
        }

        binding.asDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                deptId = if (position == 0) {
                    null
                } else {
                    deptInfos?.get(position - 1)?.id
                }
                binding.ivDeptClose.isVisible = !deptId.isNullOrEmpty() && editable
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.ivDeptClose.setOnClickListener {
            binding.ivDeptClose.isVisible = false
            deptId = ""

            binding.asDept.setSelection(0, false)
        }

        binding.btnUpdate.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            if (userName.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
                return@setOnClickListener
            }
            val nickname = binding.etNickname.text.toString()
            if (nickname.isEmpty()) {
                ToastUtils.showToast("姓名不能为空")
                return@setOnClickListener
            }

            val phoneNumber = binding.etPhoneNum.text.toString()
            if (phoneNumber.isEmpty()) {
                ToastUtils.showToast("手机号不能为空")
                return@setOnClickListener
            }

            val cardId = binding.etCardId.text.toString()
            if (cardId.isEmpty()) {
                ToastUtils.showToast("身份证号不能为空")
                return@setOnClickListener
            }

            val address = binding.etAddress.text.toString()
//            if (address.isEmpty()) {
//                ToastUtils.showToast("居住地址不能为空")
//                return@setOnClickListener
//            }

            val expiredDate = binding.tvExpiredDate.text.toString()
            if (expiredDate.isEmpty()) {
                ToastUtils.showToast("有效日期不能为空")
                return@setOnClickListener
            }

            val password = binding.etPassword.text.toString()

            if (AccountManager.instance.isMultiFactory) {
                if (userType.isNullOrEmpty()) {
                    ToastUtils.showToast("请选择用户类型")
                    return@setOnClickListener
                }
            }

            //sys_user 都不隐藏, 角色信息、工厂、部门这几项非必填
            //main_factor_cqe, 这个角色角色信息、工厂显示, 部门隐藏；工厂必填，角色非必填
            //else 角色信息、工厂、部门隐藏
            if (AccountManager.instance.isMainFactorCqe) {
                if (orgId.isNullOrEmpty()) {
                    ToastUtils.showToast("请选择工厂")
                    return@setOnClickListener
                }
            }

            //部门可以为空
            if (deptId.isNullOrEmpty() || deptId.equals("请选择部门")) {
//                ToastUtils.showToast("请选择部门")
//                return@setOnClickListener

                deptId = null
            }

            user?.let {
                it.userName = userName
                it.nickName = nickname
                it.phonenumber = phoneNumber
                it.idNo = cardId
                it.address = address
                it.expireDate = expiredDate

                accountViewModel.updateUserInfo(it.roleIds, it, orgId, deptId, password, userType)
            }
        }
    }

    private fun showFactoryPopup(target: View, isSingleCheck: Boolean) {
        val popupWindow = FactorySelectPopupWindow(this, binding.root, true, isSingleCheck, factoryInfos)
        popupWindow.setOnClickListener(object : FactorySelectPopupWindow.OnItemClick {

            override fun onSingleClick(factoryInfo: WorkFactoryInfo) {
                orgId = factoryInfo.orgId
            }

            override fun onMultiClick(factoryInfos: MutableList<WorkFactoryInfo>) {
                val checkedList = factoryInfos.filter { it.isChecked }
                orgId = checkedList.joinToString(",") { it.orgId }
                binding.tvFactory.text = checkedList.joinToString(",") { it.orgShortName }
                binding.ivFactoryClose.isVisible = !orgId.isNullOrEmpty() && editable

                if (orgId.isNullOrEmpty()) {
                    binding.tvFactory.text = "请选择工厂"
                }

            }
        })
        popupWindow.showAsDropDown(target, 0, 10)
    }

    private fun toggleEdit() {
        editable = !editable
        updateEditWidgets()
        binding.layoutPasswordRoot.isVisible = editable
    }

    private fun updateEditWidgets() {
        binding.etNickname.isEnabled = editable
        binding.etPhoneNum.isEnabled = editable
        binding.etCardId.isEnabled = editable
        binding.etAddress.isEnabled = editable
        binding.etPassword.isEnabled = editable
        binding.tvDept.isVisible = !editable
        binding.layoutDept.isVisible = editable

        binding.layoutFactory.isEnabled = editable

        binding.tvUserType.isVisible = !editable

        binding.ivFactoryClose.isVisible = !orgId.isNullOrEmpty() && editable
        binding.ivDeptClose.isVisible = !deptId.isNullOrEmpty() && editable

        updateLayoutBackground(binding.layoutNickname)
        updateLayoutBackground(binding.layoutPhoneNum)
        updateLayoutBackground(binding.layoutPassword)
        updateLayoutBackground(binding.layoutCardId)
        updateLayoutBackground(binding.layoutAddress)
        updateLayoutBackground(binding.layoutExpiredDate)
        updateLayoutBackground(binding.layoutFactory)
        updateLayoutBackground(binding.layoutDept)
        updateLayoutBackground(binding.asUserType)

        binding.btnUpdate.isVisible = editable
    }

    private fun updateLayoutBackground(layout: ViewGroup) {
        val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
        layout.background = drawable
    }

    private fun setLast(edit: EditText) {
        val len = edit.text?.length
        edit.setSelection(len ?: 0)
    }

    class UserDetailContracts: ActivityResultContract<InputData, InputData?>() {
        override fun createIntent(context: Context, input: InputData): Intent {
            return Intent(context, UserManagerDetailActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): InputData? {
            val newUser = intent?.getParcelableExtra<InputData>("output")
            return if (resultCode == Activity.RESULT_OK) {
                newUser
            } else null
        }

    }

    @Parcelize
    data class InputData(
        var user: User,
        val pos: Int
    ) : Parcelable
}