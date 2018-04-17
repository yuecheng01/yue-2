package com.yeucheng.yue.ui.activitys.impl;

import android.support.v7.widget.Toolbar;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.util.inject.FindView;

/**
 * Created by Administrator on 2018/3/6.
 * 用户注册协议页面
 */

public class RegisterProtocolActivity extends AbsBaseActivity {
    /**
     * 工具栏
     */
    @FindView(R.id.common_toolbar)
    protected Toolbar mToolBar;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_registerprorocol;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar(mToolBar, "yue用户协议");
    }
}
