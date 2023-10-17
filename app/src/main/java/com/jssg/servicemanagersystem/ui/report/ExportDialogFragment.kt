package com.jssg.servicemanagersystem.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.databinding.DialogExportLayoutBinding
import com.jssg.servicemanagersystem.databinding.DialogSingleButtonLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class ExportDialogFragment: BaseDialogFragment() {

    private lateinit var binding: DialogExportLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogExportLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener { dismiss() }

        binding.mbtConfirm.setOnClickListener {
            confirmClickLisenter?.onConfrimClick(binding.checkExportPicture.isChecked)
            dismiss()
        }

        val title = arguments?.getString("title")
        val content = arguments?.getString("content")

        title?.let {
            binding.tvTitle.text = it
        }

        content?.let {
            binding.tvContent.text = it
        }

    }

    private var confirmClickLisenter: OnConfirmClickLisenter? = null

    fun addConfrimClickLisntener(listener: OnConfirmClickLisenter): ExportDialogFragment {
        this.confirmClickLisenter = listener
        return this
    }

    interface OnConfirmClickLisenter {
        fun onConfrimClick(isExportPicture: Boolean)
    }

    companion object {
        fun newInstance(title: String, content: String): ExportDialogFragment {
            val args = Bundle()
            args.putString("title", title)
            args.putString("content", content)
            val fragment = ExportDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}