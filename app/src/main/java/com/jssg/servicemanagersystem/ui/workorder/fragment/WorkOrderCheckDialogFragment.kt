package com.jssg.servicemanagersystem.ui.workorder.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.databinding.FragmentWorkOrderCheckDialogBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.viewmodel.WorkOrderViewModel
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

class WorkOrderCheckDialogFragment : BaseDialogFragment() {
    private lateinit var listener: OnFinishListener
    private lateinit var workOrderViewModel: WorkOrderViewModel
    private lateinit var binding: FragmentWorkOrderCheckDialogBinding

    private var inputData: WorkOrderCheckInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrderCheckDialogBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            inputData = it.getParcelable<WorkOrderCheckInfo>("input")
        }

        binding.ivClose.setOnClickListener { dismiss() }

        binding.mbtConfirm.setOnClickListener {

            if (binding.rgBtn.checkedRadioButtonId == -1) {
                ToastUtils.showToast("请选择审核意见")
                return@setOnClickListener
            }

            var state = "0"
            if (binding.rbAgree.isChecked) {
                state = "0"
            }
            if (binding.rbAgreeNot.isChecked) {
                state = "1"
            }
            if (binding.rbResignation.isChecked) {
                state = "2"
            }

            inputData?.let {
                workOrderViewModel.reviewWorkOrderCheck(it.billDetailNo, binding.etRemark.text.toString(), state)
            }
        }

        workOrderViewModel = ViewModelProvider(this)[WorkOrderViewModel::class.java]

        workOrderViewModel.reviewWorkOrderDetailLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                ToastUtils.showToast("审核提交成功")
                if (this::listener.isInitialized) {
                    listener.onFinish()
                }
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

    }

    fun addOnFinishListener(listener: OnFinishListener): WorkOrderCheckDialogFragment {
        this.listener = listener
        return this
    }

    interface OnFinishListener {
        fun onFinish()
    }

    companion object {
        @JvmStatic
        fun newInstance(input: WorkOrderCheckInfo) =
            WorkOrderCheckDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("input", input)
                }
            }
    }
}