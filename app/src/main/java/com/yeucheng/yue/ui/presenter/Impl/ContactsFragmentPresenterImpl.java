package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;
import android.text.TextUtils;

import com.yeucheng.yue.app.UserInfoManager;
import com.yeucheng.yue.db.entity.Friend;
import com.yeucheng.yue.http.ICommonInteractorCallback;
import com.yeucheng.yue.http.bean.FriendBean;
import com.yeucheng.yue.ui.fragments.IContactsFragmentView;
import com.yeucheng.yue.ui.interactor.IContactsViewInteractor;
import com.yeucheng.yue.ui.interactor.impl.ContactsViewInteractorImpl;
import com.yeucheng.yue.ui.presenter.IContactsFragmentPresenter;
import com.yeucheng.yue.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;

/**
 * Created by Administrator on 2018/3/13.
 */

public class ContactsFragmentPresenterImpl implements IContactsFragmentPresenter {
    private Context mContext;
    private IContactsFragmentView mView;
    private IContactsViewInteractor mInteractor;
    private List<Friend> friendList;
    private String TAG = this.getClass().getSimpleName();

    public ContactsFragmentPresenterImpl(Context context, IContactsFragmentView iContactsFragmentView) {
        super();
        this.mContext = context;
        this.mView = iContactsFragmentView;
        this.mInteractor = new ContactsViewInteractorImpl();
    }

    @Override
    public void loadContactsList() {
        //从本地sqlite数据库读取联系人更新列表
        UserInfoManager.getInstance().getFriendList(new ICommonInteractorCallback() {
            @Override
            public void loadSuccess(Object object) {
                friendList = (List<Friend>) object;
                if (null != friendList) {
                    List<FriendBean> list = new ArrayList<>();
                    for (Friend friend : friendList
                            ) {
                        FriendBean friendBean = new FriendBean();
                        friendBean.setNickname(friend.getNickName());
                        friendBean.setPhonenum(friend.getPhoneNumber());
                        list.add(friendBean);
                    }
                    //更新列表
                    updateContacts(list);
                } else {

                }
            }

            @Override
            public void loadFailed() {

            }

            @Override
            public void loadCompleted() {

            }

            @Override
            public void addDisaposed(Disposable disposable) {

            }
        });
    }

    @Override
    public void setContactsLitters(List<FriendBean> list) {
        for (FriendBean friend : list) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNickNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            return spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
        } else {
            return "#";
        }
    }

    private void updateContacts(List<FriendBean> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNickNameSpelling(PinyinHelper.convertToPinyinString(list.get(i)
                    .getNickname(), "", PinyinFormat.WITHOUT_TONE));
//            list.get(i).setNickNameSpelling(PinyinHelper.convertToPinyinString(list.get(i)
//                    .getDisplayname(), null));
            LogUtils.d(TAG, list.get(i).getNickNameSpelling());
        }
        mView.loadFriendsList(list);
    }
}
