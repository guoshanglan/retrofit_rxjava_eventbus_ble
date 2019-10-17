package cc.hisens.hardboiled.patient.ui.activity.iief_5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.model.ActionOrderEvent;
import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.model.UploadIIEF;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.ui.activity.score.ScoreActivity;
import cc.hisens.hardboiled.patient.utils.DateUtils;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;
import cc.hisens.hardboiled.patient.wideview.ScoreView;

public class IIEF_5Activity extends BaseActivity implements IIEFView {

    @BindView(R.id.tv_back)
    public TextView tvBack; //返回
    @BindView(R.id.tv_title)
    public  TextView tvTitle;  //标题
    @BindView(R.id.tv_record)
    public TextView tvRecord;  //记录
    @BindView(R.id.score_view)
    protected ScoreView mScoreView;  //显示分数的控件
    @BindView(R.id.tv_assess_time)
    protected TextView mTvAssessTime;  //测分时间
    @BindView(R.id.tv_score_hint)
    protected TextView mTvScoreHint; //测试提示
    @BindView(R.id.btn_assess)
    protected Button mBtnAssess;  //开始测评
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView;
    @BindView(R.id.ly_score)
    public LinearLayout lyScore;  //主要布局
    public ErrorDialog errorDialog;


    public GetIIEFPresenter GetIIEFPresenter;  //IIEF的中间纽带

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        EventBus.getDefault().register(this);
    }

    //订阅的事件上传分数成功更新界面
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(UpLoadScore message) {
        if (message.iief5Score!=null) {
            int totalScore = 0;
            IIEF5Score.DatasBean bean = message.iief5Score.getDatas();
            updateUI(0,bean);

        }
    }


    //初始化数据
    private void initData() {
        tvTitle.setText(getString(R.string.title_score));
       loadingPointView.setVisibility(View.VISIBLE);
      errorDialog=new ErrorDialog(this);
      GetIIEFPresenter.getIIEFSCore();  //开始获取分数
    }

    //设置数据
    public void SetData(IIEF5Score score){
        int totalScore=-1;
        if (score.getDatas().size()>0) {
            totalScore=0;
            updateUI(totalScore,score.getDatas().get(0));
        }else{
            updateUI(totalScore,null);
        }
    }



    //更新界面
    public void  updateUI(int totalScore,IIEF5Score.DatasBean bean){

        if (bean!=null) {
            for (int i = 0; i < bean.getScores().size(); i++) {
                totalScore += bean.getScores().get(i);
            }
            setAssessTime(totalScore, bean.getCreate_time());
        }

        mScoreView.setScore(totalScore);
        if (totalScore < 0) {
            mBtnAssess.setText(getString(R.string.start_assess));
            mTvScoreHint.setText(getString(R.string.no_assessment_hint));
        } else {
            mBtnAssess.setText(getString(R.string.reassess));
            if (totalScore >= 22) {
                mTvScoreHint.setText(getString(R.string.normal_assessment_hint));
            } else {
                mTvScoreHint.setText(getString(R.string.exception_assessment_hint));
            }
        }
    }


    protected void setAssessTime(int totalScore, double timestamp) {
        String text = totalScore >= 0 ? new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(DateUtils.double2Date(timestamp)) : "--";
        mTvAssessTime.setText(getString(R.string.assess_time) + text);
    }

    @OnClick({R.id.tv_back,R.id.btn_assess})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:  //返回
                finish();
                break;
            case R.id.tv_record:  //历史记录

                break;

            case R.id.btn_assess: //开始测评
                Intent intent=new Intent(this, ScoreActivity.class);
                intent.putExtra("pagecount",5);
                intent.putExtra("maxoption",6);
                intent.putExtra("namestart","assess_subject");
                intent.putExtra("title",getString(R.string.title_score));
                startActivity(intent);

                break;




        }

    }


    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_score;
    }

    @Override
    public BasePresenter getPresenter() {
        if (GetIIEFPresenter==null){
            GetIIEFPresenter=new GetIIEFPresenter();
        }
        return GetIIEFPresenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String Userid() {  //获取当前用户id
        return UserConfig.UserInfo.getUid();
    }

    @Override
    public void GetSuccessFul(IIEF5Score score) {
        dismissProgressDialog();
        loadingPointView.setVisibility(View.GONE);
        lyScore.setVisibility(View.VISIBLE);
        SetData(score);

    }

    @Override
    public void getFair(String str) {
        dismissProgressDialog();

        if(!isFinishing()) {
            lyScore.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。", lyScore);
                        ShowToast(str);
                    }catch (Exception e){

                    }
                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
