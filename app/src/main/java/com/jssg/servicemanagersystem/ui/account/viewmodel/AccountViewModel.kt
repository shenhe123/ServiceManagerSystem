package com.jssg.servicemanagersystem.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.entity.BaseHttpResult
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.utils.HUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class AccountViewModel : AutoDisposViewModel() {

    val updateLiveData = MutableLiveData<LoadDataModel<Any>>()
    val userInfoLiveData = MutableLiveData<LoadDataModel<UserInfo?>>()

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

    fun getPermission() {
//        logoutLiveData.value = LoadDataModel()
//        RetrofitService.apiService
//            .logout()
//            .compose(RxSchedulersHelper.io_main())
//            .subscribe(createObserver(logoutLiveData))
    }

    fun updateUserInfo(nickname: String, phoneNumber: String, cardId: String, address: String) {
        updateLiveData.value = LoadDataModel()
        val params = HashMap<String, String>()
        params["nickName"] = nickname
        params["phonenumber"] = phoneNumber
        params["idNo"] = cardId
        params["address"] = address
        RetrofitService.apiService
            .updateUserInfo(HUtils.createRequestBody(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateLiveData))
    }
}