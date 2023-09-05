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
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityAddUserBinding
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.FactoryInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import java.math.BigDecimal
import java.util.Calendar

class AddUserActivity : BaseActivity() {
    private var isFactoryUser: Boolean = false
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<DeptInfo>? = null
    private var factoryInfos: MutableList<FactoryInfo> = mutableListOf()
    private var checkedRoleIds: MutableList<String>? = null
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val user = AccountManager.instance.getUser()
        isFactoryUser = user?.user?.userType?.equals("factory_user", true) == true

        initViewModel()

        addListener()
    }

    private fun addListener() {
        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.asFactory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                binding.asFactory.prompt = factoryInfos[position].orgName
                orgId = if (factoryInfos[position].orgName.equals("请选择工厂")) {
                    null
                } else {
                    factoryInfos[position].orgId
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.asDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                deptInfos?.let {
                    binding.asDept.prompt = it[position].label

                    if (!it[position].label.equals("请选择部门")) {
                        deptId = it[position].id
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.mbtSubmit.setOnClickListener {
            val nickName = binding.etNickname.text.toString()
            if (nickName.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
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
            if (address.isEmpty()) {
                ToastUtils.showToast("居住地址不能为空")
                return@setOnClickListener
            }

            val expiredDate = binding.tvExpiredDate.text.toString()
            if (expiredDate.isEmpty()) {
                ToastUtils.showToast("请选择有效日期")
                return@setOnClickListener
            }

            //工厂用户 不用判断角色信息、工厂、部门
            if (!isFactoryUser) {
                if (checkedRoleIds.isNullOrEmpty()) {
                    ToastUtils.showToast("请选择角色信息")
                    return@setOnClickListener
                }
            }

            accountViewModel.addNewUser(nickName, phoneNumber, password, cardId, address, expiredDate, checkedRoleIds, orgId, deptId)
        }

        binding.tvExpiredDate.setOnClickListener {
            val calendar = Calendar.getInstance() //获取日期格式器对象

            //chose b
            val pvTime: TimeDialogFragment =
                TimePickerBuilder(this@AddUserActivity
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
                    factoryInfos.add(FactoryInfo("", "请选择工厂"))
                    factoryInfos.addAll(it)

                    val list = mutableListOf<String>()
                    list.add("请选择工厂")
                    list.addAll(result.data!!.map { info -> info.orgName })
                    val adapter = ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, list
                    )
                    adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                    binding.asFactory.adapter = adapter
                }
            }
        }

        accountViewModel.deptInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                val list = if (result.data.isNullOrEmpty()) {
                    listOf("请选择部门")
                } else {
                    deptInfos = result.data!!
                    result.data!!.map { info -> info.label }
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asDept.adapter = adapter
            }
        }

        if (isFactoryUser) {
            binding.layoutRolesRoot.isVisible = false
            binding.layoutFactory.isVisible = false
            binding.layoutDept.isVisible = false
        } else {
            accountViewModel.getUserRoleList()
            accountViewModel.getFactoryInfo()
            accountViewModel.getDeptInfo()
        }
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


    class AddUserContracts: ActivityResultContract<Any, Boolean>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, AddUserActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }

    }
}