package com.yeucheng.yue.ui.activitys.impl;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.ui.activitys.IChatView;
import com.yeucheng.yue.ui.presenter.IChatViewPresenter;
import com.yeucheng.yue.ui.presenter.Impl.ChatViewPresenterImpl;
import com.yeucheng.yue.util.inject.FindView;

import java.util.Locale;

import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ChatActivity extends AbsBaseActivity implements IChatView {
    @FindView(R.id.common_toolbar)
    private Toolbar mToolBar;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * 业务处理对象
     */
    private IChatViewPresenter mPresenter;
    private String mTargetId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new ChatViewPresenterImpl(mContext, this);
        Intent intent = getIntent();
        mTargetId = intent.getData().getQueryParameter("targetId");
        initToolBar(mToolBar, mTargetId);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        //加载对话fragment
        mPresenter.loadChatFragment(mConversationType, mTargetId);
    }
}
