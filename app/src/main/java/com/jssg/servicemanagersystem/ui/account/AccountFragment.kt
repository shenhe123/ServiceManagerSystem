package com.jssg.servicemanagersystem.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.base.BaseFragment
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.FragmentAccountLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.MenuEnum
import com.jssg.servicemanagersystem.ui.account.network.ChooseHostActivity
import com.jssg.servicemanagersystem.ui.account.profile.ProfileInfoActivity
import com.jssg.servicemanagersystem.ui.account.role.RoleManagerActivity
import com.jssg.servicemanagersystem.ui.account.updatepwd.UpdatePasswordActivity
import com.jssg.servicemanagersystem.ui.account.usermanager.UserManagerActivity
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.ui.dialog.SingleBtnDialogFragment
import com.jssg.servicemanagersystem.ui.login.LoginActivity
import com.jssg.servicemanagersystem.ui.login.LoginViewModel
import com.jssg.servicemanagersystem.utils.RolePermissionUtils

class AccountFragment : BaseFragment() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentAccountLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountLayoutBinding.inflate(inflater, container, false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvVersion.text = "v${BuildConfig.VERSION_NAME}"

        loginViewModel.logoutLiveData.observe(viewLifecycleOwner) { result ->
            updateLoading(result, true)
            if (result.isSuccess) {
                AccountManager.instance.logout()
                LoginActivity.goActivity(requireActivity())
            }
        }

        accountViewModel.userProfileLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                judgeRolePermission()
            }
        }

        addListener()
    }

    override fun onResume() {
        super.onResume()
        judgeRolePermission()
        accountViewModel.getUserProfile()
    }

    private fun judgeRolePermission() {
        binding.tvRoleManager.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_ROLE_QUERY.printableName)
        binding.tvUserManager.isVisible =
            RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_QUERY.printableName)
    }

    private fun addListener() {
        binding.tvProfileInfo.setOnClickListener {
            ProfileInfoActivity.goActivity(requireActivity())
        }

        binding.tvRoleManager.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_ROLE_QUERY.printableName)) return@setOnClickListener

            RoleManagerActivity.goActivity(requireContext())
        }

        binding.tvUserManager.setOnClickListener {
            if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_QUERY.printableName)) return@setOnClickListener

            UserManagerActivity.goActivity(requireContext())
        }

        binding.tvNetworkManager.setOnClickListener {
            ChooseHostActivity.goActivity(requireContext())
        }

        binding.mbtLogout.setOnClickListener {
            SingleBtnDialogFragment.newInstance("退出登录", "确定要退出登录吗？")
                .addConfrimClickLisntener(object : SingleBtnDialogFragment.OnConfirmClickLisenter {
                    override fun onConfrimClick() {
                        loginViewModel.logout()
                    }

                })
                .show(childFragmentManager, "logout_dialog")
        }

        binding.tvUpdatePassword.setOnClickListener {
//            if (!RolePermissionUtils.hasPermission(MenuEnum.SYSTEM_USER_RESETPWD.printableName)) return@setOnClickListener

            UpdatePasswordActivity.goActivity(requireContext())
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