package com.jssg.servicemanagersystem.ui.workorder.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.HUtils

class WorkOrderViewModel : AutoDisposViewModel() {

    val addWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val workOrderListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderInfo>?>>()
    val workOrderCheckListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderInfo>?>>()
    fun getWorkOrderList(isRefresh: Boolean, page: Int) {
        workOrderListLiveData.value = LoadListDataModel(isRefresh)
        val mPage = if (isRefresh) {
            1
        } else {
            page + 1
        }
        RetrofitService.apiService
            .getWorkOrderList(mPage, 20)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderListLiveData, isRefresh, page))

    }

    fun searchWorkOrder(input: String) {
        workOrderListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchWorkOrderList(input, 1, 9999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderListLiveData, true, 1))
    }

    fun addWorkOrderDetail(
        billNo: String,
        place: String,
        badPicNames: String,
        boxPicNames: String,
        batchPicNames: String,
        reworkPicNames: String,
        checkNum: String,
        badNum: String,
        checkDate: String,
        state: Int,
        remark: String
    ) {
        addWorkOrderDetailLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["billNo"] = billNo
        params["place"] = place
        params["badPicNames"] = badPicNames
        params["boxPicNames"] = boxPicNames
        params["batchPicNames"] = batchPicNames
        params["reworkPicNames"] = reworkPicNames
        params["checkNum"] = checkNum
        params["badNum"] = badNum
        params["checkDate"] = checkDate
        params["state"] = state
        params["remark"] = remark
        RetrofitService.apiService
            .addWorkOrderDetail(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addWorkOrderDetailLiveData))
    }

    fun getWorkOrderCheckList() {
        workOrderCheckListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getWorkOrderCheckList(1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderCheckListLiveData, true, 1))
    }
}