package com.jssg.servicemanagersystem.core

import com.tencent.mmkv.MMKV

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AccountManager {

    fun savePassword(userName: String, pwd: String) {
        MMKV.defaultMMKV().putString("user_login_info", "$userName+$pwd")
    }

    fun getPassword(): String? {
        return MMKV.defaultMMKV().getString("user_login_info", null)
    }

    fun saveUser() {
        MMKV.defaultMMKV().putString("user_account_info", "success")
    }
    fun isLogin(): Boolean {
        return MMKV.defaultMMKV().getString("user_account_info", "")?.equals("success") == true
    }

    fun logout() {
        MMKV.defaultMMKV().putString("user_account_info", null)
    }

    fun saveChooseHost(lastChooseHost: String) {
        MMKV.defaultMMKV().putString("choose_host", lastChooseHost)
    }

    fun getChoosehost(): String? {
        return MMKV.defaultMMKV().getString("choose_host", null)
    }

    companion object{
        val instance by lazy(LazyThreadSafetyMode.NONE){
            AccountManager()
        }
    }

}