package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;

import com.flyco.animation.SlideEnter.SlideTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.yeucheng.yue.app.UserInfoManager;
import com.yeucheng.yue.base.BaseAppManager;
import com.yeucheng.yue.ui.activitys.ISettingsView;
import com.yeucheng.yue.ui.activitys.impl.StartActivity;
import com.yeucheng.yue.ui.presenter.ISettingsViewPresenter;

/**
 * Created by Administrator on 2018/3/7.
 */

public class SettingsViewPresenterImpl implements ISettingsViewPresenter {
    private Context mContext;
    private ISettingsView mView;

    public SettingsViewPresenterImpl(Context context, ISettingsView isettingsView) {
        super();
        this.mContext = context;
        this.mView = isettingsView;
    }

    public void exitUser() {
        final NormalDialog exitDialog = new NormalDialog(mContext);
        exitDialog.content("是否确定退出程序?")
                .showAnim(new SlideTopEnter())
                .dismissAnim(new SlideBottomExit())
                .show();
        exitDialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                exitDialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                UserInfoManager.getInstance().exitApp();
                BaseAppManager.getInstance().clear();
                mView.jump2Activity(StartActivity.class);
            }
        });
    }
}
