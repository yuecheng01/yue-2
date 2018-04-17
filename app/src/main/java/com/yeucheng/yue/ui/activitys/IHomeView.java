package com.yeucheng.yue.ui.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface IHomeView {
    void loadContents(List<Fragment> mlist, int index);

    void setTheme();

    void loadLeftMenuList(List<Map<String, Object>> leftMenuListData);

    void setLeftMenuListItemClickListener();
}
