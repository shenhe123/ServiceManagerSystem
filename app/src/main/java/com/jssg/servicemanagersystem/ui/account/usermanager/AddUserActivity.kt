package com.jssg.servicemanagersystem.ui.account.usermanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityAddUserBinding
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.ScreenUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import java.util.Calendar

class AddUserActivity : BaseActivity() {
    private var userType: String? = null
    private var userTypeArray: Array<String> = arrayOf()
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<DeptInfo>? = null
    private var factoryInfos: MutableList<WorkFactoryInfo> = mutableListOf()
    private var checkedRoleIds: MutableList<String>? = null
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        //sys_user 都不隐藏, 角色信息、工厂、部门这几项非必填
        //main_factor_cqe, 这个角色角色信息、工厂显示, 部门隐藏；工厂必填，角色非必填
        //else 角色信息、工厂、部门隐藏
        if (AccountManager.instance.isSysUser) {
            binding.layoutUserType.isVisible = true
            userTypeArray = resources.getStringArray(R.array.sys_user)

            binding.layoutRolesRoot.isVisible = true
            binding.layoutFactoryRoot.isVisible = true
            binding.layoutDept.isVisible = true
        } else if (AccountManager.instance.isMainFactorCqe) {
            binding.layoutUserType.isVisible = true
            userTypeArray = resources.getStringArray(R.array.main_factor_cqe)

            binding.layoutRolesRoot.isVisible = true
            binding.layoutFactoryRoot.isVisible = true
            binding.layoutDept.isVisible = false
        } else {
            binding.layoutUserType.isVisible = false

            binding.layoutRolesRoot.isVisible = false
            binding.layoutFactoryRoot.isVisible = false
            binding.layoutDept.isVisible = false
        }

        if (userTypeArray.isNotEmpty()) {
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, R.layout.simple_spinner_left_item, userTypeArray
            )
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_left_item)
            binding.asUserType.adapter = adapter
        }

        initViewModel()

        addListener()
    }

    private fun addListener() {
        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.ivFactoryClose.setOnClickListener {
            binding.ivFactoryClose.isVisible = false
            binding.tvFactory.text = "请选择工厂"
            orgId = ""

            factoryInfos.forEach { it.isChecked = false }
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

        binding.tvFactory.setOnClickListener {
            showFactoryPopup(binding.layoutFactory, !AccountManager.instance.isMultiFactory)
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

                binding.ivDeptClose.isVisible = !deptId.isNullOrEmpty()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.ivDeptClose.setOnClickListener {
            binding.ivDeptClose.isVisible = false
            deptId = ""

            binding.asDept.setSelection(0, false)
        }

        binding.mbtSubmit.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            if (userName.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
                return@setOnClickListener
            }
            val nickName = binding.etNickname.text.toString()
            if (nickName.isEmpty()) {
                ToastUtils.showToast("姓名不能为空")
                return@setOnClickListener
            }

            val phoneNumber = binding.etPhoneNum.text.toString()
            if (phoneNumber.isEmpty()) {
                ToastUtils.showToast("手机号不能为空")
                return@setOnClickListener
            }

            val password = binding.etPassword.text.toString()
            if (password.isEmpty()) {
                ToastUtils.showToast("密码不能为空")
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
                ToastUtils.showToast("请选择有效日期")
                return@setOnClickListener
            }

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

            accountViewModel.addNewUser(
                userName,
                nickName,
                phoneNumber,
                password,
                cardId,
                address,
                expiredDate,
                checkedRoleIds,
                orgId,
                deptId,
                userType
            )
        }

        binding.tvExpiredDate.setOnClickListener {
            val calendar = Calendar.getInstance() //获取日期格式器对象

            //chose b
            val pvTime: TimeDialogFragment =
                TimePickerBuilder(
                    this@AddUserActivity
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
    }

    private fun showFactoryPopup(target: View, isSingleCheck: Boolean) {
        val popupWindow =
            FactorySelectPopupWindow(this, binding.root, false, isSingleCheck, factoryInfos)
        popupWindow.setOnClickListener(object : FactorySelectPopupWindow.OnItemClick {

            override fun onSingleClick(factoryInfo: WorkFactoryInfo) {
                orgId = factoryInfo.orgId
            }

            override fun onMultiClick(factoryInfos: MutableList<WorkFactoryInfo>) {
                val checkedList = factoryInfos.filter { it.isChecked }
                orgId = checkedList.joinToString(",") { it.orgId }
                binding.tvFactory.text = checkedList.joinToString(",") { it.orgShortName }
                binding.ivFactoryClose.isVisible = !orgId.isNullOrEmpty()

                if (orgId.isNullOrEmpty()) {
                    binding.tvFactory.text = "请选择工厂"
                }

            }
        })

        if (factoryInfos.size <= 5) {
            popupWindow.showAsDropDown(target, 0, 10)
        } else {
            popupWindow.showAsDropDown(target, 0, -DpPxUtils.dip2px(this, 200f))
        }
//        popupWindow.showAsDropDown(target, 0, -DpPxUtils.dip2px(this, 200f))
//        popupWindow.showAsDropDown(target, 0, 10)

//        val rectF = ScreenUtils.calcViewScreenLocation(target)
//        popupWindow.showAtLocation(
//            target,
//            Gravity.NO_GRAVITY ,
//            rectF.left.toInt(), rectF.top.toInt()
//        )
    }

    private fun initRoleData(roleList: List<Role>) {
        checkedRoleIds = mutableListOf()
        binding.layoutRoles.removeAllViews()
        //展示除管理员的全部角色信息，并选中当前用户的角色信息
        roleList.filter { !it.admin && it.attachToApp.equals("Y") }
            .forEach {
                binding.layoutRoles.addView(addCheckBoxWidget(it))
            }
    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.userRoleListLiveData.observe(this) { result ->
            if (!result.isLoading) {
                result.data?.let {
                    initRoleData(it.roles)
                }
            }
        }

        accountViewModel.addNewUserLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("添加成功")
                setResult(Activity.RESULT_OK)

                finish()
            }
        }

        accountViewModel.factoryInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    factoryInfos.addAll(it)
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
                    this, R.layout.simple_spinner_left_item, list
                )
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_left_item)
                binding.asDept.adapter = adapter
            }
        }

//        if (isFactoryUser) {
//            binding.layoutRolesRoot.isVisible = false
//            binding.layoutFactory.isVisible = false
//            binding.layoutDept.isVisible = false
//        } else {
        accountViewModel.getUserRoleList()
        accountViewModel.getFactoryInfo()
        accountViewModel.getDeptInfo()
//        }
    }

    private fun addCheckBoxWidget(role: Role): AppCompatCheckBox {
        val checkBox = AppCompatCheckBox(this)
        checkBox.layoutParams = ViewGroup.LayoutParams(-2, DpPxUtils.dip2px(this, 25f))
        checkBox.text = role.roleName
        checkBox.tag = role.roleId
        checkBox.setTextColor(AppCompatResources.getColorStateList(this, R.color.x_text_01))

        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                checkedRoleIds?.add(role.roleId)
            } else {
                if (checkedRoleIds?.contains(role.roleId) == true) {
                    checkedRoleIds?.remove(role.roleId)
                }
            }
        }
        return checkBox
    }


    class AddUserContracts : ActivityResultContract<Any, Boolean>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, AddUserActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }

    }
}