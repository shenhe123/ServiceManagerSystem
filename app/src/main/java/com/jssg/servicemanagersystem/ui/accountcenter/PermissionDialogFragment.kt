package com.jssg.servicemanagersystem.ui.accountcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.databinding.DialogPermissionLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class PermissionDialogFragment: BaseDialogFragment() {

    private lateinit var binding: DialogPermissionLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPermissionLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.mbtConfirm.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(): PermissionDialogFragment {
            val args = Bundle()

            val fragment = PermissionDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}