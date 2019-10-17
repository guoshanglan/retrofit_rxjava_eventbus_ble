package cc.hisens.hardboiled.patient.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;


/**
 * Created by Administrator on 2018/11/8.
 *
 * 蓝牙设备相关的一些弹窗等
 */

public class ConnectDevicedialog implements View.OnClickListener {
    public Dialog mDialog;
    public View view;
    public TextView tvHelp, tvCancel, tvTry,tvDisConntect;
    public Context mContext;
    public DialogCallback deviceCallback;


    public ConnectDevicedialog(Context context) {
        this.mContext = context;

    }


    //连接失败的dialog
    public void initConnectFail() {
        view = View.inflate(mContext, R.layout.connectd_fail_dialog, null);
        tvCancel=view.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(this);
        mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();

    }

    //未发现设备的dialog
    public void initNotFoundDevice() {
        view = View.inflate(mContext, R.layout.not_found_device_dialog, null);
        tvDisConntect=view.findViewById(R.id.tv_disConnect);
        tvHelp=view.findViewById(R.id.tv_help);
        tvTry=view.findViewById(R.id.tv_re_try);
        tvDisConntect.setOnClickListener(this);
        tvTry.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

    }

    public void initCallback(DialogCallback callback) {
        this.deviceCallback = callback;
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
            case R.id.tv_disConnect:
                        if (mDialog!=null&&mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                break;
            case R.id.tv_re_try: //重试
                   deviceCallback.onTry(true);
                break;
            case R.id.tv_help:  //帮助

                break;

        }
    }

    public interface DialogCallback {
        void onTry(boolean isTry);

    }
}
