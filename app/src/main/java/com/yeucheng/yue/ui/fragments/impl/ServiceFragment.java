package com.yeucheng.yue.ui.fragments.impl;

import android.os.Bundle;
import android.widget.TextView;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.BaseLazyFragment;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.ViewUtils;

/**
 * Created by Administrator on 2018/3/5.
 */

public class ServiceFragment extends BaseLazyFragment {

    @FindView(R.id.tv)
    private TextView mTextView;
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_service_layout);
        ViewUtils.inject(this.getContentView(),this);
        init();
    }

    private void init() {
        mTextView.setText("敬请期待");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }
}
