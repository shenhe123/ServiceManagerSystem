package com.jssg.servicemanagersystem.ui.workorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo

class WorkOrderViewModel : AutoDisposViewModel() {

    val workOrderListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderInfo>?>>()
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

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}