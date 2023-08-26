package com.jssg.servicemanagersystem.base.http.observer;


import android.content.Context;

import com.jssg.servicemanagersystem.R;
import com.jssg.servicemanagersystem.base.LoadingDialog;

import io.reactivex.disposables.Disposable;

/**
 * Created by gongdongyang on 2018/7/19.
 */

public abstract class ProgressDialogObserver<T> extends WQBaseObserver<T>{


    public Context context;
    private String LoadingText;

    public ProgressDialogObserver(Context context) {
        this(context, context.getString(R.string.app_common_http_loading));
    }

    public ProgressDialogObserver(Context context, String LoadingText) {
        this.context = context;
        this.LoadingText = LoadingText;
    }

    private LoadingDialog loadingDialog = null;

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog.Builder(context)
                    .setMessage(LoadingText)
                    .setCancelable(true)
                    .create();
        }
        loadingDialog.show();
    }

    public void closeLoading() {
        if (context!=null && loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        closeLoading();
    }

    @Override
    public void onComplete() {
        closeLoading();
    }

}
