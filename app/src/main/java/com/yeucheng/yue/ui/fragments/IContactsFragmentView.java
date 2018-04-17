package com.yeucheng.yue.ui.fragments;

import com.yeucheng.yue.http.bean.FriendBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public interface IContactsFragmentView {
    void loadFriendsList(List<FriendBean> list);
}
