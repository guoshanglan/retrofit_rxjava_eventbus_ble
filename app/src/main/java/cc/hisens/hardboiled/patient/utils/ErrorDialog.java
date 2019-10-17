package cc.hisens.hardboiled.patient.utils;


import android.content.Context;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;

public class ErrorDialog implements View.OnClickListener {
    public PopupWindow popupWindow;
    public View view;
    public ImageView ivClose;
    public TextView tvMessage;
    public Context mContext;


    public ErrorDialog(Context context){
        this.mContext=context;
    }


    public void  initErrorDialog(String message,View showView){
        view = View.inflate(mContext, R.layout.tips_view, null);
        tvMessage=view.findViewById(R.id.tv_tips_view);
        ivClose=view.findViewById(R.id.iv_close_view);
        tvMessage.setText(message);
        ivClose.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true); //设置可以点击
         popupWindow.setTouchable(true); //进入退出的动画，指定刚才定义的stylepo
        popupWindow.setAnimationStyle(R.style.takePhoto);
        popupWindow.showAtLocation(showView, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_view:
                if(popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }

                break;
        }
    }
}
