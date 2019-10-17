package cc.hisens.hardboiled.patient.ui.fragment.monitor;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clj.fastble.data.BleDevice;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.bean.HealthRecords;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.EdRepositoryImpl;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.eventbus.CleanData;
import cc.hisens.hardboiled.patient.eventbus.DeviceMessage;
import cc.hisens.hardboiled.patient.eventbus.HealthMessage;
import cc.hisens.hardboiled.patient.model.EHSScore;
import cc.hisens.hardboiled.patient.model.IIEF5Score;
import cc.hisens.hardboiled.patient.model.UpLoadEdRecordModel;
import cc.hisens.hardboiled.patient.model.UpLoadMonitorType;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.SyncDataActivity;
import cc.hisens.hardboiled.patient.ui.activity.ehsassess.EHSAssessActivity;
import cc.hisens.hardboiled.patient.ui.activity.ehsassess.EHSPresenter;
import cc.hisens.hardboiled.patient.ui.activity.healthrecord.PersonHealthRecordActivity;
import cc.hisens.hardboiled.patient.ui.activity.iief_5.GetIIEFPresenter;
import cc.hisens.hardboiled.patient.ui.activity.iief_5.IIEF_5Activity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.monitorResult.MonitorResultActivity;
import cc.hisens.hardboiled.patient.ui.activity.monitorrecord.MonitorRecordActivity;
import cc.hisens.hardboiled.patient.ui.activity.searchdevice.SearchDeviceActivity;
import cc.hisens.hardboiled.patient.ui.activity.searchdevice.SearchModel;
import cc.hisens.hardboiled.patient.utils.ConnectDevicedialog;
import cc.hisens.hardboiled.patient.utils.MPChartUtils;
import cc.hisens.hardboiled.patient.utils.SyncDatadialog;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 数据监测页面
 */

public class MonitorFragment extends BaseFragment implements ConnectDevicedialog.DialogCallback {

    @BindView(R.id.iv_home_ring)
    public ImageView ivConnect;  //蓝牙连接图标
    @BindView(R.id.tv_home_disconnect)
    public TextView tvDisConnect;  //未连接文字
    @BindView(R.id.tv_battary)
    public TextView tvBattary;  //当前电量
    @BindView(R.id.iv_battery)
    ImageView ivBattery; //电量图片
    @BindView(R.id.tv_home_start_sncy)
    public TextView tvStartSync;   //开始同步
    @BindView(R.id.rl_chart)
    public RelativeLayout rl_moreRecord;  //更多记录
    @BindView(R.id.lc_home_chart)
    public CombinedChart chart;  //图表
    @BindView(R.id.lc_home_chart2)
    public CombinedChart chart2;  //图表
    @BindView(R.id.rl_iief5)
    public RelativeLayout rlIIEF; //IIEF_5评分
    @BindView(R.id.rl_ehs)
    public RelativeLayout rlEHS;  //EHS评分
    @BindView(R.id.tv_ehs_test2)
    TextView tvTest2;
    @BindView(R.id.tv_ehs_test)
    TextView tvTest;
    @BindView(R.id.tv_iief_test)
    TextView tviiefTest;
    @BindView(R.id.tv_iief_test2)
    TextView tviiefTest2;
    @BindView(R.id.tv_ed_test)
    TextView tvEdTest;

    @BindView(R.id.rl_ed)
    public RelativeLayout rlED;  //ED相关的风险因素
    @BindView(R.id.ly_sync)
    public LinearLayout lySync;//同步数据
    @BindView(R.id.tv_before_sync)
    public TextView tvbeforeSync; //佩戴前同步
    @BindView(R.id.tv_after_sync)
    public TextView tvAfterSync; //佩戴后同步
    public boolean isStartSync; //是否开始同步
    private ConnectDevicedialog connectDevicedialog;  //对话框
    private boolean isConnected;
    private List<Ed> chartList = new ArrayList<>();
    private String stringNo = "";

    //图表数据
    private List<String> xLabels = new ArrayList<>();
    private float maxValue = 600f, minValue = 0f;
    private List<Entry> NptEntries = new ArrayList<>();
    private List<Entry> AvssEntries = new ArrayList<>();


    private BLEManagerWrapper mBleManagerWrapper = BLEManagerWrapper.getInstance(); //蓝牙操作管理者
    private ISyncDataCallback mISyncDataCallback = new MISyncDataCallbackImpl();   //同步数据的回调callback如查询蓝牙设备的电量，设备号等
    private SyncDatadialog datadialog;


    //订阅的evnetbus回调蓝牙事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(DeviceMessage event) {
        if (event != null) {
            if (event.isConnected) {
                isConnected = true;
                ivConnect.setBackgroundResource(R.drawable.home_icon_ring02);

                if (event.battry != 0) {
                    tvDisConnect.setVisibility(View.GONE);
                    tvBattary.setVisibility(View.VISIBLE);
                    tvBattary.setText(event.battry + "%");
                    ivBattery.setVisibility(View.VISIBLE);
                } else {
                    tvDisConnect.setVisibility(View.VISIBLE);
                    tvDisConnect.setText("已连接");
                    tvBattary.setVisibility(View.GONE);
                    ivBattery.setVisibility(View.GONE);
                }
                if (event.battry == 100) {
                    ivBattery.setBackgroundResource(R.drawable.home_icon_battery05);
                } else if (event.battry >= 80) {
                    ivBattery.setBackgroundResource(R.drawable.home_icon_battery04);
                } else if (event.battry >= 60) {
                    ivBattery.setBackgroundResource(R.drawable.home_icon_battery03);
                } else if (event.battry >= 40) {
                    ivBattery.setBackgroundResource(R.drawable.home_icon_battery02);
                } else {
                    ivBattery.setBackgroundResource(R.drawable.home_icon_battery01);
                }

            } else {
                ResetUI();
            }
        }


    }


    //订阅的evnetbus回调蓝牙事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onClean(CleanData event) {
        if (event != null) {
            if (event.isClean) {
                dismissProgressDialog();
                ShowToast2("同步成功");
            }


        }
    }

    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onHealthy(HealthMessage event) {
        if (event != null && event.isHealth) {
            getHealthRecord();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        GetIIEFScore(getActivity(), Integer.parseInt(UserConfig.UserInfo.getUid()));

    }

    //初始化我们所需要的数据
    private void initView() {

        mBleManagerWrapper.addSyncDataCallback(mISyncDataCallback);  //添加接口数据回调
        datadialog = new SyncDatadialog(getActivity());
        connectDevicedialog = new ConnectDevicedialog(getActivity());
        connectDevicedialog.initCallback(this);
        OpenBule(); //打开蓝牙
        UpLoadEdRecord();  //上传ed数据,保证本地未上传的ed数据上传到服务器

    }


    //从数据库中加载ED数据
    @SuppressLint("CheckResult")
    public void getData() {
        EdRepositoryImpl edRepository = new EdRepositoryImpl();
        edRepository.getEds().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Ed>>() {
            @Override
            public void accept(List<Ed> edList) throws Exception {
                if (edList != null && edList.size() > 0) {
                    int AvssIndex = 0;
                    int NptIndex = 0;
                    NptEntries.clear();
                    AvssEntries.clear();
                    chartList.clear();

                    //图表最多显示30个点
                    if (edList.size() >= 30) {
                        chartList.addAll(edList.subList(0, 30));
                    } else {
                        chartList.addAll(edList);

                    }
                    for (int i = chartList.size()-1; i >=0; i--) {
                        if (chartList.get(i).isInterferential()) {  //干预模式;
                            AvssEntries.add(new Entry(AvssIndex, chartList.get(i).getAverage()));
                            AvssIndex++;
                        } else {
                            NptEntries.add(new Entry(NptIndex, chartList.get(i).getAverage()));
                            NptIndex++;
                        }
                    }

                    Log.e("图表", NptEntries.size() + "//////" + AvssEntries.size());
                    // 1.配置基础图表配置
                    MPChartUtils.configChart(chart, xLabels, maxValue, minValue, false, false);
                    MPChartUtils.configChart(chart2, xLabels, maxValue, minValue, false, false);
                    Drawable drawable1 = getResources().getDrawable(R.drawable.bg_line_green);
                    Drawable drawable2 = getResources().getDrawable(R.drawable.bg_line_red);
                    // 2,获取数据Data，这里有2条曲线
                    LineDataSet tartgetDataSet = MPChartUtils.getLineData(NptEntries, "", Color.parseColor("#35d5db"), drawable1, true);
                    LineDataSet lineDataSet = MPChartUtils.getLineData(AvssEntries, "", Color.parseColor("#FFea36"), drawable2, true);
                    // 3,初始化数据并绘制
                    MPChartUtils.initData(getActivity(), chart, new LineData(lineDataSet));
                    MPChartUtils.initData(getActivity(), chart2, new LineData(tartgetDataSet));


                } else {
                    MPChartUtils.NotShowNoDataText(chart);
                    MPChartUtils.NotShowNoDataText(chart2);

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(sharedUtils.readString("back"))) {
            mBleManagerWrapper.addSyncDataCallback(mISyncDataCallback);  //添加接口数据回调
            sharedUtils.writeString("back", "");
            Log.e("获取","1111");
        }
    }

    @OnClick({R.id.iv_home_add, R.id.rl_iief5, R.id.rl_ehs, R.id.rl_chart, R.id.tv_home_start_sncy, R.id.tv_before_sync, R.id.tv_after_sync, R.id.rl_ed})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_add:
                startActivity(new Intent(getActivity(), SearchDeviceActivity.class));
                break;
            case R.id.rl_iief5:  //跳转到IIEF_5页面
                startActivity(new Intent(getActivity(), IIEF_5Activity.class));
                break;

            case R.id.rl_ehs:   //跳转到EHS页面
                startActivity(new Intent(getActivity(), EHSAssessActivity.class));
                break;
            case R.id.rl_chart://跳转到监测记录页面
                if (chartList.size() > 0)
                    startActivity(new Intent(getActivity(), MonitorRecordActivity.class));
                break;
            case R.id.tv_home_start_sncy:
                if (mBleManagerWrapper.isConnected()) {
                    isStartSync = !isStartSync;
                    if (isStartSync) {
                        tvStartSync.setVisibility(View.GONE);
                        lySync.setVisibility(View.VISIBLE);

                    } else {
                        lySync.setVisibility(View.GONE);
                    }
                } else {
                    ShowToast("连接断开，正在进行重连。。");
                    OpenBule();
                }

                break;
            case R.id.tv_before_sync:  //佩戴前同步
                if (mBleManagerWrapper.isConnected()) {
                    Intent intent = new Intent(getActivity(), SyncDataActivity.class);
                    startActivityForResult(intent, 100);
                    getActivity().overridePendingTransition(R.anim.dialog_top_in, R.anim.dialog_top_out);
                } else {
                    ShowToast("连接断开，正在进行重连。。");
                    OpenBule();
                }
                break;

            case R.id.tv_after_sync: //佩戴后同步
                if (mBleManagerWrapper.isConnected()) {
                    initProgressDialog("正在同步...");
                    GetMonitorType();  //从网络上获取
                    //syncData();
                } else {
                    ShowToast("连接断开，正在进行重连。。");
                    OpenBule();
                }
                break;
            case R.id.rl_ed:
                startActivity(new Intent(getActivity(), PersonHealthRecordActivity.class));
                break;

        }
    }


    //上传监测模式
    public void UpLoadMonitorType() {
        initProgressDialog("正在同步..");
        Map<String, Object> params = new HashMap<>();
        if (UserConfig.UserInfo.getMonitorType().equals("AVSS")) {
            params.put("type", 1);
            params.put("factors", UserConfig.UserInfo.getTag());
        } else {
            params.put("type", 0);
        }
        RequestUtils.post(getActivity(), Url.UpLoadMonitorType, params, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result == 0) {
                    syncDataAndTime();
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast("发生网络错误，请重新选择上传");
            }
        });
    }


    //上传监测模式
    public void UpLoadEdRecord() {
        List<Object> list = new ArrayList<>();
        List<UpLoadEdRecordModel.DatasBean.ListBean> listBeans = new ArrayList<>();
        EdRepositoryImpl edRepository = new EdRepositoryImpl();
        List<Ed> edList = edRepository.getNotSyncEd();
        for (int a = 0; a < edList.size(); a++) {
            UpLoadEdRecordModel.DatasBean bean = new UpLoadEdRecordModel.DatasBean();
            Ed ed = edList.get(a);
            bean.setUser_id(Integer.parseInt(UserConfig.UserInfo.getUid()));
            bean.setStart_time(ed.getStartTimestamp() / 1000);
            bean.setEnd_time(ed.getEndTimestamp() / 1000);
            bean.setFactors(ed.getInterferenceFactor());
            bean.setAve_strength(ed.getAverage());
            if (ed.isInterferential()) {
                bean.setType(1);
            } else {
                bean.setType(0);
            }
            for (int i = 0; i < ed.getEdInfos().size(); i++) {
                UpLoadEdRecordModel.DatasBean.ListBean listBean = new UpLoadEdRecordModel.DatasBean.ListBean();
                listBean.setStart_time(ed.getEdInfos().get(i).getStartTime() / 1000);
                listBean.setStrength(ed.getEdInfos().get(i).getErectileStrength());
                listBeans.add(listBean);
            }
            bean.setList(listBeans);
            list.add(bean);
        }
        if (list.size() > 0) {
            RequestUtils.postList(getActivity(), Url.UpLoadEdRecord, list, new HashMap<>(), new MyObserver<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse result) {
                    dismissProgressDialog();
                    if (result.result == 0) {
                        edRepository.updateEdSyncState();
                    } else {
                        ShowToast(result.message);
                    }
                }

                @Override
                public void onFailure(Throwable e, String errorMsg) {
                    dismissProgressDialog();
                    ShowToast(errorMsg);
                }
            });
        }
    }


    //获取监测模式
    public void GetMonitorType() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", UserConfig.UserInfo.getUid());
        RequestUtils.get(getActivity(), Url.GetMonitorType, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                Gson gson = new Gson();
                if (result.result == 0) {
                    UpLoadMonitorType monitorType = gson.fromJson(gson.toJson(result), UpLoadMonitorType.class);
                    if (monitorType.getDatas().getType() == 1) {
                        UserConfig.UserInfo.setMonitorType("AVSS");
                        UserConfig.UserInfo.setMonitorTag(monitorType.getDatas().getFactors());
                    } else {
                        UserConfig.UserInfo.setMonitorType("NPT");
                    }
                    syncData();
                } else {
                    ShowToast(result.message);
                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
            }
        });


    }

    //跳转回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            UpLoadMonitorType();  //上传监测模式
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.monitorfragment_layout;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    //判断是否打开蓝牙
    public void OpenBule() {

        //显判断蓝牙是否可用，如果可用，那么就开始同步数据
        if (!mBleManagerWrapper.isBlueEnable()) {

            mBleManagerWrapper.enableBluetooth();  //需要打开蓝牙

        } else {
            mBleManagerWrapper.ScanDevice(); //开始扫描设备进行连接
        }

    }


    //开始同步数据
    private void syncData() {
        if (mBleManagerWrapper.isConnected()) {   //判断蓝牙是否已经连接成功

            mBleManagerWrapper.syncData();

        } else {

            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择
        }

    }


    //开始同步数据和同步时间，同时清除数据
    private void syncDataAndTime() {
        if (mBleManagerWrapper.isConnected()) {   //判断蓝牙是否已经连接成功

            mBleManagerWrapper.syncDataBefore();

        } else {

            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择
        }

    }

    //蓝牙连接重试
    @Override
    public void onTry(boolean isTry) {
        if (connectDevicedialog.mDialog.isShowing()) {
            connectDevicedialog.mDialog.dismiss();
        }

        //重新连接
        if (mBleManagerWrapper.isBlueEnable()) {
            mBleManagerWrapper.ScanDevice();
        } else {
            mBleManagerWrapper.enableBluetooth();
        }

    }


    //获取用户的IIEF的分数
    public void GetIIEFScore(Context context, int Userid) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", Userid);
        RequestUtils.get(context, Url.getIIEF_5, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result == 0) {
                    int total = 0;
                    Gson gson = new Gson();
                    IIEF5Score iief5Score = gson.fromJson(gson.toJson(result), IIEF5Score.class);
                    if (iief5Score.getDatas().size() > 0) {
                        for (int i = 0; i < iief5Score.getDatas().get(0).getScores().size(); i++) {
                            total += iief5Score.getDatas().get(0).getScores().get(i);
                        }
                        tviiefTest.setText("在过去的三个月有");
                        if (total >= 22) {
                            tviiefTest2.setText("正常");
                        } else if (total >= 12) {
                            tviiefTest2.setText("轻度勃起功能障碍");
                        } else if (total >= 8) {
                            tviiefTest2.setText("中度勃起功能障碍");
                        } else if (total >= 0) {
                            tviiefTest2.setText("重度勃起功能障碍");
                        } else {
                            tviiefTest2.setText("");
                        }

                    }

                }
                GetEHSScore(getActivity(), Integer.parseInt(UserConfig.UserInfo.getUid()));
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                ShowToast(errorMsg);
                initView();

            }
        });


    }


    //获取用户的EHS的分数
    public void GetEHSScore(Context context, int Userid) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", Userid);
        RequestUtils.get(context, Url.getEhs, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result == 0) {
                    Gson gson = new Gson();
                    EHSScore ehsScore = gson.fromJson(gson.toJson(result), EHSScore.class);
                    if (ehsScore.getDatas().size() > 0) {
                        tvTest.setText("在过去三个月勃起硬度");
                        switch (ehsScore.getDatas().get(0).getScore()) {
                            case 1:
                                tvTest2.setText(R.string.l_first_level_result);
                                break;
                            case 2:
                                tvTest2.setText(R.string.l_second_level_result);
                                break;
                            case 3:
                                tvTest2.setText(R.string.l_third_level_result);
                                break;
                            case 4:
                                tvTest2.setText(R.string.l_fourth_level_result);
                                break;
                        }
                    }

                }

                getHealthRecord();

                initView();
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                ShowToast(errorMsg);
                initView();
            }
        });


    }


    @SuppressLint("CheckResult")
    private void getHealthRecord() {
//
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", UserConfig.UserInfo.getUid());
        RequestUtils.get(getActivity(), Url.UpLoadUserInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result == 0) {
                    Gson gson = new Gson();
                    HealthRecords mHealthRecord = gson.fromJson(gson.toJson(result), HealthRecords.class);
                    if (mHealthRecord.getDatas().getTrauma().size() > 0 || mHealthRecord.getDatas().getDisease().size() > 0 || mHealthRecord.getDatas().getMedicine().size() > 0) {
                        tvEdTest.setVisibility(View.GONE);
                    } else {
                        tvEdTest.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {

                ShowToast(errorMsg);

            }
        });


    }


    /**
     * 定义的蓝牙数据回调类，这个类用来处理蓝牙的回调数据的
     */

    private class MISyncDataCallbackImpl implements ISyncDataCallback {

        @Override
        public void onBleStateOn() {   //蓝牙设备开启
            String blue = sharedUtils.readString("blue");
            if (!TextUtils.isEmpty(blue)) {
                mBleManagerWrapper.ScanDevice();
            } else {
                startActivity(new Intent(getActivity(), SearchDeviceActivity.class));
            }
        }

        @Override
        public void onBleStateOff() {  //监听蓝牙关闭
            KLog.i("-->> onBleStateOff" + "蓝牙关闭");
            ResetUI();  //重置UI
            appLication.searchModel = null;
            isConnected = false;
        }

        @Override
        public void onBattery(int value) {  //蓝牙电量
            KLog.i("-->> onBattery——Monitor" + value + "");
            Log.e("电量", value + "");
            tvDisConnect.setVisibility(View.GONE);
            tvBattary.setVisibility(View.VISIBLE);
            ivBattery.setVisibility(View.VISIBLE);
            tvBattary.setText(value + "%");
            if (value == 100) {
                ivBattery.setBackgroundResource(R.drawable.home_icon_battery05);
            } else if (value >= 80) {
                ivBattery.setBackgroundResource(R.drawable.home_icon_battery04);
            } else if (value >= 60) {
                ivBattery.setBackgroundResource(R.drawable.home_icon_battery03);
            } else if (value >= 40) {
                ivBattery.setBackgroundResource(R.drawable.home_icon_battery02);
            } else {
                ivBattery.setBackgroundResource(R.drawable.home_icon_battery01);
            }
        }

        @Override
        public void onSerialNo(String value) {  //蓝牙下围基序列号
            if (stringNo.length() == 59) {
                stringNo = "";
            }
            if (stringNo.length() < 53) {
                stringNo += value;
            } else if (stringNo.length() <= 59) {
                stringNo = stringNo + ":" + value;
            }

            KLog.i("-->> onSerialNo" + value);
            Log.e("序列号", stringNo);
            Log.e("序列号2", stringNo.length() + "");
        }

        @Override
        public void onConnectionSuccessful(BleDevice device) {  //蓝牙设备连接成功
            KLog.i("-->> onConnectionSuccessful" + device.toString());

            tvDisConnect.setText("已连接");
            ivConnect.setBackgroundResource(R.drawable.home_icon_ring02);
            SearchModel searchModel = new SearchModel();
            searchModel.bleDevice = device;
            searchModel.setConnected(true);
            appLication.searchModel = searchModel;
        }

        @Override
        public void onConnectionFailed() {  //蓝牙设备连接失败
            KLog.i("-->> onConnectionFailed" + "设备连接失败");
            connectDevicedialog.initConnectFail();
        }

        @Override
        public void onNotFoundDevice() { //没有发现设备
            KLog.i("-->> onNotFoundDevice");
            if (!mBleManagerWrapper.isConnected() && connectDevicedialog != null)
                connectDevicedialog.initNotFoundDevice();

        }

        @Override
        public void onDisconnect() {  //拒绝连接
            KLog.i("-->> onDisconnect" + "拒绝连接");
            if (connectDevicedialog != null)
                connectDevicedialog.initConnectFail();
            isConnected = true;
            ResetUI();
        }

        @Override
        public void onSyncProgressUpdate(int value) {

        }

        @Override
        public void onSyncSuccessful(long startSleep) {  //蓝牙数据同步成功
            KLog.i("-->> onSyncSuccessful" + "数据同步成功" + startSleep);
            dismissProgressDialog();
            if (startSleep != -1) {
                getData();
                UpLoadEdRecord();  //上传ed数据
                Intent intent = new Intent(getActivity(), MonitorResultActivity.class);
                startActivity(intent);
            } else {
                ShowToast("无最新数据");
            }

        }

        @Override
        public void onSyncFailed() {  //蓝牙数据同步失败
            KLog.i("-->> onSyncFailed" + "数据同步失败");
            ShowToast("数据同步失败");
            dismissProgressDialog();
        }


        //扫描中
        @Override
        public void onScaning(BleDevice bleDevice) {

        }


        @Override
        public void onSetTimeFailed() {
            KLog.i("-->> onSetTimeFailed" + "设置时间失败");
        }

        @Override
        public void onSyncData() {  //同步数据中

        }

        @Override
        public void DeviceCount(List<BleDevice> deviceList) {  //发现的蓝牙设备数
            KLog.i("-->> DeviceCount" + "扫描完成" + deviceList.size());

            String blue = sharedUtils.readString("blue");
            if (!TextUtils.isEmpty(blue)) {
                if (deviceList != null && deviceList.size() > 0) {
                    Log.e("shebeiname", deviceList.get(0).getName());
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (deviceList.get(i).getName().equals(blue)) {
                            tvDisConnect.setText("连接中..");
                            mBleManagerWrapper.ConnectDevices(deviceList.get(i));
                        }

                    }
                }

            }
        }
    }


    public void ResetUI() {
        appLication.searchModel = null;
        tvBattary.setVisibility(View.GONE);
        ivBattery.setVisibility(View.GONE);
        tvDisConnect.setVisibility(View.VISIBLE);
        tvDisConnect.setText("未连接");
        lySync.setVisibility(View.GONE);
        tvStartSync.setVisibility(View.VISIBLE);
        ivConnect.setBackgroundResource(R.drawable.home_icon_ring01);
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBleManagerWrapper.removeSyncDataCallback(mISyncDataCallback);
        connectDevicedialog = null;
        EventBus.getDefault().unregister(this);
    }


}
