package com.bigkoo.pickerview.configure;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.contrarywind.view.WheelView;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Build Options
 * Created by xiaosongzeem on 2018/3/8.
 */

public class PickerOptions implements Parcelable {

    //constant
    private static final int PICKER_VIEW_BTN_COLOR_NORMAL = 0xFF057dff;
    private static final int PICKER_VIEW_BG_COLOR_TITLE = 0xFFf5f5f5;
    private static final int PICKER_VIEW_COLOR_TITLE = 0xFF000000;
    private static final int PICKER_VIEW_BG_COLOR_DEFAULT = 0xFFFFFFFF;

    public static final int TYPE_PICKER_OPTIONS = 1;
    public static final int TYPE_PICKER_TIME = 2;

    public OnOptionsSelectListener optionsSelectListener;
    public OnTimeSelectListener timeSelectListener;
    public View.OnClickListener cancelListener;

    public OnTimeSelectChangeListener timeSelectChangeListener;
    public OnOptionsSelectChangeListener optionsSelectChangeListener;
    public CustomListener customListener;

    //options picker
    public String label1, label2, label3;//单位字符
    public int option1, option2, option3;//默认选中项
    public int x_offset_one, x_offset_two, x_offset_three;//x轴偏移量

    public boolean cyclic1 = false;//是否循环，默认否
    public boolean cyclic2 = false;
    public boolean cyclic3 = false;

    public boolean isRestoreItem = false; //切换时，还原第一项


    //time picker
    public boolean[] type = new boolean[]{true, true, true, false, false, false};//显示类型，默认显示： 年月日

    public Calendar date;//当前选中时间
    public Calendar startDate;//开始时间
    public Calendar endDate;//终止时间
    public int startYear;//开始年份
    public int endYear;//结尾年份

    public boolean cyclic = false;//是否循环
    public boolean isLunarCalendar = false;//是否显示农历

    public String label_year, label_month, label_day, label_hours, label_minutes, label_seconds;//单位
    public int x_offset_year, x_offset_month, x_offset_day, x_offset_hours, x_offset_minutes, x_offset_seconds;//单位


    public PickerOptions(int buildType) {
        if (buildType == TYPE_PICKER_OPTIONS) {
            layoutRes = R.layout.pickerview_options;
        } else {
            layoutRes = R.layout.pickerview_time;
        }
    }

    //******* general field ******//
    public int layoutRes;
    public ViewGroup decorView;
    public int[] textGravity = new int[] {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER};
    public Context context;

    public String textContentConfirm;//确定按钮文字
    public String textContentCancel;//取消按钮文字
    public String textContentTitle;//标题文字

    public int textColorConfirm = PICKER_VIEW_BTN_COLOR_NORMAL;//确定按钮颜色
    public int textColorCancel = PICKER_VIEW_BTN_COLOR_NORMAL;//取消按钮颜色
    public int textColorTitle = PICKER_VIEW_COLOR_TITLE;//标题颜色

    public int bgColorWheel = PICKER_VIEW_BG_COLOR_DEFAULT;//滚轮背景颜色
    public int bgColorTitle = PICKER_VIEW_BG_COLOR_TITLE;//标题背景颜色

    public int textSizeSubmitCancel = 17;//确定取消按钮大小
    public int textSizeTitle = 18;//标题文字大小
    public int textSizeContent = 18;//内容文字大小

    public int textColorOut = 0xFFa8a8a8; //分割线以外的文字颜色
    public int textColorCenter = 0xFF2a2a2a; //分割线之间的文字颜色
    public int dividerColor = 0xFFd5d5d5; //分割线的颜色
    public int outSideColor = -1; //显示时的外部背景色颜色,默认是灰色

    public float lineSpacingMultiplier = 1.6f; // 条目间距倍数 默认1.6
    public boolean isCenterLabel = false;//是否只显示中间的label,默认每个item都显示
    public Typeface font = Typeface.MONOSPACE;//字体样式
    public WheelView.DividerType dividerType = WheelView.DividerType.FILL;//分隔线类型
    public int itemsVisibleCount = 9; //最大可见条目数
    public boolean isAlphaGradient = false; //透明度渐变

    protected PickerOptions(Parcel in) {
        label1 = in.readString();
        label2 = in.readString();
        label3 = in.readString();
        option1 = in.readInt();
        option2 = in.readInt();
        option3 = in.readInt();
        x_offset_one = in.readInt();
        x_offset_two = in.readInt();
        x_offset_three = in.readInt();
        cyclic1 = in.readByte() != 0;
        cyclic2 = in.readByte() != 0;
        cyclic3 = in.readByte() != 0;
        isRestoreItem = in.readByte() != 0;
        type = in.createBooleanArray();
        startYear = in.readInt();
        endYear = in.readInt();
        cyclic = in.readByte() != 0;
        isLunarCalendar = in.readByte() != 0;
        label_year = in.readString();
        label_month = in.readString();
        label_day = in.readString();
        label_hours = in.readString();
        label_minutes = in.readString();
        label_seconds = in.readString();
        x_offset_year = in.readInt();
        x_offset_month = in.readInt();
        x_offset_day = in.readInt();
        x_offset_hours = in.readInt();
        x_offset_minutes = in.readInt();
        x_offset_seconds = in.readInt();
        layoutRes = in.readInt();
        textGravity = in.createIntArray();
        textContentConfirm = in.readString();
        textContentCancel = in.readString();
        textContentTitle = in.readString();
        textColorConfirm = in.readInt();
        textColorCancel = in.readInt();
        textColorTitle = in.readInt();
        bgColorWheel = in.readInt();
        bgColorTitle = in.readInt();
        textSizeSubmitCancel = in.readInt();
        textSizeTitle = in.readInt();
        textSizeContent = in.readInt();
        textColorOut = in.readInt();
        textColorCenter = in.readInt();
        dividerColor = in.readInt();
        outSideColor = in.readInt();
        lineSpacingMultiplier = in.readFloat();
        isCenterLabel = in.readByte() != 0;
        itemsVisibleCount = in.readInt();
        isAlphaGradient = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label1);
        dest.writeString(label2);
        dest.writeString(label3);
        dest.writeInt(option1);
        dest.writeInt(option2);
        dest.writeInt(option3);
        dest.writeInt(x_offset_one);
        dest.writeInt(x_offset_two);
        dest.writeInt(x_offset_three);
        dest.writeByte((byte) (cyclic1 ? 1 : 0));
        dest.writeByte((byte) (cyclic2 ? 1 : 0));
        dest.writeByte((byte) (cyclic3 ? 1 : 0));
        dest.writeByte((byte) (isRestoreItem ? 1 : 0));
        dest.writeBooleanArray(type);
        dest.writeInt(startYear);
        dest.writeInt(endYear);
        dest.writeByte((byte) (cyclic ? 1 : 0));
        dest.writeByte((byte) (isLunarCalendar ? 1 : 0));
        dest.writeString(label_year);
        dest.writeString(label_month);
        dest.writeString(label_day);
        dest.writeString(label_hours);
        dest.writeString(label_minutes);
        dest.writeString(label_seconds);
        dest.writeInt(x_offset_year);
        dest.writeInt(x_offset_month);
        dest.writeInt(x_offset_day);
        dest.writeInt(x_offset_hours);
        dest.writeInt(x_offset_minutes);
        dest.writeInt(x_offset_seconds);
        dest.writeInt(layoutRes);
        dest.writeIntArray(textGravity);
        dest.writeString(textContentConfirm);
        dest.writeString(textContentCancel);
        dest.writeString(textContentTitle);
        dest.writeInt(textColorConfirm);
        dest.writeInt(textColorCancel);
        dest.writeInt(textColorTitle);
        dest.writeInt(bgColorWheel);
        dest.writeInt(bgColorTitle);
        dest.writeInt(textSizeSubmitCancel);
        dest.writeInt(textSizeTitle);
        dest.writeInt(textSizeContent);
        dest.writeInt(textColorOut);
        dest.writeInt(textColorCenter);
        dest.writeInt(dividerColor);
        dest.writeInt(outSideColor);
        dest.writeFloat(lineSpacingMultiplier);
        dest.writeByte((byte) (isCenterLabel ? 1 : 0));
        dest.writeInt(itemsVisibleCount);
        dest.writeByte((byte) (isAlphaGradient ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PickerOptions> CREATOR = new Creator<PickerOptions>() {
        @Override
        public PickerOptions createFromParcel(Parcel in) {
            return new PickerOptions(in);
        }

        @Override
        public PickerOptions[] newArray(int size) {
            return new PickerOptions[size];
        }
    };
}
