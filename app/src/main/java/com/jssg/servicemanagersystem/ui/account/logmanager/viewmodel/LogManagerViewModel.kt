package com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.OptionLogInfo
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.LoginLogFragment
import com.jssg.servicemanagersystem.ui.account.logmanager.fragment.OptionLogFragment

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/16.
 */
class LogManagerViewModel: AutoDisposViewModel() {

    val logInfoListLiveData = MutableLiveData<LoadListDataModel<List<LogInfo>?>>()
    val optionInfoListLiveData = MutableLiveData<LoadListDataModel<List<OptionLogInfo>?>>()

    fun getLoginLogInfo(isRefresh: Boolean, page: Int) {
        logInfoListLiveData.value = LoadListDataModel(isRefresh)
        RetrofitService.apiService
            .getLoginLogInfoList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(logInfoListLiveData, isRefresh, page))
    }

    fun searchLoginLogInfo(searchParams: LoginLogFragment.SearchParams) {
        logInfoListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchLoginLogInfoList(
                searchParams.userName,
                searchParams.startDate,
                searchParams.endDate
            )
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(logInfoListLiveData, true, 1))
    }

    fun getOptionsLogInfo(isRefresh: Boolean, page: Int) {
        optionInfoListLiveData.value = LoadListDataModel(isRefresh)
        RetrofitService.apiService
            .getOptionLogInfoList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(optionInfoListLiveData, isRefresh, page))
    }

    fun searchOptionsLogInfo(searchParams: OptionLogFragment.SearchParams, isRefresh: Boolean, page: Int) {
        optionInfoListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchOptionLogInfoList(
                searchParams.title,
                searchParams.businessType,
                searchParams.operName,
                searchParams.status,
                searchParams.beginTime,
                searchParams.endTime,
                page,
                20
            )
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(optionInfoListLiveData, isRefresh, page))
    }


}