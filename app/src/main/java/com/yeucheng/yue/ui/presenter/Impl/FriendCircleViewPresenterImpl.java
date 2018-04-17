package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;

import com.yeucheng.yue.ui.activitys.IFriendCircleView;
import com.yeucheng.yue.ui.presenter.IFriendCircleViewPresenter;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FriendCircleViewPresenterImpl implements IFriendCircleViewPresenter {
    private Context mContext;
    private IFriendCircleView mView;

    public FriendCircleViewPresenterImpl(Context context, IFriendCircleView iFriendCircleView) {
        super();
        this.mContext = context;
        this.mView = iFriendCircleView;
    }
}
