package com.jssg.servicemanagersystem.ui.travelreport

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.HUtils

class TravelReportViewModel : AutoDisposViewModel() {

    val deptInfoLiveData = MutableLiveData<LoadDataModel<List<WorkDeptInfo>?>>()
    val factoryInfoLiveData = MutableLiveData<LoadDataModel<List<WorkFactoryInfo>?>>()
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

    fun searchTravelReport(input: String) {
        travelReportListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchTravelReportList(input,1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(travelReportListLiveData, true, 1))
    }

    fun getFactoryInfo() {
        factoryInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getTravelReportFactoryInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(factoryInfoLiveData))
    }

    fun getDeptInfo() {
        deptInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getTravelReportDeptInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(deptInfoLiveData))

    }

    fun addNewTravelReport(travelReportInfo: TravelReportInfo) {
//        addWorkOrderDetailLiveData.value = LoadDataModel()
//        val params = HashMap<String, Any>()
//        params["billNo"] = billNo
//        params["place"] = place
//        params["badPicNames"] = badPicNames
//        params["boxPicNames"] = boxPicNames
//        params["batchPicNames"] = batchPicNames
//        params["reworkPicNames"] = reworkPicNames
//        params["checkNum"] = checkNum
//        params["badNum"] = badNum
//        params["checkDate"] = checkDate
//        params["state"] = state
//        params["remark"] = remark
//        RetrofitService.apiService
//            .addWorkOrderDetail(HUtils.createRequestBodyMap(params))
//            .compose(RxSchedulersHelper.io_main())
//            .subscribe(createObserver(addWorkOrderDetailLiveData))
    }
}