package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderDetailBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderCheckActivity
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderCheckDetailActivity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

class WorkOrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentWorkOrderDetailBinding
    private var inputData: WorkOrderInfo? = null

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
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initView() {
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

            //已排查的隐藏去排查按钮
            binding.mbtCheckOrder.isVisible = it.checkState != 2
        }

        binding.mbtCheckOrder.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.QM_WORKORDERDETAIL_ADD.name)) return@setOnClickListener

            inputData?.let {
                WorkOrderCheckActivity.goActivity(requireContext(), it)
            }
        }

        binding.mbtCheckOrderDetail.setOnClickListener {
//            inputData?.let {
//                WorkOrderCheckDetailActivity.goActivity(requireContext(), it)
//            }
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