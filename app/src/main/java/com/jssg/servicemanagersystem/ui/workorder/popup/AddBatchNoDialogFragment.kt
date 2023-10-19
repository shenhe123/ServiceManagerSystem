package com.jssg.servicemanagersystem.ui.workorder.popup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.databinding.DialogAddBatchNoLayoutBinding
import com.jssg.servicemanagersystem.databinding.DialogSingleButtonLayoutBinding
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AddBatchNoDialogFragment: BaseDialogFragment() {

    private lateinit var binding: DialogAddBatchNoLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddBatchNoLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener { dismiss() }

        binding.mbtConfirm.setOnClickListener {
            val batchNo = binding.etContent.text.toString()
            if (batchNo.isEmpty()) {
                ToastUtils.showToast("批次号不能为空")
                return@setOnClickListener
            }
            confirmClickLisenter?.onConfirmClick(batchNo)
            dismiss()
        }

        binding.etContent.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val content = s.toString()
                binding.ivContentClose.isVisible = content.isNotEmpty()
            }

        })

        binding.ivContentClose.setOnClickListener {
            binding.etContent.setText("")
            binding.ivContentClose.isVisible = false
        }

    }

    private var confirmClickLisenter: OnConfirmClickLisenter? = null

    fun addConfrimClickLisntener(listener: OnConfirmClickLisenter): AddBatchNoDialogFragment {
        this.confirmClickLisenter = listener
        return this
    }

    interface OnConfirmClickLisenter {
        fun onConfirmClick(batchNo: String)
    }

    companion object {
        fun newInstance(): AddBatchNoDialogFragment {
            val args = Bundle()
            val fragment = AddBatchNoDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}