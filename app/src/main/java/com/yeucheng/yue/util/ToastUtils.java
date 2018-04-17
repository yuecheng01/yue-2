package com.yeucheng.yue.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeucheng.yue.R;

/**
 * Created by yuecheng on 2017/10/30.
 */

public class ToastUtils {
    private static TextView tvToast;
    private static Toast mToast;

    private ToastUtils() {
    }

    private static void getToast(Context context) {
        if (mToast == null) {
            mToast = new Toast(context);
        }
        if (tvToast == null) {
            tvToast = new TextView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            tvToast.setLayoutParams(params);
            tvToast.setBackgroundColor(context.getResources().getColor(R.color.toastbackground));
            tvToast.setTextColor(context.getResources().getColor(R.color.white));
        }
        mToast.setView(tvToast);
    }

    public static void showmessage( final Object message) {
        getToast(AppUtils.getAppContext());
        tvToast.setText(message + "");
        //居下
//                mInstance.setGravity(Gravity.CENTER, 0, CommonUtils.getScreenHeight(mContext) / 3);
        //居中
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(tvToast);
        mToast.show();

    }
}
