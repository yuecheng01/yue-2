package com.yeucheng.yue.ui.fragments.impl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.BaseLazyFragment;
import com.yeucheng.yue.ui.activitys.impl.circle.FriendCircleActivity;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;
import com.yeucheng.yue.util.inject.ViewUtils;

/**
 * Created by Administrator on 2018/3/5.
 */

public class MomentsFragment extends BaseLazyFragment {
    @FindView(R.id.friendcircle)
    private LinearLayout mFriendCircle;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_moments_layout);
        ViewUtils.inject(this.getContentView(), this);
        init();
    }

    private void init() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }

    @OnClick({R.id.friendcircle})
    private void momentOnClick(View view) {
        switch (view.getId()) {
            case R.id.friendcircle:
                startActivity(new Intent(mActivity, FriendCircleActivity.class));
                break;
        }
    }
}
