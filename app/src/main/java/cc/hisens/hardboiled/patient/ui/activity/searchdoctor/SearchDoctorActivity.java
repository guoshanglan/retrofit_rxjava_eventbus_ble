package cc.hisens.hardboiled.patient.ui.activity.searchdoctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.SearchDoctorAdapter;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.eventbus.ChatBackEvent;
import cc.hisens.hardboiled.patient.eventbus.PayResult;
import cc.hisens.hardboiled.patient.eventbus.SelectCity;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.model.SearchDoctorModel;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.presenter.SearchDoctorPresenter;
import cc.hisens.hardboiled.patient.ui.activity.searchdoctor.view.SearchDoctorView;
import cc.hisens.hardboiled.patient.ui.activity.selectcity.SelectCityActivity;
import cc.hisens.hardboiled.patient.utils.KeyboardManager;
import cc.hisens.hardboiled.patient.utils.ToastUtils;

public class SearchDoctorActivity extends BaseActivity implements SearchDoctorView, OnLoadmoreListener, OnRefreshListener, AdapterView.OnItemClickListener {
    @BindView(R.id.edittext_search_doctor)
    public EditText editText;  //搜索输入框
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回
    @BindView(R.id.tv_city)
    public TextView tvCity;  //选择城市
    @BindView(R.id.smartLayout)
    public SmartRefreshLayout smartRefreshLayout; //只能刷新控件
    @BindView(R.id.listview_search_doctor)
    public ListView listView;
    @BindView(R.id.ly_noDoctor)
    public LinearLayout lyNoDoctor;
    public SearchDoctorAdapter adapter;
    public List<Doctor>list;
    public SearchDoctorPresenter presenter;
    public int pageIndex=0;
    public String searchCity;


    //订阅的evnetbus回调蓝牙事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onCityMessage(SelectCity city) {
        if (city!=null){
            tvCity.setText(city.cityName);
            searchCity=city.cityName;
        }

    }



    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(ChatBackEvent event) {
        if (event!=null&&event.isChatBack){
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }


    //初始化数据
    private void initData() {
        tvCity.setText(appLication.cityLocation);
        searchCity=appLication.cityLocation;
        list=new ArrayList<>();
        adapter=new SearchDoctorAdapter(this,list);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        listView.setOnItemClickListener(this);
        KeyboardManager.showKeyboard(editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(editText.getText().toString().replace(" ",""))) {
                        initProgressDialog("正在查询，请稍后..");
                        presenter.SearchDoctor();
                        list.clear();
                    }else{
                        ShowToast("请输入关键字");
                    }

                    //关闭软键盘

                    //do something
                    //doSearch();
//                    ToastUtil.showToast("点击了软键盘的搜索按钮");
                    return true;
                }
                return false;
            }
        });


    }


    //点击事件
    @OnClick({R.id.tv_back,R.id.tv_city})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_back:
                    finish();
                break;

            case R.id.tv_city:
                startActivity(new Intent(this, SelectCityActivity.class));
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.searchdoctor_activity;
    }

    @Override
    public BasePresenter getPresenter() {
        if (presenter==null){
            presenter=new SearchDoctorPresenter();
        }
        return presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String SearchKey() {

        return editText.getText().toString().replace(" ","");
    }

    //页数索引值
    @Override
    public int PageIndex() {
        return pageIndex;
    }

    @Override
    public String City() {
        return searchCity;
    }

    @Override
    public MyApplication application() {
        return appLication;
    }

    //查询成功
    @Override
    public void SearchSuccess(SearchDoctorModel searchDoctorModel) {
        dismissProgressDialog();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        if (searchDoctorModel!=null&&searchDoctorModel.getList()!=null){
            lyNoDoctor.setVisibility(View.GONE);
           if (searchDoctorModel.getList().size()!=0){
               list.addAll(searchDoctorModel.getList());

           }else {
               if (pageIndex==0){
                   lyNoDoctor.setVisibility(View.VISIBLE);
               }else {
                   lyNoDoctor.setVisibility(View.VISIBLE);
                   smartRefreshLayout.finishLoadmore();
               }
            }

        }else {
            if (searchDoctorModel.getList()==null){
                lyNoDoctor.setVisibility(View.VISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //没有更多数据了
    @Override
    public void SearNomessage(String str) {
        dismissProgressDialog();
        lyNoDoctor.setVisibility(View.GONE);
        ToastUtils.show(this,str);

    }

    @Override
    public void SearchFail(String str) {
        dismissProgressDialog();
        if (str.equals("未授权访问")){  //跳转到登录页面
            ActivityCollector.finishAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,false);
            new UserRepositoryImpl().DeleteAll();
            startActivity(new Intent(this, GetVoliatCodeActivity.class));
        }else{
            lyNoDoctor.setVisibility(View.GONE);
            ShowToast(str);
        }

    }


    //加载更多
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
          smartRefreshLayout.finishLoadmore();
    }

    //刷新
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (!TextUtils.isEmpty(editText.getText().toString().replace(" ",""))) {
            initProgressDialog("正在查询，请稍后..");
            presenter.SearchDoctor();
            list.clear();
        }else{
            ShowToast("请输入关键字");
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this, DoctorDetailActivity.class);
        intent.putExtra("id",list.get(position).getDoctor_id());

        startActivity(intent);


    }
}
