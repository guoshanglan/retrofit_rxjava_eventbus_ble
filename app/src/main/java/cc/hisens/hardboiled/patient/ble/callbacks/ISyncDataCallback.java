package cc.hisens.hardboiled.patient.ble.callbacks;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;

import java.util.List;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.ble.callbacks
 * @fileName ISyncDataCallback
 * @date on 2017/7/5 14:57
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 蓝牙数据回调
 */

public interface ISyncDataCallback {
    void onBleStateOn();

    void onBleStateOff();

    void onBattery(int value);

    void onSerialNo(String serialNo);

    void onConnectionSuccessful(BleDevice device);

    void onConnectionFailed();

    void onNotFoundDevice();

    void onDisconnect();

    void onSyncProgressUpdate(int value);

    void onSyncSuccessful(long startSleep);

    void onSyncFailed();

    void onScaning(BleDevice bleDevice);

    void onSetTimeFailed();

    void onSyncData();
    void DeviceCount(List<BleDevice> deviceList);  //回调扫描到的设备数，可以用来展示在列表上





}
