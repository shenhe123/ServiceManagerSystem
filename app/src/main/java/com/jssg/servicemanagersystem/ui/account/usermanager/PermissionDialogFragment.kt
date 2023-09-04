package com.jssg.servicemanagersystem.ui.account.usermanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseDialogFragment
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.databinding.DialogPermissionLayoutBinding
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.viewmodel.AccountViewModel
import com.jssg.servicemanagersystem.utils.DpPxUtils
import com.jssg.servicemanagersystem.utils.toast.ToastUtils
import java.math.BigDecimal

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class PermissionDialogFragment: BaseDialogFragment() {

    private lateinit var listener: OnFinishListener
    private lateinit var checkedRoleIds: MutableList<String>
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var binding: DialogPermissionLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPermissionLayoutBinding.inflate(layoutInflater, container, false)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getParcelable<User>("data")

        accountViewModel.userRoleListLiveData.observe(this) { result ->
            if (!result.isLoading) {
                user?.let {
                    initRoleData(it.roleIds, result.data.roles)
                }
            }
        }

        accountViewModel.updateUserInfoLiveData.observe(this) { result ->
            if (result.isSuccess) {
                ToastUtils.showToast(result.msg)
                if (this::listener.isInitialized) {
                    user?.let {
                        it.roleIds = checkedRoleIds
                        this.listener.onFinish(it)
                    }
                }
                dismiss()
            } else if (result.isError) {
                ToastUtils.showToast(result.msg)
            }
        }

        val roleList = AccountManager.instance.getRoleList()
        if (roleList == null) {
            accountViewModel.getUserRoleList()
        } else {
            user?.let {
                initRoleData(it.roleIds, roleList)
            }
        }

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.mbtConfirm.setOnClickListener {
            if (checkedRoleIds.isEmpty()) {
                ToastUtils.showToast("请至少选择一个角色")
                return@setOnClickListener
            }

            user?.let {
                accountViewModel.updateUserInfo(checkedRoleIds, it)
            }
        }
    }

    private fun initRoleData(roleIds: List<String>?, roleList: List<Role>?) {
        if (roleList.isNullOrEmpty()) return

        checkedRoleIds = roleIds?.toMutableList() ?: mutableListOf()

        binding.layoutPermission.removeAllViews()
        //这个用户未设置任何角色
        if (roleIds.isNullOrEmpty()) {
            roleList.filter { !it.admin && it.attachToApp.equals("Y") }
                .forEach {
                binding.layoutPermission.addView(addCheckBoxWidget(it, false))
            }
            return
        }

        //展示全部角色信息，并选中当前用户的角色信息
        roleList.filter { !it.admin && it.attachToApp.equals("Y") }
            .forEach {
            binding.layoutPermission.addView(addCheckBoxWidget(it, roleIds.contains(it.roleId)))
        }
    }

    private fun addCheckBoxWidget(role: Role, isChecked: Boolean): AppCompatCheckBox {
        val checkBox = AppCompatCheckBox(requireContext())
        checkBox.layoutParams = ViewGroup.LayoutParams(-2, DpPxUtils.dip2px(requireContext(), 25f))
        checkBox.text = role.roleName
        checkBox.isChecked = isChecked
        checkBox.tag = role.roleId
        checkBox.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.x_text_01))

        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                checkedRoleIds.add(role.roleId)
            } else {
                if (checkedRoleIds.contains(role.roleId)) {
                    checkedRoleIds.remove(role.roleId)
                }
            }
        }
        return checkBox
    }

    fun addOnFinishListener(listener: OnFinishListener): PermissionDialogFragment {
        this.listener = listener
        return this
    }

    interface OnFinishListener {
        fun onFinish(newUser: User)
    }

    companion object {
        fun newInstance(user: User): PermissionDialogFragment {
            val args = Bundle()
            args.putParcelable("data", user)
            val fragment = PermissionDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}