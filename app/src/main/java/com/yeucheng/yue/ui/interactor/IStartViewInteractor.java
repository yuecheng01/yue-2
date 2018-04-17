package com.yeucheng.yue.ui.interactor;

import com.yeucheng.yue.http.ICommonInteractorCallback;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/6.
 */

public interface IStartViewInteractor {
    void getIsRegisterOrNotBean(Map<String,String> regPhoneNum, ICommonInteractorCallback
            iCommonInteractorCallback);


    void confirMessageCode(Map<String, String> messageCode, ICommonInteractorCallback
            iCommonInteractorCallback);

    void submitRegisterInfo(Map<String, String> hashMap, ICommonInteractorCallback
            iCommonInteractorCallback);

    void getLoginRequestBean(Map<String, String> map, ICommonInteractorCallback
            iCommonInteractorCallback);

    void getResetPasswordBean(Map<String, String> map, ICommonInteractorCallback iCommonInteractorCallback);
}
