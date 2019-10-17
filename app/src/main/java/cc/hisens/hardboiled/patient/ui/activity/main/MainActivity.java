package cc.hisens.hardboiled.patient.ui.activity.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.impl.ChatMsgRepoImpl;
import cc.hisens.hardboiled.patient.db.impl.ConversationRepoImpl;

import cc.hisens.hardboiled.patient.eventbus.OnMessage;
import cc.hisens.hardboiled.patient.eventbus.OnMessageCome;
import cc.hisens.hardboiled.patient.eventbus.OnWebviewFile;
import cc.hisens.hardboiled.patient.retrofit.Url;

import cc.hisens.hardboiled.patient.ui.activity.main.model.AppInfoResult;
import cc.hisens.hardboiled.patient.ui.activity.main.present.AppInfoPresenter;
import cc.hisens.hardboiled.patient.ui.activity.main.view.AppcheckInfoView;
import cc.hisens.hardboiled.patient.ui.fragment.monitor.MonitorFragment;
import cc.hisens.hardboiled.patient.ui.fragment.me.MeFragment;
import cc.hisens.hardboiled.patient.ui.fragment.doctor.DoctorFragment;
import cc.hisens.hardboiled.patient.utils.AppUpdateUtils;
import cc.hisens.hardboiled.patient.utils.DownLoadEdBean;
import cc.hisens.hardboiled.patient.utils.GPSUtils;
import cc.hisens.hardboiled.patient.utils.ReadFileUtil;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.websocket.ChatClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


//主要的Activity
public class MainActivity extends BaseActivity implements AppcheckInfoView {
    @BindView(R.id.fl_container)
    FrameLayout myFrameLayout;  //用来展示fragment的
    //底部四个按钮,ButterKnife一次性注解多个
    @BindViews({R.id.rbtn_monitor, R.id.rbtn_doctor, R.id.rbtn_me})
    public List<RadioButton> buttonList;
    @BindView(R.id.tv_doctor_message_count)
    TextView tvDoctormessageCount;  //医生消息

    private Fragment firstFragment, secondFragment;
    private MeFragment meFragment;
    private Fragment[] fragments;
    private FragmentManager fragmentmanager;
    private FragmentTransaction ft;
    private int currentTabIndex;  //当前切换的是第几个fragment
    private static Boolean mIsExit = false;
    private AppInfoPresenter appInfoPresenter;   //检查APP版本更新的第三方桥梁present
    private ChatClient mChatClient;  //webSocket客户端
  
    private ConversationRepoImpl conversationRepo;
    private ChatMsgRepoImpl chatMsgRepo=new ChatMsgRepoImpl();
    public GPSUtils gpsUtils;
    public Location location;




    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnMessageCome message) {
        if (message != null) {
          getUnreadConversion();
        }

    }

    //订阅的evnetbus回调是否有消息到来,需要重新查找数据库，排序
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessage(OnMessage message) {
        if (message != null) {
            if (message != null) {
                getUnreadConversion();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        grantPermissions();

        ConnectedWebSocket();   //进行websocket的长连接接收消息


       //appInfoPresenter.CheckAppUpdate(); //检查App版本更

//        DownLoadEdBean bean = new DownLoadEdBean(new DownLoadEdBean.CallBack() {
//            @Override
//            public void Success(File file) {
//                Log.e("下载", "下载成功" + file.toString());
//                try {
//                    String js = ReadFileUtil.fileRead("ed_file.js");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void Fail(String err) {
//                if (err.equals("无更新")) {
//                    try {
//                        String js = ReadFileUtil.fileRead("ed_file.js");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//        });
//
//        bean.checkEdUpdate(this, sharedUtils);

    }


    //初始化控件和界面
    private void initView() {
        conversationRepo=new ConversationRepoImpl(); //用来操作数据库的
        firstFragment = new MonitorFragment();
        secondFragment = new DoctorFragment();
        meFragment = new MeFragment();
        fragments = new Fragment[]{firstFragment, secondFragment, meFragment};
        fragmentmanager = getSupportFragmentManager();
        ft = fragmentmanager.beginTransaction();
        buttonList.get(0).setChecked(true);
        ft.add(R.id.fl_container, firstFragment);
        ft.show(firstFragment).commit();
        getUnreadConversion();


    }

    //获取未读消息的个数
    @SuppressLint("CheckResult")
    public void getUnreadConversion(){
       final int[] unreadCount = {0}; //未读消息的数量
        conversationRepo.getConversations().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Conversation>>() {
            @Override
            public void accept(List<Conversation> conversations) throws Exception {
                if (conversations!=null){

                    for (int i=0;i<conversations.size();i++){

                        chatMsgRepo.getChatMessageList(conversations.get(i).getFriendId()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ChatMessage>>() {
                            @Override
                            public void accept(List<ChatMessage> chatMessages) throws Exception {

                                if (chatMessages != null) {
                                  for (int a=0;a<chatMessages.size();a++){
                                      if (chatMessages.get(a).isRead()==false){
                                          unreadCount[0]++;
                                      }
                                  }

                                    if (unreadCount[0] !=0){
                                        tvDoctormessageCount.setVisibility(View.VISIBLE);
                                        if (unreadCount[0] >99){
                                            tvDoctormessageCount.setText("...");
                                        }else{
                                            tvDoctormessageCount.setText(unreadCount[0] +"");
                                        }

                                    }else{
                                        tvDoctormessageCount.setVisibility(View.GONE);
                                    }


                                }else{
                                    tvDoctormessageCount.setVisibility(View.GONE);
                                }


                            }
                        });

                    }



                }else{
                    tvDoctormessageCount.setVisibility(View.GONE);
                }
            }
        });
    }





    //Butterknife 注解点击事件,进行底部Fragment的替换
    @OnClick({R.id.rbtn_monitor, R.id.rbtn_doctor, R.id.rbtn_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbtn_monitor:
                SwitchSkip(0);
                break;
            case R.id.rbtn_doctor:

                SwitchSkip(1);

                break;

            case R.id.rbtn_me:
                SwitchSkip(2);
                break;
        }
    }


    //切换底部tab页面
    private void SwitchSkip(int index) {
        ft = fragmentmanager.beginTransaction();
        if (currentTabIndex != index) {
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fl_container, fragments[index]);
            }
            ft.hide(fragments[currentTabIndex]).show(fragments[index]).commit();

            for (int i = 0; i < buttonList.size(); i++) {
                if (i == index) {
                    buttonList.get(index).setChecked(true);
                    buttonList.get(index).setTextColor(Color.parseColor("#2ab5d7"));

                } else {
                    buttonList.get(i).setChecked(false);
                    buttonList.get(i).setTextColor(Color.parseColor("#999999"));

                }
            }

            currentTabIndex = index;
        }
    }


    //动态申请蓝牙权限，判断是否含有蓝牙权限，这里显示的是位置信息
    @SuppressLint("CheckResult")
    private void grantPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) || !rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) || !rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE)) {
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean isGranted) {
                            if (isGranted) {
                                // 判断蓝牙所需的权限是否已经有了是否开启
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        gpsUtils = new GPSUtils(MainActivity.this,appLication);
                                    }
                                }.start();

                            } else {
                                ShowToast("拒绝此权限可能导致某些功能无法正常使用，请到设置中开启此权限！");
                            }
                        }
                    });
        }else{
            // 判断蓝牙所需的权限是否已经有了是否开启
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    gpsUtils = new GPSUtils(MainActivity.this,appLication);
                }
            }.start();

        }
    }


    //回调图片资源,这个方法是回调我的页面webiview选择图片回调回来的图片路径
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:  //选择图片
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    EventBus.getDefault().post(new OnWebviewFile(selectList.get(0).getPath()));
                    break;

                case PictureConfig.CAMERA:  //照相机
                    List<LocalMedia> photoCamera = PictureSelector.obtainMultipleResult(data);
                    EventBus.getDefault().post(new OnWebviewFile(photoCamera.get(0).getPath()));

                    break;

            }

        }
    }




    // 菜单、返回键响应
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(currentTabIndex==2) {
                if(keyCode==KeyEvent.KEYCODE_BACK&&meFragment.webView.canGoBack()){
                    meFragment.webView.goBack();
                    return true;
                }else{
                    exitByDoubleClick();//这是退出方法
                }

            }else {
                exitByDoubleClick();//这是退出方法
            }

        }
        return false;
    }

//    //监控系统返回键，按两次退出当前应用
//    @Override
//    public void onBackPressed() {
//        exitByDoubleClick();
//    }


    private void exitByDoubleClick() {
        if (!mIsExit) {
            mIsExit = true; // 准备退出
            ToastUtils.show(this, R.string.double_click_exit);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityCollector.finishAll();
            System.exit(0);
        }
    }


    //用websocket与服务器进行长连接，目前服务器还没开
    public void ConnectedWebSocket() {
        URI uri = null;
        try {
            uri = URI.create(Url.WEB_SOCKET_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mChatClient = ChatClient.getInstance(uri);
        mChatClient.connect();
    }


    @Override
    protected int getLayoutId() {

        return R.layout.activity_main;
    }


    //返回一个检查APP版本的present操作对象
    @Override
    public BasePresenter getPresenter() {
        if (appInfoPresenter == null) {
            appInfoPresenter = new AppInfoPresenter();
        }
        return appInfoPresenter;
    }


    //返回上下文对象
    @Override
    public Context getContext() {
        return this;
    }


    //查询APP版本信息有更新
    @Override
    public void setCheckUpdateInfo(AppInfoResult appInfoResult) {
        if (appInfoResult != null && appInfoResult.getStatus() != 0)
            AppUpdateUtils.getGetInstence().popUpdateDialog(appInfoResult.getContent(), appInfoResult.getShop_url(), appInfoResult.getStatus(), this);
    }


    //Session失效，需要重新登录
    @Override
    public void setFailedError(String str) {
        Log.e("错误", str);
        ToastUtils.show(this, str);
        navigateToLogin();  //跳转到登录页面

    }


    //内存回收，否则可能会造成内存溢出oom
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mChatClient.cancelConnectTimer();
        mChatClient.cancelPingTimer();
        mChatClient = null; //释放对象
    }
}
