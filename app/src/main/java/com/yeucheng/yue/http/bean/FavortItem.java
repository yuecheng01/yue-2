package com.yeucheng.yue.http.bean;

/**
 * Created by Administrator on 2018/1/11.
 */

public class FavortItem {
    private String id;
    private User user;
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

    @Override
    public String toString() {
        return "FavortItem{" +
                "id='" + id + '\'' +
                ", user=" + user +
                '}';
    }
}
