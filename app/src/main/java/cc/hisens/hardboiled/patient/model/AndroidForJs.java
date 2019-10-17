package cc.hisens.hardboiled.patient.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.AppVersionUtil;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;


//js调用Android的方法
public class AndroidForJs {


    private Context mContext;
    protected SharedUtils sharedUtils;     //共享参数sp的对象
    protected ProgressDialog mProgressDialog;   //加载框
    public Toast mToast;

    public AndroidForJs(Context context) {
        this.mContext = context;
        sharedUtils = new SharedUtils(mContext);
    }

    //退出登录
    @JavascriptInterface
    public void LoginOut() {
        initProgressDialog("正在退出登录");
        RequestUtils.post(mContext, Url.LoginOut, new HashMap<>(), new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();

                ActivityCollector.finishAll();
                new UserRepositoryImpl().DeleteAll();
                sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN, false);
                mContext.startActivity(new Intent(mContext, GetVoliatCodeActivity.class));


            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);

            }
        });

    }

    //查看咨询详情
    @JavascriptInterface
    public void QueryConsultDetail(String doctorId) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("id", doctorId + "");
        mContext.startActivity(intent);

    }

    //查询版本号传递给H5
    @JavascriptInterface
    public String QueryVersion() {

        return "V"+AppVersionUtil.getLocalVersion(mContext);
    }


    //初始化进度加载框
    protected void initProgressDialog(String info) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
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

    //初始化自定义Toast

    protected void ShowToast(String msg) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(mContext).inflate(R.layout.error_view, null);
        TextView tv = toastView.findViewById(R.id.txtToastMessage);
        tv.setText(msg);

        if (mToast == null) {
            mToast = new Toast(mContext);
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(mContext, 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();


        if (msg.contains("Unauthorized")||msg.equals("")){
            ActivityCollector.finishAll();
            new UserRepositoryImpl().DeleteAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,false);
            mContext.startActivity(new Intent(mContext, GetVoliatCodeActivity.class));
        }
    }


}
