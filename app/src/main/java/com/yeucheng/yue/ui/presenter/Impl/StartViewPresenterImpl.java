package com.yeucheng.yue.ui.presenter.Impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.yeucheng.yue.R;
import com.yeucheng.yue.app.UserInfoManager;
import com.yeucheng.yue.http.ICommonInteractorCallback;
import com.yeucheng.yue.http.bean.IsRegisterOrNotBean;
import com.yeucheng.yue.http.bean.LoginBean;
import com.yeucheng.yue.http.bean.MessageCodeBean;
import com.yeucheng.yue.http.bean.RegisterBean;
import com.yeucheng.yue.http.bean.ResetPasswordBean;
import com.yeucheng.yue.sp.SharedPreferencesUtils;
import com.yeucheng.yue.sp.SpSave;
import com.yeucheng.yue.ui.activitys.IStartView;
import com.yeucheng.yue.ui.activitys.impl.HomeActivity;
import com.yeucheng.yue.ui.activitys.impl.RegisterProtocolActivity;
import com.yeucheng.yue.ui.interactor.IStartViewInteractor;
import com.yeucheng.yue.ui.interactor.impl.StartViewInteractorImpl;
import com.yeucheng.yue.ui.presenter.IStartViewPresenter;
import com.yeucheng.yue.util.AppUtils;
import com.yeucheng.yue.util.CodeUtils;
import com.yeucheng.yue.util.CommonUtils;
import com.yeucheng.yue.util.InputFilterEx;
import com.yeucheng.yue.util.LogUtils;
import com.yeucheng.yue.util.SelectorUtils;
import com.yeucheng.yue.util.ToastUtils;
import com.yeucheng.yue.util.anim.ViewAnimationUtils;
import com.yeucheng.yue.util.inject.FindView;
import com.yeucheng.yue.util.inject.Init;
import com.yeucheng.yue.util.inject.OnClick;
import com.yeucheng.yue.util.inject.ViewUtils;
import com.yeucheng.yue.widget.CustomPopWindow;
import com.yeucheng.yue.widget.PicCodeConfirDialog;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2018/3/5.
 */

public class StartViewPresenterImpl implements IStartViewPresenter {
    private Context mContext;
    private IStartView mView;
    private IStartViewInteractor mInteractor;
    /**
     * pop消失的回调
     */
    private PopupWindow.OnDismissListener mOnTouchListener;
    private boolean mIsAppear = false;
    /**
     * 点击注册弹出的注册界面pop
     */
    private CustomPopWindow mRegisterPop;
    /**
     * 获取验证码界面pop
     */
    private CustomPopWindow mGetCodePop;
    /**
     * 设置密码界面pop
     */
    private CustomPopWindow mSetPwdPop;
    /**
     * 登录界面pop
     */
    private CustomPopWindow mLoginPop;
    /**
     * 忘记密码界面pop
     */
    private CustomPopWindow mForgetPasswordPop;
    /**
     * 设置新密码界面pop
     */
    private CustomPopWindow mNewPasswordPop;
    private String mRegPhoneNum;//注册的手机号
    private String mMessageCode;//注册发送的验证码
    private String mRegNickname;//注册的网名(昵称)
    private String mRegPassword;//注册设置的密码
    private String mRegPasswordConfir;//注册确认密码
    private String mResetPwdUserId;//重置密码的用户电话;
    private SendCountMessage mSendCountMessage;//发送验证码后处理倒计时对象
    private StringBuilder mStringBuilder = null;
    //注册pop界面事件处理类
    private Register mRegisterPopEvent;
    //获取验证码pop界面事件处理类
    private GetCode mGetCodePopEvent;
    //注册设置密码pop界面事件处理类
    private RegSetPassWord mRegSetPassWordPopEvent;
    //登录界面pop事件处理类
    private LoginPopEvent mLoginPopEvent;
    //忘记密码界面pop事件处理类
    private ForgetPasswordPopEvent mForgetPasswordPopEvent;
    //设置新密码界面pop事件处理类
    private SetNewPasswordPop mSetNewPasswordPopEvent;

    public StartViewPresenterImpl(Context context, IStartView iStartView) {
        super();
        this.mContext = context;
        this.mView = iStartView;
        mOnTouchListener = new MyTouchListener();
        mInteractor = new StartViewInteractorImpl();
        if (mRegisterPopEvent == null) {
            mRegisterPopEvent = new Register();
        }
        if (mGetCodePopEvent == null) {
            mGetCodePopEvent = new GetCode();
        }
        if (mRegSetPassWordPopEvent == null) {
            mRegSetPassWordPopEvent = new RegSetPassWord();
        }
        if (mLoginPopEvent == null) {
            mLoginPopEvent = new LoginPopEvent();
        }
        if (mForgetPasswordPopEvent == null) {
            mForgetPasswordPopEvent = new ForgetPasswordPopEvent();
        }
        if (mSetNewPasswordPopEvent == null) {
            mSetNewPasswordPopEvent = new SetNewPasswordPop();
        }
    }

    class SendCountMessage extends CountDownTimer {
        private TextView mBtnCode;

        public SendCountMessage(TextView view) {
            super(60000, 1000);
            this.mBtnCode = view;
        }

        @Override
        public void onTick(long l) {
            mBtnCode.setText(l / 1000 + "/SECONDS");
            mBtnCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            mBtnCode.setText(mContext.getResources().getString(R.string.message_code_isconfir));
            mBtnCode.setClickable(true);
        }
    }
/***************************************************TODO register**********************************************************
 *
 */
    /**
     * 注册按钮点击事件
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void register() {
        mIsAppear = false;
        //点击则登录,注册按钮消失,弹出新的界面
        mView.btnRandLGone();
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_register, null);
        ViewUtils.inject(mContentView, mRegisterPopEvent);
        mRegisterPop = new CustomPopWindow.PopWindowBuilder(mContext)
                .setView(mContentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setAnimationStyle(R.style.PopupAnimation)
                .setFocusable(true)
                .setTouchable(true)
                .setBackGround(new ColorDrawable(0))
                .setOutsideTouchable(true)
                .setOnDissmissListener(mOnTouchListener)
                .create()
                .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
    }

    /**
     * 注册pop界面事件处理类
     */
    class Register {
        @FindView(R.id.userid)
        private TextInputEditText mUserID;
        @FindView(R.id.register_protocol)
        private TextView mRegisterProtocol;
        @FindView(R.id.nextstep)
        private Button mNextStep;//下一步
        @FindView(R.id.back)
        private LinearLayout mBack;//返回

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        public void initEvent() {
            /**
             * 返回按钮设置点击背景selector
             */
            mBack.setBackground(SelectorUtils.getCommenButtonBg(CommonUtils
                    .getColorByAttrId(mContext, R.attr.colorPrimary), mContext.getResources()
                    .getColor(R.color.act)));
            /**
             * 下一步按钮设置点击背景selector
             */
            mNextStep.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color.act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
            //注册 协议
            initRegisterProtocol();
        }

        @OnClick({R.id.back, R.id.nextstep})
        private void registerNextStepClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    mRegisterPop.dissmiss();
                    break;
                case R.id.nextstep:
                    mRegPhoneNum = mUserID.getText().toString().trim();
                    if (!CommonUtils.isMobileNO(mRegPhoneNum)) {
                        ToastUtils.showmessage("请输入正确的手机号码!");
                        return;
                    }
                    mInteractor.getIsRegisterOrNotBean(new HashMap<String, String>() {
                        {
                            put("userId", mRegPhoneNum);
                        }
                    }, new
                                                               ICommonInteractorCallback() {
                                                                   @Override
                                                                   public void loadSuccess(Object object) {
                                                                       IsRegisterOrNotBean isRegisterOrNotBean = (IsRegisterOrNotBean) object;
                                                                       switch (isRegisterOrNotBean.getResulecode()) {
                                                                           case 0:
                                                                               mIsAppear = true;
                                                                               mRegisterPop.dissmiss();
                                                                               View mContentView = LayoutInflater.from(mContext).inflate(R
                                                                                               .layout.pop_reg_getcode,
                                                                                       null);
                                                                               ViewUtils.inject(mContentView, mGetCodePopEvent);
                                                                               mGetCodePop = new CustomPopWindow.PopWindowBuilder(mContext)
                                                                                       .setView(mContentView)
                                                                                       .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                                                                                       .setAnimationStyle(R.style.PopupAnimation)
                                                                                       .setFocusable(true)
                                                                                       .setTouchable(true)
                                                                                       .setBackGround(new ColorDrawable(0))
                                                                                       .setOutsideTouchable(true)
                                                                                       .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                                                                                           @Override
                                                                                           public void onDismiss() {
                                                                                               if (mSendCountMessage != null)
                                                                                                   mSendCountMessage.cancel();
                                                                                               mRegisterPop.showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                                                                                           }
                                                                                       })
                                                                                       .create()
                                                                                       .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);


                                                                               break;
                                                                           case 1:
                                                                               ToastUtils.showmessage("该账号已存在,如遗忘密码,请返回登录页面点击\"忘记密码\"找回密码.");
                                                                               break;
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
                    break;
            }
        }

        /**
         * 注册 协议
         */
        private void initRegisterProtocol() {
            SpannableString spannableString = new SpannableString("点击下一步即表示同意《YUE服务协议》");
            spannableString.setSpan(new ClickableSpan() {
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary));      //设置颜色
                    ds.setUnderlineText(true);      //设置下划线
                }

                @Override
                public void onClick(View view) {
                    mView.jumpToActivity(RegisterProtocolActivity.class);
                }
            }, 10, spannableString.length(), Spanned
                    .SPAN_INCLUSIVE_EXCLUSIVE);
            mRegisterProtocol.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
            mRegisterProtocol.setText(spannableString);
            mRegisterProtocol.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        }
    }

    /**
     * 获取验证码的pop界面事件处理类
     */
    class GetCode {
        @FindView(R.id.get_code)
        private TextView mBtnGetCode;//获取验证码按钮
        @FindView(R.id.et_code)
        private TextInputEditText mCode;//填写短信验证码的输入框
        @FindView(R.id.bt_next)
        private Button mNextStep;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        private void initEvent() {
            /**
             * 下一步按钮设置点击背景selector
             */
            mNextStep.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color
                    .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
        }

        /**
         * 获取验证码按钮
         */
        @OnClick({R.id.back, R.id.get_code, R.id.bt_next})
        private void getCodeClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    mGetCodePop.dissmiss();
                    break;
                case R.id.get_code:
                    //如果按钮显示请重新输入,则是第一次输入的短信验证码有误,此时不在再次验证图形验证码,可直接再次获取短信验证码
                    if (mBtnGetCode.getText().toString().equals(mContext.getResources().getString(R
                            .string.message_code_isconfir))) {
                        reset_mStringBuilder();
                        sendConfirCode(mCode.getText().toString(), mBtnGetCode);
                    } else {
                        showPicCodeConfir(mCode, mBtnGetCode);
                    }
                    break;
                case R.id.bt_next:
                    mMessageCode = mCode.getText().toString();
                    if (mMessageCode.length() == 4) {
                        //TODO 请求server验证
                        mInteractor.confirMessageCode(new HashMap<String, String>() {
                            {
                                put("messagecode", mMessageCode);
                            }
                        }, new
                                                              ICommonInteractorCallback() {
                                                                  @Override
                                                                  public void loadSuccess(Object object) {
                                                                      MessageCodeBean messageCodeBean = (MessageCodeBean) object;
                                                                      switch (messageCodeBean.getResulecode()) {
                                                                          case 0:
                                                                              ToastUtils.showmessage("验证码有误,请重新输入");
                                                                              break;
                                                                          case 1:
                                                                              mGetCodePop.dissmiss();
                                                                              mRegisterPop.dissmiss();
                                                                              View mContentView = LayoutInflater.from(mContext)
                                                                                      .inflate(R.layout.pop_reg_setpassword,
                                                                                              null);
                                                                              ViewUtils.inject(mContentView, mRegSetPassWordPopEvent);
                                                                              mSetPwdPop = new CustomPopWindow.PopWindowBuilder(mContext)
                                                                                      .setView(mContentView)
                                                                                      .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                                                                                      .setAnimationStyle(R.style.PopupAnimation)
                                                                                      .setFocusable(true)
                                                                                      .setTouchable(true)
                                                                                      .setBackGround(new ColorDrawable(0))
                                                                                      .setOutsideTouchable(true)
                                                                                      .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                                                                                          @Override
                                                                                          public void onDismiss() {
                                                                                              mRegisterPop.showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                                                                                          }
                                                                                      })
                                                                                      .create()
                                                                                      .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                                                                              break;
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
                        mIsAppear = true;
                    } else {
                        ToastUtils.showmessage("请正确填写您收到的4位数验证码");
                        return;
                    }
                    break;
            }
        }
    }

    /**
     * 设置密码pop界面事件处理类
     */
    class RegSetPassWord {
        @FindView(R.id.usernickname)
        private TextInputEditText mNickName;
        @FindView(R.id.userpwd)
        private TextInputEditText mPwd;
        @FindView(R.id.passwordcofirm)
        private TextInputEditText mPwdCofirm;
        @FindView(R.id.confirm)
        private Button mConfirm;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        private void initEvent() {
            mConfirm.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color
                    .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
        }

        @OnClick({R.id.back, R.id.confirm})
        private void setPwdClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    mSetPwdPop.dissmiss();
                    break;
                case R.id.confirm:
                    mIsAppear = false;
                    mRegNickname = mNickName.getText().toString();
                    mRegPassword = mPwd.getText().toString();
                    mRegPasswordConfir = mPwdCofirm.getText().toString();
                    if (!mRegPassword.equals("") && !mRegPasswordConfir.equals("") && mRegPassword.equals
                            (mRegPasswordConfir)) {
                        mInteractor.submitRegisterInfo(new HashMap<String, String>() {
                            {
                                put("userId", mRegPhoneNum);
                                put("nickname", mRegNickname);
                                put("password", mRegPassword);
                                put("passwordfir", mRegPasswordConfir);
                            }
                        }, new
                                                               ICommonInteractorCallback() {
                                                                   @Override
                                                                   public void loadSuccess(Object object) {
                                                                       RegisterBean registerBean = (RegisterBean) object;
                                                                       switch (registerBean.getResulecode()) {
                                                                           case 0://用户已存在,注册失败
                                                                               ToastUtils.showmessage("该用户已存在,请直接登录!");
                                                                               break;
                                                                           case 1://注册成功
                                                                               ToastUtils.showmessage("注册成功!");
                                                                               showDialogToUserToRememberInfo(mRegPhoneNum, mRegPassword);
                                                                               break;
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
                    break;
            }
        }
    }

    /**
     * 展示用户注册信息的弹出框
     *
     * @param username
     * @param password
     */
    private void showDialogToUserToRememberInfo(String username, String password) {
        final MaterialDialog dialog = new MaterialDialog(mContext);
        dialog.btnNum(1)
                .content(
                        "您好,如下是您注册的信息,请牢记;后期如有遗忘,可在登录页点击\"忘记密码\"找回.\n" +
                                "账号:" + username + "\n密码:" + password)//提示内容
                .btnText("确定")//按钮
                .btnPressColor(mContext.getResources().getColor(R.color.aeaeae))
                .btnTextColor(CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary))
                .showAnim(new BounceTopEnter())//
                .dismissAnim(new SlideBottomExit())//
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }

    /**
     * 图形验证码
     */
    private void showPicCodeConfir(final TextInputEditText view, final TextView textView) {
        final PicCodeConfirDialog picCodeConfirDialog = new PicCodeConfirDialog(mContext);
        picCodeConfirDialog.onCreateView();
        picCodeConfirDialog.setUiBeforShow();
        //点击空白区域能不能退出
        picCodeConfirDialog.setCanceledOnTouchOutside(true);
        //按返回键能不能退出
        picCodeConfirDialog.setCancelable(true);
        picCodeConfirDialog.show();
        picCodeConfirDialog.addOnPicCodeClickListener(new PicCodeConfirDialog.OnPicCodeClickListener() {
            @Override
            public void onPicCodeClick(String inputStr) {
                picCodeConfirDialog.reSetPicCode(CodeUtils.getInstance().createBitmap());
            }

            @Override
            public void onConfirClick() {
                String getInput = picCodeConfirDialog.getInput();
                if (getInput.equalsIgnoreCase((String) SharedPreferencesUtils.getParam
                        (mContext, SpSave.PIC_CODE_CONFIR, "0000"))) {
                    picCodeConfirDialog.dismiss();
                    //图形校验码验证通过执行
                    reset_mStringBuilder();
                    mStringBuilder.append("校验通过,");
                    //发送短信验证码
                    sendConfirCode(view.getText().toString(), textView);
                } else {
                    //图形校验码验证失败执行
                    ToastUtils.showmessage("您输入的校验码有误,请重新输入!");
                    picCodeConfirDialog.setInput("");
                    picCodeConfirDialog.reSetPicCode(CodeUtils.getInstance().createBitmap());
                }
            }
        });
    }

    private void sendConfirCode(String s, TextView view) {
        //TODO 请求server端获取手机短信验证码校验注册
        //..........
        mStringBuilder.append("验证码已发送至手机,请注意查收.");
        ToastUtils.showmessage(mStringBuilder);
        mSendCountMessage = new SendCountMessage(view);
        mSendCountMessage.start();
    }

    private void reset_mStringBuilder() {
        if (mStringBuilder == null) {
            mStringBuilder = new StringBuilder();
        } else {
            mStringBuilder = null;
            mStringBuilder = new StringBuilder();
        }
    }
/******************************************************TODO login*********************************************************
 *
 */
    /**
     * 登录按钮点击事件
     */
    public void login() {
        mIsAppear = false;
        //点击则登录,注册按钮消失,弹出新的界面
        mView.btnRandLGone();
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_login,
                null);
        ViewUtils.inject(mContentView, mLoginPopEvent);
        mLoginPop = new CustomPopWindow.PopWindowBuilder(mContext)
                .setView(mContentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setAnimationStyle(R.style.PopupAnimation)
                .setFocusable(true)
                .setTouchable(true)
                .setBackGround(new ColorDrawable(0))
                .setOutsideTouchable(true)
                .setOnDissmissListener(mOnTouchListener)
                .create()
                .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
    }

    /**
     * 登录界面pop事件处理类
     */
    class LoginPopEvent {
        @FindView(R.id.login)
        private Button mLogin;
        @FindView(R.id.rememberpwd)
        private CheckBox mRememberPassword;
        @FindView(R.id.username)
        private TextInputEditText mUserID;
        @FindView(R.id.password)
        private TextInputEditText mPassWord;
        @FindView(R.id.usernamecontainer)
        private TextInputLayout mUserIDLayout;
        @FindView(R.id.passwordcontainer)
        private TextInputLayout mPassWordLayout;
        private String mUserIDStr;
        private String mUserPasswordStr;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        private void initEvent() {
            mLogin.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color
                    .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
            String mIsRemPwd = (String) SharedPreferencesUtils.getParam(AppUtils.getAppContext(),
                    SpSave.REMEMBER_PWD, "");
            if (!TextUtils.isEmpty(mIsRemPwd)) {
                switch (mIsRemPwd) {
                    case "1":
                        mRememberPassword.setChecked(true);
                        break;
                    case "-1":
                        mRememberPassword.setChecked(false);
                        break;
                }
            }
            //记住密码点击事件
            mRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        SharedPreferencesUtils.setParam(AppUtils.getAppContext(),
                                SpSave.REMEMBER_PWD,
                                "1");
                    } else {
                        SharedPreferencesUtils.setParam(AppUtils.getAppContext(),
                                SpSave.REMEMBER_PWD,
                                "-1");
                    }
                }
            });
            //设置用户名输入框末尾的图片点击事件:一键清空输入
            mUserID.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // mUserName.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                    Drawable drawable = mUserID.getCompoundDrawables()[2];
                    //如果右边没有图片，不再处理
                    if (drawable == null)
                        return false;
                    //如果不是按下事件，不再处理
                    if (motionEvent.getAction() != MotionEvent.ACTION_UP)
                        return false;
                    if (motionEvent.getX() > mUserID.getWidth()
                            - mUserID.getPaddingRight()
                            - drawable.getIntrinsicWidth()) {
                        mUserID.setText("");
                    }
                    return false;
                }
            });
            String existUserID = (String) SharedPreferencesUtils.getParam(AppUtils.getAppContext(),
                    SpSave.LOGING_PHONE, "");
            if (!TextUtils.isEmpty(existUserID)) {
                mUserID.setText(existUserID);
                mUserID.setSelection(existUserID.length());
                mPassWord.setText((String) SharedPreferencesUtils.getParam(AppUtils.getAppContext(),
                        SpSave.LOGING_PASSWORD, ""));
            }
            InputFilter[] filters = {new InputFilterEx(16)};
            mUserID.setFilters(filters);
//            mUserID.addTextChangedListener
        }

        @OnClick({R.id.login, R.id.forgetpassword, R.id.back})
        private void loginClick(View view) {
            switch (view.getId()) {
                case R.id.back://返回
                    mLoginPop.dissmiss();
                    break;
                case R.id.login://登录
                    mUserIDStr = mUserID.getText().toString().trim();
                    mUserPasswordStr = mPassWord.getText().toString().trim();
                    if (!TextUtils.isEmpty(mUserIDStr) && !TextUtils.isEmpty(mUserPasswordStr)) {
                        LogUtils.d("mUserID---->", mUserIDStr);
                        LogUtils.d("mPassWord---->", mUserPasswordStr);
                        mInteractor.getLoginRequestBean(new HashMap<String, String>() {
                            {
                                put("userId", mUserIDStr);
                                put("password", mUserPasswordStr);
                            }
                        }, new ICommonInteractorCallback() {
                            @Override
                            public void loadSuccess(Object object) {
                                if (null != object) {
                                    LoginBean loginBean = (LoginBean) object;
                                    switch (loginBean.getResuletcode()) {
                                        case 0:
                                            if (null != mView)
                                                ViewAnimationUtils.shake(mUserIDLayout);
                                            ViewAnimationUtils.shake(mPassWordLayout);
                                            ToastUtils.showmessage(mContext.getResources().getString(R
                                                    .string.faillogin));
                                            break;
                                        case 1:
                                            //成功登陆
                                            //链接融云的token
                                            String token = loginBean.getValue().getToken();
                                            LogUtils.i("token---->", "token---->" + loginBean.getValue().getToken());
                                            //保存用户名、密码、token到sp
                                            SharedPreferencesUtils.setParam(AppUtils
                                                    .getAppContext(), SpSave.LOGING_PHONE, mUserIDStr);
                                            SharedPreferencesUtils.setParam(AppUtils
                                                    .getAppContext(), SpSave.LOGING_PASSWORD, mUserPasswordStr);
                                            SharedPreferencesUtils.setParam(AppUtils
                                                    .getAppContext(), SpSave.RONGIM_TOKEN, token);
                                            //更新联系人信息
                                            UserInfoManager.getInstance().upDateContacts();
                                            if (!TextUtils.isEmpty(token)) {
                                                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                                    @Override
                                                    public void onTokenIncorrect() {

                                                    }

                                                    @Override
                                                    public void onSuccess(String s) {
                                                        ToastUtils.showmessage(
                                                                "鏈接成功" + s);
                                                        mLoginPop.dissmiss();
                                                        mView.finishActivity();
                                                        mView.jumpToActivity(HomeActivity.class);
                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                                    }
                                                });
                                            }
                                            break;
                                    }
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
                    break;
                case R.id.forgetpassword://忘记密码
                    mIsAppear = true;
                    mLoginPop.dissmiss();
                    View mContentView = LayoutInflater.from(mContext).inflate(R.layout
                                    .pop_forgetpassword,
                            null);
                    ViewUtils.inject(mContentView, mForgetPasswordPopEvent);
                    mForgetPasswordPop = new CustomPopWindow.PopWindowBuilder(mContext)
                            .setView(mContentView)
                            .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            .setAnimationStyle(R.style.PopupAnimation)
                            .setFocusable(true)
                            .setTouchable(true)
                            .setBackGround(new ColorDrawable(0))
                            .setOutsideTouchable(true)
                            .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    mLoginPop.showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                                }
                            })
                            .create()
                            .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                    break;
            }
        }
    }

    /**
     * 忘记密码界面pop事件处理类
     */
    class ForgetPasswordPopEvent {
        @FindView(R.id.username)
        private TextInputEditText mPhoneNum;
        @FindView(R.id.et_code)
        private TextInputEditText mCode;
        @FindView(R.id.get_code)
        private TextView mBtnCode;
        @FindView(R.id.bt_next)
        private Button mNextStap;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        private void initEvent() {
            InputFilter[] filters = {new InputFilterEx(16)};
            mPhoneNum.setFilters(filters);
            //设置下一步按钮点击背景selector
            mNextStap.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color
                    .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));

        }

        @OnClick({R.id.back, R.id.get_code, R.id.bt_next})
        private void onforgetPasswordClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    mForgetPasswordPop.dissmiss();
                    break;
                case R.id.get_code:
                    if (CommonUtils.isMobileNO(mPhoneNum.getText().toString())) {
                        mInteractor.getIsRegisterOrNotBean(new HashMap<String, String>() {
                            {
                                put("userId", mPhoneNum.getText().toString());
                            }
                        }, new
                                                                   ICommonInteractorCallback() {
                                                                       @Override
                                                                       public void loadSuccess(Object object) {
                                                                           IsRegisterOrNotBean IsRegisterOrNotBean = (IsRegisterOrNotBean) object;
                                                                           switch (IsRegisterOrNotBean.getResulecode()) {
                                                                               case 0:
                                                                                   break;
                                                                               case 1:
                                                                                   //图形验证码校验
                                                                                   showPicCodeConfir(mCode, mBtnCode);
                                                                                   break;
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
                    } else {
                        ToastUtils.showmessage("请输入正确的手机号码.");
                    }
                    break;
                case R.id.bt_next:
                    final String messageCode = mCode.getText().toString();
                    if (messageCode.length() == 4 && !TextUtils.isEmpty(mPhoneNum.getText())
                            && CommonUtils.isMobileNO(mPhoneNum.getText().toString())) {
                        //TODO 请求server验证
                        mInteractor.confirMessageCode(new HashMap<String, String>() {
                            {
                                put("messagecode", messageCode);
                            }
                        }, new
                                                              ICommonInteractorCallback() {
                                                                  @Override
                                                                  public void loadSuccess(Object object) {
                                                                      MessageCodeBean messageCodeBean = (MessageCodeBean) object;
                                                                      switch (messageCodeBean.getResulecode()) {
                                                                          case 1:
                                                                              mIsAppear = true;
                                                                              mForgetPasswordPop.dissmiss();
                                                                              mLoginPop.dissmiss();
                                                                              mResetPwdUserId = mPhoneNum.getText().toString();
                                                                              View mContentView = LayoutInflater.from(mContext)
                                                                                      .inflate(R.layout.pop_setnewpassword,
                                                                                              null);
                                                                              ViewUtils.inject
                                                                                      (mContentView, mSetNewPasswordPopEvent);

                                                                              mNewPasswordPop = new CustomPopWindow.PopWindowBuilder(mContext)
                                                                                      .setView(mContentView)
                                                                                      .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                                                                                      .setAnimationStyle(R.style.PopupAnimation)
                                                                                      .setFocusable(true)
                                                                                      .setTouchable(true)
                                                                                      .setBackGround(new ColorDrawable(0))
                                                                                      .setOutsideTouchable(true)
                                                                                      .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                                                                                          @Override
                                                                                          public void onDismiss() {
                                                                                              mLoginPop.showAtLocation(mView
                                                                                                      .getViewParent(), Gravity.CENTER, 0, 0);
                                                                                          }
                                                                                      })
                                                                                      .create()
                                                                                      .showAtLocation(mView.getViewParent(), Gravity.CENTER, 0, 0);
                                                                              break;
                                                                          case 0:
                                                                              ToastUtils.showmessage("验证码有误,请重新输入");
                                                                              break;
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
                    } else if (messageCode.length() != 4) {
                        ToastUtils.showmessage("请输入正确的验证码!");
                    } else if (!CommonUtils.isMobileNO(mPhoneNum.getText().toString())) {
                        ToastUtils.showmessage("手机号非法.");
                    } else {
                        ToastUtils.showmessage("请填写完整.");
                    }
                    break;
            }
        }
    }

    /**
     * 设置新密码界面pop事件处理类
     */
    class SetNewPasswordPop {
        @FindView(R.id.confirm)
        private Button mConfirm;
        @FindView(R.id.newpassword)
        private TextInputEditText mNewPassword;
        @FindView(R.id.newpasswordfir)
        private TextInputEditText mNewPasswordFir;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Init
        private void initEvent() {
            mConfirm.setBackground(SelectorUtils.getCommenButtonBg(mContext.getResources().getColor(R
                    .color
                    .act), CommonUtils.getColorByAttrId(mContext, R.attr.colorPrimary)));
        }

        @OnClick({R.id.back, R.id.confirm})
        private void setNewPasswordPopClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    mNewPasswordPop.dissmiss();
                    break;
                case R.id.confirm:
                    final String mNewPasswordStr = mNewPassword.getText().toString();
                    final String mNewPasswordFirStr = mNewPasswordFir.getText().toString();
                    if (!TextUtils.isEmpty(mNewPasswordStr) && !TextUtils.isEmpty(mNewPasswordFirStr) &&
                            mNewPasswordStr.equals(mNewPasswordFirStr)) {
                        mInteractor.getResetPasswordBean(new HashMap<String, String>() {
                            {
                                put("userId", mResetPwdUserId);
                                put("password", mNewPasswordStr);
                                put("passwordfir", mNewPasswordFirStr);
                            }
                        }, new
                                                                 ICommonInteractorCallback() {
                                                                     @Override
                                                                     public void loadSuccess(Object object) {
                                                                         if (null != object) {
                                                                             ResetPasswordBean resetPasswordBean = (ResetPasswordBean) object;
                                                                             switch (resetPasswordBean.getResulecode()) {
                                                                                 case 0:
                                                                                     ToastUtils
                                                                                             .showmessage("密码不能和原密码一样.请重新输入!");
                                                                                     break;
                                                                                 case 1:
                                                                                     mNewPasswordPop.dissmiss();
                                                                                     mForgetPasswordPop.dissmiss();
                                                                                     ToastUtils.showmessage("重置密码成功," +
                                                                                             "欢迎使用新密码登录!");
                                                                                     break;
                                                                             }
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
                    } else if (TextUtils.isEmpty(mNewPasswordStr) && TextUtils.isEmpty(mNewPasswordFirStr)) {
                        ToastUtils.showmessage("密码不能为空.");
                    } else {
                        ToastUtils.showmessage("输入不一致,请重新输入");
                    }
                    break;
            }
        }
    }

    class MyTouchListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            if (mIsAppear) {
                mIsAppear = false;
            } else {
                mView.btnRandLVisable();
            }
        }
    }
}
