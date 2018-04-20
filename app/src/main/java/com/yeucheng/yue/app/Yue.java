package com.yeucheng.yue.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yeucheng.yue.util.AppUtils;
import com.yeucheng.yue.util.LogUtils;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/3/2.
 */

public class Yue extends MultiDexApplication {
    private static Context mContext;
    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppUtils.init(mContext);
        //开启log打印
        LogUtils.setDeBug(true);
        mRefWatcher = LeakCanary.install(this);//检测内存泄漏
        //融云初始化
        RongIM.init(this);
        //用户信息管理类
        UserInfoManager.init(this);
    }
    public static RefWatcher getRefWatcher(Context context) {
        Yue application = (Yue) context.getApplicationContext();
        return application.mRefWatcher;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
