package com.jssg.servicemanagersystem.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jssg.servicemanagersystem.R;

/**
 * @Author wangqiong
 * @create 2020/3/31 7:57 PM
 */
public class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    //主题dialogfragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        setCancelable(isCancelable());
    }


    public boolean isCancelable() {
        return true;
    }


    public boolean isExpanded() {
        return true;
    }

    @Override
    public void dismiss() {
        View view = getDialog().getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
        super.dismiss();
    }


    private BottomSheetBehavior mBehavior;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBehavior = dialog.getBehavior();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        //默认全屏展开
        if (isExpanded() && mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


}
