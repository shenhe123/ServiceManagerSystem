package com.jssg.servicemanagersystem.core

import android.text.TextUtils
import com.jssg.servicemanagersystem.BuildConfig
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.utils.JsonUtils
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

    fun isAdmin(): Boolean {
        return getUser()?.user?.admin == true
    }

    fun isLogin(): Boolean {
        if (getUser() == null) return false

        return !getAuthorization().isNullOrEmpty()
    }

    fun logout() {
        user = null
        MMKV.defaultMMKV().remove("user_account_info")
        saveAuthorization("")
        saveCookie("")
        MMKV.defaultMMKV().remove("role_list")
        MMKV.defaultMMKV().remove("travel_report_info")

    }

    fun saveChooseHost(lastChooseHost: String) {
        MMKV.defaultMMKV().encode("choose_host", lastChooseHost)
    }

    fun getChooseHost(): String {
        val defaultUrl = if (BuildConfig.IS_TEST_HOST) Constants.Test_Host else Constants.Release_Host
        return MMKV.defaultMMKV().decodeString("choose_host") ?: defaultUrl
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

    fun saveRoleList(roleList: List<Role>?) {
        roleList?.let {
            val toJson = JsonUtils.toJson(roleList)
            MMKV.defaultMMKV().encode("role_list", toJson)
        }
    }

    fun getRoleList(): List<Role>? {
        val decodeString = MMKV.defaultMMKV().decodeString("role_list")
        decodeString?.let {
            return JsonUtils.getListFromJson(it, Role::class.java)
        }
        return null
    }

    fun saveNewTravelReport(travelReportInfo: TravelReportInfo?) {
        MMKV.defaultMMKV().encode("travel_report_info", travelReportInfo)
    }

    fun getNewTravelReport(): TravelReportInfo? {
        return MMKV.defaultMMKV()
            .decodeParcelable("travel_report_info", TravelReportInfo::class.java)
    }

    //工厂是否是多选模式
    val isMultiFactory: Boolean
        get() = isSysUser || isMainFactorCqe

    //是否是集团用户
    val isSysUser: Boolean
        get() = getUser()?.user?.userType.equals("sys_user")

    //是否是main_factor_cqe用户
    val isMainFactorCqe: Boolean
        get() = getUser()?.roles?.contains("main_factor_cqe") == true
    companion object {
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            AccountManager()
        }
    }

}