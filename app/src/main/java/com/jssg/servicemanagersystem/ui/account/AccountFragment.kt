package com.jssg.servicemanagersystem.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.ui.login.LoginActivity
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.databinding.FragmentAccountLayoutBinding
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.login.LoginViewModel

class AccountFragment : BaseFragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentAccountLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountLayoutBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.logoutLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                AccountManager.instance.logout()
                LoginActivity.goActivity(requireActivity())
            }
        }

        binding.tvProfileInfo.setOnClickListener {
            ProfileInfoActivity.goActivity(requireActivity())
        }

        binding.tvSystemSetting.setOnClickListener {
            SystemSettingActivity.goActivity(requireActivity())
        }

        binding.mbtLogout.setOnClickListener {
            SingleBtnDialogFragment.newInstance("退出登录", "确定要退出登录吗？")
                .addConfrimClickLisntener(object :SingleBtnDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick() {
                        loginViewModel.logout()
                    }

                })
                .show(childFragmentManager, "logout_dialog")
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