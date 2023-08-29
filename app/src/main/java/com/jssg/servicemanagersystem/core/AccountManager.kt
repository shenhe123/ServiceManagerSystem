package com.jssg.servicemanagersystem.core

import android.text.TextUtils
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.tencent.mmkv.MMKV

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class AccountManager {

    private var user: UserInfo? = null
    private var authorization: String? = ""
    private var cookieStr: String? = ""

    fun savePassword(userName: String, pwd: String) {
        MMKV.defaultMMKV().encode("user_login_info", "$userName+$pwd")
    }

    fun getPassword(): String? {
        return MMKV.defaultMMKV().getString("user_login_info", null)
    }

    fun getUser(): UserInfo? {
        if (user == null) {
            user = MMKV.defaultMMKV().decodeParcelable("user_account_info", UserInfo::class.java)
        }

        return user
    }

    fun saveUser(userInfo: UserInfo) {
        MMKV.defaultMMKV().encode("user_account_info", userInfo)
    }
    fun isLogin(): Boolean {
        if (getUser() == null) return false

        return !getAuthorization().isNullOrEmpty()
    }

    fun logout() {
        MMKV.defaultMMKV().remove("user_account_info")
        saveAuthorization("")
        saveCookie("")
    }

    fun saveChooseHost(lastChooseHost: String) {
        MMKV.defaultMMKV().encode("choose_host", lastChooseHost)
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
        MMKV.defaultMMKV().encode("cookie", cookie)
    }

    fun getAuthorization(): String? {
        if (TextUtils.isEmpty(authorization)) {
            authorization = MMKV.defaultMMKV().getString("authorization", "")
        }
        return authorization
    }

    fun saveAuthorization(authorization: String) {
        this.authorization = authorization
        MMKV.defaultMMKV().encode("authorization", authorization)
    }

    companion object{
        val instance by lazy(LazyThreadSafetyMode.NONE){
            AccountManager()
        }
    }

}