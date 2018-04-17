package com.yeucheng.yue.http.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class CircleItem {
    public final static String TYPE_URL = "1";
    public final static String TYPE_IMG = "2";
    public final static String TYPE_VIDEO = "3";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "CircleItem{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", type='" + type + '\'' +
                ", linkImg='" + linkImg + '\'' +
                ", linkTitle='" + linkTitle + '\'' +
                ", photos=" + photos +
                ", favorters=" + favorters +
                ", comments=" + comments +
                ", user=" + user +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoImgUrl='" + videoImgUrl + '\'' +
                ", isExpand=" + isExpand +
                '}';
    }

    private String id;
    private String content;
    private String createTime;
    private String type;//1:链接  2:图片 3:视频
    private String linkImg;
    private String linkTitle;
    private List<PhotoInfo> photos;
    private List<FavortItem> favorters;
    private List<CommentItem> comments;
    private User user;
    private String videoUrl;
    private String videoImgUrl;

    private boolean isExpand;

    public static String getTypeUrl() {
        return TYPE_URL;
    }

    public static String getTypeImg() {
        return TYPE_IMG;
    }

    public static String getTypeVideo() {
        return TYPE_VIDEO;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public List<PhotoInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoInfo> photos) {
        this.photos = photos;
    }

    public List<FavortItem> getFavorters() {
        return favorters;
    }

    public void setFavorters(List<FavortItem> favorters) {
        this.favorters = favorters;
    }

    public List<CommentItem> getComments() {
        return comments;
    }

    public void setComments(List<CommentItem> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImgUrl() {
        return videoImgUrl;
    }

    public void setVideoImgUrl(String videoImgUrl) {
        this.videoImgUrl = videoImgUrl;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
    public boolean hasFavort(){
        if(favorters!=null && favorters.size()>0){
            return true;
        }
        return false;
    }

    public boolean hasComment(){
        if(comments!=null && comments.size()>0){
            return true;
        }
        return false;
    }

    public String getCurUserFavortId(String curUserId){
        String favortid = "";
        if(!TextUtils.isEmpty(curUserId) && hasFavort()){
            for(FavortItem item : favorters){
                if(curUserId.equals(item.getUser().getId())){
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
