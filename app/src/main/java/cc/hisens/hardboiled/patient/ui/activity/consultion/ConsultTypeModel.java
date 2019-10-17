package cc.hisens.hardboiled.patient.ui.activity.consultion;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;

public class ConsultTypeModel implements Serializable {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"nonce_str":"jfWoGK5ZfJglM8AO1M9EFGeWpZ1VnElb","sign":"59C96D3DA4C3FD55B55EF74C2B3C135E","timestamp":"1563267218","out_trade_no":"0MhKBGwLSEQsfGBgL8FGORlPKafiBwJr","prepay_id":"wx16165323801814fe73933cb71192534400"}
     */

    private int result;
    private String message;
    private DatasBean datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {
        /**
         * nonce_str : jfWoGK5ZfJglM8AO1M9EFGeWpZ1VnElb
         * sign : 59C96D3DA4C3FD55B55EF74C2B3C135E
         * timestamp : 1563267218
         * out_trade_no : 0MhKBGwLSEQsfGBgL8FGORlPKafiBwJr
         * prepay_id : wx16165323801814fe73933cb71192534400
         */

        private String nonce_str;
        private String sign;
        private String timestamp;
        private String out_trade_no;
        private String prepay_id;

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }
    }

    //获取后台生成的预支付订单
    public void  getOrderNo(Context context, int doctorId, String content,  List<String>images,ConsultPresenter presenter){

        List<File>fileList=new ArrayList<>();
        for (int i=0;i<images.size();i++){
            File file=new File(images.get(i));
            fileList.add(file);
        }

        RequestUtils.SendFrist(context, Url.SendFristMessage, fileList,doctorId,content, new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result.result==0){

                       presenter.Success(result.getMessage());
                    }else{
                        presenter.Fair(result.message);

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                if (errorMsg.equals("retrofit2.adapter.rxjava2.HttpException: HTTP 401 Unauthorized")){
                    presenter.Fair("未授权访问");
                }else {
                    presenter.Fair(errorMsg);
                }
            }
        });

    }



}
