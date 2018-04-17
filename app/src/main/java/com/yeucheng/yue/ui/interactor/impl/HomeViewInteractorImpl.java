package com.yeucheng.yue.ui.interactor.impl;

import android.support.v4.app.Fragment;

import com.yeucheng.yue.R;
import com.yeucheng.yue.ui.fragments.impl.ContactsFragment;
import com.yeucheng.yue.ui.fragments.impl.ConversationListFragment;
import com.yeucheng.yue.ui.fragments.impl.ServiceFragment;
import com.yeucheng.yue.ui.fragments.impl.MomentsFragment;
import com.yeucheng.yue.ui.interactor.IHomeViewInteractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */

public class HomeViewInteractorImpl implements IHomeViewInteractor {
    /**
     * 获取fragments
     *
     * @return
     */
    @Override
    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<Fragment>() {
            {
                add(new ConversationListFragment());
                add(new ContactsFragment());
                add(new MomentsFragment());
                add(new ServiceFragment());
            }
        };
        return fragments;
    }

    /**
     * 主页面左侧菜单中列表
     *
     * @return
     */
    @Override
    public List getLeftMenuListData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map;
        map = new HashMap<String, Object>() {
            {
                put("img", R.drawable.ic_folder_open_black_24dp);
                put("title", "我的文件");
            }
        };
        data.add(map);
        map = new HashMap<String, Object>() {
            {
                put("img", R.drawable.ic_photo_library_black_24dp);
                put("title", "我的相册");
            }
        };
        data.add(map);
        return data;
    }

}
