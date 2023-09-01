package com.jssg.servicemanagersystem.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.utils.HUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class AccountViewModel : AutoDisposViewModel() {

    val deleteUserInfoLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updatePasswordLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updateUserInfoLiveData = MutableLiveData<LoadDataModel<Any>>()
    val addNewUserLiveData = MutableLiveData<LoadDataModel<Any>>()
    val userInfoLiveData = MutableLiveData<LoadDataModel<UserInfo?>>()
    val addNewRoleLiveData = MutableLiveData<LoadDataModel<Any>>()
    val userListLiveData = MutableLiveData<LoadListDataModel<List<User>?>>()
    val roleListLiveData = MutableLiveData<LoadListDataModel<List<Role>?>>()
    val updateUserRoleIdsLiveData = MutableLiveData<LoadDataModel<Any>>()

    /**
     * 获取个人信息并缓存
     */
    fun getUserInfo() {
        userInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .doOnNext { user ->
                user?.let {
                    AccountManager.instance.saveUser(it)
                }
            }
            .subscribe(createObserver(userInfoLiveData))
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
    fun updateUserInfo(
        nickname: String,
        phoneNumber: String,
        cardId: String,
        address: String,
        userId: String,
        roleIds: List<String>?
    ) {
        updateUserInfoLiveData.value = LoadDataModel()
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
            .updateUserInfo(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateUserInfoLiveData))
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
        val mPage = if (isRefresh) {
            1
        } else {
            page + 1
        }
        RetrofitService.apiService
            .getUserList(mPage, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(userListLiveData, isRefresh, page))
    }

    fun getRoleList() {
        roleListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getRoleList(1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .doOnNext {
                AccountManager.instance.saveRoleList(it)
            }
            .subscribe(createListObserver(roleListLiveData, true, 1))
    }

    fun updateUserRoleIds(
        checkedRoleIds: MutableList<String>,
        user: User,
    ) {
        updateUserRoleIdsLiveData.value = LoadDataModel()
        val params = HashMap<String, Any?>()
        params["userId"] = user.userId
        params["userName"] = user.userName
        params["nickName"] = user.nickName
        params["idNo"] = user.idNo
        params["address"] = user.address
        params["phonenumber"] = user.phonenumber
        params["roleIds"] = checkedRoleIds
        RetrofitService.apiService
            .updateUserRoleIds(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateUserRoleIdsLiveData))
    }

    fun deleteUserInfo(userId: Long) {
        deleteUserInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .deleteUserInfo(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(deleteUserInfoLiveData))
    }

    fun addNewUser(
        nickName: String,
        phoneNumber: String,
        password: String,
        cardId: String,
        address: String,
        expiredDate: String,
        checkedRoleIds: List<String>
    ) {
        addNewUserLiveData.value = LoadDataModel()
        val params = HashMap<String, Any?>()
        params["userName"] = nickName
        params["nickName"] = nickName
        params["idNo"] = cardId
        params["address"] = address
        params["password"] = password
        params["phonenumber"] = phoneNumber
        params["expireDate"] = expiredDate
        params["roleIds"] = checkedRoleIds
        RetrofitService.apiService
            .addNewUser(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewUserLiveData))
    }

    fun searchUser(input: String) {
        userListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchUser(input, 1, 9999)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createListObserver(userListLiveData, true, 1))
    }
}