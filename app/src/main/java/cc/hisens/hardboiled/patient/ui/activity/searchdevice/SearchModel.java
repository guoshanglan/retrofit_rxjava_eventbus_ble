package cc.hisens.hardboiled.patient.ui.activity.searchdevice;

import com.clj.fastble.data.BleDevice;

public class SearchModel {
   public BleDevice bleDevice;
   public boolean isConnected=false;

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        return "SearchModel{" +
                "bleDevice=" + bleDevice +
                ", isConnected=" + isConnected +
                '}';
    }
}
