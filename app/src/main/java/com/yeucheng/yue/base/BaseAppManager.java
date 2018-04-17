package com.yeucheng.yue.base;


import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

public class BaseAppManager {
    //单例
    private static BaseAppManager instance = null;
    //存储activity实例的集合
    private static List<AppCompatActivity> mActivities = new LinkedList<AppCompatActivity>();

    private BaseAppManager() {
    }

    public static BaseAppManager getInstance() {
        if (null == instance) {
            synchronized (BaseAppManager.class) {
                if (null == instance) {
                    instance = new BaseAppManager();
                }
            }
        }
        return instance;
    }

    public synchronized void addActivity(AppCompatActivity activity) {
        mActivities.add(activity);
    }

    public synchronized void removeActivity(AppCompatActivity activity) {
        if (mActivities.contains(activity))
            mActivities.remove(activity);
    }

    public synchronized void clear() {
        for (AppCompatActivity a :
                mActivities) {
            removeActivity(a);
            a.finish();
        }
    }
}
