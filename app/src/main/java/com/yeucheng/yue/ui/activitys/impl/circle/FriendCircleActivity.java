package com.yeucheng.yue.ui.activitys.impl.circle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.http.bean.CircleItem;
import com.yeucheng.yue.http.bean.CommentConfig;
import com.yeucheng.yue.http.bean.CommentItem;
import com.yeucheng.yue.http.bean.FavortItem;
import com.yeucheng.yue.http.bean.PhotoInfo;
import com.yeucheng.yue.ui.activitys.IFriendCircleView;
import com.yeucheng.yue.ui.adapter.CircleAdapter;
import com.yeucheng.yue.ui.adapter.CircleItemClickListenerCallBack;
import com.yeucheng.yue.ui.adapter.HFAdapter;
import com.yeucheng.yue.ui.presenter.IFriendCircleViewPresenter;
import com.yeucheng.yue.ui.presenter.Impl.FriendCircleViewPresenterImpl;
import com.yeucheng.yue.util.DatasUtil;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.ToastUtils;
import com.yeucheng.yue.util.inject.FindView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FriendCircleActivity extends AbsBaseActivity implements IFriendCircleView {
    @FindView(R.id.toolbar)
    private Toolbar mToolBar;
    //业务控制器
    private IFriendCircleViewPresenter mPresenter;
    @FindView(R.id.recycler)
    private RecyclerView mCircleList;
    private LinearLayoutManager mLinearLayoutManager;
    @FindView(R.id.refreshLayout)
    private SwipeRefreshLayout mRefreshLayout;

    private CircleAdapter mAdapter;
    private  List<CircleItem> mDatas;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_friendcircle;
    }

    @Override
    protected void initViewsAndEvents() {
        //设置工具栏
        initToolbar();
        //初始化业务处理类对象
        mPresenter = new FriendCircleViewPresenterImpl(this, this);
        //为mCircleList设置管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        mCircleList.setLayoutManager(mLinearLayoutManager);
        //添加分割线
        DividerItemDecoration d = new DividerItemDecoration(
                mContext, DividerItemDecoration.VERTICAL);
        d.setDrawable(SelectorUtils.getDividerItemDrawable());
        //实例化适配器对象
        mAdapter = new CircleAdapter(mContext);
        HFAdapter hfAdapter = new HFAdapter(mAdapter);
        hfAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.item_head_circle,
                null));
        mCircleList.setAdapter(hfAdapter);
        mCircleList.addItemDecoration(d);
        mCircleList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //todo...触摸时.回复框如果打开的则关闭
                return false;
            }
        });
        //更新
        update();
        //添加监听
        mAdapter.addItemClickListener(new ItemListener());
    }

    private void update() {
        mDatas = DatasUtil.createCircleDatas();
        mAdapter.updateData(mDatas);
    }

    /**
     * 设置工具栏
     */
    private void initToolbar() {
        setSupportActionBar(mToolBar);
        ActionBar ab = getSupportActionBar();
        //使能app bar的导航功能
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);//不使用系统的title
            ab.setDisplayHomeAsUpEnabled(true);//使能导航功能
        }
        //设置右上角菜单栏点击事件
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_send:
                        startActivity(PhotoPickActivity.class);
                        break;
                }
                return true;
            }
        });
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * item事件监听回调
     */
    private class ItemListener implements CircleItemClickListenerCallBack {
        @Override
        public void deleteCircle(String circleId) {
            List<CircleItem> circleItems = mDatas;
            for (int i = 0; i < circleItems.size(); i++) {
                if (circleId.equals(circleItems.get(i).getId())) {
                    circleItems.remove(i);
                    mAdapter.notifyDataSetChanged();
                    //circleAdapter.notifyItemRemoved(i+1);
                    return;
                }
            }
        }

        @Override
        public void deleteComment(int circlePosition, String commentItemId) {
            CircleItem item = mDatas.get(circlePosition);
            List<CommentItem> items = item.getComments();
            for (int i = 0; i < items.size(); i++) {
                if (commentItemId.equals(items.get(i).getId())) {
                    items.remove(i);
//                mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemChanged(circlePosition+1,"fuck");
                    return;
                }
            }
        }

        @Override
        public void showEditTextBody(CommentConfig config) {

        }

        @Override
        public void addFavort(int circlePosition) {
            FavortItem addItem = DatasUtil.createCurUserFavortItem();
            if (addItem != null) {
                CircleItem item = mDatas.get(circlePosition);
                item.getFavorters().add(addItem);
//            mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemChanged(circlePosition+1,"fuck");
            }
        }

        @Override
        public void deleteFavort(int circlePosition, String favorId) {
            CircleItem item = mDatas.get(circlePosition);
            List<FavortItem> items = item.getFavorters();
            for (int i = 0; i < items.size(); i++) {
                if (favorId.equals(items.get(i).getId())) {
                    items.remove(i);
//                mAdapter.notifyDataSetChanged();
                    mAdapter.notifyItemChanged(circlePosition+1,"fuck");
                    return;
                }
            }
        }

        @Override
        public void preViewPic(View view, int position, List<PhotoInfo> photos) {
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

            List<String> photoUrls = new ArrayList<String>();
            for (PhotoInfo photoInfo : photos) {
                photoUrls.add(photoInfo.url);
            }
            ImagePagerActivity.startImagePagerActivity(mContext
                    , photoUrls, position, imageSize);
        }

        @Override
        public void clickName(String userName, String userId) {
            ToastUtils.showmessage(userName + " &id = " +
                    userId);
        }

        @Override
        public void onClickCommentName(String name, String id) {
            ToastUtils.showmessage(name + " &id = " +
                    id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
