package cc.hisens.hardboiled.patient.ui.activity.consultion;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

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
import cc.hisens.hardboiled.patient.adapter.AddPictureAdapter;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.ChatMessage;
import cc.hisens.hardboiled.patient.db.bean.Conversation;
import cc.hisens.hardboiled.patient.db.bean.Doctor;
import cc.hisens.hardboiled.patient.db.bean.HealthRecords;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.eventbus.ChatBackEvent;
import cc.hisens.hardboiled.patient.eventbus.HealthMessage;
import cc.hisens.hardboiled.patient.model.ActionOrderEvent;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.PayActivity;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctor_introduce.DoctorInduceModel;
import cc.hisens.hardboiled.patient.model.OrderInfoModel;
import cc.hisens.hardboiled.patient.ui.activity.healthrecord.PersonHealthRecordActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.preview_photo.PreviewPictureActivity;
import cc.hisens.hardboiled.patient.utils.PictureSelectUtil;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.CircleImageView;
import cc.hisens.hardboiled.patient.wideview.MyGridView;
import io.realm.RealmList;

//图文问诊方式

public class ConsultTypeActivity extends BaseActivity implements TextWatcher, AddPictureAdapter.CallBack, ConsultTypeView {
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_next)
    public TextView tvNext; //下一步
    @BindView(R.id.iv_doctor_head)
    public CircleImageView ivDoctor; //头像
    @BindView(R.id.tv_doctor_name)
    public TextView tvDoctorName; //医生姓名
    @BindView(R.id.tv_zhiwei)
    public TextView tvZhiwei; //职位
    @BindView(R.id.rl_myFile)
    public RelativeLayout rlMyfile; //我的档案
    @BindView(R.id.tv_myfile)
    public TextView tvMyfile;
    @BindView(R.id.et_consultion)
    public EditText etConsultion;//输入文字
    @BindView(R.id.tv_count)
    public TextView tvCount;  //文字字数
    @BindView(R.id.gv_addpic)
    public MyGridView gridView;  //列表
    @BindView(R.id.iv_addpic)
    public ImageView ivAddPic; //添加图片
    @BindView(R.id.tv_tips)
    public TextView tvTips;
    public List<String> list;
    public List<String> listPath;  //原路径
    public AddPictureAdapter adapter;
    public String doctorName, headUrl, workPlace; //姓名，头像
    public int level, doctorId; //医生id,医生等级
    public String product_no;//资费包编号
    public ConsultPresenter presenter;
    public OrderInfoModel orderInfoModel; //用户账单model类，通过这个类来判断是服务包的使用还是要继续支付新订单
    public String price;
    public int type;
    public int consultType; //问诊方式
    public boolean MyFileComplete=false;
    public   Doctor doctor;


    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(ChatBackEvent event) {
        if (event != null && event.isChatBack) {
            finish();
        }

    }

    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(HealthMessage event) {
        if (event != null && event.isHealth) {
            getHealthRecord();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        doctorId = getIntent().getIntExtra("id", 0);
        price = getIntent().getStringExtra("price");
        type=getIntent().getIntExtra("type",1);
        consultType=getIntent().getIntExtra("consulttype",1);
        orderInfoModel = (OrderInfoModel) getIntent().getSerializableExtra("orderInfo");
        initProgressDialog("");
        getData(doctorId);
    }


    public void getData(int doctorId) {

        List<Integer> params = new ArrayList<>();
        params.add(doctorId);

        RequestUtils.get3(this, Url.DoctorInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result == 0) {
                    Gson gson = new Gson();
                    DoctorInduceModel model = gson.fromJson(gson.toJson(result), DoctorInduceModel.class);
                     doctor = model.getDatas().get(0);
                    appLication.doctor = doctor;
                    if (consultType==1) {
                        product_no = doctor.getOnce_product_no();
                    }else if (consultType==2){
                        product_no=doctor.getPack_product_no();
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


    private void initView(Doctor model) {
        doctorName = model.getName();
        headUrl = model.getHead_url();
        level = model.getLevel();

        for (int i = 0; i < model.getWorkplaces().size(); i++) {
            if (model.getWorkplaces().get(i).getIndex() == 1) {
                workPlace = model.getWorkplaces().get(i).getName();
                break;
            }
        }
        tvDoctorName.setText(doctorName);
        Glide.with(this).load(headUrl).placeholder(R.drawable.doctor_head_100).into(ivDoctor);
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
        tvTips.setText("你上传的图片仅对" + doctorName + "可见，可上传9张图片");

        list = new ArrayList<>();
        listPath = new ArrayList<>();
        adapter = new AddPictureAdapter(this, list);
        gridView.setAdapter(adapter);
        gridView.setHaveScrollbar(false);
        etConsultion.addTextChangedListener(this);
        adapter.setOnCallBack(this);

    }


    @OnClick({R.id.tv_next, R.id.tv_back, R.id.iv_addpic, R.id.rl_myFile})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next:  //下一步,这里需要处理一下服务包的任务等
                 if (MyFileComplete) {
                     if (TextUtils.isEmpty(etConsultion.getText().toString().replace(" ", ""))) {
                         ToastUtils.show(this, "请输入消息内容");
                     } else {
                         initProgressDialog("");
                         presenter.getOrde();  //传递图文文字
                     }
                 }else{
                     ToastUtils.show(this, "请完善个人信息");
                 }

                break;

            case R.id.rl_myFile:  //跳转到我的档案
                  startActivity(new Intent(this, PersonHealthRecordActivity.class));
                break;
            case R.id.tv_back:  //返回
                finish();
                break;

            case R.id.iv_addpic: //添加图片
                PictureSelectUtil.OpenPicture(this, 9 - listPath.size(), false);
                break;


        }


    }


    @SuppressLint("CheckResult")
    private void getHealthRecord() {
//
        Map<String,Object>params=new HashMap<>();
        params.put("user_id", UserConfig.UserInfo.getUid());
        RequestUtils.get(this, Url.UpLoadUserInfo, params, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){
                    Gson gson=new Gson();
                    HealthRecords mHealthRecord=gson.fromJson(gson.toJson(result),HealthRecords.class);
                   if (!TextUtils.isEmpty(mHealthRecord.getDatas().getName())&&mHealthRecord.getDatas().getBirthday()!=0){
                       tvMyfile.setVisibility(View.GONE);
                       MyFileComplete=true;
                   }else{
                       tvMyfile.setVisibility(View.VISIBLE);
                       MyFileComplete=false;
                   }
                    initView(doctor);
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {

                ShowToast(errorMsg);

            }
        });




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

                    if (selectList != null) {
                        gridView.setVisibility(View.VISIBLE);
                        ivAddPic.setVisibility(View.GONE);
                        tvTips.setVisibility(View.GONE);
                        for (LocalMedia media : selectList) {
                            listPath.add(media.getPath());
                            list.add(media.getPath());
                        }
                        if (list.contains("add")) {
                            list.remove("add");
                        }
                        if (listPath.size() != 9) {
                            list.add("add");
                        }
                        adapter.notifyDataSetChanged();

                    }


                    break;


            }

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.tuwen_consultion;
    }

    @Override
    public BasePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ConsultPresenter();
        }
        return presenter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        tvCount.setText(s.length() + "/500");
        if (s.length()>0&&MyFileComplete){
            tvNext.setBackgroundResource(R.drawable.next_clickable);
        }else {
            tvNext.setBackgroundResource(R.drawable.next_unclickable);
        }
    }

    //删除图片
    @Override
    public void DeletePic(int position) {
        list.remove(position);
        listPath.remove(position);
        if (list.size() == 1) {
            list.clear();
            listPath.clear();
            gridView.setVisibility(View.GONE);
            ivAddPic.setVisibility(View.VISIBLE);
            tvTips.setVisibility(View.VISIBLE);
        }

        if (listPath.size() != 9 && listPath.size() > 0) {
            if (list.contains("add")) {
                list.remove("add");
            }
            list.add("add");
        }
        adapter.notifyDataSetChanged();

    }

    //预览图片
    @Override
    public void PreviewPic(int position) {
        if (list.get(position).equals("add")) {  //添加图片
            PictureSelectUtil.OpenPicture(this, 9 - listPath.size(), false);
        } else {  //跳转到预览图片
            Intent intent = new Intent(this, PreviewPictureActivity.class);
            intent.putExtra("index", position + "");
            intent.putStringArrayListExtra("path", (ArrayList<String>) list);
            startActivity(intent);
        }

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int DoctorId() {
        return doctorId;
    }

    @Override
    public String desc() {   //详细描述
        return "通过文字、图片向医生提问，含5次发送消息机会";
    }

    @Override
    public String product_no() {  //资费包编号
        return product_no;
    }


    @Override
    public int Total_pack() {
        return 1;
    }

    @Override
    public List<String> image() {
        return listPath;
    }

    @Override
    public String Content() {
        return etConsultion.getText().toString().replace(" ", "");
    }

    @Override
    public void Success(String msg) {
        dismissProgressDialog();
        if (msg != null) {
            SaveMessage(); //保存刚才上传的文件内容;
            if (orderInfoModel.getDatas().getUnused_count() == 0) {
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra("id", doctorId);
                intent.putExtra("doctorname", doctorName);
                intent.putExtra("headurl", headUrl);
                intent.putExtra("workplace", workPlace);
                intent.putExtra("level", level);
                intent.putExtra("pack_no", product_no);
                intent.putExtra("price", price);
                intent.putExtra("type",type);
                startActivity(intent);
            } else {   //激活订单,如果有可用次数，就进行激活订单
                for (int i = 0; i < orderInfoModel.getDatas().getList().size(); i++) {
                    if (orderInfoModel.getDatas().getList().get(i).getExpire_date() == 0) {
                        ActionOrder(orderInfoModel.getDatas().getList().get(i).getOrder_no());

                        break;
                    }

                }


            }
        }

    }


    //激活用户服务包订单
    public void ActionOrder(String orderNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("doctor_id", doctorId);
        params.put("order_no", orderNo);

        RequestUtils.post(this, Url.ActivationOrder, params, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {

                if (result.result == 0) {  //激活成功，跳转到聊天界面

                    Intent intent = new Intent(ConsultTypeActivity.this, ChatActivity.class);
                    intent.putExtra("id", doctorId + "");
                    intent.putExtra("ispay", true);
                    startActivity(intent); //跳转到聊天页面
                    EventBus.getDefault().post(new ActionOrderEvent(true));
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                ShowToast(errorMsg);
            }
        });

    }


    //保存刚才上传的文件和文字内容
    private void SaveMessage() {
        Conversation conversation = new Conversation();
        ChatMessage chatMessage = new ChatMessage();
        Doctor doctor = appLication.doctor;

        conversation.setFriendName(doctor.getName());
        conversation.setFriendId(doctor.getDoctor_id() + "");
        conversation.setRead(true);  //是否阅读
        conversation.setLevel(level);  //医生等级
        conversation.setImageUrl(headUrl);  //头像
        conversation.setLastMessageTime(System.currentTimeMillis() / 1000);
        conversation.setCountMessage(5);  //剩余次数
        conversation.setTime(48);  //剩余时间
        //聊天消息
        chatMessage.setMessageType(3);//消息类型

        chatMessage.setRead(true);
        chatMessage.setSend(true);
        chatMessage.setSenderId(String.valueOf(UserConfig.UserInfo.getUid()));
        chatMessage.setReceiverId(String.valueOf(doctorId));
        chatMessage.setTextMessage(etConsultion.getText().toString().replace(" ", ""));
        RealmList<String> realmList = new RealmList<>();
        realmList.clear();
        realmList.addAll(listPath);
        chatMessage.setImagePath(realmList);
        chatMessage.setThumbImagePath(realmList);

        chatMessage.setTimestamp(System.currentTimeMillis() / 1000);
        conversation.setLastMessage(chatMessage);
        appLication.conversation = conversation;

    }

    @Override
    public void Fail(String str) {
        dismissProgressDialog();
        if (str.equals("未授权访问")) {  //跳转到登录页面
            ActivityCollector.finishAll();
            sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN, false);

            startActivity(new Intent(this, GetVoliatCodeActivity.class));
        } else {
            ShowToast(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
