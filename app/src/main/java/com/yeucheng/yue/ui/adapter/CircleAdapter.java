package com.yeucheng.yue.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yeucheng.yue.R;
import com.yeucheng.yue.http.bean.ActionItem;
import com.yeucheng.yue.http.bean.CircleItem;
import com.yeucheng.yue.http.bean.CommentConfig;
import com.yeucheng.yue.http.bean.CommentItem;
import com.yeucheng.yue.http.bean.FavortItem;
import com.yeucheng.yue.http.bean.PhotoInfo;
import com.yeucheng.yue.ui.dialog.CommentDialog;
import com.yeucheng.yue.util.DatasUtil;
import com.yeucheng.yue.util.UrlUtils;
import com.yeucheng.yue.widget.CommentListView;
import com.yeucheng.yue.widget.ExpandTextView;
import com.yeucheng.yue.widget.MultiImageView;
import com.yeucheng.yue.widget.PraiseListView;
import com.yeucheng.yue.widget.SnsPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class CircleAdapter extends RecyclerView.Adapter {
    private Context mContext;
    protected List<?> mDatas = new ArrayList<>();
    private CircleItemClickListenerCallBack mCallBack;

    public CircleAdapter(Context context) {
        this.mContext = context;
    }

    public void addItemClickListener(CircleItemClickListenerCallBack callBack) {
        this.mCallBack = callBack;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);
        if (viewType == CircleViewHolder.TYPE_URL) {
            viewHolder = new URLViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
            viewHolder = new ImageViewHolder(view);
        } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
            viewHolder = new VideoViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final CircleItem circleItem = (CircleItem) mDatas.get(position);
        final String circleId = circleItem.getId();
        String name = circleItem.getUser().getName();
        String headImg = circleItem.getUser().getHeadUrl();
        final String content = circleItem.getContent();
        String createTime = circleItem.getCreateTime();
        final List<FavortItem> favortDatas = circleItem.getFavorters();
        final List<CommentItem> commentsDatas = circleItem.getComments();
        boolean hasFavort = circleItem.hasFavort();
        boolean hasComment = circleItem.hasComment();
        //加载头像
        Glide.with(mContext).load(headImg).apply(new RequestOptions().diskCacheStrategy
                (DiskCacheStrategy.ALL)).into(holder.headIv);
        //设置名字
        holder.nameTv.setText(name);
        //设置标题
        holder.timeTv.setText(createTime);
        //设置文本内容
        if (!TextUtils.isEmpty(content)) {
            holder.contentTv.setExpand(circleItem.isExpand());
            holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    circleItem.setExpand(isExpand);
                }
            });

            holder.contentTv.setText(UrlUtils.formatUrlString(content));
        }
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        //判断如果是自己发的,就显示可删除按钮,否则不显示 todo
        if (DatasUtil.curUser.getId().equals(circleItem.getUser().getId())) {
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
        //设置删除按钮点击事件
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                mCallBack.deleteCircle(circleId);
            }
        });
        //如果有点赞和评论的处理
        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
                holder.praiseListView.setOnItemClickListener(new PraiseListView
                        .OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        String userName = favortDatas.get(position).getUser().getName();
                        String userId = favortDatas.get(position).getUser().getId();
                        mCallBack.clickName(userName, userId);
                    }
                });
                holder.praiseListView.setDatas(favortDatas);
                holder.praiseListView.setVisibility(View.VISIBLE);
            } else {
                holder.praiseListView.setVisibility(View.GONE);
            }

            if (hasComment) {//处理评论列表
                holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
                        CommentItem commentItem = commentsDatas.get(commentPosition);
                        if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId())) {//复制或者删除自己的评论

                            CommentDialog dialog = new CommentDialog(mContext, mCallBack,
                                    commentItem,
                                    position);
                            dialog.show();
                        } else {//回复别人的评论
                            if (mCallBack != null) {
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = position;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                config.replyUser = commentItem.getUser();
                                mCallBack.showEditTextBody(config);
                            }
                        }
                    }

                    @Override
                    public void onClickCommentName(String name, String id) {
                        mCallBack.onClickCommentName(name, id);
                    }
                });
                holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(int commentPosition) {
                        //长按进行复制或者删除
                        CommentItem commentItem = commentsDatas.get(commentPosition);
                        CommentDialog dialog = new CommentDialog(mContext, mCallBack, commentItem,
                                position);
                        dialog.show();
                    }
                });
                holder.commentList.setDatas(commentsDatas);
                holder.commentList.setVisibility(View.VISIBLE);

            } else {
                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }
        //间隔线
        holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        //判断是否已点赞
        String curUserFavortId = circleItem.getCurUserFavortId(DatasUtil.curUser.getId());
        if (!TextUtils.isEmpty(curUserFavortId)) {
            snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
        } else {
            snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        }
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(position, circleItem,
                curUserFavortId));
        //评论按钮
        holder.snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });
        holder.urlTipTv.setVisibility(View.GONE);
        //分类处理链接,图片,视频等资源.
        switch (holder.viewType) {
            case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                if (holder instanceof URLViewHolder) {
                    String linkImg = circleItem.getLinkImg();
                    String linkTitle = circleItem.getLinkTitle();
                    Glide.with(mContext).load(linkImg).into(((URLViewHolder) holder).urlImageIv);
                    ((URLViewHolder) holder).urlContentTv.setText(linkTitle);
                    ((URLViewHolder) holder).urlBody.setVisibility(View.VISIBLE);
                    ((URLViewHolder) holder).urlTipTv.setVisibility(View.VISIBLE);
                }
                break;
            case CircleViewHolder.TYPE_IMAGE:// 处理图片
                if (holder instanceof ImageViewHolder) {
                    final List<PhotoInfo> photos = circleItem.getPhotos();
                    if (photos != null && photos.size() > 0) {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                        ((ImageViewHolder) holder).multiImageView.setList(photos);
                        ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                mCallBack.preViewPic(view, position, photos);
                            }
                        });
                    } else {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                    }
                }

                break;
            case CircleViewHolder.TYPE_VIDEO://处理视频资源
//                    if (holder instanceof VideoViewHolder) {
//                        ((VideoViewHolder) holder).videoView.setVideoUrl(circleItem.getVideoUrl());
//                        ((VideoViewHolder) holder).videoView.setVideoImgUrl(circleItem.getVideoImgUrl());//视频封面图片
//                        ((VideoViewHolder) holder).videoView.setPostion(position);
//                        ((VideoViewHolder) holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
//                            @Override
//                            public void onPlayClick(int pos) {
//                                curPlayIndex = pos;
//                            }
//                        });
//                    }

                break;
            default:
                break;
        }
    }

    public void updateData(List<CircleItem> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    /**
     * 弹出的点赞,评论pop事件监听
     */
    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private CircleItem mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (mCallBack != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            mCallBack.addFavort(mCirclePosition);
                        } else {//取消点赞
                            mCallBack.deleteFavort(mCirclePosition, mFavorId);
                        }
                    }
                    break;
                case 1://发布评论
                    if (mCallBack != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        mCallBack.showEditTextBody(config);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
        CircleItem item = (CircleItem) mDatas.get(position);
        if (CircleItem.TYPE_URL.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_URL;
        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (CircleItem.TYPE_VIDEO.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_VIDEO;
        }
        return itemType;
    }
}
