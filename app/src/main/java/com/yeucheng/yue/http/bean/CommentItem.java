package com.yeucheng.yue.http.bean;

/**
 * Created by Administrator on 2018/1/11.
 */

public class CommentItem {
    @Override
    public String toString() {
        return "CommentItem{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", toReplyUser=" + toReplyUser +
                ", content='" + content + '\'' +
                '}';
    }

    private String id;
    private User user;
    private User toReplyUser;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getToReplyUser() {
        return toReplyUser;
    }

    public void setToReplyUser(User toReplyUser) {
        this.toReplyUser = toReplyUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
