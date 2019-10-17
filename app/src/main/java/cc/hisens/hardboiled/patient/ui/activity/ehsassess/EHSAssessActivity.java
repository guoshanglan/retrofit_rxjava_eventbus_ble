package cc.hisens.hardboiled.patient.ui.activity.ehsassess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cc.hisens.hardboiled.patient.model.EHSScore;
import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.ui.activity.iief_5.GetIIEFPresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.ScoreActivity;
import cc.hisens.hardboiled.patient.utils.DateUtils;
import cc.hisens.hardboiled.patient.utils.ErrorDialog;
import cc.hisens.hardboiled.patient.wideview.EHSScoreView;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;
import cc.hisens.hardboiled.patient.wideview.ScoreView;
import cc.hisens.hardboiled.patient.wideview.ViewPagerCompat;

//量表的问券调查
public class EHSAssessActivity extends BaseActivity implements EHSView {
    @BindView(R.id.tv_back)
    public TextView tvBack; //返回
    @BindView(R.id.tv_title)
    public  TextView tvTitle;  //标题
    @BindView(R.id.tv_record)
    public TextView tvRecord;  //记录
    @BindView(R.id.score_view)
    protected EHSScoreView mScoreView;  //显示分数的控件
    @BindView(R.id.tv_assess_time)
    protected TextView mTvAssessTime;  //测分时间
    @BindView(R.id.tv_score_hint)
    protected TextView mTvScoreHint; //测试提示
    @BindView(R.id.btn_assess)
    protected Button mBtnAssess;  //开始测评
    @BindView(R.id.ly_ehs)
    public LinearLayout lyehs;
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView;
    public ErrorDialog errorDialog;
    public EHSPresenter ehsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }


    //订阅的事件上传分数成功更新界面
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(UpLoadScore message) {
        if (message.ehsScore!=null) {
            int score=0;

                EHSScore.DatasBean bean = message.ehsScore.getDatas();
                score=bean.getScore();
                setAssessTime(score, bean.getTime());

            if (score>0) {

                mScoreView.setScore(score);
                mBtnAssess.setText(getString(R.string.reassess));
                if (score >= 4) {
                    mTvScoreHint.setText(getString(R.string.normal_assessment_hint));
                } else {
                    mTvScoreHint.setText(getString(R.string.exception_assessment_hint));
                }
            } else {
                mBtnAssess.setText(getString(R.string.start_assess));
                mTvScoreHint.setText(getString(R.string.no_assessment_hint));
            }

        }
    }

    //初始化所有的控件
    private void initView() {
        tvTitle.setText(getString(R.string.t_ehs_score));

        ehsPresenter.getScore();  //开始获取分数
        errorDialog=new ErrorDialog(this);

    }



    private void setData(EHSScore ehsScore) {

        int score=0;
        if (ehsScore.getDatas().size()>0) {
            EHSScore.DatasBean bean = ehsScore.getDatas().get(0);
            score=bean.getScore();
            setAssessTime(score, bean.getTime());
        }

        if (score>0) {
            mScoreView.setScore(score);
            mBtnAssess.setText(getString(R.string.reassess));
            if (score >= 4) {
                mTvScoreHint.setText(getString(R.string.normal_assessment_hint));
            } else {
                mTvScoreHint.setText(getString(R.string.exception_assessment_hint));
            }
        } else {
            mBtnAssess.setText(getString(R.string.start_assess));
            mTvScoreHint.setText(getString(R.string.no_assessment_hint));
        }
    }

    protected void setAssessTime(int totalScore, double timestamp) {
        String text = totalScore >= 0 ? new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(DateUtils.double2Date(timestamp)) : "--";
        mTvAssessTime.setText(getString(R.string.assess_time) + text);
    }


    //点击事件
    @OnClick({R.id.tv_back,R.id.tv_record,R.id.btn_assess})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:  //返回
                finish();
                break;
            case R.id.tv_record:  //历史记录

                break;
            case R.id.btn_assess:  //开始测评
                Intent intent=new Intent(this, ScoreActivity.class);
                intent.putExtra("pagecount",1);
                intent.putExtra("maxoption",4);
                intent.putExtra("namestart","ehs_assess_subject");
                intent.putExtra("title",getString(R.string.t_ehs_score));
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
        return R.layout.ehsscore_layout;
    }

    @Override
    public BasePresenter getPresenter() {
        if (ehsPresenter==null){
            ehsPresenter=new EHSPresenter();
        }
        return ehsPresenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String Userid() {
        return UserConfig.UserInfo.getUid();
    }

    //获取成功
    @Override
    public void GetSuccessFul(EHSScore score) {
        loadingPointView.setVisibility(View.GONE);
        lyehs.setVisibility(View.VISIBLE);
        setData(score);

    }

    //获取失败
    @Override
    public void getFair(String str) {

        if(!isFinishing())
            lyehs.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        errorDialog.initErrorDialog("你所请求的数据出现网络异常，请稍后重试。。", lyehs);
                        ShowToast(str);
                    }catch (Exception e){

                    }

                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
