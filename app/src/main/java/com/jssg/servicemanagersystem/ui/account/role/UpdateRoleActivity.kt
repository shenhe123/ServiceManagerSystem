package com.jssg.servicemanagersystem.ui.account.role

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityUpdateRoleBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.role.adapter.AddNewRoleAdapter
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleChildChildEntity
import com.jssg.servicemanagersystem.ui.account.role.entity.RoleParentEntity
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import com.jssg.servicemanagersystem.widgets.decoration.ThemeLineItemDecoration
import kotlinx.android.parcel.Parcelize

class UpdateRoleActivity : BaseActivity() {
    private lateinit var adapter: AddNewRoleAdapter
    private var inputData: InputData? = null
    private var editable: Boolean = false
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: ActivityUpdateRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputData = intent?.getParcelableExtra<InputData>("input")

        inputData?.let {
            binding.etRoleName.setText(it.role.roleName)
            binding.etRemark.setText(it.role.remark)
        }

        addListener()

        initView()

        initViewModel()
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(ThemeLineItemDecoration())

        val mList = initData(inputData?.role)

        adapter = AddNewRoleAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setNewInstance(mList)
    }

    private fun addListener() {

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.mbtConfirm.setOnClickListener {

            val roleName = binding.etRoleName.text.toString()
            val remark = binding.etRemark.text.toString()

            if (roleName.isEmpty()) {
                ToastUtils.showToast("角色名称不能为空")
                return@setOnClickListener
            }

            val menuIds = mutableListOf<Long>()
            adapter.data.forEach {
                if (it is RoleParentEntity) {
                    it.childNode?.forEach { child ->
                        if ((child as RoleChildChildEntity).checked) {
                            menuIds.add(child.id)
                        }
                    }
                }
            }

            if (menuIds.isEmpty()) {
                ToastUtils.showToast("请选择菜单权限")
                return@setOnClickListener
            }

            inputData?.let {
                it.role.roleName = roleName
                it.role.remark = remark
                it.role.menuIds = menuIds
                accountViewModel.updateRoleInfo(menuIds, it.role)
            }
        }

    }

    private fun initViewModel() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        accountViewModel.updateRoleInfoLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("新增角色成功")
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("output", inputData)
                })
                finish()
            }

        }
    }

    private fun initData(role: Role?): MutableList<BaseNode> {
        val list = mutableListOf<BaseNode>()

        //派工单
        val workOrderDetailP = RoleParentEntity("派工单管理")
        workOrderDetailP.isExpanded = false
        val workOrderDetailTitles = arrayOf(
            "派工单明细新增",
            "派工单明细修改",
            "派工单明细查询",
            "派工单明细删除",
            "派工单明细审核"
        )
        val workOrderDetailIds = arrayOf(
            1692454085873725443,
            1692454085873725444,
            1692454085873725442,
            1692454085873725445,
            1695608263517618177
        )

        if (workOrderDetailIds.all { role?.menuIds?.contains(it) == true }) {
            workOrderDetailP.checked = true
        }
        val workOrderDetailC = mutableListOf<BaseNode>()
        for (i in workOrderDetailTitles.indices) {
            workOrderDetailC.add(RoleChildChildEntity(workOrderDetailTitles[i], workOrderDetailIds[i], role?.menuIds?.contains(workOrderDetailIds[i]) == true))
        }
        workOrderDetailP.childNode = workOrderDetailC
        list.add(workOrderDetailP)

        //工单
        val workOrderP = RoleParentEntity("工单管理")
        workOrderP.isExpanded = false
        val workOrderTitles = arrayOf(
            "工单新增",
            "工单审核",
            "工单修改",
            "工单查询",
            "工单删除"
        )
        val workOrderIds = arrayOf(
            1692454085341048835,
            1695608092855582721,
            1692454085341048836,
            1692454085341048834,
            1692454085341048837
        )
        if (workOrderIds.all { role?.menuIds?.contains(it) == true }) {
            workOrderP.checked = true
        }
        val workOrderC = mutableListOf<BaseNode>()
        for (i in workOrderTitles.indices) {
            workOrderC.add(RoleChildChildEntity(workOrderTitles[i], workOrderIds[i], role?.menuIds?.contains(workOrderIds[i]) == true))
        }
        workOrderP.childNode = workOrderC
        list.add(workOrderP)

        //出差报告
        val tripReportP = RoleParentEntity("出差报告")
        tripReportP.isExpanded = false
        val tripReportTitles = arrayOf(
            "出差报告新增",
            "出差报告修改",
            "出差报告查询",
            "出差报告删除"
        )
        val tripReportIds = arrayOf(
            1692454084808372227,
            1692454084808372228,
            1692454084808372226,
            1692454084808372229
        )

        if (tripReportIds.all { role?.menuIds?.contains(it) == true }) {
            tripReportP.checked = true
        }
        val tripReportC = mutableListOf<BaseNode>()
        for (i in tripReportTitles.indices) {
            tripReportC.add(RoleChildChildEntity(tripReportTitles[i], tripReportIds[i], role?.menuIds?.contains(tripReportIds[i]) == true))
        }
        tripReportP.childNode = tripReportC
        list.add(tripReportP)

        //日志管理
        val logP = RoleParentEntity("日志管理")
        logP.isExpanded = false
        val logTitles = arrayOf(
            "派工单变更日志新增",
            "派工单变更日志修改",
            "派工单变更日志查询",
            "派工单变更日志删除"
        )
        val logIds = arrayOf<Long>(
            1692454086473510915,
            1692454086473510916,
            1692454086473510914,
            1692454086473510917
        )

        if (logIds.all { role?.menuIds?.contains(it) == true }) {
            logP.checked = true
        }
        val logC = mutableListOf<BaseNode>()
        for (i in logTitles.indices) {
            logC.add(RoleChildChildEntity(logTitles[i], logIds[i], role?.menuIds?.contains(logIds[i]) == true))
        }
        logP.childNode = logC
        list.add(logP)

        //系统管理
        val systemP = RoleParentEntity("系统管理")
        systemP.isExpanded = false
        val systemTitles = arrayOf(
            "登录日志查询",
            "用户查询",
            "用户新增",
            "用户修改",
            "用户删除",
            "重置密码"
        )
        val systemIds = arrayOf<Long>(
            1043,
            1001,
            1002,
            1003,
            1004,
            1007
        )

        if (systemIds.all { role?.menuIds?.contains(it) == true }) {
            systemP.checked = true
        }
        val systemC = mutableListOf<BaseNode>()
        for (i in systemTitles.indices) {
            systemC.add(RoleChildChildEntity(systemTitles[i], systemIds[i], role?.menuIds?.contains(systemIds[i]) == true))
        }
        systemP.childNode = systemC
        list.add(systemP)

        return list
    }

    class UpdateRoleContracts: ActivityResultContract<InputData, InputData?>() {
        override fun createIntent(context: Context, input: InputData): Intent {
            return Intent(context, UpdateRoleActivity::class.java).apply {
                putExtra("input", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): InputData? {
            return if (resultCode == Activity.RESULT_OK) intent?.getParcelableExtra("output")
            else null
        }

    }

    @Parcelize
    data class InputData(
        val position: Int,
        val role: Role
    ) : Parcelable
}