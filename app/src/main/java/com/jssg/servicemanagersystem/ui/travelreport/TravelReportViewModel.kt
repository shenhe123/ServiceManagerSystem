package com.jssg.servicemanagersystem.ui.travelreport

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo

class TravelReportViewModel : AutoDisposViewModel() {


    val travelReportListLiveData = MutableLiveData<LoadListDataModel<List<TravelReportInfo>?>>()

    fun getTravelReportList(isRefresh: Boolean, page: Int) {
        travelReportListLiveData.value = LoadListDataModel(isRefresh)
        val mPage = if (isRefresh) {
            1
        } else {
            page + 1
        }
        RetrofitService.apiService
            .getTravelReportList(mPage, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(travelReportListLiveData, isRefresh, page))
    }


}