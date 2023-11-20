package com.jssg.servicemanagersystem.base.http.apiservice

import com.jssg.servicemanagersystem.base.entity.BaseHttpResult
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.LogInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.entity.UserData
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.ui.account.entity.UserRoles
import com.jssg.servicemanagersystem.ui.account.logmanager.entity.OptionLogParentEntity
import com.jssg.servicemanagersystem.ui.login.entity.LoginEntity
import com.jssg.servicemanagersystem.ui.report.entity.ReportListInfo
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import com.jssg.servicemanagersystem.utils.download.UpdateEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * Created by gongdongyang on 2018/9/25.
 */
interface ApiService {

    //退出登录
    @POST("logout")
    fun logout(): Observable<BaseHttpResult<Any>>

    //登录
    @POST("login")
    fun postLogin(@Body body: RequestBody): Observable<BaseHttpResult<LoginEntity?>>

    @GET("getInfo")
    fun getInfo(): Observable<BaseHttpResult<UserInfo?>>

    @PUT("system/user/profile")
    fun updateUserProfile(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @FormUrlEncoded
    @PUT("system/user/profile/updatePwd")
    fun updatePassword(
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Observable<BaseHttpResult<Any>>

    @POST("system/role")
    fun postAddNewRole(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("system/user/list")
    fun getUserList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<User>>>

    @GET("system/role/list")
    fun getRoleList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<Role>>>

    @PUT("system/user")
    fun updateUserInfo(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @DELETE("system/user/{userId}")
    fun deleteUserInfo(@Path("userId") userId: Long): Observable<BaseHttpResult<Any>>

    @POST("system/user")
    fun addNewUser(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("system/user/")
    fun getUserRoles(): Observable<BaseHttpResult<UserRoles>>

    @GET("system/user/list")
    fun searchUser(
        @Query("userName") userName: String?,
        @Query("nickName") nickName: String?,
        @Query("phonenumber") phonenumber: String?,
        @Query("createBy") createBy: String?,
        @Query("factory") factory: String?,
        @Query("params[\"expiredStartDate\"]") expiredStartDate: String?,
        @Query("params[\"expiredEndDate\"]") expiredEndDate: String?,
        @Query("params[\"creatorStartDate\"]") creatorStartDate: String?,
        @Query("params[\"creatorEndDate\"]") creatorEndDate: String?,
        @Query("userType") userType: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<User>?>>

    @GET("system/role/list")
    fun searchRole(
        @Query("key") input: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<Role>?>>

    @GET("system/user/app/{userId}")
    fun getUserInfo(@Path("userId") userId: Long): Observable<BaseHttpResult<UserData?>>

    @GET("system/user/organizations")
    fun getFactoryInfo(): Observable<BaseHttpResult<List<WorkFactoryInfo>?>>

    @GET("system/user/deptTree")
    fun getDeptInfo(@Query("parentId") parentId: Int = 1): Observable<BaseHttpResult<List<DeptInfo>?>>

    @GET("qm/workOrder/orgList")
    fun getWorkFactoryInfo(): Observable<BaseHttpResult<List<WorkFactoryInfo>?>>

    @GET("qm/workOrder/deptList")
    fun getWorkDeptInfo(): Observable<BaseHttpResult<List<WorkDeptInfo>?>>

    @PUT("system/role")
    fun updateRoleInfo(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("qm/workOrder/list")
    fun getWorkOrderList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<WorkOrderInfo>?>>

    @GET("qm/workOrder/{billNo}/")
    fun getWorkOrderInfo(@Path("billNo") billNo: String): Observable<BaseHttpResult<WorkOrderInfo?>>

    @GET("qm/workOrderDetail/list")
    fun getWorkOrderCheckList(
        @Query("billNo") billNo: String,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<WorkOrderCheckInfo>?>>

    @GET("qm/workOrderDetail/{billDetailNo}/")
    fun getWorkOrderCheckDetailInfo(@Path("billDetailNo") billDetailNo: String): Observable<BaseHttpResult<WorkOrderCheckInfo?>>

    @GET("qm/workOrderDetail/list")
    fun searchWorkOrderCheckList(
        @Query("batchNo") batchNo: String?,
        @Query("state") state: String?,
        @Query("params[\"beginCheckDate\"]") beginApplyDate: String?,
        @Query("params[\"endCheckDate\"]") endApplyDate: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<WorkOrderCheckInfo>?>>

    @GET("qm/workOrder/list")
    fun searchWorkOrderList(
        @Query("applyName") applyName: String?,
        @Query("tel") tel: String?,
        @Query("productCode") productCode: String?,
        @Query("productDes") productDes: String?,
        @Query("params[\"beginApplyDate\"]") beginApplyDate: String?,
        @Query("params[\"endApplyDate\"]") endApplyDate: String?,
        @Query("oaBillNo") oaBillNo: String?,
        @Query("orgService") orgService: String?,
        @Query("checkState") checkState: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<WorkOrderInfo>?>>

    @POST("system/oss/upload")
    fun fileOssUpload(@Body body: MultipartBody): Observable<BaseHttpResult<UploadEntity?>>

    @POST("qm/workOrderDetail")
    fun addWorkOrderDetail(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @POST("qm/workOrder")
    fun addWorkOrder(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @PUT("qm/workOrderDetail")
    fun updateWorkOrderDetail(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("system/oss/listByIds/{ossIds}")
    fun getOssListByIds(@Path("ossIds") ids: String): Observable<BaseHttpResult<List<UploadEntity>>>

    @POST("qm/workOrderDetail/approve")
    fun reviewWorkOrderCheck(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @POST("qm/workOrder/finish")
    fun closeCaseWorkOrderCheck(@Query("billNos") billNos: String): Observable<BaseHttpResult<Any>>

    @GET("qm/tripReport/list")
    fun getTravelReportList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<TravelReportInfo>?>>

    @GET("qm/tripReport/list")
    fun searchTravelReportList(
        @Query("applyName") applyName: String?,
        @Query("params[\"beginTripDate\"]") beginTripDate: String?,
        @Query("params[\"endTripDate\"]") endTripDate: String?,
        @Query("orgName") orgName: String?,
        @Query("dept") dept: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<TravelReportInfo>?>>

    @GET("qm/tripReport/orgList")
    fun getTravelReportFactoryInfo(): Observable<BaseHttpResult<List<WorkFactoryInfo>?>>

    @GET("qm/tripReport/deptList")
    fun getTravelReportDeptInfo(): Observable<BaseHttpResult<List<WorkDeptInfo>?>>

    @POST("qm/tripReport")
    fun addNewTravelReport(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @PUT("qm/tripReport")
    fun updateTravelReport(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @DELETE("system/role/{roleId}")
    fun deleteRoleInfo(@Path("roleId") roleId: String): Observable<BaseHttpResult<Any>>

    @DELETE("qm/workOrder/{billNo}")
    fun deleteWorkOrderInfo(@Path("billNo") billNo: String): Observable<BaseHttpResult<Any>>

    @DELETE("qm/workOrderDetail/{billDetailNo}")
    fun deleteWorkOrderCheckDetailInfo(@Path("billDetailNo") billDetailNo: String): Observable<BaseHttpResult<Any>>

    @GET("qm/tripReport/{billNo}")
    fun getTravelReportInfo(@Path("billNo") billNo: String): Observable<BaseHttpResult<TravelReportInfo?>>

    @Streaming
    @GET("qm/tripReport/export")
    fun getTravelReportExport(@Query("billNo") billNo: String): Call<ResponseBody>

    @Streaming
    @POST("qm/tripReport/exportAll")
    fun getTravelReportExport(): Call<ResponseBody>

    @Streaming
    @GET("qm/workOrderDetail/reportExport")
    fun getReportListExport(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("noImage") noImage: Boolean
    ): Call<ResponseBody>

    @Streaming
    @GET("qm/workOrderDetail/reportExport")
    fun searchReportListExport(
        @Query("applyName") applyName: String?,
        @Query("batchNo") batchNo: String?,
        @Query("productCode") productCode: String?,
        @Query("productDes") productDes: String?,
        @Query("params[\"beginApplyDate\"]") beginApplyDate: String?,
        @Query("params[\"endApplyDate\"]") endApplyDate: String?,
        @Query("oaBillNo") oaBillNo: String?,
        @Query("orgService") orgService: String?,
        @Query("checkState") checkState: String?,
        @Query("noImage") noImage: Boolean
    ): Call<ResponseBody>

    @GET("monitor/logininfor/list")
    fun getLoginLogInfoList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<LogInfo>?>>

    @GET("monitor/logininfor/list")
    fun searchLoginLogInfoList(
        @Query("userName") userName: String?,
        @Query("params[\"beginTime\"]") beginApplyDate: String?,
        @Query("params[\"endTime\"]") endApplyDate: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<LogInfo>?>>

    @GET("monitor/operlog/list")
    fun getOptionLogInfoList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<OptionLogParentEntity>?>>

    @GET("monitor/operlog/list")
    fun searchOptionLogInfoList(
        @Query("title") title: String?,
        @Query("businessType") businessType: String?,
        @Query("operName") operName: String?,
        @Query("operParam") operParam: String?,
        @Query("status") status: String?,
        @Query("params[\"beginTime\"]") beginApplyDate: String?,
        @Query("params[\"endTime\"]") endApplyDate: String?,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<OptionLogParentEntity>?>>

    @GET("qm/workOrderDetail/reportList")
    fun getReportList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Observable<BaseHttpResult<List<ReportListInfo>?>>

    @GET("qm/workOrderDetail/reportList")
    fun searchReportList(
        @Query("applyName") applyName: String?,
        @Query("batchNo") batchNo: String?,
        @Query("productCode") productCode: String?,
        @Query("productDes") productDes: String?,
        @Query("params[\"beginApplyDate\"]") beginApplyDate: String?,
        @Query("params[\"endApplyDate\"]") endApplyDate: String?,
        @Query("oaBillNo") oaBillNo: String?,
        @Query("orgService") orgService: String?,
        @Query("checkState") checkState: String?
    ): Observable<BaseHttpResult<List<ReportListInfo>?>>


    @GET("qm/appVersionConfig")
    fun getUpdateInfo(): Observable<BaseHttpResult<UpdateEntity?>>
}