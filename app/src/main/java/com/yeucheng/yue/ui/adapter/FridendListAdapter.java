package com.yeucheng.yue.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yeucheng.yue.R;
import com.yeucheng.yue.http.bean.FriendBean;
import com.yeucheng.yue.util.LogUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.ViewUtils;
import com.yeucheng.yue.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class FridendListAdapter extends RecyclerView.Adapter<FridendListAdapter.ViewHolder> implements SectionIndexer {
    private List<FriendBean> mData;

    public FridendListAdapter() {
        if (null == mData) {
            this.mData = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendlist,
                parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            String firstLetters = String.valueOf((mData.get(position)).getLetters()
                    .charAt(0));
            viewHolder.mTvLetter.setVisibility(View.VISIBLE);
            viewHolder.mTvLetter.setText(firstLetters);
        } else {
            viewHolder.mTvLetter.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty((mData.get(position)).getDisplayname())) {
            viewHolder.mTvTitle.setText((mData.get(position)).getNickname());
        } else {
            viewHolder.mTvTitle.setText((mData.get(position)).getDisplayname());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIonItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void updateListView(List<FriendBean> friendList) {
        if (null != this.mData) {
            this.mData.clear();
        }
        this.mData = friendList;
//        for (FriendBean f:mData
//             ) {
//            LogUtils.d("mData",f.toString());
//        }
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        LogUtils.d("yueselectfriend", getItemCount() + "");
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = (mData.get(i)).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return (mData.get(position)).getLetters().charAt(0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @FindView(R.id.catalog)
        private TextView mTvLetter;
        @FindView(R.id.friendname)
        private TextView mTvTitle;
        @FindView(R.id.frienduri)
        private CircleImageView mImageView;
        @FindView(R.id.friend_id)
        private TextView mTvUserID;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewUtils.inject(itemView, ViewHolder.this);
        }
    }

    public void addOnItemClickListener(IonItemClickListener ionItemClickListener) {
        this.mIonItemClickListener = ionItemClickListener;
    }

    private IonItemClickListener mIonItemClickListener;

    public interface IonItemClickListener {
        void onItemClick(View view, int position);
    }
}
