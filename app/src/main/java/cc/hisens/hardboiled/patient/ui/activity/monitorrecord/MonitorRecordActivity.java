package cc.hisens.hardboiled.patient.ui.activity.monitorrecord;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.MonitorRecordAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.impl.EdRepositoryImpl;
import cc.hisens.hardboiled.patient.model.HeadTitleModel;
import cc.hisens.hardboiled.patient.ui.activity.monitorResult.MonitorResultActivity;
import cc.hisens.hardboiled.patient.utils.EdUtils;
import cc.hisens.hardboiled.patient.utils.MPChartUtils;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

//监测记录
public class MonitorRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.tv_back)
    public TextView tvBack; //返回
    @BindView(R.id.tv_title)
    public TextView tvTitle;  //页面标题
    @BindView(R.id.lc_monitorrecord)
    public CombinedChart chart;  //图表
    @BindView(R.id.lc_monitorrecord2)
    public CombinedChart chart2;  //图表
    @BindView(R.id.stickList_monitorRecord)
    public StickyListHeadersListView stickyListHeadersListView; //列表
    public MonitorRecordAdapter adapter; //适配器
    public List<Ed> edLists=new ArrayList<>();
    public List<HeadTitleModel>titleModels=new ArrayList<>();   //存储列表头部日期的时间数组

    //图表数据
    private List<String> xLabels = new ArrayList<>();
    private float maxValue = 560f, minValue = 0f;
    private List<Entry> NptEntries = new ArrayList<>();
    private List<Entry> AvssEntries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    private void initView() {
        tvTitle.setText("监测记录");

        adapter = new MonitorRecordAdapter(MonitorRecordActivity.this, edLists,titleModels);
        stickyListHeadersListView.setAdapter(adapter);
        getData();
        stickyListHeadersListView.setOnItemClickListener(this);


    }




    //从数据库中加载ED数据
    @SuppressLint("CheckResult")
    public void getData(){
        EdRepositoryImpl edRepository=new EdRepositoryImpl();
        edRepository.getEds().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Ed>>() {
            @Override
            public void accept(List<Ed> edList)  {
                if (edList!=null&&edList.size()>0){
                    int AvssIndex = 0;
                    int NptIndex = 0;
                    NptEntries.clear();
                    AvssEntries.clear();
                    List<Ed>chartList=new ArrayList<>();

                    //图表最多显示30个点
                    if (edList.size()>=30){
                        chartList.addAll(edList.subList(0,30));
                    }else{
                        chartList.addAll(edList);

                    }
                    for (int i = chartList.size()-1; i >=0; i--) {
                        if (chartList.get(i).isInterferential()) {  //干预模式
                            AvssEntries.add(new Entry(AvssIndex,chartList.get(i).getAverage()));
                            AvssIndex++;
                            Log.e("平局值",chartList.get(i).getAverage()+"");
                        } else {

                            NptEntries.add(new Entry(NptIndex, chartList.get(i).getAverage()));
                            NptIndex++;

                            Log.e("平局值",chartList.get(i).getAverage()+"");
                        }
                    }



                    //这部分代码是用来给日期置顶操作的处理
                    int index=1;
                    for (int i=0;i<edList.size();i++){
                        HeadTitleModel headTitleModel=new HeadTitleModel();
                        headTitleModel.title=TimeUtils.FormatYear(edList.get(i).getStartTimestamp());

                            if (edList.size()>1&&i>0) {
                                if (TimeUtils.FormatYear(edList.get(i).getStartTimestamp()).equals(TimeUtils.FormatYear(edList.get(i-1).getStartTimestamp()))) {

                                    headTitleModel.setTitleId(index + "");
                                } else {
                                    index++;
                                    headTitleModel.setTitleId(index + "");
                                }

                            } else {
                                headTitleModel.setTitleId(index + "");
                            }

                            titleModels.add(headTitleModel);
                        }

                    edLists.addAll(edList);
                    Log.e("图表", NptEntries.size() + "//////" + AvssEntries.size());
                    // 1.配置基础图表配置
                    MPChartUtils.configChart(chart, xLabels, maxValue, minValue, false, true);
                    MPChartUtils.configChart(chart2, xLabels, maxValue, minValue, false, true);
                    // 2,获取数据Data，这里有2条曲线
                    Drawable drawable1=getResources().getDrawable(R.drawable.bg_line_green);
                    Drawable drawable2=getResources().getDrawable(R.drawable.bg_line_red);
                    // 2,获取数据Data，这里有2条曲线
                    LineDataSet tartgetDataSet = MPChartUtils.getLineData(NptEntries, "", Color.parseColor("#35d5db"),drawable1, true);
                    LineDataSet lineDataSet = MPChartUtils.getLineData(AvssEntries, "", Color.parseColor("#FFea36"),drawable2, true);
                    // 3,初始化数据并绘制
                    MPChartUtils.initData(MonitorRecordActivity.this, chart, new LineData(lineDataSet));
                    MPChartUtils.initData(MonitorRecordActivity.this, chart2, new LineData(tartgetDataSet));
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }



    @OnClick(R.id.tv_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:  //返回
                finish();
                break;

        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.monitorrecord;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,MonitorResultActivity.class);
        appLication.ed=edLists.get(position);
        startActivity(intent);
    }
}
