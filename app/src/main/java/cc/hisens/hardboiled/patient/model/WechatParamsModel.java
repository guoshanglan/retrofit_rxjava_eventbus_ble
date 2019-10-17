package cc.hisens.hardboiled.patient.model;


//微信支付生成的与支付订单model类
public class WechatParamsModel {


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

    public static class DatasBean {
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
}
