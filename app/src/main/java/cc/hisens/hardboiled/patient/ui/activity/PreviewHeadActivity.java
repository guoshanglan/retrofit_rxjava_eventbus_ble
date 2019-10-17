package cc.hisens.hardboiled.patient.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

//预览头像
public class PreviewHeadActivity extends BaseActivity {
    @BindView(R.id.rl_preview_head)
    public RelativeLayout lyHead;
    @BindView(R.id.iv_preview_head)
    public PhotoView ivHead;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Glide.with(this).load(getIntent().getStringExtra("headurl")).placeholder(R.drawable.doctor_head_160).into(ivHead);
    }

    @OnClick(R.id.rl_preview_head)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_preview_head:
                  finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.preivew_head;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
