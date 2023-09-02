package com.jssg.servicemanagersystem.base.http.apiservice

import com.jssg.servicemanagersystem.base.entity.BaseHttpResult
import com.jssg.servicemanagersystem.ui.account.entity.DeptInfo
import com.jssg.servicemanagersystem.ui.account.entity.FactoryInfo
import com.jssg.servicemanagersystem.ui.account.entity.Role
import com.jssg.servicemanagersystem.ui.account.entity.User
import com.jssg.servicemanagersystem.ui.account.entity.UserData
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.ui.login.entity.LoginEntity
import io.reactivex.Observable
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
    fun getDeptInfo(@Query("parentId") parentId: String): Observable<BaseHttpResult<List<DeptInfo>?>>

    @PUT("staging-api/system/role")
    fun updateRoleInfo(@Body body: RequestBody): Observable<BaseHttpResult<Any>>

}