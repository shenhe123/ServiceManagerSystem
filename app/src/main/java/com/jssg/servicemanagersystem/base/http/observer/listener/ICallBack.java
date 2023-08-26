package com.jssg.servicemanagersystem.base.http.observer.listener;

/**
 * Created by gongdongyang on 2018/7/19.
 */

public interface ICallBack<T> {

    /**
     * 网络请求成功
     * @param t
     */
    void onSuccess(T t);

    /**
     * 网络请求失败
     * @param code
     * @param message
     */
     void onFailure(int code, String message);

}
