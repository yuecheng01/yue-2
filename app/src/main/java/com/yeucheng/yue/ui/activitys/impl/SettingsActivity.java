package com.yeucheng.yue.ui.activitys.impl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.ui.activitys.ISettingsView;
import com.yeucheng.yue.ui.presenter.ISettingsViewPresenter;
import com.yeucheng.yue.ui.presenter.Impl.SettingsViewPresenterImpl;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;

/**
 * Created by Administrator on 2018/3/7.
 */

public class SettingsActivity extends AbsBaseActivity implements ISettingsView {
    @FindView(R.id.common_toolbar)
    private Toolbar mToolBar;
    @FindView(R.id.exit)
    private Button mExitBtn;
    private ISettingsViewPresenter mPresenter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_settings;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initViewsAndEvents() {
        mPresenter = new SettingsViewPresenterImpl(mContext,this);
        initToolBar(mToolBar, "设置");
        mExitBtn.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                .color.act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
    }

    @OnClick({R.id.exit})
    private void settingsClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                mPresenter.exitUser();
                break;
        }
    }

    @Override
    public void jump2Activity(Class<?> startActivityClass) {
        SettingsActivity.this.finish();
        startActivity(StartActivity.class);
    }
}
