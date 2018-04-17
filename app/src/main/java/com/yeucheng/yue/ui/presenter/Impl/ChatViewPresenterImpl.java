package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.yeucheng.yue.R;
import com.yeucheng.yue.ui.activitys.IChatView;
import com.yeucheng.yue.ui.presenter.IChatViewPresenter;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/3/7.
 */

public class ChatViewPresenterImpl implements IChatViewPresenter {
    private Context mContext;
    private IChatView mView;

    public ChatViewPresenterImpl(Context context, IChatView iChatView) {
        super();
        this.mContext = context;
        this.mView = iChatView;
    }

    public void loadChatFragment(Conversation.ConversationType conversationType, String targetId) {
        ConversationFragment fragment = (ConversationFragment) ((AppCompatActivity) mContext)
                .getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();

        fragment.setUri(uri);
    }
}
