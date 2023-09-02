package com.jssg.servicemanagersystem.ui.workorder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityWorkOrderDetailBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo

class WorkOrderDetailActivity : BaseActivity() {
    private var inputData: WorkOrderInfo? = null
    private lateinit var binding: ActivityWorkOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        showBack()

        inputData = intent?.getParcelableExtra("input")
        inputData?.let {
            binding.tvApplyName.text = "申请人：${it.applyName}"
            binding.tvApplyDept.text = "申请部门：${it.applyDept}"
            binding.tvApplyFactory.text = "申请工厂：${it.orgService}"
            binding.tvApplyDate.text = "申请日期：${it.applyDate}"

            binding.tvClientName.text = "服务客户名称：${it.salesManager}"
            binding.tvServiceName.text = "服务人员名称：${it.salesManager}"
            binding.tvTel.text = "联系方式：${it.tel}"
            binding.tvCheckNum.text = "排查数量：${it.checkNumTotal}"
            binding.tvUnitPrice.text = "服务单价：${it.unitPrice}"
            binding.tvServicePeriod.text = "预估服务周期：${it.servicePeriod}"
            binding.tvTotalPrice.text = "预估总费用：${it.totalPrice}"
            binding.tvAddress.text = "服务地点：${it.serviceAdd}"
            binding.tvProductNum.text = "不良数量：${it.productNum}"
            binding.etRemark.setText(it.remark)
        }

        binding.mbtCheckOrder.setOnClickListener {
            inputData?.let {
                WorkOrderCheckActivity.goActivity(this, it)
            }
        }
    }

    companion object {
        fun goActivity(context: Context, input: WorkOrderInfo) {
            context.startActivity(Intent(context, WorkOrderDetailActivity::class.java).apply {
                putExtra("input", input)
            })
        }
    }
}