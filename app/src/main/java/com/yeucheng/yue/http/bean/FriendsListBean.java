package com.yeucheng.yue.http.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FriendsListBean {
    int resulecode;
    List<FriendBean> value;

    public int getResulecode() {
        return resulecode;
    }

    public void setResulecode(int resulecode) {
        this.resulecode = resulecode;
    }

    public List<FriendBean> getValue() {
        return value;
    }

    public void setValue(List<FriendBean> value) {
        this.value = value;
    }
}
