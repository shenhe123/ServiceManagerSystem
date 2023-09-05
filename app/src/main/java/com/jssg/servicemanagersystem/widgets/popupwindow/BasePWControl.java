package com.jssg.servicemanagersystem.widgets.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.LayoutRes;

/**
 * popupwindow控制器
 * create by shenhe at 2020.3.9
 */
public abstract class BasePWControl {
    protected boolean mIsLand;
    protected View mLocationView;
    protected PopupWindow mPopupWindow;
    protected View mView;
    protected Context mContext;
    protected int popupWidth;
    protected int popupHeight;

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        mPopupWindow = popupWindow;
    }

    public BasePWControl(Context context, ViewGroup layoutParent) {
        mContext = context;
        mView = ((Activity) context).getLayoutInflater().inflate(injectLayout(), layoutParent, false);
        mView.setFocusable(true);
        mPopupWindow = new PopupWindow(mView, injectParamsWight(), injectParamsHeight(),true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setClippingEnabled(false);
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = mView.getMeasuredWidth();
        popupHeight =  mView.getMeasuredHeight();
        injectBackDrawable();
        mPopupWindow.setOnDismissListener(this::dismissEnd);
        if (injectAnimationStyle() != -1){
            mPopupWindow.setAnimationStyle(injectAnimationStyle());
        }
        if (isBackKeyDismiss()) {
            mPopupWindow.setFocusable(false);
            mView.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return isBackKeyDismiss();
                }
                return false;
            });
        }
        initView();
    }

    public BasePWControl(Context context, ViewGroup layoutParent, View locationView, boolean isLand) {
        mContext = context;
        mView = ((Activity) context).getLayoutInflater().inflate(injectLayout(), layoutParent, false);
        mView.setFocusable(true);
        mLocationView = locationView;
        mIsLand = isLand;
        mPopupWindow = new PopupWindow(mView, injectParamsWight(), injectParamsHeight(),true);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setClippingEnabled(false);
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = mView.getMeasuredWidth();
        popupHeight =  mView.getMeasuredHeight();
        injectBackDrawable();
        mPopupWindow.setOnDismissListener(this::dismissEnd);
        if (injectAnimationStyle() != -1){
            mPopupWindow.setAnimationStyle(injectAnimationStyle());
        }
        if (isBackKeyDismiss()) {
            mPopupWindow.setFocusable(false);
            mView.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return isBackKeyDismiss();
                }
                return false;
            });
        }
        initView();
    }

    public boolean isBackKeyDismiss() {
        return false;
    }

    /**
     * 控件初始化
     */
    protected abstract void initView();

    /**
     * 设置布局资源
     *
     * @return
     */
    protected abstract @LayoutRes
    int injectLayout();

    /**
     * 设置动画
     */
    protected abstract int injectAnimationStyle();

    /**
     * 设置背景图
     */
    protected  void injectBackDrawable(){
        mPopupWindow.setBackgroundDrawable(null);
    }

    /**
     * 返回popupwindow高度
     *
     * @return
     */
    public int injectParamsHeight() {
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    /**
     * 返回popupwindow宽度
     */
    public int injectParamsWight() {
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    /**
     * 返回popupwindow高度
     *
     * @return
     */
    public int getPopupWidth() {
        return popupWidth;
    }
    /**
     * 返回popupwindow宽度
     */
    public int getPopupHeight() {
        return popupHeight;
    }


    /**
     * 显示位置
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        mPopupWindow.showAtLocation(parent, gravity, x, y);
        showStart();
    }

    public void showAsDropDown(View parent, int x, int y) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            parent.getGlobalVisibleRect(visibleFrame);
            int height = parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            mPopupWindow.setHeight(height - y);//y是正值，则高度需要减去y；y是负值，需要上移，高度+y
            mPopupWindow.showAsDropDown(parent, x, y);
        } else {
            mPopupWindow.showAsDropDown(parent, x, y);
        }

        showStart();
    }

    /**
     * 是否正在显示
     */
    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    /**
     * 消除popupwindwo
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
        dismissEnd();
    }

    /**
     * cancel
     */
    protected void cancel() {

    }

    /**
     * 开始显示
     */
    protected void showStart(){
    }
    /**
     * 控件消失
     */
    protected void dismissEnd(){
    }
}
