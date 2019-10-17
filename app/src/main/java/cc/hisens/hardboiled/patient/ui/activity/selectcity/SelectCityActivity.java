package cc.hisens.hardboiled.patient.ui.activity.selectcity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.CityRecyclerAdapter;
import cc.hisens.hardboiled.patient.adapter.ProviceRecyclerAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.eventbus.SelectCity;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.selectcity.model.SelectCityModel;


public class SelectCityActivity extends BaseActivity implements ProviceRecyclerAdapter.onItemClick, CityRecyclerAdapter.onItemClick {
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.recyclerview_provice)
    public RecyclerView proRecyclerview;  //省份列表
    @BindView(R.id.recyclerview_city)
    public RecyclerView cityRecyclerview;  //城市列表
    @BindView(R.id.tv_city)
    public TextView tvCity;//定位城市
    public ProviceRecyclerAdapter proviceRecyclerAdapter;  //省份adapter
    public CityRecyclerAdapter cityRecyclerAdapter;  //城市adapter
    public List<SelectCityModel.DatasBean> prolist = new ArrayList<>();
    public List<String> cityList = new ArrayList<>();
    public List<SelectCityModel> selectCityModelList = new ArrayList<>();
    public String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();


    }

    //初始化数据
    private void initData() {
        tvTitle.setText("选择地区");
        tvCity.setText(appLication.cityLocation + " (自动定位)");
        proviceRecyclerAdapter = new ProviceRecyclerAdapter(prolist);
        cityRecyclerAdapter = new CityRecyclerAdapter(cityList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        //新加入的代码
        proRecyclerview.setLayoutManager(layoutManager);
        cityRecyclerview.setLayoutManager(layoutManager2);
        proRecyclerview.setAdapter(proviceRecyclerAdapter);
        cityRecyclerview.setAdapter(cityRecyclerAdapter);
        proviceRecyclerAdapter.notifyDataSetChanged();
        cityRecyclerAdapter.notifyDataSetChanged();
        proviceRecyclerAdapter.OnProItemClick(this);
        cityRecyclerAdapter.OnItemClick(this);
        if (TextUtils.isEmpty(sharedUtils.readString("city"))) {
            getData();
        }else{
            Gson gson=new Gson();
            SelectCityModel city=gson.fromJson(sharedUtils.readString("city"),SelectCityModel.class);
            if (city.getDatas().size()>0) {
                prolist.addAll(city.getDatas());
                prolist.get(0).setClick(true);
                cityList.addAll(prolist.get(0).getCities());
                proviceRecyclerAdapter.notifyDataSetChanged();
                cityRecyclerAdapter.notifyDataSetChanged();
            }
            getData();
        }
    }

    public void getData(){
        RequestUtils.get(this, Url.City, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                Gson gson=new Gson();
                SelectCityModel city=gson.fromJson(gson.toJson(result),SelectCityModel.class);
                if (city!=null&&city.getDatas()!=null){
                   if (TextUtils.isEmpty(sharedUtils.readString("city"))) {
                       prolist.clear();
                       cityList.clear();
                       if (city.getDatas().size()>0) {
                           prolist.addAll(city.getDatas());
                           prolist.get(0).setClick(true);
                           cityList.addAll(prolist.get(0).getCities());
                       }
                       proviceRecyclerAdapter.notifyDataSetChanged();
                       cityRecyclerAdapter.notifyDataSetChanged();
                   }
                    sharedUtils.writeString("city",gson.toJson(result));
                }


            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {

            }
        });
    }


    @OnClick({R.id.tv_back, R.id.tv_city})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:  //点击返回
                finish();
                break;

            case R.id.tv_city:  //点击定位城市
                EventBus.getDefault().post(new SelectCity(appLication.cityLocation));
                finish();
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.select_city;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    //省份点击事件
    @Override
    public void onProviceItemClick(int positon) {
        for (int i = 0; i < prolist.size(); i++) {
            if (positon == i) {
                prolist.get(i).setClick(true);
            } else {
                prolist.get(i).setClick(false);
            }
        }
        cityList.clear();
        cityList.addAll(prolist.get(positon).getCities());
        cityRecyclerAdapter.notifyDataSetChanged();
        proviceRecyclerAdapter.notifyDataSetChanged();
    }

    //城市选择点击
    @Override
    public void onCityItemClick(int positon) {
        String city = cityList.get(positon);
        EventBus.getDefault().post(new SelectCity(city));
        finish();
    }
}
