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

    fun getReportList(page: Int) {
        reportListLiveData.value = LoadListDataModel(page == 1)
        RetrofitService.apiService
            .getReportList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .doOnNext { list ->
                mapCheckState(list)
            }
            .subscribe(createListObserver(reportListLiveData, page == 1, page))
    }

    private fun mapCheckState(list: List<ReportListInfo>?) {
        list?.forEach {
            when (it.checkState) {
                0 -> it.checkStateName = "未开始"
                1 -> it.checkStateName = "排查中"
                2 -> it.checkStateName = "已完成"
            }
        }
    }

    fun searchReportList(searchParams: WorkOrderFragment.SearchParams) {
        reportListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchReportList(
                searchParams.applyName,
                searchParams.batchNo,
                searchParams.productCode,
                searchParams.productDesc,
                searchParams.startDate,
                searchParams.endDate,
                searchParams.oaBillNo,
                searchParams.factory,
                searchParams.state)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .doOnNext { list ->
                mapCheckState(list)
            }
            .subscribe(createListObserver(reportListLiveData, true, 1))
    }


}