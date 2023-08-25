package com.jssg.servicemanagersystem.utils.toast;

import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.StringRes;

/**
 * 避免同样的信息多次触发重复弹出的问题
 */
public class ToastUtils {

    private ToastUtils() {
        throw new RuntimeException("ToastUtils cannot be initialized");
    }

    public static void showToast(String message) {

        if (TextUtils.isEmpty(message)) return;
        //非Ui线程不能toast
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) return;
        ToastTool.show(message);
    }

    public static void showToast(@StringRes int resId) {
        ToastTool.show(resId);
    }


}
