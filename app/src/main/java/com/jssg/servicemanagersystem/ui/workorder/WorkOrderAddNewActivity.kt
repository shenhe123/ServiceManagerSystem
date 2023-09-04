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
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderAddNewBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class WorkOrderAddNewActivity : BaseActivity() {
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<WorkDeptInfo>? = null
    private var factoryInfos: List<WorkFactoryInfo>? = null
    private lateinit var binding: ActivityWorkOrderAddNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkOrderAddNewBinding.inflate(layoutInflater)
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
                val list = if (result.data.isNullOrEmpty()) {
                    listOf("请选择工厂")
                } else {
                    factoryInfos = result.data!!
                    result.data!!.map { info -> info.orgShortName }
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
                val list = if (result.data.isNullOrEmpty()) {
                    listOf("请选择部门")
                } else {
                    deptInfos = result.data!!
                    result.data!!.map { info -> info.deptName }
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asDept.adapter = adapter
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
                factoryInfos?.let {
                    binding.asFactory.prompt = it[position].orgShortName

                    if (!it[position].orgName.equals("请选择工厂")) {
                        orgId = it[position].orgId
                    }

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
                    binding.asDept.prompt = it[position].deptName

                    if (!it[position].deptName.equals("请选择部门")) {
                        deptId = it[position].deptId.toString()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.mbtSubmit.setOnClickListener {

            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDER_ADD.name)) return@setOnClickListener

            val nickName = binding.etApplyName.text.toString()
            if (nickName.isEmpty()) {
                ToastUtils.showToast("用户名不能为空")
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

            val productCode = binding.etProductCode.text.toString()
            if (productCode.isEmpty()) {
                ToastUtils.showToast("产品编号不能为空")
                return@setOnClickListener
            }

            val productDesc = binding.etProductDesc.text.toString()
            if (productDesc.isEmpty()) {
                ToastUtils.showToast("产品问题描述不能为空")
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


            workOrderViewModel.addNewWorkOrder(
                nickName,
                phoneNumber,
                clientName,
                serviceName,
                checkNum,
                servicePrice,
                servicePeriod,
                serviceTotal,
                serviceAddress,
                remark,
                orgId,
                deptId,
                productCode,
                productDesc,
                badNum
            )

//            workOrderViewModel.addNewWorkOrder()
        }

    }

    class AddNewWorkOrderContracts : ActivityResultContract<Any, Boolean?>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, WorkOrderAddNewActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else false
        }

    }

}