package com.yeucheng.yue.util;

/**
 * Created by Administrator on 2018/3/5.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import com.yeucheng.yue.R;

/**
 * 定义背景selector
 */
public class SelectorUtils {
    private SelectorUtils() {
    }

    /**
     * 按钮点击背景selector
     * 圆角矩形
     */
    public static Drawable getCommenButtonBg(int pressColor,int nomalColor) {

        StateListDrawable stateDrawable = new StateListDrawable();
        GradientDrawable normalDrawable = new GradientDrawable();
        GradientDrawable pressedDrawable = new GradientDrawable();
        GradientDrawable disabledDrawable = new GradientDrawable();

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
//        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_enabled};
        // 为各种状态下的 drawable 设置 attr 颜色值
        pressedDrawable.setColor(pressColor);
        normalDrawable.setColor(nomalColor);
        disabledDrawable.setColor(nomalColor);

        // 为各种状态下的 drawable 设置圆角等属性.
        normalDrawable.setCornerRadius(5);
        pressedDrawable.setCornerRadius(5);
        disabledDrawable.setCornerRadius(5);

        stateDrawable.addState(states[0], pressedDrawable);
//        stateDrawable.addState(states[1], pressedDrawable);
        stateDrawable.addState(states[3], disabledDrawable);
        stateDrawable.addState(states[2], normalDrawable);

        return stateDrawable;
    }
    public static ColorStateList getTextColorDrawable(Context context){
        int[][] states = new int[2][];
        states[0] =  new int[]{android.R.attr.state_checked};
        states[1] = new int[]{};
        int[] colors = new int[]{CommonUtils.getColorByAttrId(context, R.attr.colorPrimary),
                context.getResources().getColor(R.color.F888888)};
        ColorStateList stateDrawable = new ColorStateList(states,colors);
        return stateDrawable;
    }
    public static Drawable getDividerItemDrawable(){
        GradientDrawable drawable =new GradientDrawable();
        drawable.setSize(CommonUtils.getScreenWidth(AppUtils.getAppContext()),1);
        drawable.setColor(CommonUtils.getColorByAttrId(AppUtils.getAppContext(),R.attr.colorPrimary));
        return drawable;
    }

    /**
     * 定制toast背景
     * @return
     */
    public static Drawable getToastBackDrawable(){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(5);
        drawable.setColor(AppUtils.getAppContext().getResources().getColor(R.color.toastbackground));
        return drawable;
    }
}
