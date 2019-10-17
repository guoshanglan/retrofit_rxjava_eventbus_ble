package cc.hisens.hardboiled.patient.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements PresenterCallback {
    protected View mRootView;  //返回的布局
    private Unbinder mUnbinder;
    protected BasePresenter mPresenter;
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;  //共享参数sp
    protected  Toast mToast;
    protected ProgressDialog mProgressDialog;   //加载框



    //初始化布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(getLayoutId(), container, false);
        // 初始化黄牛刀
        mUnbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    //初始化所需要的东西
    protected void init(View view) {
        appLication=MyApplication.getInstance();
        sharedUtils=new SharedUtils(getActivity());
        if(mPresenter==null) {
            mPresenter = getPresenter();
        }
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

        mUnbinder = ButterKnife.bind(this, view);// 初始化黄牛刀

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

    }



  //toast提示信息
    protected  void  ShowToast(String msg){
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(getActivity()).inflate(R.layout.error_view, null);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);
        if (msg.contains("Unauthorized")||msg.equals("")||msg.contains("未经授权的访问")){
            tv.setText("你的账号已在其他地方登录,请重新进行登录");
        }else if (msg.contains("未知错误")){
            tv.setText("网络异常或服务器异常,请稍后重试");
        }else{
            tv.setText(msg);
        }

        if (mToast == null) {
            mToast = new Toast(getActivity());
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(getActivity(), 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();


        if (msg.contains("Unauthorized")||msg.equals("")||msg.contains("未经授权的访问")){
            ActivityCollector.finishAll();
            new UserRepositoryImpl().DeleteAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,false);
            startActivity(new Intent(getActivity(), GetVoliatCodeActivity.class));
        }
    }


    //toast提示信息
    protected  void  ShowToast2(String msg){
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(getActivity()).inflate(R.layout.error_view, null);
        LinearLayout lybackground=toastView.findViewById(R.id.ly_background);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);

        tv.setText(msg);

        lybackground.setBackgroundResource(R.color.sync_success_color);
        if (mToast == null) {
            mToast = new Toast(getActivity());
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(getActivity(), 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();

    }


    //返回布局的Id
    protected abstract int getLayoutId();



    //初始化进度加载框
    protected void initProgressDialog( String info) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });

        }
        mProgressDialog.setMessage(info);
        mProgressDialog.show();
    }

    //进度框消失
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    //页面销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }

    }






}
