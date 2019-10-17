package cc.hisens.hardboiled.patient.ui.activity.searchdevice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.clj.fastble.data.BleDevice;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.DeviceAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.ble.callbacks.ISyncDataCallback;
import cc.hisens.hardboiled.patient.eventbus.DeviceMessage;


import cc.hisens.hardboiled.patient.utils.ConnectDevicedialog;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.RippleView;
import io.reactivex.functions.Consumer;


//搜索设备，

public class SearchDeviceActivity extends BaseActivity implements ConnectDevicedialog.DialogCallback, AdapterView.OnItemClickListener {
    @BindView(R.id.listview_searchDevice)
    public ListView mListview;
    @BindView(R.id.tv_search_title)
    public TextView tvDeviceTitle;
    @BindView(R.id.tv_search_tips)
    public TextView tvTips;
    @BindView(R.id.iv_search_device)
    public ImageView ivDevice;
    @BindView(R.id.bt_search)
    public TextView tvSearch; //重新搜索
    @BindView(R.id.ripple_view)
    public RippleView rippleView;
    private ConnectDevicedialog connectDevicedialog;  //对话框
    private BLEManagerWrapper mBleManagerWrapper;  //蓝牙操作管理者
    private ISyncDataCallback mIScanDataCallback = new IScanCallbackImpl();   //同步数据的回调callback如查询·蓝牙设备的电量，设备号等
    private List<SearchModel> deviceList;
    private DeviceAdapter adapter; //设备适配器
    private  int battery; //电量值



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissionForBlue();

    }

    //初始化数据和界面
    private void initView() {
        deviceList = new ArrayList<>();
        adapter = new DeviceAdapter(this, deviceList);
        if (appLication.searchModel!=null){
            deviceList.add(appLication.searchModel);
        }
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(this);
        mBleManagerWrapper = BLEManagerWrapper.getInstance();
        mBleManagerWrapper.addSyncDataCallback(mIScanDataCallback);
        connectDevicedialog = new ConnectDevicedialog(this);
        connectDevicedialog.initCallback(this);
        TextState(getString(R.string.searching_device), getString(R.string.near_to_phone));
        rippleView.setVisibility(View.VISIBLE);

    }


    //打开蓝牙and扫描
    public void OpenAndScan() {

//
        //显判断蓝牙是否可用，如果可用，开始连接
        if (mBleManagerWrapper.isBlueEnable()) {
            rippleView.setVisibility(View.VISIBLE);
            TextState(getString(R.string.searching_device), getString(R.string.near_to_phone));
            ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_ring);
            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择

        } else {
            rippleView.setVisibility(View.GONE);
            ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_phone);
            mBleManagerWrapper.enableBluetooth();  //需要打开蓝牙
        }
    }


    //改变文字状态
    public void TextState(String title, String tip) {
        tvDeviceTitle.setText(title);
        tvTips.setText(tip);
    }


    @OnClick({R.id.bt_search,R.id.tv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_search:  //重新搜索
                ResetState();
                OpenAndScan();

                break;
            case R.id.tv_back:   //返回
                finish();
                sharedUtils.writeString("back","1");
                break;


        }
    }




    //界面恢复初始状态，如果蓝牙断开
    public void ResetState(){
        tvSearch.setVisibility(View.GONE);
        rippleView.setVisibility(View.GONE);
        TextState(getString(R.string.dis_connected), getString(R.string.go_setting_openblue));
        ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_phone);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.search_device;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    //连接不成功重试
    @Override
    public void onTry(boolean isTry) {
        if (connectDevicedialog.mDialog.isShowing()) {
            connectDevicedialog.mDialog.dismiss();
        }
         OpenAndScan();


    }


    //listview的项点击事件，点击连接
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (deviceList != null && deviceList.size() != 0) {
            sharedUtils.writeString("blue", deviceList.get(position).bleDevice.getName());
            initProgressDialog("正在连接");
            mBleManagerWrapper.ConnectDevices(deviceList.get(position).bleDevice);
        }


    }


    //检查相机权限
    @SuppressLint("CheckResult")
    public void rxPermissionForBlue() {
        final boolean[] permission = {false};
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {

                    initView();
                    OpenAndScan();

                } else {
                    // 权限被拒绝
                    ToastUtils.show(SearchDeviceActivity.this, "拒绝可能导致某些功能无法使用");

                }
            }
        });

    }

    /**
     * 定义的蓝牙数据回调类，这个类用来处理蓝牙的回调数据的
     */

    public class IScanCallbackImpl implements ISyncDataCallback {

        @Override
        public void onBleStateOn() {   //监听蓝牙设备开启

            rippleView.setVisibility(View.VISIBLE);
            TextState(getString(R.string.searching_device), getString(R.string.near_to_phone));
            ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_ring);
            mBleManagerWrapper.ScanDevice();   //蓝牙管理者发送指令进行连接,扫描设备，可能会有多个设备，要进行选择

        }

        //监听蓝牙关闭
        @Override
        public void onBleStateOff() {
            KLog.i("-->> onBleStateOff" + "蓝牙关闭");
           ResetState();
            deviceList.clear();
            appLication.searchModel=null;
            adapter.notifyDataSetChanged();
            EventBus.getDefault().post(new DeviceMessage(battery,false));

        }

        @Override
        public void onBattery(int value) {  //蓝牙电量
            KLog.i("-->> onBattery" + value + "");
            battery=value;
            EventBus.getDefault().post(new DeviceMessage(battery,true));
        }

        //设备序列号
        @Override
        public void onSerialNo(String serialNo) {  //蓝牙下围基序列号
            KLog.i("-->> onSerialNo" + serialNo.toString());
            Log.e("xuliehao",serialNo.toString());
        }

        //蓝牙设备连接成功
        @Override
        public void onConnectionSuccessful(BleDevice device) {
            KLog.i("-->> onConnectionSuccessful" + device.toString());
            dismissProgressDialog();
            if (deviceList != null && deviceList.size() != 0) {
                String blue = sharedUtils.readString("blue");
                if (!TextUtils.isEmpty(blue)) {
                    for (int i = 0; i < deviceList.size(); i++) {
                        if (deviceList.get(i).bleDevice.getName().equals(blue)) {
                            deviceList.get(i).isConnected = true;
                            appLication.searchModel=deviceList.get(i);

                        } else {
                            deviceList.get(i).isConnected = false;
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
                EventBus.getDefault().post(new DeviceMessage(battery,true));
                sharedUtils.writeString("back","1");
                 finish();
            }

        }

        //蓝牙设备连接失败
        @Override
        public void onConnectionFailed() {
            KLog.i("-->> onConnectionFailed" + "设备连接失败");
            if (checkActivityIsRun(SearchDeviceActivity.this))
            connectDevicedialog.initConnectFail();
            dismissProgressDialog();
        }


        //没有发现设备
        @Override
        public void onNotFoundDevice() {

            KLog.i("-->> onNotFoundDevice");
            if (checkActivityIsRun(SearchDeviceActivity.this)&& deviceList.size()==0) {
                connectDevicedialog.initNotFoundDevice();

                for (int i = 0; i < deviceList.size(); i++) {
                    deviceList.get(i).isConnected = false;
                }
                adapter.notifyDataSetChanged();
            }
            TextState(getString(R.string.search_success), getString(R.string.please_select_connect_device));
            rippleView.setVisibility(View.GONE);
            tvSearch.setVisibility(View.VISIBLE);
        }

        //拒绝连接
        @Override
        public void onDisconnect() {
            KLog.i("-->> onDisconnect" + "拒绝连接");
            TextState(getString(R.string.dis_connected), getString(R.string.go_setting_openblue));
            ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_phone);
            rippleView.setVisibility(View.GONE);
            dismissProgressDialog();
            appLication.searchModel=null;
            for (int i = 0; i < deviceList.size(); i++) {
                deviceList.get(i).isConnected = false;
            }
            adapter.notifyDataSetChanged();
            EventBus.getDefault().post(new DeviceMessage(battery,false));

        }

        @Override
        public void onSyncProgressUpdate(int value) {

        }

        @Override
        public void onSyncSuccessful(long startSleep) {  //蓝牙数据同步成功
            KLog.i("-->> onSyncSuccessful" + "数据同步成功" + startSleep);

        }

        @Override
        public void onSyncFailed() {  //蓝牙数据同步失败
            KLog.i("-->> onSyncFailed" + "数据同步失败");
        }

        @Override
        public void onScaning(BleDevice bleDevice) {  //正在搜索中b
            KLog.i("-->> onScaning" + bleDevice);

        }

        @Override
        public void onSetTimeFailed() {
            KLog.i("-->> onSetTimeFailed" + "设置时间失败");
        }

        //数据同步
        @Override
        public void onSyncData() {  //同步数据中

        }

        //蓝牙设备数量
        @Override
        public void DeviceCount(List<BleDevice> list) {  //发现的蓝牙设备数用来显示在列表中
            KLog.i("-->> DeviceCount" + "扫描完成" + list.size());

            TextState(getString(R.string.search_success), getString(R.string.please_select_connect_device));
            rippleView.setVisibility(View.GONE);
            ivDevice.setBackgroundResource(R.drawable.bluetooth_pic_ring);
            tvSearch.setVisibility(View.VISIBLE);
              deviceList.clear();
            if (list != null) {
                if (appLication.searchModel!=null){
                    deviceList.add(appLication.searchModel);
                }
                for (int i = 0; i < list.size(); i++) {
                    SearchModel moel = new SearchModel();
                    moel.bleDevice = list.get(i);
                    moel.isConnected = false;
                    if (!deviceList.contains(moel))
                    deviceList.add(moel);
                }
                adapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedUtils.writeString("back","1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBleManagerWrapper.removeSyncDataCallback(mIScanDataCallback);
    }
}
