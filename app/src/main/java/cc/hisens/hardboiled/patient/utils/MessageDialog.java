package cc.hisens.hardboiled.patient.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;


/**
 * Created by Administrator on 2018/11/8.
 * <p>
 * 提示性的一些信息弹窗
 */

public class MessageDialog implements View.OnClickListener {
    public Dialog mDialog;
    public View view;
    public TextView tvMessage, tvSure, tvCancel;
    public Context mContext;
    public DialogCallback deviceCallback;


    public MessageDialog(Context context) {
        this.mContext = context;

    }


    //初始化dialog
    public void initDialog(String message,String sureText,String cancelText) {
        view = View.inflate(mContext, R.layout.tips_dialog, null);
        tvSure = view.findViewById(R.id.tv_sure);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvMessage=view.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        tvSure.setText(sureText);
        tvCancel.setText(cancelText);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();

    }


    public void initCallback(DialogCallback callback) {
        this.deviceCallback = callback;
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_sure: //确定
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                deviceCallback.OnSure(true);

                break;


        }
    }

    public interface DialogCallback {
        void OnSure(boolean sure);

    }
}
