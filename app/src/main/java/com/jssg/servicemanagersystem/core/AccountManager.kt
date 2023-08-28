package com.jssg.servicemanagersystem.core

import android.text.TextUtils
import com.tencent.mmkv.MMKV

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AccountManager {

    private var authorization: String? = ""
    private var cookieStr: String? = ""

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
        saveAuthorization("")
        saveCookie("")
    }

    fun saveChooseHost(lastChooseHost: String) {
        MMKV.defaultMMKV().putString("choose_host", lastChooseHost)
    }

    fun getChooseHost(): String? {
        return MMKV.defaultMMKV().getString("choose_host", null)
    }

    fun getCookie(): String? {
        if (TextUtils.isEmpty(cookieStr)) {
            cookieStr = MMKV.defaultMMKV().getString("cookie", "")
        }
        return cookieStr
    }

    fun saveCookie(cookie: String) {
        cookieStr = cookie
        MMKV.defaultMMKV().putString("cookie", cookie)
    }

    fun getAuthorization(): String? {
        if (TextUtils.isEmpty(authorization)) {
            authorization = MMKV.defaultMMKV().getString("authorization", "")
        }
        return authorization
    }

    fun saveAuthorization(authorization: String) {
        this.authorization = authorization
        MMKV.defaultMMKV().putString("authorization", authorization)
    }

    companion object{
        val instance by lazy(LazyThreadSafetyMode.NONE){
            AccountManager()
        }
    }

}