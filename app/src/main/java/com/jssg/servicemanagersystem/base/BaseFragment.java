package com.jssg.servicemanagersystem.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel;
import com.jssg.servicemanagersystem.utils.toast.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseFragment extends Fragment {


    private CompositeDisposable compositeDisposable = null;

    public void showProgressbarLoading() {
//        showProgressbarLoading(getString(R.string.app_common_http_loading));
        showProgressbarLoading("");
    }


    public void showProgressbarLoading(String msg) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressbarLoading(msg);
        }
    }

    public void showProgressbarLoading(String msg, boolean isShowMsg, boolean isCancel) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressbarLoading(msg, isShowMsg, isCancel);
        }
    }

    public void hideLoading() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
        }
    }

    /**
     * toast网络的错误回调信息
     *
     * @param loadDataModel
     */
    public void handleErrorToast(@NonNull LoadDataModel loadDataModel) {
        if (loadDataModel.isError() && isVisible() && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            ToastUtils.showToast(loadDataModel.getMsg());
        }
    }


    protected void updateLoading(@NonNull LoadDataModel loadDataModel) {
        updateLoading(loadDataModel, false);
    }

    /**
     * 显示loading
     *
     * @param loadDataModel
     * @param showToast     是否需要在error的时候 toast ErrorMessage
     */
    protected void updateLoading(@NonNull LoadDataModel loadDataModel, boolean showToast) {

        if (loadDataModel == null) {
            hideLoading();
            return;
        }

        if (loadDataModel.isLoading()) {
            showProgressbarLoading();
        } else {
            hideLoading();
        }
        if (showToast && loadDataModel.isError()) {
            handleErrorToast(loadDataModel);
        }
    }


    public void addDisposable(Disposable disposable) {
        if (disposable == null) return;
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLoading();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

    }

    public void clearComposite() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        clearComposite();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
