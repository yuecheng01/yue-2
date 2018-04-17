package com.yeucheng.yue.ui.interactor;

import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface IHomeViewInteractor {
    List<Fragment> getFragments();

    List<Map<String, Object>> getLeftMenuListData();
}
