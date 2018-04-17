package com.yeucheng.yue.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.sp.SpSave;

/**
 * Created by Administrator on 2018/3/2.
 */

public class AppUtils {
    private static Context mContext;
    private static Thread mUiThread;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void init(Context context) { //在Application中初始化
        mContext = context;
        mUiThread = Thread.currentThread();
    }

    /**
     * 获取application Context
     *
     * @return Context
     */
    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 获取assets目录
     *
     * @return
     */
    public static AssetManager getAssets() {
        return mContext.getAssets();
    }

    /**
     * 获取res目录
     *
     * @return
     */
    public static Resources getResource() {
        return mContext.getResources();
    }

    public static Resources.Theme getTheme() {
        return mContext.getTheme();
    }

    /**
     * 判断是否UI线程
     *
     * @return
     */
    public static boolean isUIThread() {
        return Thread.currentThread() == mUiThread;
    }

    /**
     * 在UI线程执行
     *
     * @param r
     */
    public static void runOnUI(Runnable r) {
        sHandler.post(r);
    }

    public static void runOnUIDelayed(Runnable r, long delayMills) {
        sHandler.postDelayed(r, delayMills);
    }

    public static void removeRunnable(Runnable r) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null);
        } else {
            sHandler.removeCallbacks(r);
        }
    }

    /**
     * 检查是否登录
     *
     * @return
     */
    public static boolean checkIsLogin() {
        String token = (String) SharedPreferencesUtils.getParam(mContext,
                SpSave.RONGIM_TOKEN, ""
        );
        String nickName = (String) SharedPreferencesUtils.getParam(mContext,
                SpSave.LOGING_PHONE,
                "");
        String passWord = (String) SharedPreferencesUtils.getParam(mContext,
                SpSave.LOGING_PHONE,
                "");
        return (!TextUtils.isEmpty(token));
    }
}
