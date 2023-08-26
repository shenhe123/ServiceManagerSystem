package com.jssg.servicemanagersystem.core

import com.tencent.mmkv.MMKV

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AccountManager {

    fun savePassword(userName: String, pwd: String) {
        MMKV.defaultMMKV().putString("user_account_info", "$userName+$pwd")
    }

    fun getPassword(): String? {
        return MMKV.defaultMMKV().getString("user_account_info", null)
    }

    companion object{
        val instance by lazy(LazyThreadSafetyMode.NONE){
            AccountManager()
        }
    }

}