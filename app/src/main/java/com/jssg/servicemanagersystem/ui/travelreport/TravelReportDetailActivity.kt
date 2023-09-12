package com.jssg.servicemanagersystem.ui.travelreport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimeDialogFragment
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityTravelReportDetailBinding
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.travelreport.viewmodel.TravelReportViewModel
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import kotlinx.android.parcel.Parcelize
import java.util.Calendar

class TravelReportDetailActivity : BaseActivity() {
    private var travelReportInputData: TravelReportInputData? = null
    private var deptId: String? = null
    private var orgId: String? = null
    private var deptInfos: List<WorkDeptInfo>? = null
    private var factoryInfos: List<WorkFactoryInfo>? = null
    private var editable: Boolean = false
    private lateinit var travelReportViewModel: TravelReportViewModel
    private var inputData: TravelReportInfo? = null
    private lateinit var binding: ActivityTravelReportDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTravelReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addListener()

        travelReportInputData = intent?.getParcelableExtra("inputData")
        inputData = travelReportInputData?.inputData
        inputData?.let {
            updateTravelReportInfo(it)
        }

        initViewModel()
    }

    private fun initViewModel() {
        travelReportViewModel = ViewModelProvider(this)[TravelReportViewModel::class.java]

        travelReportViewModel.travelReportInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    updateTravelReportInfo(it)
                }
            }
        }

        travelReportViewModel.factoryInfoLiveData.observe(this) { result ->
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
                binding.acsFactory.adapter = adapter

                //初始化工厂默认值
                inputData?.orgId?.let {
                    val pos = list.indexOf(it)
                    if (pos != -1) {
                        binding.acsFactory.setSelection(pos, false)
                    }
                }

                binding.acsFactory.prompt = inputData?.orgId ?: "请选择工厂"
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
                    this, android.R.layout.simple_spinner_item, list
                )
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.acsDept.adapter = adapter

                //初始化部门默认值
                inputData?.dept?.let {
                    val pos = list.indexOf(it)
                    if (pos != -1) {
                        binding.acsDept.setSelection(pos, false)
                    }
                }

                binding.acsDept.prompt = inputData?.orgId ?: "请选择部门"
            }
        }

        travelReportViewModel.updateTravelReportLiveData.observe(this) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                ToastUtils.showToast("更新成功")

                binding.tvFactory.text = binding.acsFactory.prompt
                binding.tvDept.text = binding.acsDept.prompt

                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("output", travelReportInputData)
                })
            }
        }

        inputData?.let {
            travelReportViewModel.getTravelReportInfo(it.billNo)
        }

        travelReportViewModel.getFactoryInfo()
        travelReportViewModel.getDeptInfo()
    }

    private fun updateTravelReportInfo(it: TravelReportInfo) {
        binding.tvFactory.text = it.orgId
        orgId = it.orgId
        binding.tvDept.text = it.dept
        deptId = it.dept
        binding.etApplyName.setText(it.applyName)
        binding.etPartner.setText(it.partner)
        binding.etCustomer.setText(it.customer)
        binding.etProductCode.setText(it.productCode)
        binding.etProjectCode.setText(it.projectCode)
        binding.etPlaceFrom.setText(it.placeFrom)
        binding.etPlaceTo.setText(it.placeTo)
        binding.tvStartDate.text = it.startDate
        binding.tvEndDate.text = it.endDate
        binding.etAddress.setText(it.address)

        binding.etPurpose.setText(it.purpose)
        binding.etMainTask.setText(it.mainTask)
        binding.etExpectedResult.setText(it.expectedResult)
        binding.etSchedule.setText(it.schedule)
    }

    private fun addListener() {
        binding.toolBar.setNavigationOnClickListener { finish() }

        binding.tvEdit.setOnClickListener {
            editable = !editable
            updateViewStatus()
        }

        binding.acsFactory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                factoryInfos?.let {
                    binding.acsFactory.prompt = it[position].orgName
                    orgId = if (it[position].orgName.equals("请选择工厂")) {
                        null
                    } else {
                        it[position].orgName
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.acsDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                deptInfos?.let {
                    binding.acsDept.prompt = it[position].deptName

                    deptId = if (!it[position].deptName.equals("请选择部门")) {
                        it[position].deptName
                    } else {
                        null
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.btnUpdate.setOnClickListener{
            if (checkParams()) {
                inputData?.let {
                    it.orgId = orgId
                    it.dept = deptId!!
                    it.applyName = binding.etApplyName.text.toString()
                    it.partner = binding.etPartner.text.toString()
                    it.customer = binding.etCustomer.text.toString()
                    it.productCode = binding.etProductCode.text.toString()
                    it.projectCode = binding.etProjectCode.text.toString()
                    it.placeFrom = binding.etPlaceFrom.text.toString()
                    it.placeTo = binding.etPlaceTo.text.toString()
                    it.address = binding.etAddress.text.toString()
                    it.startDate = binding.tvStartDate.text.toString()
                    it.endDate = binding.tvEndDate.text.toString()
                    it.purpose = binding.etPurpose.text.toString()
                    it.mainTask = binding.etMainTask.text.toString()
                    it.expectedResult = binding.etExpectedResult.text.toString()
                    it.schedule = binding.etSchedule.text.toString()

                    travelReportViewModel.updateTravelReport(it)
                }
            }
        }

        binding.tvStartDate.setOnClickListener {
            showSelectDateDialog(binding.tvStartDate)
        }

        binding.tvEndDate.setOnClickListener {
            showSelectDateDialog(binding.tvEndDate)
        }

        binding.tvStartDate.isClickable = false
        binding.tvEndDate.isClickable = false
    }

    private fun showSelectDateDialog(textView: TextView) {
        val calendar = Calendar.getInstance() //获取日期格式器对象

        //chose b
        val pvTime: TimeDialogFragment =
            TimePickerBuilder(
                this@TravelReportDetailActivity
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

    private fun checkParams(): Boolean {
        val nickName = binding.etApplyName.text.toString()
        if (nickName.isEmpty()) {
            ToastUtils.showToast("申请人不能为空")
            return false
        }

        val partner = binding.etPartner.text.toString()

        val clientName = binding.etCustomer.text.toString()
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

        val startDate = binding.tvStartDate.text.toString()
        if (startDate.isEmpty()) {
            ToastUtils.showToast("出差起始时间不能为空")
            return false
        }

        val endDate = binding.tvEndDate.text.toString()
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


        return true
    }

    private fun updateViewStatus() {

//        binding.etApplyName.isEnabled = editable
        binding.etPartner.isEnabled = editable
        binding.etCustomer.isEnabled = editable
        binding.etProductCode.isEnabled = editable
        binding.etProjectCode.isEnabled = editable
        binding.etPlaceFrom.isEnabled = editable
        binding.etPlaceTo.isEnabled = editable
        binding.etAddress.isEnabled = editable

        binding.etPurpose.isEnabled = editable
        binding.etMainTask.isEnabled = editable
        binding.etExpectedResult.isEnabled = editable
        binding.etSchedule.isEnabled = editable

        binding.tvStartDate.isClickable = editable
        binding.tvEndDate.isClickable = editable

        updateLayoutBackground(binding.etApplyName)
        updateLayoutBackground(binding.etPartner)
        updateLayoutBackground(binding.etCustomer)
        updateLayoutBackground(binding.etProductCode)
        updateLayoutBackground(binding.etProjectCode)
        updateLayoutBackground(binding.etPlaceFrom)
        updateLayoutBackground(binding.etPlaceTo)
        updateLayoutBackground(binding.etAddress)
        updateLayoutBackground(binding.tvStartDate)
        updateLayoutBackground(binding.tvEndDate)

        binding.tvFactory.isVisible = !editable
        binding.tvDept.isVisible = !editable
        binding.acsFactory.isVisible = editable
        binding.acsDept.isVisible = editable

        binding.btnUpdate.isVisible = editable
    }

    private fun updateLayoutBackground(textView: TextView) {
        val drawable = if (editable) ResourcesCompat.getDrawable(resources, R.drawable.selector_input_stroke, null) else null
        textView.background = drawable
    }

    class TravelReportDetailContracts: ActivityResultContract<TravelReportInputData, TravelReportInputData?>() {
        override fun createIntent(context: Context, input: TravelReportInputData): Intent {
            return Intent(context, TravelReportDetailActivity::class.java).apply {
                putExtra("inputData", input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): TravelReportInputData? {
            return if (resultCode == Activity.RESULT_OK) intent?.getParcelableExtra("output")
            else null
        }

    }

    @Parcelize
    data class TravelReportInputData(
        val inputData: TravelReportInfo,
        val pos: Int
    ) : Parcelable
}