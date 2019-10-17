package cc.hisens.hardboiled.patient.ui.activity.score;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.eventbus.UpLoadScore;
import cc.hisens.hardboiled.patient.model.UploadEHS;
import cc.hisens.hardboiled.patient.model.UploadIIEF;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.UpLoadIIEFScorePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.view.ScoreView;
import cc.hisens.hardboiled.patient.utils.DoubleClickUtil;
import cc.hisens.hardboiled.patient.wideview.AssessView;
import cc.hisens.hardboiled.patient.wideview.ViewPagerCompat;

/**
 * 这个Activity是用来给ehs和IIEF_5评分的界面
 */

public class ScoreActivity extends BaseActivity implements AssessView.OnOptionSelectedListener, ScoreView {
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回上一个界面
    @BindView(R.id.tv_title)
    public TextView tvTitle;  //顶部标题
    @BindView(R.id.viewpager_assess)
    public ViewPagerCompat viewPagerCompat;
    @BindView(R.id.btn_sumbit)
    public Button btnSubmit;  //提交
    public int pageCount, maxOption;  //viewpager的页数，maxoption 最大的选项数
    public String nameStart;  //以什么字符串开头，因为在本地的string文件中定义好了内容
    public int[] IIEF = new int[5];  //总的分数,用数组的形式进行上传
    public List<Integer>addList=new ArrayList<>();    //定义这个集合主要是用来判断提交按钮是否显示，判断他的长度
    public int ehsscore;
    public UpLoadIIEFScorePresenter iiefScorePresenter;
    public int index = -1;  //判断当前选项卡角标位置
    public boolean isShow=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    //初始化所有的控件
    private void initView() {
        pageCount = getIntent().getIntExtra("pagecount", 0);
        maxOption = getIntent().getIntExtra("maxoption", 0);
        nameStart = getIntent().getStringExtra("namestart");
        tvTitle.setText(getIntent().getStringExtra("title"));
        initViewPager(pageCount, maxOption, nameStart);
        for (int a=0;a<5;a++){
            addList.add(0);
        }
    }


    //初始化viewpager内容填充
    protected void initViewPager(int pageCount, int maxOptions, String nameStartWith) {
        final List<AssessView> fragments = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            AssessView view = new AssessView(this, i, maxOptions, nameStartWith, this);
            fragments.add(view);
        }
        viewPagerCompat.setAdapter(new ViewPagerCompat.PagerAdapterImpl(fragments));

    }


    //点击事件
    @OnClick({R.id.tv_back, R.id.btn_sumbit})
    public void onClick(View v) {
        if (DoubleClickUtil.isFastClick())
            return;
        switch (v.getId()) {
            case R.id.tv_back:
                finish();  //返回到上一层
                break;

            case R.id.btn_sumbit:  //提交评分
                UploadScore();
                break;


        }

    }


    //上传评分记录,
    public void UploadScore() {
        initProgressDialog("正在提交...");
        if (pageCount == 1) {
            iiefScorePresenter.UpLoadehsScore();
        } else {
            iiefScorePresenter.UpLoadIIefScore();
        }


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_assess;
    }

    @Override
    public BasePresenter getPresenter() {

            if (iiefScorePresenter == null) {
                iiefScorePresenter = new UpLoadIIEFScorePresenter();
            }
            return iiefScorePresenter;

    }

    //viewpager里面内容按钮选中回调事件
    @Override
    public void onOptionSelected(int score, int subjectIndex) {

        //通过这个来判断是从ehs还是从IIEF_5界面跳转过来的
        if (pageCount == 1) {
            this.ehsscore = score;
        } else {

            addList.set(subjectIndex,score+1);
            IIEF[subjectIndex] = score;
        }

        if (subjectIndex < viewPagerCompat.getAdapter().getCount() - 1) {
            viewPagerCompat.setCurrentItem(subjectIndex + 1);
        }

        if (pageCount != 1) {

               if (!addList.contains(0))
                btnSubmit.setVisibility(View.VISIBLE);


        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }
        index = subjectIndex;

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int EHSScore() {  //返回ehs的分
        return ehsscore+1;
    }


    //上传成功保存ehs的数据
    @Override
    public void UploadEHSSuccess(UploadEHS ehsScore) {
        if (ehsScore != null) {
            dismissProgressDialog();
            EventBus.getDefault().post(new UpLoadScore(ehsScore));
            finish();

        }
    }

    @Override
    public void FailedEHSError(String str) {
        if (!TextUtils.isEmpty(str)) {
            dismissProgressDialog();
            ShowToast(str);
        }
    }


    //下面是IIEF_5所需要的
    @Override
    public int[] IIEFScore() {
        return IIEF;
    }



    @Override
    public void UploadIIEFSuccess(UploadIIEF iief5Score) {
        //保存数据
        if (iief5Score != null) {
            dismissProgressDialog();
            EventBus.getDefault().post(new UpLoadScore(iief5Score));
            finish();

        }

    }

    @Override
    public void FailedIIEFError(String str) {
        if (!TextUtils.isEmpty(str)) {
            dismissProgressDialog();
            ShowToast(str);
        }
    }

}
