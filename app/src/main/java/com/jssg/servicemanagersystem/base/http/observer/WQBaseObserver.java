package com.jssg.servicemanagersystem.base.http.observer;


import com.jssg.servicemanagersystem.base.http.observer.error.ApiException;
import com.jssg.servicemanagersystem.base.http.observer.error.ExceptionEngin;
import com.jssg.servicemanagersystem.core.AccountManager;
import com.jssg.servicemanagersystem.core.AppApplication;
import com.jssg.servicemanagersystem.ui.login.LoginActivity;

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
        if (AppApplication.Companion.get() .isRunInBackground()) {
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
                AccountManager.Companion.getInstance().logout();
                LoginActivity.Companion.goActivity(AppApplication.Companion.get().getLastActivity());
                break;
                case ExceptionEngin.FORBIDDEN:
                case ExceptionEngin.BAD_GATEWAY:
                case ExceptionEngin.INTERNAL_SERVER_ERROR:
                case ExceptionEngin.SERVER_ERROR:
                    onFailure(code, message);
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
