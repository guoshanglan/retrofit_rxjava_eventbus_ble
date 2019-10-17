package cc.hisens.hardboiled.patient.ui.activity.patient_evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.EvaluationAdapter;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.model.ConSultionModel;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.presenter.DoctorDetailPresenter;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.view.DoctorDetailView;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.LoadingPointView;

public class Patient_evaluationActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener,DoctorDetailView {
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回
    @BindView(R.id.tv_title)
    public TextView tvTitle;  //标题
    @BindView(R.id.smartLayout)
    public SmartRefreshLayout refreshLayout; //刷新控件
    @BindView(R.id.listview_paient)
    public ListView mListView;
    @BindView(R.id.ly_noMessage)
    public LinearLayout lyNoMessage;
    @BindView(R.id.id_loading_point_view)
    public LoadingPointView loadingPointView;
    public EvaluationAdapter adapter;
    public List<ConSultionModel.DatasBean.ListBean>list;
    public int pageIndex=0,doctorId;
    public DoctorDetailPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    //初始化数据
    private void initData() {
        doctorId=getIntent().getIntExtra("id",0);
        list=new ArrayList<>();
        adapter=new EvaluationAdapter(this,list,2);
        tvTitle.setText("患者评价");
        mListView.setAdapter(adapter);
        presenter.getScoreList();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);



    }


    @OnClick(R.id.tv_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.patient_evaluation;
    }

    @Override
    public BasePresenter getPresenter() {
        if (presenter==null){
            presenter=new DoctorDetailPresenter();
        }
        return presenter;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
      pageIndex=0;
      presenter.getScoreList();

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
      pageIndex++;
      presenter.getScoreList();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int PageIndex() {
        return pageIndex;
    }

    @Override
    public int DoctorId() {
        return doctorId;
    }

    @Override
    public void Success(ConSultionModel conSultionModel) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
        dismissProgressDialog();
        if (conSultionModel!=null){
            loadingPointView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            list.addAll(conSultionModel.getDatas().getList());
            adapter.notifyDataSetChanged();
        }

    }



    @Override
    public void NoMoreData(String str) {
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefresh();
        dismissProgressDialog();
        loadingPointView.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
        ToastUtils.show(this,str);

    }

    @Override
    public void Fail(String str) {
        refreshLayout.finishLoadmore();
        refreshLayout.finishRefresh();
        dismissProgressDialog();
        ShowToast(str);


    }
}
