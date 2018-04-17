package com.yeucheng.yue.ui.activitys.impl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/3/14.
 */

public class UserDetailActivity extends AbsBaseActivity {
    @FindView(R.id.toolbar)
    private Toolbar mToolbar;
    @FindView(R.id.chat_call)
    private Button mChatCall;
    @FindView(R.id.chat_word)
    private Button mChatWord;
    private String mUserId;
    private String mUserNickName;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_userdetial;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initViewsAndEvents() {
        //获取intent传值
        mUserId = getIntent().getStringExtra("userId");
        mUserNickName = getIntent().getStringExtra("userNickName");
        //设置工具栏
        initToolBar(mToolbar, mUserNickName);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置语音聊天按钮点击背景selector
        mChatCall.setBackground(SelectorUtils.getCommenButtonBg(CommonUtils
                .getColorByAttrId(mContext, R.attr.colorPrimary), getResources().getColor(R.color
                .act)));
        //设置发消息按钮设置点击背景selector
        mChatWord.setBackground(SelectorUtils.getCommenButtonBg(getResources().getColor(R.color
                .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
    }

    @OnClick({R.id.chat_call, R.id.chat_word})
    private void onChatClick(View view) {
        switch (view.getId()) {
            case R.id.chat_call:
                break;
            case R.id.chat_word:
                if (!TextUtils.isEmpty(mUserId)) {
                    RongIM.getInstance().startPrivateChat(this, mUserId,
                            mUserNickName);
                } else {

                }
                finish();
                break;
        }
    }
}
