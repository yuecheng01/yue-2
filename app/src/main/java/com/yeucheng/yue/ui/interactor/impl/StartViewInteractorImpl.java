package com.yeucheng.yue.ui.interactor.impl;

import com.yeucheng.yue.http.ApiServicesManager;
import com.yeucheng.yue.http.ICommonInteractorCallback;
import com.yeucheng.yue.http.bean.IsRegisterOrNotBean;
import com.yeucheng.yue.http.bean.LoginBean;
import com.yeucheng.yue.http.bean.MessageCodeBean;
import com.yeucheng.yue.http.bean.RegisterBean;
import com.yeucheng.yue.http.bean.ResetPasswordBean;
import com.yeucheng.yue.ui.interactor.IStartViewInteractor;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/6.
 */

public class StartViewInteractorImpl implements IStartViewInteractor {
    /**
     * 判断手机号是否已注册
     *
     * @param regPhoneNum               注册的id(手机号码)
     * @param iCommonInteractorCallback 结果回调
     */
    @Override
    public void getIsRegisterOrNotBean(Map<String, String> regPhoneNum, final
    ICommonInteractorCallback
            iCommonInteractorCallback) {
        ApiServicesManager.getInstence().getYueapi()
                .getIsRegisterOrNotBean(regPhoneNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IsRegisterOrNotBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(IsRegisterOrNotBean isRegisterOrNotBean) {
                        iCommonInteractorCallback.loadSuccess(isRegisterOrNotBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonInteractorCallback.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 验证验证码
     *
     * @param messageCode
     * @param iCommonInteractorCallback
     */
    @Override
    public void confirMessageCode(Map<String, String> messageCode, final ICommonInteractorCallback iCommonInteractorCallback) {
        ApiServicesManager.getInstence().getYueapi()
                .getMessageCodeBean(messageCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageCodeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageCodeBean messageCodeBean) {
                        iCommonInteractorCallback.loadSuccess(messageCodeBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonInteractorCallback.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 注册
     *
     * @param hashMap
     * @param iCommonInteractorCallback
     */
    @Override
    public void submitRegisterInfo(Map<String, String> hashMap, final ICommonInteractorCallback iCommonInteractorCallback) {
        ApiServicesManager.getInstence().getYueapi()
                .getRegisterBean(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RegisterBean registerBean) {
                        iCommonInteractorCallback.loadSuccess(registerBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonInteractorCallback.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 登录
     *
     * @param map
     * @param iCommonInteractorCallback
     */
    @Override
    public void getLoginRequestBean(Map<String, String> map, final ICommonInteractorCallback iCommonInteractorCallback) {
        ApiServicesManager.getInstence().getYueapi()
                .getLoginBean(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        iCommonInteractorCallback.loadSuccess(loginBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonInteractorCallback.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设置新的密码
     *
     * @param map
     * @param iCommonInteractorCallback
     */
    @Override
    public void getResetPasswordBean(Map<String, String> map, final ICommonInteractorCallback iCommonInteractorCallback) {
        ApiServicesManager.getInstence().getYueapi()
                .getResetPasswordBean(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResetPasswordBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResetPasswordBean resetPasswordBean) {
                        iCommonInteractorCallback.loadSuccess(resetPasswordBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iCommonInteractorCallback.loadFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
