package com.yeucheng.yue.http.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by yuecheng on 2017/11/11.
 */

public class FriendBean implements Serializable {
    @Override
    public String toString() {
        return "FriendBean{" +
                "nickname='" + nickname + '\'' +
                ", displayname='" + displayname + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", letters='" + letters + '\'' +
                ", displayNameSpelling='" + displayNameSpelling + '\'' +
                ", nickNameSpelling='" + nickNameSpelling + '\'' +
                '}';
    }

    private String nickname;
    private String displayname;
    private String phonenum;
    private String letters;
    private String displayNameSpelling;
    private String nickNameSpelling;

    public String getDisplayNameSpelling() {
        return displayNameSpelling;
    }

    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getNickNameSpelling() {
        return nickNameSpelling;
    }

    public void setNickNameSpelling(String nickNameSpelling) {
        this.nickNameSpelling = nickNameSpelling;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public boolean isExitsDisplayName() {
        return !TextUtils.isEmpty(displayname);
    }
}
