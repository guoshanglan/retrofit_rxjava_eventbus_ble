package cc.hisens.hardboiled.patient.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.SyncFragmentAdapter;


/**
 * Created by Administrator on 2018/11/8.
 */

public class SyncDatadialog {
    public Dialog mDialog;
    public View view;
    public TextView tv1,tv2;
    public Activity mContext;
    public ViewPager viewPager;
    public SyncFragmentAdapter adapter; //适配器
    public List<Fragment>fragmentList;




    public SyncDatadialog(Activity context) {
        this.mContext = context;
    }


    public void inintDialog() {
        view = View.inflate(mContext, R.layout.synctype_dialog, null);
        mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setGravity(Gravity.TOP); //设置在底部
        mDialog.getWindow().setWindowAnimations(R.style.syncData); // 添加动画
        mDialog.show();

    }



}
