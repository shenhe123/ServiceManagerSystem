package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.AddWorkOrderCheckActivity
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.BigDecimalUtils.bigDecimal
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

class WorkOrderDetailFragment : BaseFragment() {
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderDetailBinding
    private var inputData: WorkOrderInfo? = null

    private val addNewLauncer =
        registerForActivityResult(AddWorkOrderCheckActivity.AddWordOrderDetailContracts()) { newOrder ->
            newOrder?.let {
                if (it) {
                    (requireActivity() as WorkOrderDetailActivity).goBackForResult()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inputData = it.getParcelable("inputData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initViewModel()

        addListener()
    }

    private fun initViewModel() {
        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]

        workOrderViewModel.workOrderInfoLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                result.data?.let {
                    inputData = it
                    initView()

                    checkButtonState(it)
                }
            }
        }

        inputData?.let {
            workOrderViewModel.getWorkOrderInfo(it.billNo)
        }
    }

    private fun checkButtonState(workOrderInfo: WorkOrderInfo) {
        //已排查的隐藏去排查按钮
        // 0 -> = "未开始"
        // 1 -> = "排查中"
        // 2 -> = "已完成"

        val isVisible =
            workOrderInfo.checkState != 2 && RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_ADD.printableName)
        binding.mbtCheckOrder.isVisible = isVisible

        if (isVisible) {
            //已排查的总数 小于 总共需要排查的数量
            val canCheck = workOrderInfo.submitCheckCount.bigDecimal() < workOrderInfo.checkNum.bigDecimal()
            binding.mbtCheckOrder.isEnabled = canCheck
            if (!canCheck) {
                binding.mbtCheckOrder.text = "已排查完"
            }
        }
    }

    private fun addListener() {

        binding.mbtCheckOrder.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_ADD.printableName)) return@setOnClickListener

            inputData?.let {
                addNewLauncer.launch(it)
            }
        }
    }

    private fun initView() {
        inputData?.let {
            binding.tvApplyName.text = "申请人：${it.applyName}"
            binding.tvApplyDept.text = "申请部门：${it.applyDept}"
            binding.tvApplyFactory.text = "申请工厂：${it.orgService}"
            binding.tvApplyDate.text = "申请日期：${it.applyDate}"

            binding.tvProductDesc.text = "问题描述：${it.productDes}"
            binding.tvProductCode.text = "产品编号：${it.productCode}"
            binding.tvProductNum.text = "问题数量：${it.productNum ?: "0"}"

            binding.tvClientName.text = "服务客户名称：${it.customer ?: ""}"
            binding.tvServiceName.text = "服务人员名称：${it.salesManager}"
            binding.tvTel.text = "联系方式：${it.tel}"
            binding.tvCheckNum.text = "总排查数量：${it.checkNum ?: 0}"
            binding.tvUnitPrice.text = "服务单价：${it.unitPrice}"
            binding.tvServicePeriod.text = "预估服务周期：${it.servicePeriod}"
            binding.tvTotalPrice.text = "预估总费用：${it.totalPrice}"
            binding.tvAddress.text = "服务地点：${it.serviceAdd}"
            binding.tvCheckNumTotal.text = "已排查数量：${it.checkNumTotal ?: "0"}"
            binding.etRemark.setText(it.remark)

        }


    }

    companion object {
        @JvmStatic
        fun newInstance(inputData: WorkOrderInfo?) =
            WorkOrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("inputData", inputData)
                }
            }
    }
}