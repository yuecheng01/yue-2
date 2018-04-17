package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.yeucheng.yue.R;
import com.yeucheng.yue.base.BaseAppManager;
import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.sp.SpSave;
import com.yeucheng.yue.ui.activitys.IHomeView;
import com.yeucheng.yue.ui.interactor.IHomeViewInteractor;
import com.yeucheng.yue.ui.interactor.impl.HomeViewInteractorImpl;
import com.yeucheng.yue.ui.presenter.IHomeViewPresenter;
import com.yeucheng.yue.util.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

public class HomeViewPresenterImpl implements IHomeViewPresenter {
    private Context mContext;
    private IHomeView mView;
    private boolean mIsFirst = true;
    private IHomeViewInteractor mInteractor;
    private List<Fragment> mlist;

    public HomeViewPresenterImpl(Context context, IHomeView iHomeView) {
        this.mContext = context;
        this.mView = iHomeView;
        mInteractor = new HomeViewInteractorImpl();
    }

    /**
     * 加载第index页fragment
     *
     * @param index
     */
    public void loadFragments(int index) {
        mView.loadContents(mlist, index);
    }

    /**
     * 加载主页面视图
     */
    public void loadMainView() {
        if (null != mlist) {
            mlist.clear();
            mlist = null;
        }
        mlist = mInteractor.getFragments();
        if (mIsFirst) {
            mView.loadContents(mlist, 0);
            mIsFirst = false;
        }
    }

    /**
     * 加载主页面左侧菜单视图
     */
    public void loadLeftView() {
        mView.loadLeftMenuList(mInteractor.getLeftMenuListData());
        mView.setLeftMenuListItemClickListener();
    }

    /**
     * 切换主题
     */
    public void changeTheme() {
        final String[] stringItems = CommonUtils.getThemeStyles();
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.isTitleShow(false)
                .itemTextColor(CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary))
                .cancelText(CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary))
                .show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferencesUtils.setParam(mContext, SpSave.YUE_THEME, stringItems[position]);
                dialog.dismiss();
                BaseAppManager.getInstance().clear();
                mView.setTheme();
            }
        });
    }
}
