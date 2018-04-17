package com.yeucheng.yue.http;


import com.yeucheng.yue.http.bean.FriendsListBean;
import com.yeucheng.yue.http.bean.IsRegisterOrNotBean;
import com.yeucheng.yue.http.bean.LoginBean;
import com.yeucheng.yue.http.bean.MessageCodeBean;
import com.yeucheng.yue.http.bean.RegisterBean;
import com.yeucheng.yue.http.bean.ResetPasswordBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yuecheng on 2017/11/5.
 */

public interface YUE_API {

    @FormUrlEncoded
    @POST("yue/imapi.php/isRegisterOrNot")
    Observable<IsRegisterOrNotBean> getIsRegisterOrNotBean(
            @FieldMap Map<String, String> map
    );

    /**
     * 短信验证接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("yue/imapi.php/getMessageCode")
    Observable<MessageCodeBean> getMessageCodeBean(
            @FieldMap Map<String, String> map
    );

    /**
     * 注册接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("yue/imapi.php/register")
    Observable<RegisterBean> getRegisterBean(
            @FieldMap Map<String, String> map
    );

    /**
     * 登录
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("yue/imapi.php/login")
    Observable<LoginBean> getLoginBean(
            @FieldMap Map<String, String> map
    );

    /**
     * 重置密码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("yue/imapi.php/resetPassword")
    Observable<ResetPasswordBean> getResetPasswordBean(
            @FieldMap Map<String, String> map
    );

    /**
     * 获取联系人列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("yue/imapi.php/getFriendsList")
    Observable<FriendsListBean> getFriendsList(
            @FieldMap Map<String, String> map
    );
}
