package com.jssg.servicemanagersystem.base.http.apiservice

import com.jssg.servicemanagersystem.base.entity.BaseHttpResult
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.FactoryInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.entity.UserData
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.ui.login.entity.LoginEntity
import com.jssg.servicemanagersystem.ui.travelreport.entity.TravelReportInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkDeptInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkFactoryInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderCheckInfo
import com.jssg.servicemanagersystem.ui.workorder.entity.WorkOrderInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by gongdongyang on 2018/9/25.
 */
interface ApiService {

    //退出登录
    @POST("staging-api/logout")
    fun logout(): Observable<BaseHttpResult<Any>>

    //登录
    @POST("staging-api/login")
    fun postLogin(@Body body: RequestBody): Observable<BaseHttpResult<LoginEntity?>>

    @GET("staging-api/getInfo")
    fun getInfo(): Observable<BaseHttpResult<UserInfo?>>

    @PUT("staging-api/system/user/profile")
    fun updateUserProfile(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @FormUrlEncoded
    @PUT("staging-api/system/user/profile/updatePwd")
    fun updatePassword(@Field("oldPassword") oldPassword: String, @Field("newPassword") newPassword: String): Observable<BaseHttpResult<Any>>

    @POST("staging-api/system/role")
    fun postAddNewRole(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("staging-api/system/user/list")
    fun getUserList(@Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<User>>>

    @GET("staging-api/system/role/list")
    fun getRoleList(@Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<Role>>>

    @PUT("staging-api/system/user")
    fun updateUserInfo(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @DELETE("staging-api/system/user/{userId}")
    fun deleteUserInfo(@Path("userId") userId: Long): Observable<BaseHttpResult<Any>>

    @POST("staging-api/system/user")
    fun addNewUser(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("staging-api/system/user/list")
    fun searchUser(@Query("key") input: String, @Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<User>?>>

    @GET("staging-api/system/role/list")
    fun searchRole(@Query("key") input: String, @Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<Role>?>>

    @GET("staging-api/system/user/app/{userId}")
    fun getUserInfo(@Path("userId") userId: Long): Observable<BaseHttpResult<UserData?>>

    @GET("staging-api/system/user/organizations")
    fun getFactoryInfo(): Observable<BaseHttpResult<List<FactoryInfo>?>>

    @GET("staging-api/system/user/deptTree")
    fun getDeptInfo(@Query("parentId") parentId: Int = 1): Observable<BaseHttpResult<List<DeptInfo>?>>

    @GET("staging-api/qm/workOrder/orgList")
    fun getWorkFactoryInfo(): Observable<BaseHttpResult<List<WorkFactoryInfo>?>>

    @GET("staging-api/qm/workOrder/deptList")
    fun getWorkDeptInfo(): Observable<BaseHttpResult<List<WorkDeptInfo>?>>

    @PUT("staging-api/system/role")
    fun updateRoleInfo(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("staging-api/qm/workOrder/list")
    fun getWorkOrderList(@Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<WorkOrderInfo>?>>

    @GET("staging-api/qm/workOrderDetail/list")
    fun getWorkOrderCheckList(@Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<WorkOrderCheckInfo>?>>

    @GET("staging-api/qm/workOrderDetail/list")
    fun searchWorkOrderCheckList(@Query("key") input: String, @Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<WorkOrderCheckInfo>?>>

    @GET("staging-api/qm/workOrder/list")
    fun searchWorkOrderList(@Query("key") input: String, @Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<WorkOrderInfo>?>>

    @POST("staging-api/system/oss/upload")
    fun fileOssUpload(@Body body: MultipartBody): Observable<BaseHttpResult<UploadEntity?>>

    @POST("staging-api/qm/workOrderDetail")
    fun addWorkOrderDetail(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @POST("staging-api/qm/workOrder")
    fun addWorkOrder(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @PUT("staging-api/qm/workOrderDetail")
    fun updateWorkOrderDetail(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @GET("staging-api/system/oss/listByIds/{ossIds}")
    fun getOssListByIds(@Path("ossIds") ids: String): Observable<BaseHttpResult<List<UploadEntity>>>

    @POST("staging-api/qm/workOrderDetail/approve")
    fun reviewWorkOrderCheck(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

    @POST("staging-api/qm/workOrder/finish")
    fun closeCaseWorkOrderCheck(@Query("billNos") billNos: String): Observable<BaseHttpResult<Any>>

    @GET("staging-api/qm/tripReport/list")
    fun getTravelReportList(@Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<TravelReportInfo>?>>

    @GET("staging-api/qm/tripReport/list")
    fun searchTravelReportList(@Query("key") input: String, @Query("pageNum") pageNum: Int, @Query("pageSize") pageSize: Int): Observable<BaseHttpResult<List<TravelReportInfo>?>>

    @GET("staging-api/qm/tripReport/orgList")
    fun getTravelReportFactoryInfo(): Observable<BaseHttpResult<List<WorkFactoryInfo>?>>

    @GET("staging-api/qm/tripReport/deptList")
    fun getTravelReportDeptInfo(): Observable<BaseHttpResult<List<WorkDeptInfo>?>>
}