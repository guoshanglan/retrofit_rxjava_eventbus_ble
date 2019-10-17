package cc.hisens.hardboiled.patient.ui.activity.login.present;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.login.view.LoginView;


//登录的中间者，用来与view交互数据的
public class LoginPresenter extends BasePresenter<LoginView> {

    private User user; //用来处理网络请求等耗时操作的
    public LoginPresenter(){
        this.user=new User();
    }


    //通过activity点击的时候调用这个网络请求方法
    public void login(){
        user.login(mView.getContext(),mView.getNumber(),mView.getVoliatCode(),this);
    }


    //获取验证码
    public void getVerificationCode(){
        user.getVerificationCode2(mView.getContext(),mView.getNumber(),this);
    }

    //获取我关注的列表
    public void  getFollowedList(){
        user.getCurrentConsultionList(mView.getContext(),this);
    }



    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void loginSuccess(User loginBean) {
        if (loginBean!=null)
            mView.setLoginsuccessful(loginBean);

    }


    //获取用户关注列表成功
    public void getFollowedSuccess(boolean state) {
        if (state)
            mView.getFollowedsuccessful(state);

    }

    public void getFollwedFair(String err){
        mView.getFollowedFair(err);
    }



    //登录失败
    public void loginFailed(String str) {
        if (!TextUtils.isEmpty(str)){
            mView.setFailedError(str);
        }

    }





}
