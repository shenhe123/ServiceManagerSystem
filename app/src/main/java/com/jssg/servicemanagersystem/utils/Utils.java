package com.jssg.servicemanagersystem.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jssg.servicemanagersystem.core.AppApplication;

import java.util.List;

public class Utils {

    //关闭软键盘
    public static void hideKeyBoard(Activity context) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && context.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (context.getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //关闭软键盘
    public static void hideKeyBoard(Dialog context) {
        View view = context.getWindow().peekDecorView();//注意：这里要根据EditText位置来获取
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) AppApplication.Companion.get().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //关闭软键盘
    public static void hideKeyBoard(EditText et) {
        InputMethodManager inputmanger = (InputMethodManager) AppApplication.Companion.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    //打开软键盘
    public static void showKeyBoard(EditText et) {
        InputMethodManager inputManager = (InputMethodManager) AppApplication.Companion.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 复制文本内容
     */
    public static void copyStringText(CharSequence text, Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(ClipData.newPlainText(text, text));
    }


    //生成随机数
    public static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }


    public static String RANDOMSTR = "abcdefghijklmnopqrstuvwxyz";

    //生成随机数
    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = RANDOMSTR.length();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOMSTR.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }


    public static String RANDOMSTR_ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //生成随机数
    public static String getRandomString2(int length) {
        StringBuffer sb = new StringBuffer();
        int len = RANDOMSTR_ALL.length();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOMSTR_ALL.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }


    /**
     * 判断是否使用的系统自带键盘
     *
     * @param mCtx
     * @return
     */
    public static boolean isUsingCustomInputMethod(Context mCtx) {
        InputMethodManager imm = (InputMethodManager)
                mCtx.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties
                = imm.getEnabledInputMethodList();
        final int
                N = mInputMethodProperties.size();
        for (int i
             = 0; i < N; i++) {
            InputMethodInfo imi =
                    mInputMethodProperties.get(i);
            if (imi.getId().equals(Settings.Secure.getString(mCtx.getContentResolver(),
                    Settings.Secure.DEFAULT_INPUT_METHOD))) {
                if ((imi.getServiceInfo().applicationInfo.flags
                        & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断程序是否在后台运行
     */
    public static boolean isRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 表明程序在后台运行
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    public static int getNetWorkType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
