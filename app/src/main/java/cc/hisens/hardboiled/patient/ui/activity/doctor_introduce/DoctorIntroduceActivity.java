package cc.hisens.hardboiled.patient.ui.activity.doctor_introduce;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.PreviewHeadActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.wideview.CircleImageView;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;

public class DoctorIntroduceActivity extends BaseActivity implements DoctorInduceView {
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.doctor_name)
    public TextView tvDoctorName;
    @BindView(R.id.doctor_zhiwei)
    public TextView tvZhiwei;
    @BindView(R.id.doctor_workplace)
    public TextView tvWorkPlace; //工作地点
    @BindView(R.id.ly_zhiye)
    public LinearLayout lyzhiye;
    @BindView(R.id.tv_doctor_intro)
    public TextView tvIntro; //个人履历
    @BindView(R.id.doctor_head_image)
    CircleImageView circleImageView;
    @BindView(R.id.tv_doctor_goodAt)
    public TextView tvGoodAt;  //擅长
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView;
    @BindView(R.id.ly_introduce)
    public LinearLayout lyIntroduce; //医生介绍
    public int doctorId;
    public DoctorInducePresenter presenter;
    public String headurl;
    public ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId=getIntent().getIntExtra("id",0);

        presenter.getData();
        errorDialog=new ErrorDialog(this);
    }

    //初始化数据
    private void initData(DoctorInduceModel model) {
        Doctor bean=model.getDatas().get(0);
        headurl=bean.getHead_url();
        tvTitle.setText("医生履历");
        tvDoctorName.setText(bean.getName());
        Glide.with(this).load(bean.getHead_url()).placeholder(R.drawable.doctor_head_100).into(circleImageView);

        for (int i=0;i<bean.getWorkplaces().size();i++){
            if (bean.getWorkplaces().get(i).getIndex()==1){
                tvWorkPlace.setText(bean.getWorkplaces().get(i).getName());
            }

            View view=View.inflate(this,R.layout.zhiyedian_item,null);
            TextView textView=view.findViewById(R.id.tv_doctor_workplace1);
            textView.setText(bean.getWorkplaces().get(i).getName());
             lyzhiye.addView(view);
        }
        if (bean.getLevel()==1){
            tvZhiwei.setText("主任医师");
        }else if (bean.getLevel()==2){
            tvZhiwei.setText("副主任医师");
        }else if (bean.getLevel()==3){
            tvZhiwei.setText("主治医师");
        }else if (bean.getLevel()==4){
            tvZhiwei.setText("住院医师");
        }else if (bean.getLevel()==5){
            tvZhiwei.setText("助理医师");
        }
        tvGoodAt.setText(bean.getSpeciality());

        tvIntro.setText(bean.getIntro().getAcademic_experience()+bean.getIntro().getWork_experience());


    }


    @OnClick({R.id.tv_back,R.id.doctor_head_image})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:  //返回
                  finish();
                break;
            case R.id.doctor_head_image:  //查看头像
                Intent intent=new Intent(this, PreviewHeadActivity.class);
                intent.putExtra("headurl",headurl);
                startActivity(intent);
                break;



        }

    }



    @Override
    protected int getLayoutId() {
        return R.layout.doctor_introduce;
    }

    @Override
    public BasePresenter getPresenter() {
        if (presenter==null){
            presenter=new DoctorInducePresenter();
        }
        return presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int DoctorId() {
        return doctorId;
    }

    @Override
    public void Success(DoctorInduceModel doctorInduceModel) {
         loadingPointView.setVisibility(View.GONE);
         lyIntroduce.setVisibility(View.VISIBLE);
        if (doctorInduceModel!=null&&doctorInduceModel.getDatas().size()>0) {

            initData(doctorInduceModel);
        }

    }

    @Override
    public void Fail(String str) {

        if (str.equals("未授权访问")){  //跳转到登录页面
            ActivityCollector.finishAll();
            new UserRepositoryImpl().DeleteAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,false);
            startActivity(new Intent(this, GetVoliatCodeActivity.class));
        }else{
            if(!isFinishing())
                lyIntroduce.post(new Runnable() {
                    @Override
                    public void run() {
                        errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。",lyIntroduce);
                    }
                });
        }
    }
}
