package com.yeucheng.yue.app;

import android.content.Context;

import com.yeucheng.yue.db.DBManager;
import com.yeucheng.yue.db.entity.Friend;
import com.yeucheng.yue.http.ApiServicesManager;
import com.yeucheng.yue.http.ICommonInteractorCallback;
import com.yeucheng.yue.http.bean.FriendBean;
import com.yeucheng.yue.http.bean.FriendsListBean;
import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.sp.SpSave;
import com.yeucheng.yue.util.AppUtils;
import com.yeucheng.yue.util.LogUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2018/3/7.
 */

public class UserInfoManager {
    private Context mContext;
    private static UserInfoManager mInstance;

    private UserInfoManager(Context context) {
        super();
        this.mContext = context;
    }

    public static UserInfoManager getInstance() {
        return mInstance;
    }

    public static void init(Context context) {
        mInstance = new UserInfoManager(context);
    }

    /**
     * 退出App
     */
    public void exitApp() {
        //退出融云
        RongIM.getInstance().logout();
        //清除本地token
        SharedPreferencesUtils.setParam(AppUtils.getAppContext(), SpSave
                .RONGIM_TOKEN, "");
        //是否记住密码
        String mIsRememberPwd = (String) SharedPreferencesUtils.getParam(AppUtils.getAppContext()
                , SpSave.REMEMBER_PWD, "");
        switch (mIsRememberPwd) {
            case "1":
                break;
            case "-1":
//               YUE_SharedPreferencesUtils.setParam(YUE_AppUtils.getAppContext(),
//                       YUE_SPsave.LOGING_PHONE,
//                       "");
                SharedPreferencesUtils.setParam(AppUtils.getAppContext(),
                        SpSave.LOGING_PASSWORD,
                        "");
                break;
        }
    }

    /**
     * 当新用户登录时,将清除本地好友列表数据库数据,再从服务端同步数据更新到本地好友列表数据库
     */
    public void upDateContacts() {
        //登录则清空好友列表数据库,从服务端获取,并更新到本地数据库,
        DBManager.getInstance().getSession().getFriendDao().deleteAll();
        ApiServicesManager.getInstence().getYueapi().getFriendsList(
                new HashMap<String, String>() {
                    {
                        put("userId", (String) SharedPreferencesUtils
                                .getParam(mContext, SpSave.LOGING_PHONE, ""));
                        put("statues", "1");
                    }
                })
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())//这里没有操作ui直接更新数据库,那就在io线程继续操作数据库,不转ui线程
                .subscribe(new Observer<FriendsListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FriendsListBean friendsListBean) {
                        List<FriendBean> list = friendsListBean.getValue();
                        //存数据库
                        insertIntoSql(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void insertIntoSql(List<FriendBean> list) {
        DBManager.getInstance().getSession().getFriendDao().deleteAll();
        for (FriendBean friendBean : list
                ) {
            Friend friend = new Friend(null, friendBean.getNickname(), null,
                    null, null, null,
                    null, null, friendBean.getPhonenum());
            DBManager.getInstance().getSession().getFriendDao().insert(friend);
        }

        LogUtils.d("insert--YUE_FriendsBean", list.size() + "insert Success!");
        List<Friend> l = DBManager.getInstance().getSession().getFriendDao().loadAll();
        for (Friend friend : l
                ) {
            LogUtils.d("结果:-->", friend.getNickName() + "," + friend.getPhoneNumber());
        }
    }

    /**
     * 从本地数据库读取好友列表,异步操作
     *
     * @return 好友列表信息集合
     */

    public void getFriendList(final ICommonInteractorCallback l) {
        Observable.create(new ObservableOnSubscribe<List<Friend>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Friend>> e) throws Exception {
                List<Friend> list = DBManager.getInstance().getSession().getFriendDao().loadAll();
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Friend>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        l.addDisaposed(d);
                    }

                    @Override
                    public void onNext(List<Friend> friends) {
                        l.loadSuccess(friends);
                    }

                    @Override
                    public void onError(Throwable e) {
                        l.loadFailed();
                    }

                    @Override
                    public void onComplete() {
                        l.loadCompleted();
                    }
                });
    }
}
