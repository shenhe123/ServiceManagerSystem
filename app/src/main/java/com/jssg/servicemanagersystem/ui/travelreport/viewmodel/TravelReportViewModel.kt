package com.jssg.servicemanagersystem.ui.travelreport.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.travelreport.TravelReportFragment
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.utils.HUtils

class TravelReportViewModel : AutoDisposViewModel() {

    val travelReportInfoLiveData = MutableLiveData<LoadDataModel<TravelReportInfo?>>()
    val updateTravelReportLiveData = MutableLiveData<LoadDataModel<Any>>()
    val addNewTravelReportLiveData = MutableLiveData<LoadDataModel<Any>>()
    val deptInfoLiveData = MutableLiveData<LoadDataModel<List<WorkDeptInfo>?>>()
    val factoryInfoLiveData = MutableLiveData<LoadDataModel<List<WorkFactoryInfo>?>>()
    val travelReportListLiveData = MutableLiveData<LoadListDataModel<List<TravelReportInfo>?>>()

    fun getTravelReportList(isRefresh: Boolean, page: Int) {
        travelReportListLiveData.value = LoadListDataModel(isRefresh)
        RetrofitService.apiService
            .getTravelReportList(page, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(travelReportListLiveData, isRefresh, page))
    }

    fun searchTravelReport(searchParams: TravelReportFragment.TravelSearchParams) {
        travelReportListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchTravelReportList(
                searchParams.applyName,
                searchParams.startDate,
                searchParams.endDate,
                searchParams.orgName,
                searchParams.dept,
                1,
                9999)
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
        addNewTravelReportLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["dept"] = travelReportInfo.dept ?: ""
        params["deptId"] = travelReportInfo.deptId ?: ""
        params["orgId"] = travelReportInfo.orgId ?: ""
        params["orgName"] = travelReportInfo.orgName ?: ""
        params["partner"] = travelReportInfo.partner
        params["customer"] = travelReportInfo.customer
        params["productCode"] = travelReportInfo.productCode
        params["projectCode"] = travelReportInfo.projectCode
        params["placeTo"] = travelReportInfo.placeTo
        params["address"] = travelReportInfo.address
        params["startDate"] = travelReportInfo.startDate
        params["endDate"] = travelReportInfo.endDate
        params["purpose"] = travelReportInfo.purpose
        params["mainTask"] = travelReportInfo.mainTask
        params["expectedResult"] = travelReportInfo.expectedResult
        params["schedule"] = travelReportInfo.schedule
        RetrofitService.apiService
            .addNewTravelReport(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewTravelReportLiveData))
    }

    fun updateTravelReport(travelReportInfo: TravelReportInfo) {
        updateTravelReportLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["billNo"] = travelReportInfo.billNo
        params["dept"] = travelReportInfo.dept ?: ""
        params["deptId"] = travelReportInfo.deptId ?: ""
        params["orgId"] = travelReportInfo.orgId ?: ""
        params["orgName"] = travelReportInfo.orgName ?: ""
        params["partner"] = travelReportInfo.partner
        params["customer"] = travelReportInfo.customer
        params["productCode"] = travelReportInfo.productCode
        params["projectCode"] = travelReportInfo.projectCode
        params["placeTo"] = travelReportInfo.placeTo
        params["address"] = travelReportInfo.address
        params["startDate"] = travelReportInfo.startDate
        params["endDate"] = travelReportInfo.endDate
        params["purpose"] = travelReportInfo.purpose
        params["mainTask"] = travelReportInfo.mainTask
        params["expectedResult"] = travelReportInfo.expectedResult
        params["schedule"] = travelReportInfo.schedule
        RetrofitService.apiService
            .updateTravelReport(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateTravelReportLiveData))
    }

    fun getTravelReportInfo(billNo: String) {
        travelReportInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getTravelReportInfo(billNo)
            .compose(RxSchedulersHelper.io_main())
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(travelReportInfoLiveData))
    }
}