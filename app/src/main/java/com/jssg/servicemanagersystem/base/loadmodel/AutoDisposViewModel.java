package com.jssg.servicemanagersystem.base.loadmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jssg.servicemanagersystem.base.entity.BaseHttpResult;
import com.jssg.servicemanagersystem.base.http.observer.WQBaseObserver;
import com.jssg.servicemanagersystem.ui.account.entity.User;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AutoDisposViewModel extends ViewModel {


    public AutoDisposViewModel() {

    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public void addDispos(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected void clearCompositeDisposable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        clearCompositeDisposable();
    }

    protected <T> WQBaseObserver<T> createObserver(@NonNull MutableLiveData<LoadDataModel<T>> liveData) {
        return new WQBaseObserver<T>() {
            @Override
            public void onSuccess(T t) {
                if (t instanceof BaseHttpResult) {
                    if (((BaseHttpResult) t).isSuccess()) {
                        liveData.setValue(new LoadDataModel<>(t));
                    } else {
                        liveData.setValue(new LoadDataModel<>(((BaseHttpResult) t).code, ((BaseHttpResult) t).getMsg()));
                    }
                } else {
                    liveData.postValue(new LoadDataModel<>(t));
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                liveData.setValue(new LoadDataModel<>(code, message));
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addDispos(d);

            }
        };
    }

    protected <T> WQBaseObserver<T> createListObserver(@NonNull MutableLiveData<LoadListDataModel<T>> liveData, boolean isRefresh, int page) {
        return new WQBaseObserver<T>() {
            @Override
            public void onSuccess(T t) {
                if (t instanceof BaseHttpResult) {
                    BaseHttpResult result = (BaseHttpResult) t;
                    if (result.isSuccess()) {
                        liveData.setValue(new LoadListDataModel<>(t, isRefresh));
                    } else {
                        liveData.setValue(new LoadListDataModel<>(result.code, result.getMsg(), isRefresh));
                    }
                } else {
                    liveData.postValue(new LoadListDataModel<>(t, isRefresh));
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                liveData.setValue(new LoadListDataModel<>(code, message, isRefresh));
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addDispos(d);

            }
        };
    }

    protected <T> WQBaseObserver<BaseHttpResult<T>> createObserver2(@NonNull MutableLiveData<LoadDataModel<BaseHttpResult<T>>> liveData) {
        return new WQBaseObserver<BaseHttpResult<T>>() {
            @Override
            public void onSuccess(BaseHttpResult<T> result) {
                if (result.isSuccess()) {
                    liveData.setValue(new LoadDataModel<>(result));
                } else {
                    liveData.setValue(new LoadDataModel<>(result.code, result.getMsg()));
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                liveData.setValue(new LoadDataModel<>(code, message));
            }

            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                addDispos(d);
            }
        };
    }
}
