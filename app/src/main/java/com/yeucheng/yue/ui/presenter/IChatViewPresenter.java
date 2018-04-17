package com.yeucheng.yue.ui.presenter;

import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IChatViewPresenter {
    void loadChatFragment(Conversation.ConversationType conversationType, String targetId);
}
