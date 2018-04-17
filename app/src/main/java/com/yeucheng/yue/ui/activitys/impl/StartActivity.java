package com.yeucheng.yue.ui.activitys.impl;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.ui.activitys.IStartView;
import com.yeucheng.yue.ui.presenter.IStartViewPresenter;
import com.yeucheng.yue.ui.presenter.Impl.StartViewPresenterImpl;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.anim.AnimationUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;
import com.yeucheng.yue.widget.CustomVideoView;

/**
 * Created by Administrator on 2018/3/5.
 * splash引导页完了后如首次未登录用户则首先进入此页面登录后才可使用
 */

public class StartActivity extends AbsBaseActivity implements IStartView {
    //背景播放视频控件
    @FindView(R.id.videoplayer)
    private CustomVideoView mVideoView;
    /**
     * 包含登录,注册按钮LinearLayout,给其设置出现和消失的动画,
     */
    @FindView(R.id.randl)
    private LinearLayout mLinearLayout;
    //登录按钮
    @FindView(R.id.login)
    private Button mLogin;
    //注册按钮
    @FindView(R.id.register)
    private Button mRegister;
    //界面跟布局
    @FindView(R.id.rl)
    private RelativeLayout mRelativeLayout;
    //业务处理对象
    private IStartViewPresenter mPresenter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initViewsAndEvents() {
        mPresenter = new StartViewPresenterImpl(mContext, this);
        //背景播放视频
        videoPlay();
        //注册按钮设置点击背景selector
        mRegister.setBackground(SelectorUtils.getCommenButtonBg(CommonUtils
                .getColorByAttrId(mContext, R.attr.colorPrimary), getResources().getColor(R.color
                .act)));
        //登录按钮设置点击背景selector
        mLogin.setBackground(SelectorUtils.getCommenButtonBg(getResources().getColor(R.color
                .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick(value = {R.id.login, R.id.register}, preventRepetClick = true)
    private void onClickRegisterOrLogin(View view) {
        switch (view.getId()) {
            case R.id.register:
                mPresenter.register();
                break;
            case R.id.login:
                mPresenter.login();
                break;
        }
    }

    /**
     * 重写Activity.attachBaseContext方法;解决VideoView导致的内存泄漏问题
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }

    private void videoPlay() {
        mVideoView.setMediaController(new MediaController(mContext));
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/login"));
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/login"));
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onPostResume() {
        if (mVideoView.isPlaying())
            mVideoView.resume();
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.suspend();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mVideoView.isPlaying())
            mVideoView.pause();
        super.onPause();
    }


    /**
     * //隐藏登录注册按钮布局
     */
    @Override
    public void btnRandLGone() {
        mLinearLayout.setVisibility(View.GONE);
        mLinearLayout.setAnimation(AnimationUtils.bottomExitAnimation());
    }

    /**
     * //登录注册按钮布局可见
     */
    @Override
    public void btnRandLVisable() {
        mLinearLayout.setVisibility(View.VISIBLE);
        mLinearLayout.setAnimation(AnimationUtils.topInAnimation());
    }

    @Override
    public void jumpToActivity(Class<?> clazz) {
        startActivity(clazz);
    }

    /**
     * 结束当前activity
     */
    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public View getViewParent() {
        return mRelativeLayout;
    }
}
