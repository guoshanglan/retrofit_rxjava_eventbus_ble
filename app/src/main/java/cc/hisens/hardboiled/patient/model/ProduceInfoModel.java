package cc.hisens.hardboiled.patient.model;


import java.util.List;

//服务资费包model类
public class ProduceInfoModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : {"product_no":"REOP3dZhIEiPVxb2mga3oJDPCHRCjngn","fee":100,"pack_count":1}
     */

    private int result;
    private String message;
    private List<DatasBean> datas;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static class DatasBean {
        /**
         * product_no : REOP3dZhIEiPVxb2mga3oJDPCHRCjngn
         * fee : 100
         * pack_count : 1
         */

        private String product_no;
        private int fee;
        private int pack_count;

        public String getProduct_no() {
            return product_no;
        }

        public void setProduct_no(String product_no) {
            this.product_no = product_no;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getPack_count() {
            return pack_count;
        }

        public void setPack_count(int pack_count) {
            this.pack_count = pack_count;
        }
    }
}
