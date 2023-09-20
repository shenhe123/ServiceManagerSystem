package com.jssg.servicemanagersystem.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel;
import com.jssg.servicemanagersystem.utils.toast.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    public void showBack() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        setTitle("");

    }


    public boolean isNight() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }


    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {

        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }


    public void setAndroidNativeLightStatusBar(Activity activity, boolean isFull, boolean dark) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                if (isFull) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                if (isFull) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                } else {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }

            int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (!isNight()) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR; // 黑色
            } else {
                //白色
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }

            activity.getWindow().getDecorView().setSystemUiVisibility(vis);
        }
    }

    /**
     * 设置当前act 状态栏
     *
     * @param activity
     * @param dark
     */
    public void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        setAndroidNativeLightStatusBar(activity, false, dark);
    }


    @Override
    protected void onPause() {
        super.onPause();
        /***解决切换到任务状态页面显示内容的安全隐私问题*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    private LoadingDialog loadingDialog = null;


    private CompositeDisposable compositeDisposable = null;

    public void showProgressbarLoading() {
        showProgressbarLoading("");
    }

    public void showProgressbarLoading(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(true)
                    .create();
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public void showProgressbarLoading(String msg, boolean isShowMsg, boolean isCancel) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog.Builder(this)
                    .setMessage(msg)
                    .setShowMessage(isShowMsg)
                    .setCancelable(isCancel)
                    .create();
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }


    public void hideLoading() {
        if (loadingDialog == null) return;
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void destroyLoading() {
        hideLoading();
        if (loadingDialog != null) {
            loadingDialog = null;
        }
    }


    public void updateLoading(LoadDataModel loadDataModel) {
        updateLoading(loadDataModel, false);
    }

    public void updateLoading(LoadDataModel loadDataModel, boolean showToast) {
        if (loadDataModel.isLoading()) {
            showProgressbarLoading();
        } else {
            hideLoading();
        }
        if (showToast && loadDataModel.isError()) {
            ToastUtils.showToast(loadDataModel.getMsg());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        destroyLoading();
        super.onDestroy();
    }

}
