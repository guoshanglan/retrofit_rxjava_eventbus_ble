package cc.hisens.hardboiled.patient.ui.activity.monitorResult;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.bean.EdInfo;
import cc.hisens.hardboiled.patient.db.impl.EdRepositoryImpl;
import cc.hisens.hardboiled.patient.model.BarData;
import cc.hisens.hardboiled.patient.model.GetEdinfoBean;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.wideview.MyBarChartView;

public class MonitorResultActivity extends BaseActivity {
    @BindView(R.id.iv_back_monitor_result)
    ImageView ivBack;  //返回
    @BindView(R.id.preview_record)
    ImageView ivPrevious;  //上一条记录
    @BindView(R.id.next_record)
    ImageView ivNext; //下一条记录
    @BindView(R.id.tv_record_date)
    TextView tvRecordDate; //检测记录时间
    @BindView(R.id.barchart)
    MyBarChartView barChartView;  //柱状图控件
    @BindView(R.id.tv_monitro_type)
    TextView tvMonitorType;  //测量模式
    @BindView(R.id.tv_monitor_yinsu)
    TextView tvMonitorYinsu;  //干扰因素
    @BindView(R.id.tv_duration)
    TextView tvMonitorDuration; //检测时长
    @BindView(R.id.tv_monitor_count)
    TextView tvBoqiCount; //勃起次数
    @BindView(R.id.tv_boqi_duration)
    TextView tvBoqiDuration; //勃起时长
    @BindView(R.id.tv_time1)
    TextView tvTime1;  //g≥190g的
    @BindView(R.id.tv_time2)
    TextView tvTime2;  //140≤g＜190（mins）
    @BindView(R.id.tv_time3)
    TextView tvTime3;  //90≤g＜140（mins）
    @BindView(R.id.tv_time4)
    TextView tvTime4;  //g＜90(mins)
    @BindView(R.id.tv_avarage_strength)
    TextView tvAvager; //平均硬度
    @BindView(R.id.tv_max_strength)
    TextView tvMaxStrength; //最大硬度
    public Ed ed;  //从其前面传过来的一条ed数据
    public ArrayList<BarData> innerData = new ArrayList<>(); //图表数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }


    //初始化数据
    private void initData() {
        if (appLication.ed!=null) {
            ed = appLication.ed;
            tvRecordDate.setText(TimeUtils.NowYearRecord(ed.getStartTimestamp()) + " " + TimeUtils.RecordTime(ed.getStartTimestamp()) + "-" + TimeUtils.RecordTime(ed.getEndTimestamp()));

            if (ed.isInterferential()) {
                tvMonitorType.setText("NPT");
            } else {
                tvMonitorType.setText("干预模式(AVSS)");
            }

            if (ed.getEdInfos()==null||ed.getEdInfos().size()==0) {
              getEdInfos();
            }else {

                GetErectionData();
            }

        }
    }




    public void getEdInfos(){
        initProgressDialog("");
        Map<String,Object>map=new HashMap<>();
        map.put("id",ed.getId());
        RequestUtils.get(this, Url.GetEdInfoRecord, map, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result==0){
                    Gson gson=new Gson();
                    GetEdinfoBean bean=gson.fromJson(gson.toJson(result),GetEdinfoBean.class);
                    if (bean.getDatas().size()>0){
                        List<EdInfo> edInfoList=new ArrayList<>();
                        for (int a=0;a<bean.getDatas().size();a++){
                            EdInfo edInfo=new EdInfo();
                            edInfo.setStartTime(bean.getDatas().get(a).getStart_time()*1000);
                            edInfo.setErectileStrength(bean.getDatas().get(a).getStrength());
                            edInfo.setEndTime(bean.getDatas().get(a).getStart_time()*1000);
                            edInfo.setDuration(5000);
                            edInfoList.add(edInfo);
                        }
                        Log.e("颗粒度",edInfoList.toString());
                        ed.setEdInfos(edInfoList);
                        GetErectionData();
                        new EdRepositoryImpl().saveEdinfo(edInfoList,ed.getId());
                    }

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                    ShowToast(errorMsg);

            }
        });
    }


    //获取勃起数据
    @SuppressLint("SetTextI18n")
    public void GetErectionData() {
        int total = 0;
        int erectCount = 0;  //勃起次数
        long erectDuration = 0;

        boolean once = false; //是否记为一次的次数
        for (int a = 0; a < ed.getEdInfos().size()-36; a++) {
            boolean flag3 = true;  //满足持续三分钟的标志
            boolean flag10 = false;  //是否间断十分钟的标志
            for (int j = a; j < a + 36; j++) {
                if (ed.getEdInfos().get(j + 1).getStartTime() - (ed.getEdInfos().get(j).getStartTime()) != 5 * 1000) {
                    flag3 = false;
                }

                if (ed.getEdInfos().get(j + 1).getStartTime() -  (ed.getEdInfos().get(j).getStartTime()) >= 5 * 1000 * 12 * 10) {
                    flag10 = true;
                }
            }

            if (!once && flag3 && !flag10) {
                erectCount++;
                once = true;
            }

            if (once) {  //如果满足一次了，就加一次时间间隔
                erectDuration+=5*1000;
            }

            if (flag10) {
                once = false;
            }
        }

        if (once) {
            erectDuration += 3 * 1000 * 60;  //然后这边需要加一次满足3分钟的时长
        }
        int maxValue=0;
        long duration450 = 0, duration270 = 0, duration160 = 0, duration = 0; //分别是强度在g≥450、270≤g＜450、160≤g＜270、g＜160
        for (int i = 0; i < ed.getEdInfos().size(); i++) {
            int erectileStregth = ed.getEdInfos().get(i).getErectileStrength();
            maxValue = Math.max(maxValue,ed.getEdInfos().get(i).getErectileStrength());
            total += erectileStregth;
            if (erectileStregth >= 190) {
                duration450 = duration450 + 5000;
            } else if (erectileStregth < 190 && erectileStregth >= 140) {
                duration270 = duration270 + 5000;
            } else if (erectileStregth < 140 && erectileStregth >= 90) {
                duration160 = duration160 + 5000;
            } else if (erectileStregth < 90) {
                duration = duration + 5000;
            }


            if (ed.getEdInfos().size() > 1 && i > 0) {
                if ((ed.getEdInfos().get(i).getStartTime()) - (ed.getEdInfos().get(i - 1).getStartTime()) >= 10 * 60 * 1000) {
                    int px = (int) (((ed.getEdInfos().get(i).getStartTime()) - ed.getEdInfos().get(i - 1).getStartTime() - 10 * 60 * 1000)) / 1000;
                    for (int a = 0; a < 15 + px; a++) {
                        innerData.add(new BarData(0, ""));
                    }
                    innerData.add(new BarData(erectileStregth, ""));
                } else {
                    innerData.add(new BarData(erectileStregth, ""));
                }

            } else {
                innerData.add(new BarData(erectileStregth, ""));
            }

        }

        Log.e("shuju",innerData.toString());

        barChartView.setBarChartData(innerData);
        Log.e("450",duration450+"..."+duration270+"...."+duration160+"..."+duration);
        tvMonitorDuration.setText(TimeUtils.TimeSpaceErection(ed.getEndTimestamp()-ed.getStartTimestamp()+10*1000));
        tvBoqiDuration.setText(TimeUtils.TimeSpaceErection(erectDuration));
        tvBoqiCount.setText(erectCount+"次");
        tvTime1.setText(TimeUtils.TimeSpaceErection(duration450));
        tvTime2.setText(TimeUtils.TimeSpaceErection(duration270));
        tvTime3.setText(TimeUtils.TimeSpaceErection(duration160));
        tvTime4.setText(TimeUtils.TimeSpaceErection(duration));
        tvAvager.setText(total / ed.getEdInfos().size() + "g");  //平均硬度
        tvMaxStrength.setText(maxValue + "g");
        if (ed.isInterferential()){
            tvMonitorType.setText("干预模式(AVSS)");
            tvMonitorYinsu.setText(ed.getInterferenceFactor());
        }else{
            tvMonitorType.setText("NPT");
            tvMonitorYinsu.setText("无");
        }
    }





    @OnClick({R.id.iv_back_monitor_result, R.id.preview_record, R.id.next_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_monitor_result:  //返回
                finish();
                break;
            case R.id.preview_record:  //上一条数据

                break;
            case R.id.next_record: //下一条数据

                break;


        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.monitor_result;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
