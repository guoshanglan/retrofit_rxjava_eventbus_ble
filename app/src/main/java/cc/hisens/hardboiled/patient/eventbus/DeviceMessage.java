package cc.hisens.hardboiled.patient.eventbus;



//蓝牙设备回调的设备信息类
public class DeviceMessage {

    public int battry; //电量
    public boolean isConnected; //是否连接

    public DeviceMessage(int battry, boolean isConnected) {
        this.battry=battry;
        this.isConnected=isConnected;
    }

    @Override
    public String toString() {
        return "DeviceMessage{" +
                "battry=" + battry +
                ", isConnected=" + isConnected +
                '}';
    }
}
