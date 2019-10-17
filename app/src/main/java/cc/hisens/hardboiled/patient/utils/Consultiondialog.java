package cc.hisens.hardboiled.patient.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.consultion.ConsultTypeActivity;
import cc.hisens.hardboiled.patient.model.OrderInfoModel;


/**
 * Created by Administrator on 2018/11/8.
 */

public class Consultiondialog implements View.OnClickListener {
    public Dialog mDialog;
    public View view;
    public LinearLayout lyTuwen,lyWenzhen;
    public TextView tvTuwenPrice,tvPackagePrice;
    public ImageView ivClose;
    public Context mContext;
    public String doctorname,headUrl,workPlace;
    public int doctorId,level;
    public OrderInfoModel orderInfoModel;
    public float onePackPrice,morePackPrice; //一个资费包价格，服务包价格
    public int type;//用这个来标记是从医生履历界面跳转过来的，还是从聊天页面跳转过来的



    public Consultiondialog(Context context,String doctorName,String headurl,int doctorId,int level,float onepackPrice,float morePackPrice,OrderInfoModel model,int type) {
        this.mContext = context;
        this.doctorname=doctorName;
        this.headUrl=headurl;
        this.doctorId=doctorId;
        this.level=level;
        this.orderInfoModel=model;
        this.onePackPrice=onepackPrice;
        this.morePackPrice=morePackPrice;
        this.type=type;


    }


    public void inintDialog() {
        view = View.inflate(mContext, R.layout.consultion_popwindow, null);

        tvTuwenPrice=view.findViewById(R.id.tv_tuwen_price);
        tvPackagePrice=view.findViewById(R.id.tv_package_price);
         lyTuwen=view.findViewById(R.id.ly_tuwen);
         lyWenzhen=view.findViewById(R.id.ly_wenzhen);
         ivClose=view.findViewById(R.id.iv_close);
         lyTuwen.setOnClickListener(this);
         lyWenzhen.setOnClickListener(this);
         ivClose.setOnClickListener(this);

        mDialog = new Dialog(mContext, R.style.MyDialog);

        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        Window window=mDialog.getWindow();

        window.setWindowAnimations(R.style.takePhoto); // 添加动画
        if (morePackPrice==0){
            lyWenzhen.setVisibility(View.GONE);
        }else{
            tvPackagePrice.setText(String.format("%.2f", morePackPrice)+"元");
        }
        if (onePackPrice==0){
            lyTuwen.setVisibility(View.GONE);
        }else{
            tvTuwenPrice.setText(String.format("%.2f", onePackPrice)+"元");
        }



        window.setGravity(Gravity.BOTTOM);//设置显示在底部
        WindowManager windowManager=window.getWindowManager();
        Display display= windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=display.getWidth();//设置Dialog的宽度为屏幕宽度
        window.setAttributes(layoutParams);

        mDialog.show();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_tuwen:  //图文
                  Intent intent = new Intent(mContext, ConsultTypeActivity.class);
                  intent.putExtra("id", doctorId);
                  intent.putExtra("orderInfo",orderInfoModel);
                  intent.putExtra("price",String.format("%.2f", onePackPrice)+"");
                  intent.putExtra("consulttype",1);
                  mContext.startActivity(intent);
                  if (mDialog.isShowing()) {
                      mDialog.dismiss();
                  }

                break;

            case R.id.ly_wenzhen: //问诊服务包
                if (mDialog.isShowing()){
                    mDialog.dismiss();
                }
                    Intent intent2 = new Intent(mContext, ConsultTypeActivity.class);
                    intent2.putExtra("id", doctorId);
                    intent2.putExtra("orderInfo", orderInfoModel);
                    intent2.putExtra("price", String.format("%.2f", morePackPrice) + "");
                    intent2.putExtra("consulttype", 2);
                    mContext.startActivity(intent2);
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }

                break;
            case R.id.iv_close:
                if (mDialog.isShowing()){
                    mDialog.dismiss();
                }
                break;
        }
    }
}
