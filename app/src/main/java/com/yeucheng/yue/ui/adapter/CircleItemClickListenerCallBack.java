package com.yeucheng.yue.ui.adapter;

import android.view.View;

import com.yeucheng.yue.http.bean.CommentConfig;
import com.yeucheng.yue.http.bean.PhotoInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public interface CircleItemClickListenerCallBack {
    void deleteCircle(String circleId);

    void deleteComment(int circlePosition, String commentItemId);

    void showEditTextBody(CommentConfig config);

    void addFavort(int circlePosition);

    void deleteFavort(int circlePosition, String favorId);

    void preViewPic(View view, int position, List<PhotoInfo> photos);

    void clickName(String userName, String userId);

    void onClickCommentName(String name, String id);
}
