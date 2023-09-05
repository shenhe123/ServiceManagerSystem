package com.jssg.servicemanagersystem.core

import android.text.TextUtils
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.FactoryInfo
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

    fun saveFactoryInfo(factoryInfos: List<FactoryInfo>?) {
        factoryInfos?.let {
            MMKV.defaultMMKV().encode("factory_infos", JsonUtils.toJson(it))
        }
    }

    fun getFactoryInfo(): List<FactoryInfo>?{
        val json = MMKV.defaultMMKV().decodeString("factory_infos", "")
        json?.let {
            return JsonUtils.getListFromJson(it, FactoryInfo::class.java)
        }
        return null
    }

    fun saveDeptInfo(deptInfos: List<DeptInfo>?) {
        deptInfos?.let {
            MMKV.defaultMMKV().encode("dept_infos", JsonUtils.toJson(it))
        }
    }

    fun getDeptInfo(): List<DeptInfo>?{
        val json = MMKV.defaultMMKV().decodeString("dept_infos", "")
        json?.let {
            return JsonUtils.getListFromJson(it, DeptInfo::class.java)
        }
        return null
    }

    fun saveNewTravelReport(travelReportInfo: TravelReportInfo) {
        MMKV.defaultMMKV().encode("travel_report_info", travelReportInfo)
    }

    fun getNewTravelReport(): TravelReportInfo? {
        return MMKV.defaultMMKV().decodeParcelable("travel_report_info", TravelReportInfo::class.java)
    }

    companion object{
        val instance by lazy(LazyThreadSafetyMode.NONE){
            AccountManager()
        }
    }

}