package com.jssg.servicemanagersystem.utils;

import android.content.Context;

/**
 * Created by bitmaxltd on 2018/10/8.
 */

public class DpPxUtils {
    /**
     * dip转换成Px
     *
     * @param context
     * @param dipValue
     * @Description:
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     *
     * @param context
     * @param pxValue
     * @Description:
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
