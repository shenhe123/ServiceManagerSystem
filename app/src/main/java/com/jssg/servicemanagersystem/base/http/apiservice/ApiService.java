package com.jssg.servicemanagersystem.base.http.apiservice;

import com.jssg.servicemanagersystem.base.entity.BaseHttpResult;
import com.jssg.servicemanagersystem.ui.login.entity.LoginEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by gongdongyang on 2018/9/25.
 */

public interface ApiService {

    //退出登录
    @POST("staging-api/logout")
    Observable<BaseHttpResult> logout();

    //登录
    @POST("staging-api/login")
    Observable<BaseHttpResult<LoginEntity>> postLogin(@Body RequestBody body);

}
