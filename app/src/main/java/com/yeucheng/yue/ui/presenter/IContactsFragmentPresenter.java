package com.yeucheng.yue.ui.presenter;

import com.yeucheng.yue.http.bean.FriendBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface IContactsFragmentPresenter {
    void loadContactsList();

    void setContactsLitters(List<FriendBean> list);
}
