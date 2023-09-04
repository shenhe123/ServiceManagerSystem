package com.jssg.servicemanagersystem.ui.travelreport

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.ActivityAddTravelReportBinding
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import java.util.Calendar

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

        initData()

        addListener()

        initViewModel()

    }

    private fun initViewModel() {
        travelReportViewModel = ViewModelProvider(this)[TravelReportViewModel::class.java]

        travelReportViewModel.factoryInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                val list = if (result.data.isNullOrEmpty()) {
                    listOf("请选择工厂")
                } else {
                    factoryInfos = result.data!!
                    result.data!!.map { info -> info.orgShortName }
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asFactory.adapter = adapter
            }
        }

        travelReportViewModel.deptInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                val list = if (result.data.isNullOrEmpty()) {
                    listOf("请选择部门")
                } else {
                    deptInfos = result.data!!
                    result.data!!.map { info -> info.deptName }
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.asDept.adapter = adapter
            }
        }

        travelReportViewModel.getFactoryInfo()
        travelReportViewModel.getDeptInfo()

    }

    private fun initData() {
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
            binding.etStartDate.text = it.startDate
            binding.etEndDate.setText(it.endDate)
            binding.etAddress.setText(it.address)

            binding.etPurpose.setText(it.purpose)
            binding.etMainTask.setText(it.mainTask)
            binding.etExpectedResult.setText(it.expectedResult)
            binding.etSchedule.setText(it.schedule)
        }
    }

    private fun addListener() {

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
                travelReportInfo?.let {
                    travelReportViewModel.addNewTravelReport(it)
                }
            }
        }

        binding.etStartDate.setOnClickListener {
            showSelectDateDialog(binding.etStartDate)
        }

        binding.layoutEndDate.setOnClickListener {
            showSelectDateDialog(binding.etEndDate)
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

    private fun showSelectDateDialog(textView: TextView) {
        val calendar = Calendar.getInstance() //获取日期格式器对象

        //chose b
        val pvTime: TimeDialogFragment =
            TimePickerBuilder(
                this@AddTravelReportActivity
            ) { date -> //选中事件回调
                textView.setText(DateUtil.getFullData(date.time))
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
                .setSubmitText(getString(com.jssg.servicemanagersystem.R.string.app_confirm)) //确认按钮文字
                .setContentTextSize(18) //滚轮文字大小
                .setTitleSize(18) //标题文字大小
                .setTitleText("选择过期时间") //标题文字
                .isCyclic(true) //是否循环滚动
                .setTextColorCenter(getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //设置选中项的颜色
                .setTitleColor(getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //标题文字颜色
                .setSubmitColor(getColor(com.jssg.servicemanagersystem.R.color.purple_700)) //确定按钮文字颜色
                .setCancelColor(getColor(com.jssg.servicemanagersystem.R.color.x_text_01)) //取消按钮文字颜色
                .setTitleBgColor(getColor(com.jssg.servicemanagersystem.R.color.white)) //标题背景颜色 Night mode
                .setBgColor(getColor(com.jssg.servicemanagersystem.R.color.white)) //滚轮背景颜色 Night mode
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