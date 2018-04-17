package com.yeucheng.yue.ui.activitys.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.ui.activitys.IHomeView;
import com.yeucheng.yue.ui.adapter.LeftMenuAdapter;
import com.yeucheng.yue.ui.presenter.IHomeViewPresenter;
import com.yeucheng.yue.ui.presenter.Impl.HomeViewPresenterImpl;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.ToastUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;
import com.yeucheng.yue.widget.CircleImageView;
import com.yeucheng.yue.widget.CustomXViewPager;
import com.yeucheng.yue.widget.NoScrollListView;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 * 主界面
 */

public class HomeActivity extends AbsBaseActivity implements IHomeView {
    //主布局用具有侧滑功能DrawerLayout
    @FindView(R.id.drawer)
    private DrawerLayout mDrawerLayout;
    @FindView(R.id.searchlayout)
    private FrameLayout mSearchLayout;
    @FindView(R.id.chat)
    //底部导航四按钮
    private RadioButton mChat;
    @FindView(R.id.contact)
    private RadioButton mContact;
    @FindView(R.id.moment)
    private RadioButton mMoment;
    @FindView(R.id.service)
    private RadioButton mService;
    //工具栏
    @FindView(R.id.common_toolbar)
    private Toolbar mToolBar;
    //业务类处理对象
    private IHomeViewPresenter mPresenter;
    //搜索框
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    //设置按钮
    @FindView(R.id.settings)
    private LinearLayout mSettingBtn;
    //切换主题按钮
    @FindView(R.id.changethem)
    private LinearLayout mChangeBtn;
    //左侧菜单list
    @FindView(R.id.lv_setting)
    private NoScrollListView mLeftMenuList;
    @FindView(R.id.content)
    private CustomXViewPager mMainPager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViewsAndEvents() {
        //设置页面可滑动
        mMainPager.setScanScroll(true);
        //关闭右滑返回上一级
        getSwipeBackLayout().setEnableGesture(false);
        mPresenter = new HomeViewPresenterImpl(mContext, this);
        //设置工具栏
        initTool();
        //设置底部导航栏字体的背景颜色selector
        setRadioButtonTextBgColor(mChat, mContact, mMoment, mService);
        clearButtonStatues();
        mChat.setChecked(true);
        //加载主视图
        mPresenter.loadMainView();
        //加载左边菜单页
        mPresenter.loadLeftView();
    }

    /**
     * 设置底部导航栏点击字体selector
     *
     * @param radioButton
     */
    private void setRadioButtonTextBgColor(RadioButton... radioButton) {
        for (RadioButton r :
                radioButton) {
            r.setTextColor(SelectorUtils.getTextColorDrawable(mContext));
        }
    }

    /**
     * 设置工具栏
     */
    private void initTool() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_titlebar, null);
        TextView toolBarTitle = (TextView) view.findViewById(R.id.toolbartitle);
        CircleImageView backIcon = (CircleImageView) view.findViewById(R.id
                .toolbar_ic_back);
        toolBarTitle.setText("YUE");
        backIcon.setImageDrawable(getResources().getDrawable(R.drawable.yue));
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mToolBar.addView(view);
        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(false);
        ab.setDisplayShowTitleEnabled(false);//设置不显示默认appname
        ab.setDisplayHomeAsUpEnabled(false);//关闭默认回退功能
        //菜单点击事件 todo
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(mContext, SelectFriendsActivity.class);
                switch (item.getItemId()) {
                    case R.id.action_startchat://发起聊天isAddGroupMember
//                        intent.putExtra("isAddGroupMember", true);
//                        mContext.startActivity(intent);
                        break;
                    case R.id.action_joinfriend://添加好友
//                        startActivity(AddFriendActivity.class);
                        break;
                    case R.id.action_creategroup://创建群组createGroup
//                        intent.putExtra("createGroup", true);
//                        mContext.startActivity(intent);
                        break;
                    case R.id.action_qrcode://扫一扫
//                        startActivityForResult(new Intent(HomeActivity.this, QRcodeActivity.class), REQUEST_CODE);
                        break;
                    case R.id.action_forhelp://帮助
//                        startActivity(ForHelpActivity.class);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 清除按钮点击状态
     */
    private void clearButtonStatues() {
        mChat.setChecked(false);
        mContact.setChecked(false);
        mMoment.setChecked(false);
        mService.setChecked(false);
    }

    /**
     * 设置按钮点击状态
     *
     * @param position
     */
    private void setcheck(int position) {
        clearButtonStatues();
        switch (position) {
            case 0:
                mChat.setChecked(true);
                break;
            case 1:
                mContact.setChecked(true);
                break;
            case 2:
                mMoment.setChecked(true);
                break;
            case 3:
                mService.setChecked(true);
                break;
        }
    }

    /**
     * 底部导航栏点击事件
     *
     * @param view
     */
    @OnClick({R.id.chat, R.id.moment, R.id.contact, R.id.service})
    private void onBottomTebClick(View view) {
        switch (view.getId()) {
            case R.id.chat:
                setcheck(0);
                mMainPager.setCurrentItem(0);
                break;
            case R.id.contact:
                setcheck(1);
                mMainPager.setCurrentItem(1);
                break;
            case R.id.moment:
                setcheck(2);
                mMainPager.setCurrentItem(2);
                break;
            case R.id.service:
                setcheck(3);
                mMainPager.setCurrentItem(3);
                break;

        }
    }

    /**
     * 侧边菜单点击事件
     *
     * @param view
     */
    @OnClick(value = {R.id.changethem, R.id.settings}, preventRepetClick = true)
    private void leftMenuClick(View view) {
        switch (view.getId()) {
            case R.id.settings:
                startActivity(SettingsActivity.class);
                break;
            case R.id.changethem:
                mPresenter.changeTheme();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_right, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initSearchView(mSearchView);
        setSearchViewListener(mSearchView);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置SearchView参数,
     *
     * @param mSearchView
     */
    private void setSearchViewListener(SearchView mSearchView) {
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchLayout.setVisibility(View.GONE);
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchLayout.setVisibility(View.VISIBLE);
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //todo
                showMessage("TextSubmit----->" + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //todo
                showMessage("TextChange----->" + s);
                return false;
            }
        });
    }

    /**
     * 让弹出的菜单同时显示图标和文字
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 设置SearchView
     *
     * @param mSearchView
     */
    private void initSearchView(SearchView mSearchView) {
        mSearchAutoComplete = (SearchView.SearchAutoComplete)
                mSearchView.findViewById(R.id
                        .search_src_text);
        mSearchAutoComplete.setTextColor(getResources().getColor(R.color.white));
        mSearchAutoComplete.setHintTextColor(getResources().getColor(R.color.black));
        //修改searview光标样式，避免和主题色相同而看不见，
        CommonUtils.setCursorIcon(mSearchView);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(true);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.setIconifiedByDefault(true);
        //设置搜索框直接展开显示。左侧无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.onActionViewExpanded();
        //设置输入框提示语
        mSearchView.setQueryHint("请输入关键字");
        //设置最大宽度
        mSearchView.setMaxWidth(mScreenWidth);
        //设置是否显示搜索框展开时的提交按钮
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 手机按键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }//如果搜索框还是打开的
            else if (mSearchAutoComplete.isShown()) {
                try {
                    Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                    method.setAccessible(true);
                    method.invoke(mSearchView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                moveTaskToBack(false);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 加载主页面fragments
     *
     * @param fragments
     * @param index
     */
    @Override
    public void loadContents(final List<Fragment> fragments, int index) {
        if (null != fragments && !fragments.isEmpty()) {
            FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }
            };
            mMainPager.setAdapter(adapter);
            mMainPager.setOffscreenPageLimit(4);
            mMainPager.addOnPageChangeListener(new MainPagerOnpageChangeLisener());
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ftr = fm.beginTransaction();
//            ftr.replace(R.id.content, fragments.get(index));
//            ftr.commit();
        }
    }


    //页面变化监听
    class MainPagerOnpageChangeLisener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setcheck(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置主题
     */
    @Override
    public void setTheme() {
        Intent themeintent = getIntent();
        themeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(themeintent);
    }

    /**
     * 加载左侧菜单
     *
     * @param leftMenuListData
     */
    @Override
    public void loadLeftMenuList(List<Map<String, Object>> leftMenuListData) {
        LeftMenuAdapter adapter = new LeftMenuAdapter(mContext, leftMenuListData);
        mLeftMenuList.setAdapter(adapter);
    }

    /**
     * 左侧菜单item点击监听
     */
    @Override
    public void setLeftMenuListItemClickListener() {
        mLeftMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        ToastUtils.showmessage("我的文件");
                        break;
                    case 1:
                        ToastUtils.showmessage("我的相册");
                        break;
                }
            }
        });
    }

}
