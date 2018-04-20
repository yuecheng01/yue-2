package com.yeucheng.yue.base;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yeucheng.yue.R;
import com.yeucheng.yue.app.Yue;
import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.sp.SpSave;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.LogUtils;
import com.yeucheng.yue.util.ToastUtils;
import com.yeucheng.yue.util.inject.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by Administrator on 2018/3/2.
 */

public abstract class AbsBaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    protected String TAG = getClass().getSimpleName();
    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;
    /**
     * context
     */
    protected Context mContext = null;

    /**
     * 加载过度期弹出的对话框
     */
    //    public LoadingDialog mLoadingDialog;
    /**
     * 是否设置状态栏
     */
    protected boolean mStatusBarCompat = true;

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //******************设置主题*************************
        String mThem = (String) SharedPreferencesUtils.getParam(this, SpSave.YUE_THEME,
                "default");
        //获取theme的集合;
        Map<String, Integer> map = CommonUtils.getThtmeMap();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            LogUtils.d(TAG, "key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            if (mThem.equals(entry.getKey())) {
                //设置主题
                setTheme(entry.getValue());
                //匹配上就不再轮循
                break;
            }
        }
        //******************设置主题*************************
        BaseAppManager.getInstance().addActivity(this);
        //获取屏幕尺寸
        mScreenWidth = CommonUtils.getScreenWidth(this);
        mScreenHeight = CommonUtils.getScreenHeight(this);
        //设置状态栏
        if (mStatusBarCompat) {

        }
        //绑定布局
        if (getLayoutID() != 0) {
            setContentView(getLayoutID());
        } else {
            throw new IllegalArgumentException("You must return a right content resourse id");
        }
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        initViewsAndEvents();
    }

    protected abstract void initViewsAndEvents();

    protected abstract int getLayoutID();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtils.inject(this);
    }


    /**
     * 设置工具栏
     *
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, String title) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            //使能app bar的导航功能
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
    }

    protected void startActivity(Class clazz) {
        super.startActivity(new Intent(mContext, clazz));
    }

    /**
     * 弹出toast
     *
     * @param s
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void showMessage(String s) {
        ToastUtils.showmessage(s);
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG,"onDestory");
        // 观察内存泄漏情况
        Yue.getRefWatcher(this).watch(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        LogUtils.d(TAG,"finish");
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }

    //********************************************************implements SwipeBackActivityBase**********************************************************************
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    //********************************************************implements SwipeBackActivityBase**********************************************************************
}
