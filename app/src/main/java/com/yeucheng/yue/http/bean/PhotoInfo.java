package com.yeucheng.yue.http.bean;

/**
 * Created by Administrator on 2018/1/11.
 */

public class PhotoInfo {
    public String url;
    public int w;
    public int h;

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "url='" + url + '\'' +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}
