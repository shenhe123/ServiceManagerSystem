package com.jssg.servicemanagersystem.ui.account.logmanager.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.logmanager.LogManagerActivity

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/16.
 */
class LogManagerViewModel: AutoDisposViewModel() {

    val logInfoListLiveData = MutableLiveData<LoadListDataModel<List<LogInfo>?>>()

    fun getLogInfo(isRefresh: Boolean, page: Int) {
        logInfoListLiveData.value = LoadListDataModel(isRefresh)
        RetrofitService.apiService
            .getLogInfoList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(logInfoListLiveData, isRefresh, page))
    }

    fun searchLogInfo(searchParams: LogManagerActivity.SearchParams) {
        logInfoListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchLogInfoList(
                searchParams.userName,
                searchParams.startDate,
                searchParams.endDate
            )
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(logInfoListLiveData, true, 1))
    }


}