package com.jssg.servicemanagersystem.ui.report.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.report.entity.ReportListInfo
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/17.
 */
class ReportViewModel: AutoDisposViewModel() {

    val reportListLiveData = MutableLiveData<LoadListDataModel<List<ReportListInfo>?>>()

    fun getReportList() {
        reportListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getReportList()
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(reportListLiveData, true, 1))
    }

    fun searchReportList(searchParams: WorkOrderFragment.SearchParams) {
        reportListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchReportList(
                searchParams.productCode,
                searchParams.productDesc,
                searchParams.startDate,
                searchParams.endDate,
                searchParams.oaBillNo,
                searchParams.factory,
                searchParams.state)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(reportListLiveData, true, 1))
    }

}