package com.yeucheng.yue.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/3/5.
 */

public abstract class AbsBaseFragment extends Fragment {
    protected LayoutInflater inflater;
    private View contentView;
    private Context context;
    private ViewGroup container;
    public Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onCreateView(savedInstanceState);
        if (contentView == null)
        return super.onCreateView(inflater, container, savedInstanceState);
        return contentView;
    }
    protected void onCreateView(Bundle savedInstanceState) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        contentView = null;
        container = null;
        inflater = null;
    }
    public Context getApplicationContext() {
        return context;
    }
    public void setContentView(int layoutResID) {
        setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
    }
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }
}
