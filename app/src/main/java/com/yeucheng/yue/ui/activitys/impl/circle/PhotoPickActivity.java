package com.yeucheng.yue.ui.activitys.impl.circle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.yeucheng.yue.R;
import com.yeucheng.yue.base.AbsBaseActivity;
import com.yeucheng.yue.ui.adapter.AddPicAdapter;
import com.yeucheng.yue.ui.adapter.HFAdapter;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.inject.FindView;

/**
 * Created by Administrator on 2018/3/15.
 */

public class PhotoPickActivity extends AbsBaseActivity {
    @FindView(R.id.common_toolbar)
    private Toolbar mToolBar;
    @FindView(R.id.recycler)
    private RecyclerView mRecyclerView;
    //展示已添加的图片列表适配器
    private AddPicAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_photopick;
    }

    @Override
    protected void initViewsAndEvents() {
        //设置工具栏
        initTool();
        mAdapter = new AddPicAdapter();
        mGridLayoutManager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        HFAdapter hfAdapter = new HFAdapter(mAdapter);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_addpic_footer, null);
        //设置监听
        setListener(view);
        //增加脚部
        hfAdapter.addFooterView(view);
        mRecyclerView.setAdapter(hfAdapter);
    }

    /**
     * "+"点击事件
     *
     * @param view
     */
    private void setListener(View view) {
        ImageView imageView = view.findViewById(R.id.footer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出pop
                showPop();
            }
        });
    }

    /**
     * 选择图片或拍照的pop
     */
    private void showPop() {
        final String[] stringItems = {"图库", "相机"};
        final ActionSheetDialog dialog = new ActionSheetDialog(PhotoPickActivity.this, stringItems, null);
        dialog.isTitleShow(false)
                .cancelText(CommonUtils.getColorByAttrId(PhotoPickActivity.this, R.attr.colorPrimary))
                .itemTextColor(CommonUtils.getColorByAttrId(PhotoPickActivity.this, R.attr.colorPrimary))
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://图库

                        break;
                    case 1://相机

                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void initTool() {
        initToolBar(mToolBar, "选择图片");
        //返回按钮点击事件
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //菜单栏点击事件
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_send:
                        //todo 右上角发布按钮事件

                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
