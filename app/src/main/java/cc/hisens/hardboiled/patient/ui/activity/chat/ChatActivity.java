package cc.hisens.hardboiled.patient.ui.activity.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.dialog.PictureSpinView;
import com.luck.picture.lib.entity.LocalMedia;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.ChatRecyclerAdappter;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.HealthRecords;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.ChatMsgRepoImpl;
import cc.hisens.hardboiled.patient.db.impl.ConversationRepoImpl;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.eventbus.ChatBackEvent;
import cc.hisens.hardboiled.patient.eventbus.OnMessage;
import cc.hisens.hardboiled.patient.eventbus.OnMessageCome;
import cc.hisens.hardboiled.patient.model.ActionOrderEvent;
import cc.hisens.hardboiled.patient.model.ProduceInfoModel;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatPresenter.ChatPresenter;
import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendImageMessageModel;
import cc.hisens.hardboiled.patient.ui.activity.chat.model.SendTextMessageModel;
import cc.hisens.hardboiled.patient.ui.activity.chat.view.ChatView;
import cc.hisens.hardboiled.patient.model.OrderInfoModel;
import cc.hisens.hardboiled.patient.ui.activity.consultion.ConsultTypeActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctor_introduce.DoctorInduceModel;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.utils.Consultiondialog;
import cc.hisens.hardboiled.patient.utils.DateUtils;
import cc.hisens.hardboiled.patient.utils.DoubleClickUtil;
import cc.hisens.hardboiled.patient.utils.KeyboardManager;
import cc.hisens.hardboiled.patient.utils.MediaManager;
import cc.hisens.hardboiled.patient.utils.MessageDialog;
import cc.hisens.hardboiled.patient.utils.PictureSelectUtil;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.RecordButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.realm.RealmList;

//聊天Activity
public class ChatActivity extends BaseActivity implements ChatView, SwipeRefreshLayout.OnRefreshListener, TextWatcher, MessageDialog.DialogCallback {
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回
    @BindView(R.id.tv_title)
    public TextView tvTitle;  //标题
    @BindView(R.id.tv_zhiwei)
    public TextView tvZhiwei; //职位
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout refreshLayout;  //下拉刷新控件
    @BindView(R.id.recyclerView)
    RecyclerView recyclerListview; //聊天聊天列表控件
    @BindView(R.id.ivAudio)
    public ImageView ivAudio; //录音图标
    @BindView(R.id.ivAdd)
    public ImageView ivAdd;  //添加文件图标
    @BindView(R.id.btnAudio)
    public RecordButton recordButton; //录音控件
    @BindView(R.id.et_content)
    public EditText etContent; //输入文本内容
    @BindView(R.id.tv_send)
    public TextView tvSend;  //发送
    @BindView(R.id.rl_shenyuCount)
    RelativeLayout rlShenyuCount;//剩余次数的布局
    @BindView(R.id.tv_end_chat)
    public TextView tvEnd;  //问诊结束
    @BindView(R.id.tv_time_end)
    public TextView tvendTime; //聊天剩余时间
    @BindView(R.id.tv_shenyu_count)
    public TextView tvShenyuCount;  //聊天剩余次数
    @BindView(R.id.ly_chat)
    public LinearLayout lyChat; //聊天框
    @BindView(R.id.continue_consultion)
    public LinearLayout lyContinue_consultion; //继续咨询
    @BindView(R.id.tv_servicepackage_count)
    public TextView tvPackageCount; //服务包剩余数量
    @BindView(R.id.iv_loading)
    public PictureSpinView loading;  //loading图标
    public Animation animation;
    public ChatRecyclerAdappter chatAdapter;
    public ChatPresenter chatPresenter;
    public boolean isAudio = false, isAdd = false;
    public String MyaudioPath;
    public List<LocalMedia> imageList = new ArrayList<>();
    public List<ChatMessage> messageList = new ArrayList<>(); //消息集合
    public String UserName, doctorid, avator;
    public int level;  //医生等级
    public int count = 5; //当前会话的会话次数,默认为5次
    public long time = 48 * 60 * 60; //默认剩余时间
    public int voiceTime;
    public ConversationRepoImpl conversationRepo = new ConversationRepoImpl();; //会话消息操作对象
    public boolean isPay;  //是否是从支付那边跳转过来的
    public Gson gson = new Gson();
    public OrderInfoModel orderInfoModel; //用户账单信息类
    public Consultiondialog consultiondialog;
    public MessageDialog dialog;  //消费服务包次数咨询
    public float onePackagePrice, morePackagePrice; //一个资费包的价格，多个资费包的价格
    public LinearLayoutManager layoutManager;
    public ChatMsgRepoImpl chatMsgRepo = new ChatMsgRepoImpl();
    public String username,age;


    //订阅的evnetbus回调是否有消息到来,需要更新列表
    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(OnMessageCome message) {
        if (message != null) {
            if (message.chatMessage.getReceiverId().equals(doctorid)) {
                appLication.indexPlay=-1;
                messageList.add(message.chatMessage);
                chatAdapter.notifyDataSetChanged();
                layoutManager.setStackFromEnd(true);
                layoutManager.scrollToPositionWithOffset(chatAdapter.getItemCount() - 1, Integer.MIN_VALUE);
                //消息全部改为已读
                chatMsgRepo.setDoctorUnreadMessageState(doctorid, true);
                EventBus.getDefault().post(new OnMessage(true));
            }
        }
    }
    //订阅的事件是否是从激活订单跳转过来的
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDeviceMessage(ActionOrderEvent message) {
        if (message != null) {
            appLication.indexPlay=-1;
            isPay = message.isActionOrder;
            getUserOrderInfo();//获取用户账单信息
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initProgressDialog("加载中...");
        Intent intent = getIntent();
        doctorid = intent.getStringExtra("id");
        isPay = intent.getBooleanExtra("ispay", false);

        getUserOrderInfo();//获取用户账单信息
    }



    @OnClick({R.id.ivAudio, R.id.ivAdd, R.id.tv_back, R.id.tv_send, R.id.continue_consultion})
    public void OnClick(View view) {
        if (DoubleClickUtil.isFastClick())
            return;
        switch (view.getId()) {
            case R.id.ivAudio: //录音
                if (rxPermissionForRecord() == false) {
                    return;
                }
                isAudio = !isAudio;
                if (isAudio) {
                    recordButton.setVisibility(View.VISIBLE);
                    etContent.setVisibility(View.GONE);
                    KeyboardManager.hideKeyboard(etContent);
                    ivAudio.setBackgroundResource(R.drawable.talk_btn_keyboard);

                } else {
                    recordButton.setVisibility(View.GONE);
                    etContent.setVisibility(View.VISIBLE);
                    KeyboardManager.showKeyboard(etContent);
                    ivAudio.setBackgroundResource(R.drawable.talk_btn_voice);
                }

                isAdd = !isAudio;
                break;

            case R.id.ivAdd: //添加文件
                PictureSelectUtil.OpenPicture(this, 9, true);
                isAdd = !isAdd;
                if (isAdd) {

                    KeyboardManager.hideKeyboard(etContent);
                } else {
                    KeyboardManager.showKeyboard(etContent);

                }
                recordButton.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                isAudio = !isAdd;
                break;


            case R.id.tv_send:  //发送文本消息
                if (!TextUtils.isEmpty(etContent.getText().toString())) {
                    tvSend.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading.startAnimation(animation);
                    chatPresenter.SendTextMessage();
                }
                break;

            case R.id.tv_back:
                EventBus.getDefault().post(new ChatBackEvent(true));
                finish();
                break;

            case R.id.continue_consultion:  //继续咨询
                if (orderInfoModel.getDatas().getUnused_count() != 0) {
                    dialog.initDialog("确认使用1次图文问诊服务继续咨询吗？", "继续咨询", "取消");
                } else {
                    consultiondialog.inintDialog();
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new ChatBackEvent(true));
    }

    //检查相机权限
    @SuppressLint("CheckResult")
    public boolean rxPermissionForRecord() {
        final boolean[] permission = {false};

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {

                    permission[0] = true;

                } else {
                    // 权限被拒绝
                    permission[0] = false;
                    ToastUtils.show(ChatActivity.this, "拒绝可能导致某些功能无法使用");

                }
            }
        });
        return permission[0];
    }


    //回调图片资源
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:  //选择图片
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    adapter.setList(selectList);
//                    adapter.notifyDataSetChanged();
                    if (selectList.get(0).isCompressed())
                        KLog.i("图片选择" + selectList.get(0).getCompressPath().toString());
                    if (selectList != null) {
                        initProgressDialog("正在发送..");
                        imageList.addAll(selectList);
                        chatPresenter.SendImageMessage();
                    }
                    break;

                case PictureConfig.CAMERA:  //照相机
                    List<LocalMedia> photoCamera = PictureSelector.obtainMultipleResult(data);
                    if (photoCamera.get(0).isCompressed())
                        KLog.i("照相机" + photoCamera.get(0).getCompressPath().toString());
                    if (photoCamera != null) {
                        imageList.addAll(photoCamera);
                        chatPresenter.SendImageMessage();
                    }
                    break;

            }

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.chat_activity_layout;
    }

    @Override
    public BasePresenter getPresenter() {
        if (chatPresenter == null) {
            chatPresenter = new ChatPresenter();
        }
        return chatPresenter;
    }

    //文本信息
    @Override
    public String TextMessage() {

        return etContent.getText().toString().replace(" ", "");
    }

    //图片信息
    @Override
    public List<LocalMedia> Image() {
        return imageList;
    }

    //录音信息
    @Override
    public String AudioPath() {
        return MyaudioPath;
    }

    @Override
    public int AudioTime() {
        return voiceTime;
    }

    //上下文对象
    @Override
    public Context getContext() {
        return this;
    }

    //发送消息的userid
    @Override
    public String to_User_id() {
        return doctorid;
    }

    //发送文本成功
    @Override
    public void SendTextSuccessFul(SendTextMessageModel messageModel) {
        if (messageModel != null) {
            etContent.setText("");
            stopAnimation();
            tvSend.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            UpdateUI(messageModel.getExpire_date(), messageModel.getCurrent_index());  //更新界面UI
            SaveMessage(messageModel, 0); //存储消息
        }

    }

    //发送图片成功
    @Override
    public void SendImageSuccessFul(SendImageMessageModel messageModel) {
        dismissProgressDialog();
        if (messageModel != null) {
            imageList.clear();
            UpdateUI(messageModel.getDatas().get(0).getExpire_date(), messageModel.getDatas().get(0).getCurrent_index());  //更新界面UI
            SaveImageMessage(messageModel, 1);
        }

    }

    //发送语音消息成功
    @Override
    public void SendAudioSuccessFul(SendTextMessageModel messageModel) {
        if (messageModel != null) {
            dismissProgressDialog();
            UpdateUI(messageModel.getExpire_date(), messageModel.getCurrent_index());  //更新界面UI
            SaveMessage(messageModel, 2);
        }

    }


    //发送失败
    @Override
    public void SendFailedError(String errormessage) {
        dismissProgressDialog();
        stopAnimation();
        if (errormessage.equals("未授权访问")) {  //跳转到登录页面
            ActivityCollector.finishAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN, false);
            new UserRepositoryImpl().DeleteAll();
            startActivity(new Intent(this, GetVoliatCodeActivity.class));
        } else {
            ShowToast("网络错误，请稍后再试");
        }

    }

    //下拉刷新消息
    @Override
    public void onRefresh() {

        getUserOrderInfo();//获取用户账单信
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            tvSend.setClickable(true);
        } else {
            tvSend.setClickable(false);

        }

    }


    //如果有效期时间到了或者剩余次数小于0更新界面状态，可能不能进行聊天了
    public void UpdateUI(int expire_date, int currentIndex) {
        if (expire_date - System.currentTimeMillis() / 1000 > 0 && 5 - currentIndex > 0) {
            tvEnd.setVisibility(View.GONE);
            rlShenyuCount.setVisibility(View.VISIBLE);
            lyChat.setVisibility(View.VISIBLE);
            lyContinue_consultion.setVisibility(View.GONE);
            tvShenyuCount.setText(count + "");
            tvendTime.setText(TimeUtils.TimeSpace(expire_date));
            tvShenyuCount.setText(5 - currentIndex + "");

        } else {
            tvEnd.setVisibility(View.VISIBLE);
            rlShenyuCount.setVisibility(View.GONE);
            lyChat.setVisibility(View.GONE);
            lyContinue_consultion.setVisibility(View.VISIBLE);
        }
    }


    //存储会话消息
    public void SaveMessage(SendTextMessageModel messageModel, int type) {
        //会话消息
        Conversation conversation = new Conversation();
        ChatMessage chatMessage = new ChatMessage();
        conversation.setFriendName(UserName);
        conversation.setFriendId(doctorid);
        conversation.setRead(true);  //是否阅读
        conversation.setLevel(level);  //医生等级
        conversation.setImageUrl(avator);  //头像
        conversation.setLastMessageTime(messageModel.getCreate_time());
        conversation.setCountMessage(count);  //剩余次数
        conversation.setTime(time);  //剩余时间
        //聊天消息
        chatMessage.setMessageType(type);//消息类型
        chatMessage.setMsgIndex(messageModel.getMsg_index());
        chatMessage.setRead(true);
        chatMessage.setSend(true);
        chatMessage.setSenderId(String.valueOf(messageModel.getFrom_user_id()));
        chatMessage.setReceiverId(String.valueOf(messageModel.getTo_user_id()));
        if (type == 0) {
            chatMessage.setTextMessage(messageModel.getText());
        } else if (type == 2) {
            chatMessage.setVoicePath(messageModel.getOrigin());
        }
        chatMessage.setTimestamp(messageModel.getCreate_time());
        chatMessage.setVoiceTime(voiceTime);
        conversation.setLastMessage(chatMessage);
        updateMsg(chatMessage);
        conversationRepo.saveConversation(conversation);
        EventBus.getDefault().post(new OnMessage(true));


    }


    //存储发送图片消息
    public void SaveImageMessage(SendImageMessageModel messageModel, int type) {
        //会话消息
        Conversation conversation = new Conversation();
        ChatMessage chatMessage = new ChatMessage();
        conversation.setFriendName(UserName);
        conversation.setFriendId(doctorid);
        conversation.setRead(true);  //是否阅读
        conversation.setImageUrl(avator);  //头像
        conversation.setLastMessageTime(messageModel.getDatas().get(0).getCreate_time());
        conversation.setCountMessage(count);  //剩余次数
        conversation.setTime(time);  //剩余时间
        conversation.setLevel(level); //医生等级
        //聊天消息
        chatMessage.setMessageType(type);//消息类型
        chatMessage.setRead(true);
        chatMessage.setSend(true);
        chatMessage.setMsgIndex(messageModel.getDatas().get(0).getMsg_index());
        chatMessage.setSenderId(String.valueOf(messageModel.getDatas().get(0).getFrom_user_id()));
        chatMessage.setReceiverId(String.valueOf(messageModel.getDatas().get(0).getTo_user_id()));

        RealmList<String> orginList = new RealmList<>();
        RealmList<String> thumbList = new RealmList<>();
        orginList.clear();
        thumbList.clear();
        for (int i = 0; i < messageModel.getDatas().size(); i++) {
            orginList.add(messageModel.getDatas().get(i).getOrigins());
            thumbList.add(messageModel.getDatas().get(i).getThumbs());
            chatMessage.setImageWidth(messageModel.getDatas().get(0).getWidth());
            chatMessage.setImageHeight(messageModel.getDatas().get(0).getHeight());
        }

        chatMessage.setImagePath(orginList);
        chatMessage.setThumbImagePath(thumbList);

        chatMessage.setTimestamp(messageModel.getDatas().get(0).getCreate_time());

        conversation.setLastMessage(chatMessage);

        updateMsg(chatMessage);
        conversationRepo.saveConversation(conversation);

        EventBus.getDefault().post(new OnMessage(true));
    }


    //更新列表消息
    private void updateMsg(final ChatMessage mMessgae) {
        recyclerListview.scrollToPosition(chatAdapter.getItemCount());
        //更新单个子条目
        messageList.add(mMessgae);
        chatAdapter.addData(messageList);
        chatAdapter.notifyItemChanged(messageList.size());
    }


    //关闭动画
    public void stopAnimation() {
        loading.clearAnimation();
    }


    //获取用户订单信息
    public void getUserOrderInfo() {
        initProgressDialog("");
        Map<String, Object> params = new HashMap<>();
        params.put("doctor_id", doctorid);
        RequestUtils.get(this, Url.getUserOrderInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result == 0) {
                    orderInfoModel = gson.fromJson(gson.toJson(result), OrderInfoModel.class);
                    getDoctorInfo(doctorid);

                } else {
                    ToastUtils.show(ChatActivity.this, result.message);
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
            }
        });


    }


    //获取医生的个人信息接口
    //查询医生信息
    public void getDoctorInfo(String doctorId) {

        List<Integer> params = new ArrayList<>();
        params.add(Integer.valueOf(doctorId));

        RequestUtils.get3(this, Url.DoctorInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result == 0) {
                    DoctorInduceModel model = gson.fromJson(gson.toJson(result), DoctorInduceModel.class);
                    //更新医生信息
                    Conversation conversation = conversationRepo.getConversation(doctorId);

                    Doctor doctor = model.getDatas().get(0);
                    avator = doctor.getHead_url();
                    UserName = doctor.getName();
                    level = doctor.getLevel();

                    //判断是否不相等，那么要更新信息
                    if (conversation!=null) {
                        if (!doctor.getHead_url().equals(conversation.getImageUrl()) || !doctor.getName().equals(conversation.getFriendName()) || doctor.getLevel() != conversation.getLevel()) {

                            conversationRepo.UpdateConversionInfo(doctorId, avator, UserName, level);  //更新信息
                            EventBus.getDefault().post(new OnMessage(true));
                        }
                    }
                    if (!TextUtils.isEmpty(model.getDatas().get(0).getOnce_product_no())&&!TextUtils.isEmpty(model.getDatas().get(0).getPack_product_no())) {
                        getPackInfo(model.getDatas().get(0).getOnce_product_no(), model.getDatas().get(0).getPack_product_no());
                    }else{
                        getHealthRecord();
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


    //获取资费包信息，资费包信息用来展示弹框的价格
    public void getPackInfo(String once_product_no, String pack_product_no) {
        List<String> params = new ArrayList<>();
        params.add(once_product_no);
        params.add(pack_product_no);
        RequestUtils.get2(this, Url.getProduct_Info, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result == 0) {
                    ProduceInfoModel model = gson.fromJson(gson.toJson(result), ProduceInfoModel.class);
                    for (int i = 0; i < model.getDatas().size(); i++) {
                        if (model.getDatas().get(i).getProduct_no().equals(once_product_no)) {
                            onePackagePrice = (float) (model.getDatas().get(i).getFee()*0.01);
                        } else if (model.getDatas().get(i).getProduct_no().equals(pack_product_no)) {
                            morePackagePrice = (float) (model.getDatas().get(i).getFee()*0.01);
                        }
                    }

                     getHealthRecord();


                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
            }
        });
    }


    //获取个人健康信息
    @SuppressLint("CheckResult")
    private void getHealthRecord() {
//
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", UserConfig.UserInfo.getUid());
        RequestUtils.get(this, Url.UpLoadUserInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result == 0) {
                    Gson gson = new Gson();
                    HealthRecords mHealthRecord = gson.fromJson(gson.toJson(result), HealthRecords.class);
                    username=mHealthRecord.getDatas().getName();
                    age= String.valueOf(DateUtils.getAge(
                            new Date(TimeUtils.toMillis(mHealthRecord.getDatas().getBirthday()))));
                    InitData(orderInfoModel);
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);


            }
        });
    }


    //初始化数据
    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    private void InitData(OrderInfoModel model) {
        if (isPay) {  //是否是从支付接口跳转过来的，如果是就需要存储下来
            if (appLication.conversation != null) {
                for (int i=0;i<model.getDatas().getList().size();i++){
                    if (model.getDatas().getList().get(i).getExpire_date()!=0){
                        appLication.conversation.getLastMessage().setMsgIndex(model.getDatas().getList().get(i).getMsg_index().get(0));
                    }
                }
                conversationRepo.saveConversation(appLication.conversation);
                EventBus.getDefault().post(new OnMessage(true));
                isPay = false;
            }
        }
        if (model.getDatas().getList().size() == 0) {
            tvEnd.setVisibility(View.VISIBLE);
            rlShenyuCount.setVisibility(View.GONE);
            lyChat.setVisibility(View.GONE);
            lyContinue_consultion.setVisibility(View.VISIBLE);
            tvPackageCount.setVisibility(View.GONE);
        } else {
            //查询聊天剩余时间
            for (int i = 0; i < model.getDatas().getList().size(); i++) {
                OrderInfoModel.DatasBean.ListBean listBean = model.getDatas().getList().get(i);
                if (listBean.getExpire_date() != 0) {
                    count = 5 - listBean.getCurrent_index();
                    UpdateUI(listBean.getExpire_date(), listBean.getCurrent_index());  //更新界面UI
                } else if (model.getDatas().getUse_count() == 0) {
                    tvEnd.setVisibility(View.VISIBLE);
                    rlShenyuCount.setVisibility(View.GONE);
                    lyChat.setVisibility(View.GONE);
                    lyContinue_consultion.setVisibility(View.VISIBLE);
                    if (model.getDatas().getUnused_count() != 0) {
                        tvPackageCount.setVisibility(View.VISIBLE);
                        tvPackageCount.setText("（还剩" + model.getDatas().getUnused_count() + "次图文问诊服务）");
                    } else {
                        tvPackageCount.setVisibility(View.GONE);
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(UserName)) {
            tvTitle.setText(UserName);
        }

        if (level == 1) {
            tvZhiwei.setText("主任医师");
        } else if (level == 2) {
            tvZhiwei.setText("副主任医师");
        } else if (level == 3) {
            tvZhiwei.setText("主治医师");
        } else if (level == 4) {
            tvZhiwei.setText("住院医师");
        } else if (level == 5) {
            tvZhiwei.setText("助理医师");
        }

        refreshLayout.setOnRefreshListener(this);
        chatAdapter = new ChatRecyclerAdappter(this, avator, doctorid,username,age,appLication);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerListview.setLayoutManager(layoutManager);
        recyclerListview.setAdapter(chatAdapter);
        animation = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.roate_anim);

        chatMsgRepo.getChatMessageList(doctorid).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ChatMessage>>() {
            @Override
            public void accept(List<ChatMessage> chatMessages) throws Exception {
                if (chatMessages != null) {
                    messageList.clear();
                    messageList.addAll(chatMessages);
                    chatMsgRepo.setDoctorUnreadMessageState(doctorid, true);
                    chatAdapter.addData(messageList);
                    chatAdapter.notifyDataSetChanged();
                    if (chatAdapter.getItemCount() > 4) {
                        layoutManager.setStackFromEnd(true);
                        layoutManager.scrollToPositionWithOffset(chatAdapter.getItemCount() - 1, Integer.MIN_VALUE);
                    } else {
                        recyclerListview.scrollToPosition(chatAdapter.getItemCount() - 1);
                    }

                }


            }
        });

        //消息全部改为已读
        chatMsgRepo.setDoctorUnreadMessageState(doctorid, true);
        EventBus.getDefault().post(new OnMessage(true));

        //   添加动画
        recyclerListview.setItemAnimator(new DefaultItemAnimator());

        etContent.addTextChangedListener(this);
        //点击空白区域关闭键盘
        recyclerListview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardManager.hideKeyboard(etContent);
                return false;
            }
        });

        //底部布局弹出,聊天列表上滑
        recyclerListview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerListview.post(new Runnable() {
                        @Override
                        public void run() {
                            if (chatAdapter.getItemCount() > 0) {
                                recyclerListview.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                            }
                        }
                    });
                }
            }
        });

        (recordButton).setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                KLog.i("录音结束回调路径：" + audioPath + "时间:" + time);
                File file = new File(audioPath);
                if (file.exists()) {
                    KLog.i("文件：" + file.toString());
                }
                if (!TextUtils.isEmpty(audioPath)) {
                    voiceTime = time;
                    MyaudioPath = audioPath;
                    initProgressDialog("正在发送..");
                    chatPresenter.SendAudio();
                }
            }
        });
        consultiondialog = new Consultiondialog(this, UserName, avator, Integer.parseInt(doctorid), level, onePackagePrice, morePackagePrice, orderInfoModel, 2);
        dialog = new MessageDialog(this);
        dialog.initCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appLication.indexPlay=-1;
        MediaManager.reset();
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        appLication.indexPlay=-1;
        MediaManager.reset();
    }

    @Override
    public void OnSure(boolean sure) {
        Intent intent = new Intent(this, ConsultTypeActivity.class);
        intent.putExtra("id", Integer.parseInt(doctorid));
        intent.putExtra("orderInfo", orderInfoModel);
        startActivity(intent);
    }
}
