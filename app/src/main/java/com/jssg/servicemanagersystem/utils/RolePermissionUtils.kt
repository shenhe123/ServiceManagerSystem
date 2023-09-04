package com.jssg.servicemanagersystem.utils

import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.utils.toast.ToastUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/4.
 */
object RolePermissionUtils {

    fun hasPermission(menuPermission: String): Boolean {
        val user = AccountManager.instance.getUser()
        user?.let {
            if (it.user.admin) {
                return true
            } else {
                val hasPerm = it.permissions.any { item -> item.equals(menuPermission, true) }
                if (!hasPerm) {
                    ToastUtils.showToast("请联系管理员申请权限")
                }
                return hasPerm
            }
        }

        ToastUtils.showToast("请联系管理员申请权限")
        return false
    }

}