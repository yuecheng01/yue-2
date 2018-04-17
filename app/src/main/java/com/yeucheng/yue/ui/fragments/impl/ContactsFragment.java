package com.yeucheng.yue.ui.fragments.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yeucheng.yue.R;
import com.yeucheng.yue.base.BaseLazyFragment;
import com.yeucheng.yue.http.bean.FriendBean;
import com.yeucheng.yue.ui.activitys.impl.UserDetailActivity;
import com.yeucheng.yue.ui.adapter.FridendListAdapter;
import com.yeucheng.yue.ui.adapter.HFAdapter;
import com.yeucheng.yue.ui.fragments.IContactsFragmentView;
import com.yeucheng.yue.ui.presenter.IContactsFragmentPresenter;
import com.yeucheng.yue.ui.presenter.Impl.ContactsFragmentPresenterImpl;
import com.yeucheng.yue.util.LogUtils;
import com.yeucheng.yue.util.PinyinComparator;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.OnClick;
import com.yeucheng.yue.util.inject.ViewUtils;
import com.yeucheng.yue.widget.SideBar;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

public class ContactsFragment extends BaseLazyFragment implements IContactsFragmentView {
    private String TAG = this.getClass().getSimpleName();
    private IContactsFragmentPresenter mPresenter;
    @FindView(R.id.friendlistview)
    private RecyclerView mContactsListView;
    @FindView(R.id.show_no_friend)
    private TextView mNoFriends;
    @FindView(R.id.sidrbar)
    private SideBar mSidBar;
    //存放好友列表
    private List<FriendBean> mFriendList;
    //排序后的好友列表
    private List<FriendBean> mFilteredFriendList;
    //联系人列表
    private FridendListAdapter mAdapter;
    private PinyinComparator mPinyinComparator;
    //中部展示的字母提示
    @FindView(R.id.group_dialog)
    private TextView mDialogTextView;

    private LinearLayoutManager mLinearLayoutManager;
    //HeaderView中事件处理类
    private HeadViewControl mHeadViewControl;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_contacts_layout);
        ViewUtils.inject(getContentView(), ContactsFragment.this);
        init();
    }

    private void init() {
        mPresenter = new ContactsFragmentPresenterImpl(mActivity, this);
        mPinyinComparator = PinyinComparator.getInstance();
        if (null == mHeadViewControl) {
            mHeadViewControl = new HeadViewControl();
        }
       /* synchronized (ContactsFragment.class) {
            if (null == mFriendList | null == mFilteredFriendList) {
                mFriendList = new ArrayList<>();
                mFilteredFriendList = new ArrayList<>();
            }
        }*/
        mAdapter = new FridendListAdapter();
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mContactsListView.setLayoutManager(mLinearLayoutManager);
        HFAdapter hfAdapter = new HFAdapter(mAdapter);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_contact_list_header,
                null);
        //处理HeadView中的事件
        ViewUtils.inject(view, mHeadViewControl);
        hfAdapter.addHeaderView(view);
        mContactsListView.setAdapter(hfAdapter);
        //加载联系人列表
        mPresenter.loadContactsList();
        //列表item点击事件
        mAdapter.addOnItemClickListener(new FridendListAdapter.IonItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtils.d(TAG, position);
                Intent intent = new Intent(mActivity,UserDetailActivity.class);
                intent.putExtra("userId", mFriendList.get(position).getPhonenum());
                intent.putExtra("userNickName", mFriendList.get(position).getNickname());
                startActivity(intent);
            }
        });
        mSidBar.setTextView(mDialogTextView);
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLinearLayoutManager.scrollToPositionWithOffset(position + 1, 0);
                }
            }
        });
    }

    /**
     * HeadView事件处理类
     */
    class HeadViewControl {
        @FindView(R.id.re_newfriends)
        private RelativeLayout mNewFriend;
        @FindView(R.id.re_chatroom)
        private RelativeLayout mGroup;
        @FindView(R.id.publicservice)
        private RelativeLayout mPublicService;
        @FindView(R.id.contact_me_item)
        private RelativeLayout mAboutUser;


        @OnClick({R.id.re_newfriends, R.id.re_chatroom, R.id.publicservice, R.id.contact_me_item})
        private void ItemOnClick(View view) {
            switch (view.getId()) {
                case R.id.re_newfriends:
                    LogUtils.d(TAG, "re_newfriends");
                    break;
                case R.id.re_chatroom:

                    LogUtils.d(TAG, "re_chatroom");
                    break;
                case R.id.publicservice:
                    LogUtils.d(TAG, "publicservice");

                    break;
                case R.id.contact_me_item:
                    LogUtils.d(TAG, "contact_me_item");

                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }

    @Override
    public void loadFriendsList(List<FriendBean> list) {

        if (list.size() > 0) {
            this.mFriendList = list;
            mNoFriends.setVisibility(View.GONE);
            mPresenter.setContactsLitters(mFriendList);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);
        mSidBar.setVisibility(View.VISIBLE);
        mAdapter.updateListView(mFriendList);
    }

}
