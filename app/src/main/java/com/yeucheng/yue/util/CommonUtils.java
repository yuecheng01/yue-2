package com.yeucheng.yue.util;

/**
 * Created by Administrator on 2018/3/5.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.yeucheng.yue.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共工具类
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 获取appthem中主题颜色
     *
     * @param context
     * @param attrIdForColor appthem中引用颜色id
     * @return
     */
    public static int getColorByAttrId(Context context, @AttrRes int attrIdForColor) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attrIdForColor, typedValue, true);
        return typedValue.data;
    }

    //设置searchview的光标样式
    public static void setCursorIcon(SearchView searchview) {
        try {

            Class cls = Class.forName("android.support.v7.widget.SearchView");
            Field field = cls.getDeclaredField("mSearchSrcTextView");
            field.setAccessible(true);
            TextView tv = (TextView) field.get(searchview);


            Class[] clses = cls.getDeclaredClasses();
            for (Class cls_ : clses) {
                Log.e("TAG", cls_.toString());
                if (cls_.toString().endsWith("android.support.v7.widget.SearchView$SearchAutoComplete")) {
                    Class targetCls = cls_.getSuperclass().getSuperclass().getSuperclass().getSuperclass();
                    Field cuosorIconField = targetCls.getDeclaredField("mCursorDrawableRes");
                    cuosorIconField.setAccessible(true);
                    cuosorIconField.set(tv, R.drawable.color_cursor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "ERROR setCursorIcon = " + e.toString());
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 验证手机号码是否错误
     *
     * @param mobiles 手机号码
     * @return 手机号码是否是否正确（true---正确，false---错误的）
     */
    public static boolean isMobileNO(String mobiles) {
                    /*
                        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
                        联通：130、131、132、152、155、156、185、186
                        电信：133、153、180、189、（1349卫通）
                        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
                    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 返回主题集合
     *
     * @return
     */
    public static Map<String, Integer> getThtmeMap() {
        final String[] stringItems = CommonUtils.getThemeStyles();
        Map<String, Integer> map = new HashMap<String, Integer>() {
            {
                put(stringItems[0], R.style.DefaultAppThem);
                put(stringItems[1], R.style.RedAppThem);
                put(stringItems[2], R.style.PurpleAppThem);
                put(stringItems[3], R.style.BlackAppThem);
            }
        };
        return map;
    }

    /**
     * 主题的数组
     *
     * @return 主题数组
     */
    public static String[] getThemeStyles() {
        return new String[]{"default", "red", "purple", "black"};
    }
    /**
     * dp转pixel
     */
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * pixel转dp
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
