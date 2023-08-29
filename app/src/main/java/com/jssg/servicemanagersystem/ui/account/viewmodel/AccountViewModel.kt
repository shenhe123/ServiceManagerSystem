package com.jssg.servicemanagersystem.ui.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.core.AccountManager
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class AccountViewModel : AutoDisposViewModel() {

    val userInfoLiveData = MutableLiveData<LoadDataModel<UserInfo?>>()
    val logoutLiveData = MutableLiveData<LoadDataModel<Any>>()

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
        logoutLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .logout()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(logoutLiveData))
    }
}