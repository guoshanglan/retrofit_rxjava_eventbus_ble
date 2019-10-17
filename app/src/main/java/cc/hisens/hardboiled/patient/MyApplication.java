package cc.hisens.hardboiled.patient;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.StrictMode;


import com.tencent.bugly.crashreport.CrashReport;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.Ed;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.ui.activity.searchdevice.SearchModel;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import io.realm.Realm;

public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;
    private static Context mContext;
    public Location location;
    public String cityLocation = "全国";
    public SearchModel searchModel;  //存储连接的蓝牙设备进行信息传递
    public Doctor doctor;  //医生数据
    public Ed ed;  //ed数据
    public ChatMessage message; //聊天消息
    public Conversation conversation; //会话消息
    private BLEManagerWrapper bleManagerWrapper;
    public int indexPlay=-1;  //标记当前播放语音的index,在聊天中使用

    public static BLEManagerWrapper getBLEManagerWrapper() {
        return BLEManagerWrapper.getInstance();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.resetDensity(this);  //初始化屏幕适配的工具类
        init();
    }


    //初始化各种数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init() {

        Realm.init(this);   //数据库初始化
        UserConfig.init(this);
        instance = this;
        mContext = getApplicationContext();

        //解决7.0手机拍照的uri问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        CrashReport.initCrashReport(getApplicationContext(), "a4af3eeb50", true);  //tencent bugly的初始化
        bleManagerWrapper = BLEManagerWrapper.getInstance();
        bleManagerWrapper.initialize(this);

    }


    //返回application对象
    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }


    //应用被销毁
    @Override
    public void onTerminate() {
        super.onTerminate();
        getBLEManagerWrapper().recycle();
    }


}
