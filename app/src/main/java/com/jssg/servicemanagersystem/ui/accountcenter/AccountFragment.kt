package com.jssg.servicemanagersystem.ui.accountcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.databinding.FragmentAccountLayoutBinding

class AccountFragment : BaseFragment() {

    private lateinit var binding: FragmentAccountLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProfileInfo.setOnClickListener {
            ProfileInfoActivity.goActivity(requireActivity())
        }

        binding.tvSystemSetting.setOnClickListener {
            SystemSettingActivity.goActivity(requireActivity())
        }
    }

    companion object {
        fun newInstance(): AccountFragment {
            val args = Bundle()

            val fragment = AccountFragment()
            fragment.arguments = args
            return fragment
        }
    }
}