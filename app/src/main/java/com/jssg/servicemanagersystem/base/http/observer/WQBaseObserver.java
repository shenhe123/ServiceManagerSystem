package com.jssg.servicemanagersystem.base.http.observer;


import com.jssg.servicemanagersystem.core.AppApplication;
import com.jssg.servicemanagersystem.base.http.observer.error.ApiException;
import com.jssg.servicemanagersystem.base.http.observer.error.ExceptionEngin;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by gongdongyang on 2018/7/19.
 */

public abstract class WQBaseObserver<T> implements Observer<T> {


    private Disposable disposable;

    public WQBaseObserver() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }


    @Override
    public void onError(Throwable t) {
        ApiException exception = ExceptionEngin.handleException(t);
        handleErrorCode(exception.getCode(), exception.getDisplayMessage());
        if (AppApplication.Companion.getInstance() .isRunInBackground()) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }


    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onComplete() {

    }


    /**
     * 拦截登录失效的错误码信息
     *
     * @param code
     * @param message
     */
    private void handleErrorCode(int code, String message) {
        //网络异常 其他 错误信息
        switch (code) {
            case ExceptionEngin.UNAUTHORIZED:
            case 20051://无法验证用户身份（Token过期）
            case 20053://您已在另一个设备登出，请重新登录
            case 20054://当前登录信息已失效，请重新登录
            case 2005101://当前登录信息已失效，请重新登录
            case 20055://当前登录信息已失效，请重新登录
            case 20056://您的密码已修改，请重新登录
            case 20057://您的谷歌2FA设置有变更，请重新登录
            case 2010://用户不存在
//            case 200001://账户冻结、cookie 解析失败 这样产品要求已经和nirui 确定 。pc之前有地方也是验证2fa返回200001的code，但是实际上后端没有登出
            case -1://退出 就完事了。原因看msg
//                AccountManger
//                        .getInstance().userAuthExpire();
//                //发送登录信息过期事件
//                EventBus.getDefault().post(new LoginExpiresEvent(message));
                break;
            case 209999://使用旧的cookie 访问时返回这个错误码 弹出登陆框不可取消 点击确定到登陆
//                AccountManger
//                        .getInstance().userAuthExpire();
//                BMApplication.get().showLogoutDialog(message);
                break;
            default:
                if (code != ExceptionEngin.HTTP_ERROR_TIME_OUT) {
                    onFailure(code, message);
                } else {
                    onComplete();
                }
                break;
        }


    }


    /**
     * 网络请求失败
     *
     * @param code
     * @param message
     */
    public void onFailure(int code, String message) {

    }

    public abstract void onSuccess(T o);


    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public boolean isDisposed() {
        return disposable != null && disposable.isDisposed();
    }

}
