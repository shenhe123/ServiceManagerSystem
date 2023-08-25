package com.jssg.servicemanagersystem.utils.toast.style;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;


/**
 * @Author wangqiong
 * @create 2019-05-09 16:28
 */
public class BitMaxStyle extends BlackToastStyle {

    private boolean isNight;

    public BitMaxStyle(boolean isNight) {
        this.isNight = isNight;
    }

    @Override
    protected int getTextColor(Context context) {
        return Color.WHITE;
    }

    @Override
    protected Drawable getBackgroundDrawable(Context context) {
        GradientDrawable drawable = new GradientDrawable();
        // 设置颜色
        drawable.setColor(isNight ? 0xFF252E3d : 0xa0000000);
        // 设置圆角
        drawable.setCornerRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, context.getResources().getDisplayMetrics()));
        return drawable;
    }


    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 90;
    }


    @Override
    protected float getTranslationZ(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
    }

}
