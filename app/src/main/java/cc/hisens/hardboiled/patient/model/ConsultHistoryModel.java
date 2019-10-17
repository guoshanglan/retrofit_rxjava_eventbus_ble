package cc.hisens.hardboiled.patient.model;

import java.util.List;

public class ConsultHistoryModel {


    /**
     * result : 0
     * message : 请求成功
     * datas : [{"doctor_id":512,"create_time":1564039663},{"doctor_id":511,"create_time":1564039565}]
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * doctor_id : 512
         * create_time : 1564039663
         */

        private int doctor_id;
        private int create_time;

        public int getDoctor_id() {
            return doctor_id;
        }

        public void setDoctor_id(int doctor_id) {
            this.doctor_id = doctor_id;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }
    }
}
