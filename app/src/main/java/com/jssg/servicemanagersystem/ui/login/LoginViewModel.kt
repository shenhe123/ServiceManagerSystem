package com.jssg.servicemanagersystem.ui.login

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.ui.login.entity.LoginEntity
import com.jssg.servicemanagersystem.utils.HUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/28.
 */
class LoginViewModel: AutoDisposViewModel() {

    val loginLiveData = MutableLiveData<LoadDataModel<LoginEntity?>>()
    val logoutLiveData = MutableLiveData<LoadDataModel<Any>>()

    fun login(userName: String, password: String) {
        loginLiveData.value = LoadDataModel()
        val params = HashMap<String, String>()
        params["username"] = userName
        params["password"] = password
        RetrofitService.apiService
            .postLogin(HUtils.createRequestBody(params))
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(loginLiveData))
    }

    fun logout() {
        logoutLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .logout()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(logoutLiveData))
    }

}