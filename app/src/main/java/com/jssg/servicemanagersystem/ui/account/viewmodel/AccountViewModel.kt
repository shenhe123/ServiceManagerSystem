package com.jssg.servicemanagersystem.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.entity.UserData
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.ui.account.entity.UserRoles
import com.jssg.servicemanagersystem.ui.account.usermanager.UserManagerActivity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.HUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class AccountViewModel : AutoDisposViewModel() {

    val deptInfoLiveData = MutableLiveData<LoadDataModel<List<DeptInfo>?>>()
    val factoryInfoLiveData = MutableLiveData<LoadDataModel<List<WorkFactoryInfo>?>>()
    val deleteRoleInfoLiveData = MutableLiveData<LoadDataModel<Any>>()
    val deleteUserInfoLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updatePasswordLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updateUserProfileLiveData = MutableLiveData<LoadDataModel<Any>>()
    val addNewUserLiveData = MutableLiveData<LoadDataModel<Any>>()
    val userProfileLiveData = MutableLiveData<LoadDataModel<UserInfo?>>()
    val userInfoLiveData = MutableLiveData<LoadDataModel<UserData?>>()
    val addNewRoleLiveData = MutableLiveData<LoadDataModel<Any>>()
    val userListLiveData = MutableLiveData<LoadListDataModel<List<User>?>>()
    val roleListLiveData = MutableLiveData<LoadListDataModel<List<Role>?>>()
    val userRoleListLiveData = MutableLiveData<LoadDataModel<UserRoles>>()
    val updateUserInfoLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updateRoleInfoLiveData = MutableLiveData<LoadDataModel<Any>>()

    /**
     * 获取个人信息并缓存
     */
    fun getUserProfile() {
        userProfileLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .doOnNext { user ->
                user?.let {
                    AccountManager.instance.saveUser(it)
                }
            }
            .subscribe(createObserver(userProfileLiveData))
    }

    /**
     * 修改个人信息
     * @param nickname String 用户名
     * @param phoneNumber String 手机号码
     * @param cardId String 身份证号
     * @param address String 居住地址
     * @param userId String 用户ID
     * @param roleIds List<Long>? 角色ids
     */
    fun updateUserProfile(
        nickname: String,
        phoneNumber: String,
        cardId: String,
        address: String,
        userId: String,
        roleIds: List<String>?
    ) {
        updateUserProfileLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["nickName"] = nickname
        params["phonenumber"] = phoneNumber
        params["idNo"] = cardId
        params["address"] = address
        params["userId"] = userId
        roleIds?.let {
            params["roleIds"] = roleIds
        }
        RetrofitService.apiService
            .updateUserProfile(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateUserProfileLiveData))
    }

    /**
     * 修改Miami
     * @param oldPwd String 旧密码
     * @param newPwd String 新密码
     */
    fun updatePassword(oldPwd: String, newPwd: String) {
        updatePasswordLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["oldPassword"] = oldPwd
        params["newPassword"] = newPwd
        RetrofitService.apiService
            .updatePassword(oldPwd, newPwd)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updatePasswordLiveData))
    }

    /**
     * 新增角色
     * @param roleName String
     * @param remark String
     * @param menuIds MutableList<Long>
     */
    fun postAddNewRole(roleName: String, remark: String, menuIds: MutableList<Long>) {
        addNewRoleLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["roleName"] = roleName
        params["roleKey"] = roleName
        params["attachToApp"] = "Y"
        params["roleSort"] = 0
        params["status"] = "0"
        params["remark"] = remark
        params["menuIds"] = menuIds
        RetrofitService.apiService
            .postAddNewRole(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewRoleLiveData))
    }

    fun getUserList(page: Int, isRefresh: Boolean) {
        userListLiveData.value = LoadListDataModel(isRefresh)

        RetrofitService.apiService
            .getUserList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(userListLiveData, isRefresh, page))
    }

    fun getRoleList() {
        roleListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getRoleList(1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(roleListLiveData, true, 1))
    }

    fun getUserRoleList() {
        userRoleListLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getUserRoles()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .doOnNext { AccountManager.instance.saveRoleList(it.roles) }
            .subscribe(createObserver(userRoleListLiveData))
    }

    fun updateUserInfo(
        checkedRoleIds: List<String>?,
        user: User,
        orgId: String?,
        deptId: String?,
        password: String = "",
        userType: String?,
    ) {
        updateUserInfoLiveData.value = LoadDataModel()
        val params = HashMap<String, Any?>()
        params["userId"] = user.userId
        params["userName"] = user.userName
        params["nickName"] = user.nickName
        params["idNo"] = user.idNo
        params["address"] = user.address
        if (password.isNotEmpty()) {
            params["password"] = password
        }
        params["phonenumber"] = user.phonenumber
        checkedRoleIds?.let {
            params["roleIds"] = checkedRoleIds
        }
        params["expireDate"] = user.expireDate
        params["orgId"] = orgId ?: ""
        params["deptId"] = deptId ?: ""

        params["userType"] = userType ?: ""
        RetrofitService.apiService
            .updateUserInfo(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateUserInfoLiveData))
    }

    fun deleteUserInfo(userId: Long) {
        deleteUserInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .deleteUserInfo(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(deleteUserInfoLiveData))
    }

    fun addNewUser(
        userName: String,
        nickName: String,
        phoneNumber: String,
        password: String,
        cardId: String,
        address: String,
        expiredDate: String,
        checkedRoleIds: List<String>?,
        orgId: String?,
        deptId: String?,
        userType: String?
    ) {
        addNewUserLiveData.value = LoadDataModel()
        val params = HashMap<String, Any?>()
        params["userName"] = userName
        params["nickName"] = nickName
        params["idNo"] = cardId
        params["address"] = address
        params["password"] = password
        params["phonenumber"] = phoneNumber
        params["expireDate"] = expiredDate
        params["roleIds"] = checkedRoleIds ?: listOf<String>()
        params["orgId"] = orgId ?: ""
        params["deptId"] = deptId ?: ""
        params["userType"] = userType ?: ""
        RetrofitService.apiService
            .addNewUser(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewUserLiveData))
    }

    fun searchUser(searchParams: UserManagerActivity.SearchParams) {
        userListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchUser(
                searchParams.userName,
                searchParams.nickName,
                searchParams.phoneNum,
                searchParams.creator,
                searchParams.factory,
                searchParams.expiredStartDate,
                searchParams.expiredEndDate,
                searchParams.creatorStartDate,
                searchParams.creatorEndDate,
                searchParams.state,
                1, 9999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(userListLiveData, true, 1))
    }

    fun searchRole(input: String) {
        roleListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchRole(input, 1, 9999)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createListObserver(roleListLiveData, true, 1))
    }

    fun getUserInfo(userId: Long) {
        userInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getUserInfo(userId)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(userInfoLiveData))
    }

    fun getFactoryInfo() {
        factoryInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getFactoryInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
//            .doOnNext { AccountManager.instance.saveFactoryInfo(it) }
            .subscribe(createObserver(factoryInfoLiveData))
    }

    fun getDeptInfo() {
        deptInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getDeptInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
//            .doOnNext { AccountManager.instance.saveDeptInfo(it) }
            .subscribe(createObserver(deptInfoLiveData))

    }

    fun updateRoleInfo(menuIds: MutableList<Long>, role: Role) {
        updateRoleInfoLiveData.value = LoadDataModel()
        val params = HashMap<String, Any?>()
        params["roleId"] = role.roleId
        params["roleName"] = role.roleName
        params["roleKey"] = role.roleKey
        params["attachToApp"] = "Y"
        params["roleSort"] = "0"
        params["remark"] = role.remark
        params["menuIds"] = menuIds
        RetrofitService.apiService
            .updateRoleInfo(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateRoleInfoLiveData))
    }

    fun deleteRoleInfo(roleId: String) {
        deleteRoleInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .deleteRoleInfo(roleId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(deleteRoleInfoLiveData))
    }
}