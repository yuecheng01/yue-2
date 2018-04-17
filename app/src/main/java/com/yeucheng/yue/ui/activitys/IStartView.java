package com.yeucheng.yue.ui.activitys;

import android.view.View;

import com.yeucheng.yue.ui.activitys.impl.RegisterProtocolActivity;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface IStartView {
    void btnRandLGone();

    View getViewParent();

    void btnRandLVisable();

    void jumpToActivity(Class<?> a);

    void finishActivity();

}
