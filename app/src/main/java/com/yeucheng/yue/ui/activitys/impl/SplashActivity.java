package com.yeucheng.yue.ui.activitys.impl;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.sp.SpSave;
import com.yeucheng.yue.util.AppUtils;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.LogUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2018/3/2.
 */

public class SplashActivity extends AbsBaseActivity {
    @FindView(R.id.splashBg)
    private ImageView mSplashBg;
    @FindView(R.id.skipe)
    private TextView mSkipe;
    //存储图片id数组
    private int[] mImgs = new int[]{
            R.mipmap.p01,
            R.mipmap.p02,
            R.mipmap.p03,
            R.mipmap.p04,
            R.mipmap.p05,
            R.mipmap.p06,
            R.mipmap.p07,
            R.mipmap.p08
    };
    //倒计时
    private CountDownTimer mTimer;

    //是否登录
    private boolean mIsLogin;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initViewsAndEvents() {
        mIsLogin = AppUtils.checkIsLogin();
        mSkipe.setBackground(SelectorUtils.getCommenButtonBg(CommonUtils
                .getColorByAttrId(mContext, R.attr.colorPrimary), getResources().getColor(R.color
                .act)));
        init();
    }

    private void init() {
        int index = (int) (Math.random() * mImgs.length);
        mSplashBg.setImageResource(mImgs[index]);
        mTimer = new CountDownTimer(5500, 1000) {
            @Override
            public void onTick(long l) {
                mSkipe.setText(String.format(getResources().getString(R.string.skip), (int)
                        (l / 1000 + 0.1)));
            }

            @Override
            public void onFinish() {
                mSkipe.setText(String.format(getResources().getString(R.string.skip), 0));
                judeAndJump();
            }
        };
        mTimer.start();
        if (mIsLogin) {
            RongIM.connect((String) SharedPreferencesUtils.getParam(this, SpSave.RONGIM_TOKEN, ""), new
                    RongIMClient.ConnectCallback() {
                        @Override
                        public void onSuccess(String s) {
                            LogUtils.d(TAG, "ConnectCallback connect onSuccess");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            LogUtils.d(TAG, "ConnectCallback connect onError");
                        }

                        @Override
                        public void onTokenIncorrect() {
                            LogUtils.d(TAG, "ConnectCallback connect onTokenIncorrect");
                        }
                    });
        }
    }

    //跳转至新的界面
    private void jumpNewActivity(Class clazz) {
        startActivity(clazz);
        finish();
    }

    /**
     * 判断是否已登录,未登录跳转至登录页,已登录跳主页
     */
    private void judeAndJump() {
        finish();
        if (mIsLogin) {
            jumpNewActivity(HomeActivity.class);
        } else {
            jumpNewActivity(StartActivity.class);
        }
    }

    /**
     * 右上角跳过点击事件,
     * 点击跳过
     *
     * @param view
     */
    @OnClick(R.id.skipe)
    private void onSkipe(View view) {
        if (mTimer != null)
            mTimer.cancel();
        judeAndJump();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }
}
