package com.yeucheng.yue.ui.fragments.impl;

import android.net.Uri;
import android.os.Bundle;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.BaseLazyFragment;

import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/3/5.
 */

public class ConversationListFragment extends BaseLazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_conversationlist_layout);
        init();
    }

    private void init() {
        io.rong.imkit.fragment.ConversationListFragment fragment = (io.rong.imkit.fragment.ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }
}
