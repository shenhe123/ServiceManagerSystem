package com.jssg.servicemanagersystem.ui.workorder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityAddWorkOrderBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class AddWorkOrderActivity : BaseActivity() {
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<WorkDeptInfo>? = null
    private var factoryInfos: List<WorkFactoryInfo>? = null
    private lateinit var binding: ActivityAddWorkOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener { finish() }

        initViewModel()

        addListener()
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]

        workOrderViewModel.factoryInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                factoryInfos = result.data
                val list = mutableListOf("请选择工厂")
                factoryInfos?.let {
                    list.addAll(it.map { info -> info.orgShortName })
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asFactory.adapter = adapter
            }
        }

        workOrderViewModel.deptInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                deptInfos = result.data
                val list = mutableListOf("请选择部门")
                deptInfos?.let {
                    list.addAll(it.map { info -> info.deptName })
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asDept.adapter = adapter

                //赋值默认部门
                val defaultDeptName = AccountManager.instance.getUser()?.user?.dept?.deptName
                defaultDeptName?.let {
                    val index = list.indexOf(defaultDeptName)
                    if (index > 0) {
                        binding.asDept.setSelection(index, false)
                        deptId = it
                    } else {
                        binding.asDept.setSelection(0, false)
                    }
                }
            }
        }

        workOrderViewModel.addNewWorkOrderLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("添加成功")
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("output", true)
                })
                finish()
            }
        }

        workOrderViewModel.getFactoryInfo()
        workOrderViewModel.getDeptInfo()
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
                orgId = if (position == 0) {
                    null
                } else {
                    factoryInfos?.get(position - 1)?.orgShortName
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
                deptId = if (position == 0) {
                    null
                } else {
                    deptInfos?.get(position - 1)?.deptName
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.mbtSubmit.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_ADD.printableName)) return@setOnClickListener

            val nickName = binding.etApplyName.text.toString()
            if (nickName.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
                return@setOnClickListener
            }

            if (orgId.isNullOrEmpty()) {
                ToastUtils.showToast("服务工厂不能为空")
                return@setOnClickListener
            }

            if (deptId.isNullOrEmpty()) {
                ToastUtils.showToast("申请部门不能为空")
                return@setOnClickListener
            }

            val phoneNumber = binding.etPhoneNum.text.toString()
            if (phoneNumber.isEmpty()) {
                ToastUtils.showToast("手机号不能为空")
                return@setOnClickListener
            }

            val clientName = binding.etClientName.text.toString()
            if (clientName.isEmpty()) {
                ToastUtils.showToast("客户名称不能为空")
                return@setOnClickListener
            }

            val serviceName = binding.etServiceName.text.toString()
            if (serviceName.isEmpty()) {
                ToastUtils.showToast("服务人员名称不能为空")
                return@setOnClickListener
            }

            val salesManager = binding.etSalesManager.text.toString()
            if (salesManager.isEmpty()) {
                ToastUtils.showToast("销售客服经理不能为空")
                return@setOnClickListener
            }

            val oaBillNo = binding.etOaBillNo.text.toString()

            val productCode = binding.etProductCode.text.toString()
            if (productCode.isEmpty()) {
                ToastUtils.showToast("产品编码不能为空")
                return@setOnClickListener
            }

            val projectCode = binding.etProjectCode.text.toString()
            if (projectCode.isEmpty()) {
                ToastUtils.showToast("产品项目号不能为空")
                return@setOnClickListener
            }

            val productDesc = binding.etProductDesc.text.toString()
            if (productDesc.isEmpty()) {
                ToastUtils.showToast("产品名称不能为空")
                return@setOnClickListener
            }

            val badNum = binding.etBadNum.text.toString()
            if (badNum.isEmpty()) {
                ToastUtils.showToast("不良数量数量不能为空")
                return@setOnClickListener
            }

            val checkNum = binding.etCheckNum.text.toString()
            if (checkNum.isEmpty()) {
                ToastUtils.showToast("排查数量不能为空")
                return@setOnClickListener
            }

            val servicePrice = binding.etServicePrice.text.toString()
            if (servicePrice.isEmpty()) {
                ToastUtils.showToast("服务单价不能为空")
                return@setOnClickListener
            }

            val servicePeriod = binding.etServicePeroid.text.toString()
            if (servicePeriod.isEmpty()) {
                ToastUtils.showToast("预估服务周期不能为空")
                return@setOnClickListener
            }

            val serviceTotal = binding.etServiceTotal.text.toString()
            if (serviceTotal.isEmpty()) {
                ToastUtils.showToast("预估总费用不能为空")
                return@setOnClickListener
            }

            val serviceAddress = binding.etServiceAddress.text.toString()
            if (serviceAddress.isEmpty()) {
                ToastUtils.showToast("服务地点不能为空")
                return@setOnClickListener
            }

            val remark = binding.etRemark.text.toString()
            if (remark.isEmpty()) {
                ToastUtils.showToast("内容描述不能为空")
                return@setOnClickListener
            }

            var priceUnit = binding.etUnitPrice.text.toString()
            if (priceUnit.isEmpty()) priceUnit = "元/小时"

            var periodUnit = binding.etUnitTime.text.toString()
            if (periodUnit.isEmpty()) periodUnit = "小时"
            workOrderViewModel.addNewWorkOrder(
                nickName,
                phoneNumber,
                clientName,
                serviceName,
                salesManager,
                checkNum,
                servicePrice,
                servicePeriod,
                priceUnit,
                periodUnit,
                serviceTotal,
                serviceAddress,
                remark,
                deptId,
                orgId,
                productCode,
                productDesc,
                badNum,
                oaBillNo,
                projectCode
            )
        }

    }

    class AddNewWorkOrderContracts : ActivityResultContract<Any, Boolean?>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, AddWorkOrderActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else false
        }

    }

}