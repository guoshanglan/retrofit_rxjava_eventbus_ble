package cc.hisens.hardboiled.patient.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;


/**
 * Created by Administrator on 2018/11/8.
 */

public class SelectPhoto implements View.OnClickListener {
    public Dialog mDialog;
    public View view;
    public TextView tvCarema,tvXiangce,tvCancel;
    public Context mContext;
    public PhotoCallback photoCallback;


      public SelectPhoto(Context context){
          this.mContext=context;

      }


      public void inintDialog(){
          view=View.inflate(mContext, R.layout.take_photo_window,null);
          tvXiangce=view.findViewById(R.id.tv_xiangce);
          tvCarema=view.findViewById(R.id.tv_carema);
          tvCancel=view.findViewById(R.id.tvCancel);
          tvXiangce.setOnClickListener(this);
          tvCarema.setOnClickListener(this);
          tvCancel.setOnClickListener(this);
          mDialog=new Dialog(mContext,R.style.MyDialog);
          mDialog.setContentView(view);
          mDialog.setCanceledOnTouchOutside(false);
          mDialog.getWindow().setGravity(Gravity.BOTTOM); //设置在底部
          mDialog.getWindow().setWindowAnimations(R.style.takePhoto); // 添加动画
          mDialog.show();

      }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_carema:   //相机
                if (photoCallback!=null){
                 photoCallback.onCarema(1);
                }
                if (mDialog!=null)
                 mDialog.dismiss();
                break;

            case R.id.tv_xiangce:   //相册
                if (photoCallback!=null){
                    photoCallback.onCarema(2);
                }
                if (mDialog!=null)
                mDialog.dismiss();
                break;

            case R.id.tvCancel:   //取消
                if (mDialog!=null)
                mDialog.dismiss();
                if (photoCallback!=null){
                    photoCallback.onCarema(3);
                }
                break;

        }
    }

     public void initCallback(PhotoCallback callback){
          this.photoCallback=callback;
     }

    public interface PhotoCallback{
          void onCarema(int type);

    }
}
