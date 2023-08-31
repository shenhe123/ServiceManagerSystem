package com.jssg.servicemanagersystem.widgets.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.jssg.servicemanagersystem.R;
import com.jssg.servicemanagersystem.core.AppApplication;
import com.jssg.servicemanagersystem.utils.DpPxUtils;

/**
 * newto
 * author:shenhe on 2018/1/24.
 */

public class ThemeLineItemDecoration extends RecyclerView.ItemDecoration {


    private int dividerHeight;
    private Paint dividerPaint;

    private int paddingLeft;
    private int paddingRight;


    public ThemeLineItemDecoration(@ColorInt int color, int dividerHeight, int margin) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        this.dividerHeight = dividerHeight;
        paddingLeft = DpPxUtils.dip2px(AppApplication.Companion.get(), margin);
        paddingRight = DpPxUtils.dip2px(AppApplication.Companion.get(), margin);
    }

    public ThemeLineItemDecoration(@ColorInt int color, int dividerHeight, int marginleft, int marginright) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        this.dividerHeight = dividerHeight;
        paddingLeft = DpPxUtils.dip2px(AppApplication.Companion.get(), marginleft);
        paddingRight = DpPxUtils.dip2px(AppApplication.Companion.get(), marginright);
    }


    public ThemeLineItemDecoration(@ColorInt int color, int dividerHeight) {
        this(color, dividerHeight, 16);
    }

    public ThemeLineItemDecoration(@ColorInt int color) {
        this(color, 1);
    }

    public ThemeLineItemDecoration() {
        this(AppApplication.Companion.get().getResources().getColor(R.color.x_divider));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);


        int childCount = parent.getChildCount();
        int left = paddingLeft;
        int right = parent.getWidth() - paddingRight;

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
