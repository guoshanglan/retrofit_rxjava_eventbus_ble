package cc.hisens.hardboiled.patient.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.eventbus.PayResult;
import cc.hisens.hardboiled.patient.utils.ToastUtils;

//微信支付回调的Activity

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Appconfig.WXPAY_APPID);
        api.handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.getType()) {
            case ConstantsAPI.COMMAND_PAY_BY_WX:

                switch (baseResp.errCode) {
                    case 0://展示成功页面
                        ToastUtils.show(WXPayEntryActivity.this,"支付成功");
                        EventBus.getDefault().post(new PayResult(true));



                        break;
                    case -1://签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                        EventBus.getDefault().post(new PayResult(false));
                        ToastUtils.show(WXPayEntryActivity.this,"支付异常");
                        break;
                    case -2://无需处理。发生场景：用户不支付了，点击取消，返回APP。
                        ToastUtils.show(WXPayEntryActivity.this,"支付取消");
                        EventBus.getDefault().post(new PayResult(false));
                        break;
                }
                  finish();

                break;
        }
    }



}
