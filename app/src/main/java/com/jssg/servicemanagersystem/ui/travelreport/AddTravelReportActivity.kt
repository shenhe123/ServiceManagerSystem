package com.jssg.servicemanagersystem.ui.travelreport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityAddTravelReportBinding
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class AddTravelReportActivity : BaseActivity() {
    private lateinit var travelReportViewModel: TravelReportViewModel
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<WorkDeptInfo>? = null
    private var factoryInfos: List<WorkFactoryInfo>? = null
    private var travelReportInfo: TravelReportInfo? = null
    private lateinit var binding: ActivityAddTravelReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTravelReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelReportInfo = AccountManager.instance.getNewTravelReport()
        travelReportInfo?.let {
            binding.asDept.prompt = it.dept
            binding.asFactory.prompt = it.orgId
            binding.etApplyName.setText(it.applyName)
            binding.etPartner.setText(it.partner)
            binding.etClientName.setText(it.customer)
            binding.etProductCode.setText(it.productCode)
            binding.etProjectCode.setText(it.projectCode)
            binding.etPlaceFrom.setText(it.placeFrom)
            binding.etPlaceTo.setText(it.placeTo)
            binding.etStartDate.setText(it.startDate)
            binding.etEndDate.setText(it.endDate)
            binding.etAddress.setText(it.address)

            binding.etPurpose.setText(it.purpose)
            binding.etMainTask.setText(it.mainTask)
            binding.etExpectedResult.setText(it.expectedResult)
            binding.etSchedule.setText(it.schedule)
        }

        travelReportViewModel = ViewModelProvider(this)[TravelReportViewModel::class.java]
        travelReportViewModel.getFactoryInfo()
        travelReportViewModel.getDeptInfo()

        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.mbtSave.setOnClickListener {
            if (checkParams()) {
                travelReportInfo?.let {
                    AccountManager.instance.saveNewTravelReport(it)
                    ToastUtils.showToast("保存成功")
                }
            }
        }

        binding.mbtSubmit.setOnClickListener {
            if (checkParams()) {

            }
        }

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
                        orgId = it[position].orgShortName
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
                        deptId = it[position].deptName
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun checkParams(): Boolean {
        val nickName = binding.etApplyName.text.toString()
        if (nickName.isEmpty()) {
            ToastUtils.showToast("申请人不能为空")
            return false
        }

        val partner = binding.etPartner.text.toString()

        val clientName = binding.etClientName.text.toString()
        if (clientName.isEmpty()) {
            ToastUtils.showToast("客户名称不能为空")
            return false
        }

        val productCode = binding.etProductCode.text.toString()
        if (productCode.isEmpty()) {
            ToastUtils.showToast("产品编码不能为空")
            return false
        }

        val projectCode = binding.etProjectCode.text.toString()
        if (projectCode.isEmpty()) {
            ToastUtils.showToast("项目编号不能为空")
            return false
        }

        val placeFrom = binding.etPlaceFrom.text.toString()
        if (placeFrom.isEmpty()) {
            ToastUtils.showToast("出差起点不能为空")
            return false
        }

        val placeTo = binding.etPlaceTo.text.toString()
        if (placeTo.isEmpty()) {
            ToastUtils.showToast("出差终点不能为空")
            return false
        }

        val address = binding.etAddress.text.toString()
        if (address.isEmpty()) {
            ToastUtils.showToast("单位地址不能为空")
            return false
        }

        val startDate = binding.etStartDate.text.toString()
        if (startDate.isEmpty()) {
            ToastUtils.showToast("出差起始时间不能为空")
            return false
        }

        val endDate = binding.etEndDate.text.toString()
        if (endDate.isEmpty()) {
            ToastUtils.showToast("出差终止时间不能为空")
            return false
        }

        val purpose = binding.etPurpose.text.toString()
        if (purpose.isEmpty()) {
            ToastUtils.showToast("出差目的不能为空")
            return false
        }

        val mainTask = binding.etMainTask.text.toString()
        if (mainTask.isEmpty()) {
            ToastUtils.showToast("主要任务不能为空")
            return false
        }

        val expectedResult = binding.etExpectedResult.text.toString()
        if (expectedResult.isEmpty()) {
            ToastUtils.showToast("预期结果不能为空")
            return false
        }

        val schedule = binding.etSchedule.text.toString()
        if (schedule.isEmpty()) {
            ToastUtils.showToast("待办事项不能为空")
            return false
        }

        if (deptId.isNullOrEmpty()) {
            ToastUtils.showToast("请选择部门")
            return false
        }

        if (orgId.isNullOrEmpty()) {
            ToastUtils.showToast("请选择工厂")
            return false
        }

        travelReportInfo = TravelReportInfo(
            "",
            orgId!!,
            deptId!!,
            nickName,
            partner,
            clientName,
            productCode,
            projectCode,
            placeFrom,
            placeTo,
            address,
            startDate,
            endDate,
            purpose,
            mainTask,
            expectedResult,
            schedule
        )
        return true
    }

    class AddTravelReportContracts: ActivityResultContract<Any, Boolean?>() {
        override fun createIntent(context: Context, input: Any): Intent {
            return Intent(context, AddTravelReportActivity::class.java)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
            return if (resultCode == Activity.RESULT_OK) intent?.getBooleanExtra("output", false)
            else false
        }

    }
}