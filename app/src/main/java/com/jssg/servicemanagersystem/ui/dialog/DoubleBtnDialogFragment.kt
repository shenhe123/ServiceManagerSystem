package com.jssg.servicemanagersystem.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.databinding.DialogDoubleButtonLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class DoubleBtnDialogFragment : BaseDialogFragment() {

    private lateinit var binding: DialogDoubleButtonLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDoubleButtonLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener { dismiss() }

        binding.mbtCancel.setOnClickListener {
            cancelClickLisenter?.onCancelClick()
            dismiss()
        }

        binding.mbtConfirm.setOnClickListener {
            confirmClickLisenter?.onConfrimClick()
            dismiss()
        }

        val title = arguments?.getString("title")
        val content = arguments?.getString("content")
        val cancelTxt = arguments?.getString("cancelTxt")
        val confirmTxt = arguments?.getString("confirmTxt")

        title?.let {
            binding.tvTitle.text = it
        }

        content?.let {
            binding.tvContent.text = it
        }

        cancelTxt?.let {
            binding.mbtCancel.text = it
        }

        confirmTxt?.let {
            binding.mbtConfirm.text = it
        }

    }

    private var confirmClickLisenter: OnConfirmClickLisenter? = null

    fun addConfirmClickLisntener(listener: OnConfirmClickLisenter): DoubleBtnDialogFragment {
        this.confirmClickLisenter = listener
        return this
    }

    private var cancelClickLisenter: OnCancelClickLisenter? = null

    fun addCancelClickLisntener(listener: OnCancelClickLisenter): DoubleBtnDialogFragment {
        this.cancelClickLisenter = listener
        return this
    }

    interface OnConfirmClickLisenter {
        fun onConfrimClick()
    }

    interface OnCancelClickLisenter {
        fun onCancelClick()
    }

    companion object {
        fun newInstance(
            title: String,
            content: String,
            cancelTxt: String = "取消",
            confirmTxt: String = "确定"
        ): DoubleBtnDialogFragment {
            val args = Bundle()
            args.putString("title", title)
            args.putString("content", content)
            args.putString("cancelTxt", cancelTxt)
            args.putString("confirmTxt", confirmTxt)
            val fragment = DoubleBtnDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}