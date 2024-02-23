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
import com.jssg.servicemanagersystem.ui.workorder.WorkOrderFragment
import com.jssg.servicemanagersystem.utils.DateUtil
import com.jssg.servicemanagersystem.utils.HUtils
import java.util.ArrayList

class WorkOrderViewModel : AutoDisposViewModel() {

    val deptInfoLiveData = MutableLiveData<LoadDataModel<List<WorkDeptInfo>?>>()
    val factoryInfoLiveData = MutableLiveData<LoadDataModel<List<WorkFactoryInfo>?>>()
    val addNewWorkOrderLiveData = MutableLiveData<LoadDataModel<Any>>()
    val deleteWorkOrderLiveData = MutableLiveData<LoadDataModel<Any>>()
    val deleteWorkOrderCheckDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val closeCaseWorkOrderLiveData = MutableLiveData<LoadDataModel<Any>>()
    val reviewWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val updateWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val addWorkOrderDetailLiveData = MutableLiveData<LoadDataModel<Any>>()
    val workOrderListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderInfo>?>>()
    val workOrderInfoLiveData = MutableLiveData<LoadDataModel<WorkOrderInfo?>>()
    val workOrderCheckInfoLiveData = MutableLiveData<LoadDataModel<WorkOrderCheckInfo?>>()
    val workOrderCheckListLiveData = MutableLiveData<LoadListDataModel<List<WorkOrderCheckInfo>?>>()

    fun searchWorkOrder(
        searchParams: WorkOrderFragment.SearchParams?,
        isRefresh: Boolean,
        page: Int
    ) {
        workOrderListLiveData.value = LoadListDataModel(true)
        val observable = if (searchParams == null) {
            RetrofitService.apiService
                .getWorkOrderList(page, 20)
        } else {
            RetrofitService.apiService
                .searchWorkOrderList(
                    searchParams.applyName,
                    searchParams.checkerName,
                    searchParams.tel,
                    searchParams.productCode,
                    searchParams.productDesc,
                    searchParams.startDate,
                    searchParams.endDate,
                    searchParams.oaBillNo,
                    searchParams.factory,
                    searchParams.state,
                    page,
                    20
                )
        }
        observable.compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderListLiveData, isRefresh, page))
    }

    fun addWorkOrderDetail(
        billNo: String,
        billDetailNo: String?,
        place: String,
        badPicNames: String,
        boxPicNames: String,
        batchPicNames: String,
        reworkPicNames: String,
        voicesNames: String,
        checkNum: String,
        badNum: String,
        checkDate: String,
        state: Int,
        remark: String,
        batchNo: String
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
        params["voices"] = voicesNames
        params["checkNum"] = checkNum
        params["badNum"] = badNum
        params["checkDate"] = checkDate
        params["state"] = state
        params["remark"] = remark
        params["batchNo"] = batchNo
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
        remark: String,
        batchNo: String
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
        params["batchNo"] = batchNo
        RetrofitService.apiService
            .updateWorkOrderDetail(HUtils.createRequestBodyMap(params))
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(updateWorkOrderDetailLiveData))
    }

    fun getWorkOrderCheckList(billNo: String) {
        workOrderCheckListLiveData.value = LoadListDataModel(true)
        RetrofitService.apiService
            .getWorkOrderCheckList(billNo, 1, 9999)
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
        salesManager: String,
        checkNum: String,
        servicePrice: String,
        servicePeriod: String,
        priceUnit: String,
        periodUnit: String,
        serviceTotal: String,
        serviceAddress: String,
        remark: String,
        applyDept: String?,
        orgService: String?,//服务工厂
        productCode: String,
        productDesc: String,
        badNum: String,
        oaBillNo: String?,
        projectCode: String,
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
        params["priceUnit"] = priceUnit
        params["timeUnit"] = periodUnit
        params["checkNum"] = checkNum
        params["totalPrice"] = serviceTotal
        params["serviceStaff"] = serviceName
        params["salesManager"] = salesManager
        params["serviceAdd"] = serviceAddress
        params["productCode"] = productCode
        params["projectCode"] = projectCode
        params["oaBillNo"] = oaBillNo ?: ""
        params["productDes"] = productDesc
        params["productNum"] = badNum
        params["state"] = 1
        params["remark"] = remark
        RetrofitService.apiService
            .addWorkOrder(HUtils.createRequestBodyMap(params))
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
            .searchWorkOrderCheckList(
                searchParams.batchNo,
                searchParams.state,
                searchParams.startDate,
                searchParams.endDate,
                1,
                999
            )
            .compose(RxSchedulersHelper.ObsResultWithMain2())
            .subscribe(createListObserver(workOrderCheckListLiveData, true, 1))
    }

    fun deleteWorkOrderInfo(billNo: String) {
        deleteWorkOrderLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .deleteWorkOrderInfo(billNo)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(deleteWorkOrderLiveData))
    }

    fun deleteWorkOrderCheckDetailInfo(billDetailNo: String) {
        deleteWorkOrderCheckDetailLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .deleteWorkOrderCheckDetailInfo(billDetailNo)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(createObserver(deleteWorkOrderCheckDetailLiveData))
    }

    fun getWorkOrderInfo(billNo: String) {
        workOrderInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getWorkOrderInfo(billNo)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(workOrderInfoLiveData))
    }

    fun getWorkOrderDetailInfo(billDetailNo: String) {
        workOrderCheckInfoLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getWorkOrderCheckDetailInfo(billDetailNo)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(workOrderCheckInfoLiveData))
    }

}