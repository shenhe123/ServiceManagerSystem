package com.jssg.servicemanagersystem.ui.workorder.viewmodel

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadListDataModel
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderCheckListFragment
import com.jssg.servicemanagersystem.ui.workorder.fragment.WorkOrderFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.HUtils
import java.util.ArrayList

class WorkOrderViewModel : AutoDisposViewModel() {

    val deptInfoLiveData = MutableLiveData<LoadDataModel<List<WorkDeptInfo>?>>()
    val factoryInfoLiveData = MutableLiveData<LoadDataModel<List<WorkFactoryInfo>?>>()
    val addNewWorkOrderLiveData = MutableLiveData<LoadDataModel<Any>>()
    val closeCaseWorkOrderLiveData = MutableLiveData<LoadDataModel<Any>>()
    val reviewWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updateWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val addWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val workOrderListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderInfo>?>>()
    val workOrderCheckListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderCheckInfo>?>>()
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

    fun searchWorkOrder(searchParams: WorkOrderFragment.SearchParams) {
        workOrderListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchWorkOrderList(
                searchParams.productCode,
                searchParams.productDesc,
                searchParams.startDate,
                searchParams.endDate,
                searchParams.oaBillNo,
                searchParams.factory,
                searchParams.state,
                1,
                9999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderListLiveData, true, 1))
    }

    fun addWorkOrderDetail(
        billNo: String,
        billDetailNo: String?,
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
        billDetailNo?.let {
            params["billDetailNo"] = it
        }
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

    fun updateWorkOrderDetail(
        billNo: String,
        billDetailNo: String,
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
        updateWorkOrderDetailLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["billNo"] = billNo
        params["billDetailNo"] = billDetailNo
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
            .updateWorkOrderDetail(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateWorkOrderDetailLiveData))
    }

    fun getWorkOrderCheckList(billNo: String) {
        workOrderCheckListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getWorkOrderCheckList(billNo,1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderCheckListLiveData, true, 1))
    }

    fun reviewWorkOrderCheck(billDetailNo: String, remark: String, state: String) {
        reviewWorkOrderDetailLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["billDetailNo"] = billDetailNo
        params["remark"] = remark
        params["state"] = state
        RetrofitService.apiService
            .reviewWorkOrderCheck(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(reviewWorkOrderDetailLiveData))
    }

    fun closeCaseWorkOrder(checkedBillNos: ArrayList<String>) {
        closeCaseWorkOrderLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .closeCaseWorkOrderCheck(checkedBillNos.joinToString(","))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(closeCaseWorkOrderLiveData))
    }

    fun addNewWorkOrder(
        nickName: String,
        phoneNumber: String,
        clientName: String,
        serviceName: String,
        checkNum: String,
        servicePrice: String,
        servicePeriod: String,
        serviceTotal: String,
        serviceAddress: String,
        remark: String,
        applyDept: String?,
        orgService: String?,//服务工厂
        productCode: String,
        productDesc: String,
        badNum: String,
    ) {
        addNewWorkOrderLiveData.value = LoadDataModel()
        val params = HashMap<String, Any>()
        params["applyName"] = nickName
        params["applyDate"] = DateUtil.getFullData(System.currentTimeMillis())
        applyDept?.let {
            params["applyDept"] = applyDept
        }
        orgService?.let {
            params["orgService"] = orgService
        }
        params["customer"] = clientName
        params["tel"] = phoneNumber
        params["servicePeriod"] = servicePeriod
        params["unitPrice"] = servicePrice
        params["checkNum"] = checkNum
        params["totalPrice"] = serviceTotal
        params["salesManager"] = serviceName
        params["serviceAdd"] = serviceAddress
        params["productCode"] = productCode
        params["productDes"] = productDesc
        params["productNum"] = badNum
        params["state"] = 1
        params["remark"] = remark
        RetrofitService.apiService
            .addWorkOrder(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewWorkOrderLiveData))
    }

    fun addNewWorkOrder() {
        addNewWorkOrderLiveData.value = LoadDataModel()
        val json = "{\"unitPrice\":\"123\",\"productNum\":\"4\",\"checkNum\":\"23\",\"totalPrice\":\"4235\",\"remark\":\"你要走里呀就语文预习\",\"productDes\":\"你中午五\",\"servicePeriod\":\"3个月\",\"applyDept\":\"1000\",\"productCode\":\"13369494\",\"orgService\":\"1\",\"salesManager\":\"你有\",\"tel\":\"135969994499\",\"applyName\":\"你名字\",\"applyDate\":\"2023-09-03 20:09:04\",\"serviceAdd\":\"明知\"}"
        RetrofitService.apiService
            .addWorkOrder(HUtils.createJson(json))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(addNewWorkOrderLiveData))
    }

    fun getFactoryInfo() {
        factoryInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getWorkFactoryInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(factoryInfoLiveData))
    }

    fun getDeptInfo() {
        deptInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getWorkDeptInfo()
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(deptInfoLiveData))

    }

    fun searchWorkOrderDetail(searchParams: WorkOrderCheckListFragment.SearchParams) {
        workOrderCheckListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .searchWorkOrderCheckList(searchParams.state, searchParams.startDate, searchParams.endDate,1, 999)
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderCheckListLiveData, true, 1))
    }

}