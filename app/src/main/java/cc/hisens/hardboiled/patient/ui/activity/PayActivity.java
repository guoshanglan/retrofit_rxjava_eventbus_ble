package cc.hisens.hardboiled.patient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

import cc.hisens.hardboiled.patient.eventbus.ChatBackEvent;
import cc.hisens.hardboiled.patient.eventbus.PayResult;
import cc.hisens.hardboiled.patient.model.ActionOrderEvent;
import cc.hisens.hardboiled.patient.model.WechatParamsModel;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.chat.ChatActivity;
import cc.hisens.hardboiled.patient.ui.activity.doctordetail.DoctorDetailActivity;
import cc.hisens.hardboiled.patient.utils.MessageDialog;
import cc.hisens.hardboiled.patient.wideview.CircleImageView;

//支付的Activity
public class PayActivity extends BaseActivity implements MessageDialog.DialogCallback {
    @BindView(R.id.tv_back)
    public TextView tvBack;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_doctor_name)
    public TextView tvDoctorName; //医生姓名
    @BindView(R.id.iv_doctor_head)
    public CircleImageView ivDoctor; //医生头像
    @BindView(R.id.tv_zhiwei1)
    public TextView tvZhiwei;
    @BindView(R.id.tv_workPlace)
    public TextView tvWorkPlace;
    @BindView(R.id.tv_price)
    public TextView tvPrice; //显示价格
    @BindView(R.id.rl_wechat)
    public RelativeLayout rlWechat; //微信支付
    @BindView(R.id.rl_alipay)
    public RelativeLayout rlAlipay; //支付宝支付
    @BindView(R.id.cb_alipay)
    public ImageView ivAlipay;   //支付宝支付的图标
    @BindView(R.id.cb_wechat)
    public ImageView ivWechat; //微信支付的图标
    @BindView(R.id.tv_price_pay)
    public TextView tvPricePay; //要支付的price，底部的
    @BindView(R.id.tv_pay_now)
    public TextView tvPayNow;  //立即支付
    @BindView(R.id.tv_pay_cancel)
    public TextView tvPayCancel;
    public MessageDialog dialog; //提示弹窗
    public int type=1;  //支付方式： 1：代表微信支付，2：代表支付宝支付
    public String doctorName,headurl,workPlace;
    public int level,doctorid; //等级
    public String packNo;
    public boolean payResult=false;
    public  WechatParamsModel model;//微信预支付订单model
    public String price; //价格
    public int flag;//用这个来标记是从什么位置跳转过来的，医生履历或者聊天页面






    //订阅的evnetbus回调支付事件
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(PayResult event) {
        if (event!=null&&event.isSuccess==false){
           tvPayNow.setText("继续支付");
           tvPayCancel.setVisibility(View.VISIBLE);
            payResult=false;
        }else if (event.isSuccess){

            payResult=true;

        }

    }
    //订阅的是否需要finish这个activity,从聊天页面返回
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onPay(ChatBackEvent event) {
        if (event!=null&&event.isChatBack){
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //跳转到聊天界面
        if (payResult) {
            Intent intent = new Intent(PayActivity.this, ChatActivity.class);
            intent.putExtra("id", doctorid+"");
            intent.putExtra("ispay",true);
            startActivity(intent); //跳转到聊天页面
            EventBus.getDefault().post(new ActionOrderEvent(true));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }

    //初始化数据
    private void initData() {

        doctorName=getIntent().getStringExtra("doctorname");
        headurl=getIntent().getStringExtra("headurl");
        workPlace=getIntent().getStringExtra("workplace");
        level=getIntent().getIntExtra("level",0);
        doctorid=getIntent().getIntExtra("id",0);
        packNo=getIntent().getStringExtra("pack_no");
        price=getIntent().getStringExtra("price");

        flag=getIntent().getIntExtra("type",1);
        tvPrice.setText(price+"元");
        tvPricePay.setText(price+"元");
        tvDoctorName.setText(doctorName);
        tvWorkPlace.setText(workPlace);
        Glide.with(this).load(headurl).placeholder(R.drawable.doctor_head_100).into(ivDoctor);
        if (level==1){
            tvZhiwei.setText("主任医师");
        }else if (level==2){
            tvZhiwei.setText("副主任医师");
        }else if (level==3){
            tvZhiwei.setText("主治医师");
        }else if (level==4){
            tvZhiwei.setText("住院医师");
        }else if (level==5){
            tvZhiwei.setText("助理医师");
        }

        tvTitle.setText("支付方式");
        ivWechat.setSelected(true);
        dialog=new MessageDialog(this);
        dialog.initCallback(this);


    }

    @OnClick({R.id.rl_wechat,R.id.rl_alipay,R.id.tv_pay_cancel,R.id.tv_pay_now,R.id.tv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_back:  //返回
                finish();
                break;

            case R.id.rl_wechat:   //选择微信支付
                ivWechat.setSelected(true);
                ivAlipay.setSelected(false);
                type=1;
                break;
            case R.id.rl_alipay:   //选择支付宝支付
                ivWechat.setSelected(false);
                ivAlipay.setSelected(true);
                type=2;
                break;
            case R.id.tv_pay_now:
                if (type==1){
                    initProgressDialog("");
                    getWechatOrderParams();
                }else {                            //支付宝支付

                }

                break;

            case R.id.tv_pay_cancel:

                dialog.initDialog("你确定要取消订单吗","确认","取消");
                break;



        }

    }


    //生成预支付订单
    public void getWechatOrderParams(){
        Map<String,Object>params=new HashMap<>();
        params.put("body","测试商品描述");
        params.put("product_no",packNo);
        params.put("doctor_id",doctorid);
        RequestUtils.post(this, Url.getOrder, params, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                dismissProgressDialog();
                if (result.result==0){
                    Gson gson=new Gson();
                     model=gson.fromJson(gson.toJson(result),WechatParamsModel.class);
                     WechatPay(model);

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dismissProgressDialog();
                ShowToast(errorMsg);
            }
        });
    }



    public void WechatPay(WechatParamsModel model){
        //这些所有的信息应该从后台取

        IWXAPI api = WXAPIFactory.createWXAPI(this, Appconfig.WXPAY_APPID);
        api.registerApp(Appconfig.WXPAY_APPID);
        PayReq req = new PayReq();
        req.appId           = Appconfig.WXPAY_APPID;//你的微信appid
        req.partnerId       = Appconfig.PartnerId;//商户号
        req.prepayId        = model.getDatas().getPrepay_id();//预支付交易会话ID
        req.nonceStr        = model.getDatas().getNonce_str();//随机字符串
        req.timeStamp       = model.getDatas().getTimestamp();//时间戳
        req.packageValue    = "Sign=WXPay";//扩展字段,这里固定填写Sign=WXPay
        req.sign            = model.getDatas().getSign();//签名
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.pay_activity;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void OnSure(boolean sure) {   //确认取消订单,跳转到医生详情界面
//           Intent intent=new Intent(this, DoctorDetailActivity.class);
//           intent.putExtra("id",doctorid);
//           startActivity(intent);
           finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
