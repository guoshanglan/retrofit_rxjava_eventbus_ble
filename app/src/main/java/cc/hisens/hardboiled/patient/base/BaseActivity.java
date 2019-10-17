package cc.hisens.hardboiled.patient.base;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.ButterKnife;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements PresenterCallback{

    protected BasePresenter mPresenter;   //基本交互类presenter
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;     //共享参数sp的对象
    protected ProgressDialog mProgressDialog;   //加载框
    protected Toast mToast;
    public ErrorDialog errorDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
       //屏幕适配一定要设置在setcontentView之前
        ScreenUtil.resetDensity(this);  //Android屏幕适配
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        init();

    }


    //初始化我们所需要的配置
    protected  void init(){
        //绑定初始化application
        appLication=MyApplication.getInstance();
        sharedUtils=new SharedUtils(this);
        errorDialog=new ErrorDialog(this);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

    }


    public void navigateToLogin(){
        finish();
        ActivityCollector.finishAll();
        UserConfig.UserInfo.setLogin(false);
        startActivity(new Intent(this, LoginActivity.class));
    }



    //初始化进度加载框
    protected void initProgressDialog( String info) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    //初始化自定义Toast

    protected  void  ShowToast(String msg){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(this).inflate(R.layout.error_view, null);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);
        if (msg.contains("Unauthorized")||msg.equals("")||msg.contains("未经授权的访问")){
            tv.setText("你的账号已在其他地方登录,请重新进行登录");

        }else if (msg.contains("未知错误")){
            tv.setText("网络异常或服务器异常,请稍后重试");
        }else{
            tv.setText(msg);
        }
        if (mToast == null) {
            mToast = new Toast(this);
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(this, 40));
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
            startActivity(new Intent(this, GetVoliatCodeActivity.class));
        }
    }




    //进度框消失
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //返回布局的Id
    protected abstract int getLayoutId();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenUtil.resetDensity(this);
    }





    /**
     * 检查Activity是否已关闭
     */
    public static Boolean checkActivityIsRun(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !activity.isFinishing() || !activity.isDestroyed();
        }
        return !activity.isFinishing();
    }


    //页面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
        ActivityCollector.removeActivity(this);



    }



}
